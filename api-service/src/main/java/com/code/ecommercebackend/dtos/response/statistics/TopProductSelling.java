package com.code.ecommercebackend.dtos.response.statistics;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TopProductSelling {
    private String id;
    private long totalSaleQuantity;
    private long totalImportQuantity;
}
