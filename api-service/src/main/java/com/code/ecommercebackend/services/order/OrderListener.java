package com.code.ecommercebackend.services.order;

import com.code.ecommercebackend.exceptions.DataNotFoundException;
import com.code.ecommercebackend.models.*;
import com.code.ecommercebackend.models.enums.InventoryStatus;
import com.code.ecommercebackend.repositories.InventoryRepository;
import com.code.ecommercebackend.repositories.ProductRepository;
import com.code.ecommercebackend.utils.NotificationHandler;
import com.code.ecommercebackend.utils.SocketHandler;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Slf4j
@RequiredArgsConstructor
@Component
public class OrderListener {

    private final ProductRepository productRepository;
    private final InventoryRepository inventoryRepository;
    private final RedissonClient redissonClient;
    private final ObjectMapper objectMapper;
    private final NotificationHandler notificationHandler;
    private final SocketHandler socketHandler;

    @KafkaListener(topics = "cancel-order-topic", groupId = "cancel-order-topic")
    public void handleCancelOrder(ConsumerRecord<String, byte[]> record) throws IOException, DataNotFoundException, InterruptedException {
        Order order = getOrder(record);
        List<ProductOrder> productOrders = order.getProductOrders();

        for (ProductOrder productOrder : productOrders) {
            List<InventoryOrder> inventoryOrders = productOrder.getInventoryOrders();
            for (InventoryOrder inventoryOrder : inventoryOrders) {

                Inventory inventory = inventoryRepository.findById(inventoryOrder.getInventoryId())
                        .orElseThrow(() -> new DataNotFoundException("Inventory not found"));
                RLock lock = redissonClient.getLock("productLock:" + inventory.getVariantId());
                if (lock.tryLock(60, 65, TimeUnit.SECONDS)) {
                    try {
                        inventory.setSaleQuantity(inventory.getSaleQuantity() - inventoryOrder.getQuantity());
                        inventory.setInventoryStatus(InventoryStatus.IN_STOCK);
                        Product product = productRepository.findById(productOrder.getProductId())
                                .orElseThrow(() -> new DataNotFoundException("Product not found"));
                        product.setTotalQuantity(product.getTotalQuantity() + inventoryOrder.getQuantity());
                        product.setBuyQuantity(product.getBuyQuantity() - inventoryOrder.getQuantity());
                        inventoryRepository.save(inventory);
                        productRepository.save(product);

                    } finally {
                        lock.unlock();
                    }
                }

            }
        }
        Notification notification = notificationHandler.saveNotification(order);
        socketHandler.sendNotificationToSocket(notification);

    }

    private Order getOrder(ConsumerRecord<String, byte[]> record) throws IOException {
        byte[] messageBytes = record.value();
        return objectMapper.readValue(messageBytes, Order.class);
    }
}
