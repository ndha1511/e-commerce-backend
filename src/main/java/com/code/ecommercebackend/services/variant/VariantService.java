package com.code.ecommercebackend.services.variant;

import com.code.ecommercebackend.dtos.response.variant.VariantResponse;
import com.code.ecommercebackend.models.Variant;
import com.code.ecommercebackend.services.BaseService;

import java.util.List;

public interface VariantService extends BaseService<Variant, String> {
    List<VariantResponse> findAllByVariantId(List<String> variantsId);
}
