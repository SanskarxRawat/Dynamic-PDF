package com.dynamic.pdf.service;

import com.dynamic.pdf.entity.Invoice;
import org.springframework.http.HttpHeaders;

import java.util.Map;

public interface InvoiceService {

    String generateInvoiceHash(Invoice invoice);

    byte[] generatePDFBytes(String htmlContent) throws Exception;

    String generateHtmlContent(Invoice invoice);

    byte[] getPdfFromFileSystem(String fileName);

    String getFileLocation(String invoiceHash);

    HttpHeaders createDownloadHeaders(String fileName);

    void updatePDFCache(String invoiceHash,byte[] pdfBytes);

    Map<String,byte[]> getPDFCache();
}
