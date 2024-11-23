package com.code.ecommercebackend.repositories.customizations.inventory;

import com.code.ecommercebackend.models.InventoryDetail;
import com.code.ecommercebackend.models.enums.InventoryStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class InventoryCustomRepositoryImpl implements InventoryCustomRepository {

    private final MongoTemplate mongoTemplate;

    @Override
    public List<InventoryDetail> getInventoryByVariantId(String variantId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("variantId").is(variantId)
                .and("inventoryStatus").is(InventoryStatus.IN_STOCK));
        return mongoTemplate.find(query, InventoryDetail.class);
    }
}
