package kz.sabyrzhan.exceptions;

public class InvoiceException extends RuntimeException {
    public InvoiceException(Exception e) {
        super("Failed to generate invoice", e);
    }
}