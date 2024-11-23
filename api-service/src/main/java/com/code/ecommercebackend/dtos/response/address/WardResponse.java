package com.code.ecommercebackend.dtos.response.address;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class WardResponse implements Serializable {
    @JsonProperty("WardCode")
    private int wardCode;
    @JsonProperty("DistrictID")
    private int districtID;
    @JsonProperty("WardName")
    private String wardName;
}
