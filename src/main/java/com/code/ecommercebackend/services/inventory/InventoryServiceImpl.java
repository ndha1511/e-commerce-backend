package com.code.ecommercebackend.services.inventory;

import com.code.ecommercebackend.dtos.request.inventory.CreateInventoryRequest;
import com.code.ecommercebackend.dtos.request.inventory.InventoryDto;
import com.code.ecommercebackend.mappers.inventory.InventoryMapper;
import com.code.ecommercebackend.models.Inventory;
import com.code.ecommercebackend.models.PurchaseOrder;
import com.code.ecommercebackend.models.enums.InventoryStatus;
import com.code.ecommercebackend.repositories.InventoryRepository;
import com.code.ecommercebackend.repositories.PurchaseOrderRepository;
import com.code.ecommercebackend.services.BaseServiceImpl;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class InventoryServiceImpl extends BaseServiceImpl<Inventory, String> implements InventoryService {
    private final InventoryRepository inventoryRepository;
    private final InventoryMapper inventoryMapper;
    private final PurchaseOrderRepository purchaseOrderRepository;

    public InventoryServiceImpl(MongoRepository<Inventory, String> repository,
                                InventoryRepository inventoryRepository,
                                InventoryMapper inventoryMapper, PurchaseOrderRepository purchaseOrderRepository) {
        super(repository);
        this.inventoryRepository = inventoryRepository;
        this.inventoryMapper = inventoryMapper;
        this.purchaseOrderRepository = purchaseOrderRepository;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveInventory(CreateInventoryRequest createInventoryRequest) {
        List<InventoryDto> inventoriesDto = createInventoryRequest.getInventories();
        List<Inventory> inventories = inventoriesDto.stream()
                .map(inventoryMapper::toInventory).toList();
        inventories.forEach(i -> {
            i.setImportDate(LocalDateTime.now());
            i.setInventoryStatus(InventoryStatus.IN_STOCK);
        });
        inventoryRepository.saveAll(inventories);
        savePurchaseOrder(inventories);
    }

    private void savePurchaseOrder(List<Inventory> inventories) {
        PurchaseOrder purchaseOrder = new PurchaseOrder();
        Set<String> inventoriesId = inventories.stream().map(Inventory::getId).collect(Collectors.toSet());
        purchaseOrder.setInventories(inventoriesId);
        purchaseOrder.setOrderDate(LocalDateTime.now());
        purchaseOrder.setTotalPrice(inventories.stream()
                .mapToDouble(Inventory::getImportPrice).sum());
        purchaseOrder.setTotalQuantity(inventories.stream()
                .mapToInt(Inventory::getImportQuantity).sum());
        purchaseOrderRepository.save(purchaseOrder);

    }
}
