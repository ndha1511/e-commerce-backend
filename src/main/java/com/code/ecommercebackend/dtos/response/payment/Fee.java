package com.code.ecommercebackend.dtos.response.payment;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class Fee implements Serializable {
    private int fee;
    @JsonProperty("insurance_fee")
    private int insuranceFee;
}
