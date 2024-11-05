package com.code.ecommercebackend.services.payment;

import com.code.ecommercebackend.dtos.request.payment.KafkaMessageOrder;
import com.code.ecommercebackend.dtos.request.payment.OrderItem;
import com.code.ecommercebackend.dtos.request.payment.OrderRequest;
import com.code.ecommercebackend.models.*;
import com.code.ecommercebackend.models.enums.InventoryStatus;
import com.code.ecommercebackend.models.enums.OrderStatus;
import com.code.ecommercebackend.repositories.*;
import com.code.ecommercebackend.repositories.customizations.inventory.InventoryCustomRepository;
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
public class PaymentListener {

    private final ObjectMapper objectMapper;
    private final CartRepository cartRepository;
    private final VariantRepository variantRepository;
    private final UserRepository userRepository;
    private final UserBehaviorRepository userBehaviorRepository;
    private final RedissonClient redissonClient;
    private final InventoryCustomRepository inventoryCustomRepository;
    private final InventoryRepository inventoryRepository;
    private final OrderRepository orderRepository;
    private final VoucherRepository voucherRepository;
    private final VoucherUsageRepository voucherUsageRepository;

    @KafkaListener(topics = "order-topic", groupId = "order-topic")
    public void handleOrder(ConsumerRecord<String, byte[]> record) throws IOException {
        deleteCart(record);
        saveUserBehavior(record);
    }


    public void deleteCart(ConsumerRecord<String, byte[]> record) throws IOException {
        log.info("start delete cart......");

        KafkaMessageOrder kafkaMessageOrder = getKafkaMessageOrder(record);
        OrderRequest orderRequest = kafkaMessageOrder.getOrderRequest();
        if (orderRequest.getOrderFrom().equals("cart")) {
            Cart cart = cartRepository.findByUserId(orderRequest.getUserId())
                    .orElse(null);
            if (cart != null) {
                List<ProductCart> pCarts = cart.getProductCarts();
                for (int i = pCarts.size() - 1; i >= 0; i--) {
                    ProductCart pCart = pCarts.get(i);
                    for (OrderItem orderItem : orderRequest.getOrderItems()) {
                        if (orderItem.getVariantId().equals(pCart.getVariantId())) {
                            pCarts.remove(pCart);
                        }
                    }
                }
                cart.setProductCarts(pCarts);
                cartRepository.save(cart);
            }
        }

        log.info("end delete cart......");
    }

    @KafkaListener(topics = "inventory-topic", groupId = "inventory")
    public void inventory(ConsumerRecord<String, byte[]> record) throws IOException {

        log.info("start inventory......");

        KafkaMessageOrder kafkaMessageOrder = getKafkaMessageOrder(record);
        OrderRequest orderRequest = kafkaMessageOrder.getOrderRequest();
        Order order = kafkaMessageOrder.getOrder();
        List<OrderItem> orderItems = orderRequest.getOrderItems();
        for (OrderItem orderItem : orderItems) {
            RLock lock = redissonClient.getLock("productLock:" + orderItem.getVariantId());
            try {
                if (lock.tryLock(60, 65, TimeUnit.SECONDS)) {
                    try {
                        int buyQuantity = orderItem.getQuantity();
                        List<Inventory> inventories = inventoryCustomRepository
                                .getInventoryByVariantId(orderItem.getVariantId());
                        if(!inventories.isEmpty()) {
                            for (Inventory inventory : inventories) {
                                int quantityInStock = inventory.getImportQuantity() - inventory.getSaleQuantity();
                                if(quantityInStock >= buyQuantity) {
                                    inventory.setSaleQuantity(inventory.getSaleQuantity() + buyQuantity);
                                    if(inventory.getSaleQuantity() == inventory.getImportQuantity()) {
                                        inventory.setInventoryStatus(InventoryStatus.OUT_OF_STOCK);
                                    }
                                    buyQuantity = 0;
                                    break;
                                } else {
                                    buyQuantity = buyQuantity - quantityInStock;
                                    inventory.setSaleQuantity(inventory.getSaleQuantity() + (buyQuantity - quantityInStock));
                                    if(inventory.getSaleQuantity() == inventory.getImportQuantity()) {
                                        inventory.setInventoryStatus(InventoryStatus.OUT_OF_STOCK);
                                    }
                                }
                            }
                            if(buyQuantity == 0) {
                               inventoryRepository.saveAll(inventories);

                            } else {

                            }
                        } else {
                            // thông báo hết hàng
                        }
                    } finally {
                        lock.unlock();
                    }
                } else {

                }
            } catch (InterruptedException e) {
                throw new RuntimeException("Lock interrupted", e);
            }
        }
        if(orderRequest.getVoucherCode() != null) {
            Voucher voucher = voucherRepository.findByCode(orderRequest.getVoucherCode())
                    .orElse(null);
            if(voucher != null) {
                VoucherUsage voucherUsage = new VoucherUsage();
                voucherUsage.setVoucherId(orderRequest.getVoucherCode());
                voucherUsage.setUserId(orderRequest.getUserId());
                voucherUsageRepository.save(voucherUsage);
            }
        };
        order.setOrderStatus(OrderStatus.AWAITING_PICKUP);
        orderRepository.save(order);

        log.info("end inventory......");

    }

    public void saveUserBehavior(ConsumerRecord<String, byte[]> record) throws IOException {
        log.info("start save user behavior......");

        KafkaMessageOrder kafkaMessageOrder = getKafkaMessageOrder(record);
        OrderRequest orderRequest = kafkaMessageOrder.getOrderRequest();

        User user = userRepository.findById(orderRequest.getUserId())
                .orElse(null);
        for (OrderItem orderItem : orderRequest.getOrderItems()) {
            Variant variant = variantRepository.findById(orderItem.getVariantId())
                    .orElse(null);
            if (variant != null) {

                UserBehavior userBehavior = new UserBehavior();
                userBehavior.setBehavior(2);
                userBehavior.setUserId(user != null ? user.getNumId() : 0);
                userBehavior.setProductId(variant.getProduct().getNumId());
                userBehavior.setBuyQuantity(orderItem.getQuantity());
                userBehaviorRepository.save(userBehavior);
            }
        }

        log.info("end save user behavior......");


    }


    private KafkaMessageOrder getKafkaMessageOrder(ConsumerRecord<String, byte[]> record) throws IOException {
        byte[] messageBytes = record.value();
        return objectMapper.readValue(messageBytes, KafkaMessageOrder.class);
    }
}
