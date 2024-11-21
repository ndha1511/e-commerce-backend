package com.code.ecommercebackend.crawlData.res;

import com.code.ecommercebackend.crawlData.model.Product;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class TikiRes {
    private List<Product> data;
}
