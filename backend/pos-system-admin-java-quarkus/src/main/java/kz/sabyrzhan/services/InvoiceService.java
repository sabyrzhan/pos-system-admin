package kz.sabyrzhan.services;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import io.smallrye.mutiny.Uni;
import kz.sabyrzhan.entities.OrderEntity;
import kz.sabyrzhan.entities.OrderItemEntity;
import kz.sabyrzhan.exceptions.InvoiceException;
import kz.sabyrzhan.model.InvoiceResult;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.time.StopWatch;
import org.apache.fop.apps.FOUserAgent;
import org.apache.fop.apps.Fop;
import org.apache.fop.apps.FopFactory;
import org.apache.fop.apps.MimeConstants;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.xml.transform.Result;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.sax.SAXResult;
import javax.xml.transform.stream.StreamSource;
import java.io.Closeable;
import java.io.File;
import java.io.FileOutputStream;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.Map;

@ApplicationScoped
@Slf4j
public class InvoiceService {
    @ConfigProperty(name = "app.invoice.path")
    String invoicePath;

    @ConfigProperty(name = "app.invoice_bucket")
    String invoiceBucket;

    String xslDataTemplate;
    String xmlDataTemplate;

    @PostConstruct
    void postConstruct() throws Exception {
        try (var xsltFile = getClass().getClassLoader().getResourceAsStream("/invoicetemplate/standard/style.xsl");
             var xmlSource = getClass().getClassLoader().getResourceAsStream("/invoicetemplate/standard/data.xml")) {
            xslDataTemplate = IOUtils.toString(xsltFile, StandardCharsets.UTF_8);
            xmlDataTemplate = IOUtils.toString(xmlSource, StandardCharsets.UTF_8);
        }
    }

    public Uni<InvoiceResult> generatePdf(OrderEntity order) {
        final String invoiceName = "invoice_" + order.getId() + ".pdf";
        final String invoiceFilePath = invoicePath + "/" + invoiceName;

        final StopWatch stopWatch = new StopWatch("Total time taken for PDF generation and S3 upload:");
        stopWatch.start();
        return Uni.createFrom()
                .deferred(() -> {
                    StringReader xsltFileReader = null;
                    StringReader xmlSourceReader = null;
                    FileOutputStream out = null;
                    try {
                        var invoiceDate = DateTimeFormatter.ofPattern("dd.MM.y")
                                                        .withZone(ZoneId.from(ZoneOffset.UTC))
                                                        .format(order.getCreated());
                        String dataString = xmlDataTemplate
                                .replace("INVOICE_ID", order.getId())
                                .replace("INVOICE_DATE", invoiceDate)
                                .replace("BILL_TO", order.getCustomerName())
                                .replace("ITEMS_AND_SUMS", generateXmlData(order));
                        xsltFileReader = new StringReader(xslDataTemplate);
                        xmlSourceReader = new StringReader(dataString);
                        FopFactory fopFactory = FopFactory.newInstance(new File(".").toURI());
                        FOUserAgent foUserAgent = fopFactory.newFOUserAgent();
                        out = new FileOutputStream(invoiceFilePath);
                        Fop fop = fopFactory.newFop(MimeConstants.MIME_PDF, foUserAgent, out);
                        TransformerFactory factory = TransformerFactory.newInstance();
                        Transformer transformer = factory.newTransformer(new StreamSource(xsltFileReader));
                        Result res = new SAXResult(fop.getDefaultHandler());
                        transformer.transform(new StreamSource(xmlSourceReader), res);
                    } catch (Exception e) {
                        log.error("Error generating invoice for order={}: {}",order.getId(), e.toString(), e);
                        return Uni.createFrom().failure(new InvoiceException(e));
                    } finally {
                        closeStream(xsltFileReader, "Error while closing xslt reader");
                        closeStream(xmlSourceReader, "Error while closing xml data reader");
                        closeStream(out, "Error while closing pdf output stream");
                    }

                    return Uni.createFrom().nullItem();
                })
                .onItem().transformToUni(v -> {
                    AmazonS3 s3 = AmazonS3ClientBuilder.standard().build();
                    try {
                        s3.putObject(invoiceBucket, invoiceName, new File(invoiceFilePath));
                    } catch (Exception e) {
                        log.error("Error uploading invoice {} to S3: {}", invoiceName, e.toString(), e);
                        return Uni.createFrom().failure(new InvoiceException(e));
                    }

                    FileUtils.deleteQuietly(new File(invoiceFilePath));

                    String s3FilePath = "https://s3.amazonaws.com/" + invoiceBucket + "/" + invoiceName;
                    return Uni.createFrom().item(new InvoiceResult(s3FilePath));
                }).onItemOrFailure().transformToUni((invoiceResult, throwable) -> {
                    stopWatch.stop();
                    log.info(stopWatch.toString());

                    if (throwable != null) {
                        return Uni.createFrom().failure(throwable);
                    }

                    return Uni.createFrom().item(invoiceResult);
                });
    }

    public String generateXmlData(OrderEntity entity) {
        StringBuilder builder = new StringBuilder();
        if (entity.getItems() != null) {
            builder.append("<items>");
            for(OrderItemEntity item : entity.getItems()) {
                builder.append("<item>");
                builder.append("<productName>").append(item.getProductName()).append("</productName>");
                builder.append("<quantity>").append(item.getQuantity()).append("</quantity>");
                builder.append("<price>").append(item.getPrice()).append("</price>");
                builder.append("<total>").append(item.getQuantity() * item.getPrice()).append("</total>");
                builder.append("</item>");
            }
            builder.append("</items>");
        }

        Map<String, Object> keyValues = new LinkedHashMap<>();
        keyValues.put("Subtotal", entity.getSubtotal());
        keyValues.put("Tax", entity.getTax());
        keyValues.put("Discount", entity.getDiscount());
        keyValues.put("GRANT TOTAL", entity.getTotal());
        keyValues.put("Paid", entity.getPaid());
        keyValues.put("Due", entity.getDue());
        keyValues.put("Payment type", entity.getPaymentType());

        builder.append("<sums>");
        for(Map.Entry<String, Object> entry : keyValues.entrySet()) {
            builder.append("<sum>");
            builder.append("<key>").append(entry.getKey()).append("</key>");
            builder.append("<value>").append(entry.getValue()).append("</value>");
            builder.append("</sum>");
        }
        builder.append("</sums>");

        return builder.toString();
    }

    private void closeStream(Closeable closeable, String errorMessage) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (Exception e) {
                log.warn(errorMessage, e.toString(), e);
            }
        }
    }
}
