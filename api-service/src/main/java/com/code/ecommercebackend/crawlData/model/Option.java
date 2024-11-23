package com.code.ecommercebackend.crawlData.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class Option {
    private String name;
    private List<OptionValue> values;
}
