package com.dynamic.pdf.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Item {

    private String name;

    private String quantity;

    private Double rate;

    private Double amount;
}
