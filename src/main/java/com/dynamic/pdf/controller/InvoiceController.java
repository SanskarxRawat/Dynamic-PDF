package com.dynamic.pdf.controller;

import com.dynamic.pdf.constants.InvoiceConstants;
import com.dynamic.pdf.entity.Invoice;
import com.dynamic.pdf.exceptions.PdfGenerationException;
import com.dynamic.pdf.service.InvoiceService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;


@Controller
@RequestMapping("/dynamic")
@Slf4j
public class InvoiceController {


    @Autowired
    private InvoiceService invoiceService;

    @PostMapping(path = "/generate-pdf")
    @Operation(summary = "This api is used to generate PDF")
    public ResponseEntity<byte[]> generatePDF(@RequestBody @Valid Invoice invoice) throws Exception {
        log.info("Invoice PDF to generate: {}",invoice);
        String invoiceHash=invoiceService.generateInvoiceHash(invoice);
        byte[] pdfBytes;
        HttpHeaders headers=new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData(InvoiceConstants.ATTACHEMENT,InvoiceConstants.INVOICE_PDF);

        if(invoiceService.getPDFCache().containsKey(invoiceHash)){
            log.info("Invoice was previously Generated");
            pdfBytes=invoiceService.getPDFCache().get(invoiceHash);
            return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);
        }

        try{
            log.info("Generating Invoice");
            String htmlContent = invoiceService.generateHtmlContent(invoice);
            pdfBytes = invoiceService.generatePDFBytes(htmlContent);
            invoiceService.updatePDFCache(invoiceHash,pdfBytes);
            return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);
        }catch (Exception e){
            throw new PdfGenerationException("Failed to generate PDF", e);
        }

    }
}
