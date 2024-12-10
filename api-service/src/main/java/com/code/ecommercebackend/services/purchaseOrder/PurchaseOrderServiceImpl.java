package com.code.ecommercebackend.services.purchaseOrder;

import com.code.ecommercebackend.dtos.response.purchaseOrder.PurchaseDetail;
import com.code.ecommercebackend.dtos.response.purchaseOrder.PurchaseItem;
import com.code.ecommercebackend.exceptions.DataNotFoundException;
import com.code.ecommercebackend.models.Inventory;
import com.code.ecommercebackend.models.PurchaseOrder;
import com.code.ecommercebackend.models.Variant;
import com.code.ecommercebackend.repositories.InventoryRepository;
import com.code.ecommercebackend.repositories.PurchaseOrderRepository;
import com.code.ecommercebackend.repositories.VariantRepository;
import com.code.ecommercebackend.repositories.customizations.redis.RedisRepository;
import com.code.ecommercebackend.services.BaseServiceImpl;
import com.code.ecommercebackend.utils.RedisKeyHandler;
import com.code.ecommercebackend.utils.enums.RedisKeyEnum;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class PurchaseOrderServiceImpl
        extends BaseServiceImpl<PurchaseOrder, String> implements PurchaseOrderService {

    private final PurchaseOrderRepository purchaseOrderRepository;
    private final InventoryRepository inventoryRepository;
    private final VariantRepository variantRepository;
    private final RedisRepository redisRepository;

    public PurchaseOrderServiceImpl(MongoRepository<PurchaseOrder, String> repository,
                                    PurchaseOrderRepository purchaseOrderRepository, InventoryRepository inventoryRepository, VariantRepository variantRepository, RedisRepository redisRepository) {
        super(repository);
        this.purchaseOrderRepository = purchaseOrderRepository;
        this.inventoryRepository = inventoryRepository;
        this.variantRepository = variantRepository;
        this.redisRepository = redisRepository;
    }


    @Override
    public PurchaseDetail findByPurchaseOrderId(String purchaseOrderId) throws DataNotFoundException, JsonProcessingException {
        String redisKey = RedisKeyHandler.createKeyWithId(purchaseOrderId, RedisKeyEnum.HISTORY_IMPORT);
        PurchaseDetail purchaseDetail = redisRepository.getDataFromCache(redisKey, PurchaseDetail.class);

        if (purchaseDetail == null) {
            purchaseDetail = new PurchaseDetail();
            PurchaseOrder purchaseOrder = purchaseOrderRepository.findById(purchaseOrderId)
                    .orElseThrow(() -> new DataNotFoundException("purchase order not found"));
            Set<String> inventoriesId = purchaseOrder.getInventories();
            purchaseDetail.setPurchaseOrder(purchaseOrder);
            List<PurchaseItem> purchaseItems = new ArrayList<>();
            for (String inventoryId : inventoriesId) {
                Inventory inventory = inventoryRepository.findById(inventoryId)
                        .orElseThrow(() -> new DataNotFoundException("inventory not found"));
                Variant variant = variantRepository.findById(inventory.getVariantId())
                        .orElseThrow(() -> new DataNotFoundException("variant not found"));
                PurchaseItem purchaseItem = new PurchaseItem();
                purchaseItem.setImportPrice(inventory.getImportPrice());
                purchaseItem.setQuantity(inventory.getImportQuantity());
                purchaseItem.setVariant(variant);
                purchaseItems.add(purchaseItem);
            }
            purchaseDetail.setPurchaseItems(purchaseItems);
            purchaseDetail.setPurchaseItems(purchaseItems);
            redisRepository.saveDataInCache(redisKey, purchaseDetail);
        }


        return purchaseDetail;
    }
}
