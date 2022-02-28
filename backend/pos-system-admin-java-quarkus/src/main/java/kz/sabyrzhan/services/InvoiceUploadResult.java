package kz.sabyrzhan.services;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class InvoiceUploadResult {
    private String invoicePath;
}
