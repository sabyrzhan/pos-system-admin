package kz.sabyrzhan.services;

import io.smallrye.mutiny.Uni;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class InvoiceUploaderNone implements InvoiceUploader {
    @Override
    public Uni<InvoiceUploadResult> upload(InvoiceUploadData data) {
        var result = InvoiceUploadResult.builder()
                .invoicePath("https://s2.q4cdn.com/175719177/files/doc_presentations/Placeholder-PDF.pdf")
                .build();
        return Uni.createFrom().item(result);
    }
}
