package com.dynamic.pdf.exceptions;

public class InvoiceHashGenerationException extends RuntimeException {

    public InvoiceHashGenerationException(String message) {
        super(message);
    }

    public InvoiceHashGenerationException(String message, Throwable cause) {
        super(message, cause);
    }
}
