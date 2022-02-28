package kz.sabyrzhan.services;

import io.smallrye.mutiny.Uni;
import kz.sabyrzhan.exceptions.InvoiceException;
import software.amazon.awssdk.core.async.AsyncRequestBody;
import software.amazon.awssdk.services.s3.S3AsyncClient;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.io.File;

@ApplicationScoped
public class InvoiceUploaderS3 implements InvoiceUploader {
    @Inject
    S3AsyncClient s3;

    @Override
    public Uni<InvoiceUploadResult> upload(InvoiceUploadData data) {
        return Uni.createFrom().completionStage(() -> {
            PutObjectRequest request = PutObjectRequest.builder()
                    .bucket(data.getInvoiceBucket())
                    .key(data.getInvoiceName())
                    .build();

            return s3.putObject(request, AsyncRequestBody.fromFile(new File(data.getFilePath())));
        })
        .onItem()
        .transform(r -> InvoiceUploadResult.builder()
                .invoicePath("https://s3.amazonaws.com/" + data.getInvoiceBucket() + "/" + data.getInvoiceName())
                .build());
    }
}
