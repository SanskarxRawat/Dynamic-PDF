package com.dynamic.pdf.entity;


import com.dynamic.pdf.constants.DynamicPDFExceptionConstants;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.List;

@Data
@NoArgsConstructor
public class Invoice {


    @NotBlank(message = DynamicPDFExceptionConstants.EMPTY_FIELD)
    private String seller;

    @NotBlank(message = DynamicPDFExceptionConstants.EMPTY_FIELD)
    private String sellerGstin;

    @NotBlank(message = DynamicPDFExceptionConstants.EMPTY_FIELD)
    private String sellerAddress;

    @NotBlank(message = DynamicPDFExceptionConstants.EMPTY_FIELD)
    private String buyer;

    @NotBlank(message = DynamicPDFExceptionConstants.EMPTY_FIELD)
    private String buyerGstin;

    @NotBlank(message = DynamicPDFExceptionConstants.EMPTY_FIELD)
    private String buyerAddress;

    @NotEmpty(message = DynamicPDFExceptionConstants.EMPTY_FIELD)
    private List<Item> items;

}
