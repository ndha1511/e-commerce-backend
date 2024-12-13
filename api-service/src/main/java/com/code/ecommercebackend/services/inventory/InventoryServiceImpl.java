package com.code.ecommercebackend.services.inventory;

import com.code.ecommercebackend.components.LocalDateTimeVN;
import com.code.ecommercebackend.dtos.request.inventory.CreateInventoryRequest;
import com.code.ecommercebackend.dtos.request.inventory.InventoryDto;
import com.code.ecommercebackend.exceptions.DataNotFoundException;
import com.code.ecommercebackend.mappers.inventory.InventoryMapper;
import com.code.ecommercebackend.models.Inventory;
import com.code.ecommercebackend.models.Product;
import com.code.ecommercebackend.models.PurchaseOrder;
import com.code.ecommercebackend.models.User;
import com.code.ecommercebackend.repositories.InventoryRepository;
import com.code.ecommercebackend.repositories.ProductRepository;
import com.code.ecommercebackend.repositories.PurchaseOrderRepository;
import com.code.ecommercebackend.repositories.UserRepository;
import com.code.ecommercebackend.services.BaseServiceImpl;
import com.code.ecommercebackend.services.auth.JwtService;
import com.code.ecommercebackend.utils.CookieHandler;
import jakarta.servlet.http.HttpServletRequest;
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
    private final ProductRepository productRepository;
    private final JwtService jwtService;
    private final CookieHandler cookieHandler;
    private final UserRepository userRepository;

    public InventoryServiceImpl(MongoRepository<Inventory, String> repository,
                                InventoryRepository inventoryRepository,
                                InventoryMapper inventoryMapper, PurchaseOrderRepository purchaseOrderRepository, ProductRepository productRepository, JwtService jwtService, CookieHandler cookieHandler, UserRepository userRepository) {
        super(repository);
        this.inventoryRepository = inventoryRepository;
        this.inventoryMapper = inventoryMapper;
        this.purchaseOrderRepository = purchaseOrderRepository;
        this.productRepository = productRepository;
        this.jwtService = jwtService;
        this.cookieHandler = cookieHandler;
        this.userRepository = userRepository;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveInventory(CreateInventoryRequest createInventoryRequest, HttpServletRequest request) throws DataNotFoundException {
        List<InventoryDto> inventoriesDto = createInventoryRequest.getInventories();
        List<Inventory> inventories = inventoriesDto.stream()
                .map(inventoryMapper::toInventory).toList();
        for (Inventory inventory: inventories) {
            inventory.setImportDate(LocalDateTimeVN.now());
            Product product = productRepository.findById(inventory.getProductId())
                    .orElseThrow(() -> new DataNotFoundException("product not found"));
            product.setTotalQuantity(product.getTotalQuantity() + inventory.getImportQuantity());
            productRepository.save(product);
        }

        inventoryRepository.saveAll(inventories);
        String token = cookieHandler.getCookie(request, "access_token");
        if(token != null && !token.isEmpty()) {
            String username = jwtService.extractUsername(token);
            if(username != null) {
                User user = userRepository.findByUsername(username)
                        .orElseThrow(() -> new DataNotFoundException("user not found"));
                savePurchaseOrder(inventories, user.getName());
            }
        }

    }

    private void savePurchaseOrder(List<Inventory> inventories, String staffName) {
        PurchaseOrder purchaseOrder = new PurchaseOrder();
        Set<String> inventoriesId = inventories.stream().map(Inventory::getId).collect(Collectors.toSet());
        purchaseOrder.setInventories(inventoriesId);
        purchaseOrder.setOrderDate(LocalDateTimeVN.now());
        purchaseOrder.setTotalPrice(inventories.stream()
                .mapToDouble(inventory -> inventory.getImportPrice() * inventory.getImportQuantity())
                .sum());
        purchaseOrder.setImportStaffName(staffName);
        purchaseOrder.setTotalQuantity(inventories.stream()
                .mapToInt(Inventory::getImportQuantity).sum());
        purchaseOrderRepository.save(purchaseOrder);

    }
}
