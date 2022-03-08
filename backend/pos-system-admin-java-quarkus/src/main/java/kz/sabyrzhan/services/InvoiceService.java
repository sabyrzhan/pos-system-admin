package kz.sabyrzhan.services;

import io.smallrye.mutiny.Uni;
import io.smallrye.mutiny.tuples.Tuple2;
import io.smallrye.mutiny.unchecked.Unchecked;
import kz.sabyrzhan.entities.OrderEntity;
import kz.sabyrzhan.entities.OrderItemEntity;
import kz.sabyrzhan.exceptions.InvoiceException;
import kz.sabyrzhan.model.InvoiceResult;
import kz.sabyrzhan.model.InvoiceStorage;
import kz.sabyrzhan.model.InvoiceType;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.fop.apps.FOUserAgent;
import org.apache.fop.apps.Fop;
import org.apache.fop.apps.FopFactory;
import org.apache.fop.apps.MimeConstants;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.xml.transform.Result;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.sax.SAXResult;
import javax.xml.transform.stream.StreamSource;
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

    @ConfigProperty(name = "app.invoice.storage_type")
    InvoiceStorage invoiceStorage;

    @Inject
    InvoiceUploaderS3 invoiceUploaderS3;

    @Inject
    InvoiceUploaderNone invoiceUploaderNone;

    public Uni<InvoiceResult> generateInvoice(OrderEntity order, InvoiceType type) {
        final String invoiceName = String.format("invoice_%s.pdf", order.getId());
        final String invoiceFilePath = String.format("%s/%s", invoicePath, invoiceName);

        var startTime = System.currentTimeMillis();
        return generateXslAndXmlDataTemplate(type)
                .onItem().transformToUni(tuple -> {
                    String xslTemplate = tuple.getItem1();
                    String xmlDataTemplate = tuple.getItem2();
                    var invoiceDate = DateTimeFormatter.ofPattern("dd.MM.y")
                            .withZone(ZoneId.from(ZoneOffset.UTC))
                            .format(order.getCreated());
                    String dataString = xmlDataTemplate
                            .replace("INVOICE_ID", order.getId())
                            .replace("INVOICE_DATE", invoiceDate)
                            .replace("BILL_TO", order.getCustomerName())
                            .replace("ITEMS_AND_SUMS", generateXmlData(order));

                    try(var xsltFileReader = new StringReader(xslTemplate);
                        var xmlSourceReader = new StringReader(dataString);
                        var out = new FileOutputStream(invoiceFilePath)) {
                        FopFactory fopFactory = FopFactory.newInstance(new File(".").toURI());
                        FOUserAgent foUserAgent = fopFactory.newFOUserAgent();
                        Fop fop = fopFactory.newFop(MimeConstants.MIME_PDF, foUserAgent, out);
                        TransformerFactory factory = TransformerFactory.newInstance();
                        Transformer transformer = factory.newTransformer(new StreamSource(xsltFileReader));
                        Result res = new SAXResult(fop.getDefaultHandler());
                        transformer.transform(new StreamSource(xmlSourceReader), res);
                    } catch (Exception e) {
                        log.error("Error generating invoice for order={}: {}",order.getId(), e, e);
                        return Uni.createFrom().failure(new InvoiceException(e));
                    }

                    return Uni.createFrom().nullItem();
                })
                .onItem().transformToUni(v -> {
                    InvoiceUploader uploader;
                    InvoiceUploadData uploaderData;
                    switch (invoiceStorage) {
                        case S3:
                            log.info("Using S3 uploader");
                            uploader = invoiceUploaderS3;
                            uploaderData = InvoiceUploadData.builder()
                                    .filePath(invoiceFilePath)
                                    .invoiceBucket(invoiceBucket)
                                    .invoiceName(invoiceName)
                                    .build();
                            break;
                        case NONE:
                        default:
                            log.info("Using NONE uploader");
                            uploader = invoiceUploaderNone;
                            uploaderData = InvoiceUploadData.builder().build();
                    }

                    return uploader.upload(uploaderData);
                }).onItemOrFailure().transformToUni((uploaderResult, throwable) -> {
                    var stopTime = System.currentTimeMillis();
                    log.info("Total time taken for PDF generation and upload: {} ms", stopTime - startTime);

                    if (invoiceStorage != InvoiceStorage.FILE) {
                        log.info("Deleting file {} since invoiceStorage is not FILE", invoiceFilePath);
                        FileUtils.deleteQuietly(new File(invoiceFilePath));
                    }

                    if (throwable != null) {
                        log.error("Error uploading invoice: {}. Error: {}", invoiceName, throwable.toString(), throwable);
                        return Uni.createFrom().failure(throwable);
                    }

                    return Uni.createFrom().item(new InvoiceResult(uploaderResult.getInvoicePath()));
                });
    }

    private Uni<Tuple2<String, String>> generateXslAndXmlDataTemplate(InvoiceType type) {
        return Uni.createFrom().item(Unchecked.supplier(() -> {
            String xslFile;
            switch (type) {
                case THERMAL:
                    xslFile = "invoicetemplate/style.thermal.xsl";
                    break;
                case STANDARD:
                default:
                    xslFile = "invoicetemplate/style.standard.xsl";
                    break;
            }

            String xmlFile = "invoicetemplate/data.xml";
            String xslTemplate;
            String xmlDataTemplate;

            try (var xsltFile = getClass().getClassLoader().getResourceAsStream(xslFile);
                 var xmlSource = getClass().getClassLoader().getResourceAsStream(xmlFile)) {
                if (xsltFile == null) {
                    throw new IllegalArgumentException("xsl template not found.");
                }
                if (xmlSource == null) {
                    throw new IllegalArgumentException("xml data template not found.");
                }
                xslTemplate = IOUtils.toString(xsltFile, StandardCharsets.UTF_8);
                xmlDataTemplate = IOUtils.toString(xmlSource, StandardCharsets.UTF_8);
            } catch (Exception e) {
                log.error("Error while reading xml template file: {}", e.toString(), e);
                throw new InvoiceException(e);
            }

            return Tuple2.of(xslTemplate, xmlDataTemplate);
        }));
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
}
