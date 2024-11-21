package com.code.ecommercebackend.dtos.response.address;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class ProvinceResponse implements Serializable {
    @JsonProperty("ProvinceID")
    private int provinceID;
    @JsonProperty("ProvinceName")
    private String provinceName;
}
