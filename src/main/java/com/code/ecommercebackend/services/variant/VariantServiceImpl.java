package com.code.ecommercebackend.services.variant;

import com.code.ecommercebackend.dtos.response.variant.VariantResponse;
import com.code.ecommercebackend.mappers.attribute.VariantMapper;
import com.code.ecommercebackend.models.Promotion;
import com.code.ecommercebackend.models.Variant;
import com.code.ecommercebackend.models.enums.DiscountType;
import com.code.ecommercebackend.repositories.PromotionRepository;
import com.code.ecommercebackend.repositories.VariantRepository;
import com.code.ecommercebackend.services.BaseServiceImpl;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Optional;


@Service
public class VariantServiceImpl extends BaseServiceImpl<Variant, String> implements VariantService {

    private final VariantRepository variantRepository;
    private final PromotionRepository promotionRepository;
    private final VariantMapper variantMapper;

    public VariantServiceImpl(MongoRepository<Variant, String> repository,
                              VariantRepository variantRepository,
                              PromotionRepository promotionRepository,
                              VariantMapper variantMapper) {
        super(repository);
        this.variantRepository = variantRepository;
        this.promotionRepository = promotionRepository;
        this.variantMapper = variantMapper;
    }

    @Override
    public List<VariantResponse> findAllByVariantId(List<String> variantsId) {
        List<Variant> variants = variantRepository.findAllById(variantsId);
        return null;
    }




}
