package com.dynamic.pdf.service.impl;

import com.dynamic.pdf.constants.InvoiceConstants;
import com.dynamic.pdf.entity.Invoice;
import com.dynamic.pdf.exceptions.InvoiceHashGenerationException;
import com.dynamic.pdf.service.InvoiceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.xhtmlrenderer.pdf.ITextRenderer;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
public class InvoiceServiceImpl implements InvoiceService {

    @Autowired
    private SpringTemplateEngine springTemplateEngine;

    private Map<String,byte[]> pdfCache=new HashMap<>();

    @Override
    public String generateInvoiceHash(Invoice invoice) {
        try{
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(invoice.toString().getBytes(StandardCharsets.UTF_8));

            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
            return hexString.toString();
        }catch (NoSuchAlgorithmException e){
            throw new InvoiceHashGenerationException("Failed to generate invoice hash", e);
        }
    }

    @Override
    public byte[] generatePDFBytes(String htmlContent) throws Exception {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ITextRenderer renderer = new ITextRenderer();
        renderer.setDocumentFromString(htmlContent);
        renderer.layout();
        renderer.createPDF(outputStream);
        return outputStream.toByteArray();
    }

    @Override
    public String generateHtmlContent(Invoice invoice) {
        Context context = new Context();
        context.setVariable(InvoiceConstants.INVOICE, invoice);
        log.info("Context : {}",context);
        return springTemplateEngine.process(InvoiceConstants.INVOICE_TEMPLATE,context);
    }

    @Override
    public byte[] getPdfFromFileSystem(String fileName) {
        log.info("File Name : {}",fileName);
        try {
            return Files.readAllBytes(Paths.get(fileName));
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public String getFileLocation(String invoiceHash) {
        String userHome = System.getProperty(InvoiceConstants.USER_HOME);
        return userHome + File.separator + "Downloads" + File.separator + invoiceHash + ".pdf";
    }

    @Override
    public HttpHeaders createDownloadHeaders(String fileName) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentDisposition(ContentDisposition.attachment().filename(fileName).build());
        headers.setContentType(MediaType.APPLICATION_PDF);
        log.info("Http Headers : {}",headers);
        return headers;
    }

    @Override
    public void updatePDFCache(String invoiceHash, byte[] pdfBytes) {
        pdfCache.put(invoiceHash,pdfBytes);
        log.info("PDF Cache : {}",pdfCache);
    }

    @Override
    public Map<String,byte[]> getPDFCache() {
        return pdfCache;
    }
}
