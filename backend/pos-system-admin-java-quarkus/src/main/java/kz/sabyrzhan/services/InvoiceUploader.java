package kz.sabyrzhan.services;

import io.smallrye.mutiny.Uni;

public interface InvoiceUploader {
    Uni<InvoiceUploadResult> upload(InvoiceUploadData data);
}
