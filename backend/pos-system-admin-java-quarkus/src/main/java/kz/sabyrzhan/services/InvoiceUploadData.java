package kz.sabyrzhan.services;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class InvoiceUploadData {
    private String invoiceBucket;
    private String invoiceName;
    private String filePath;
}
