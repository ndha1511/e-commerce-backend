package com.code.ecommercebackend.services.order;

import com.code.ecommercebackend.exceptions.DataNotFoundException;
import com.code.ecommercebackend.models.*;
import com.code.ecommercebackend.models.enums.OrderStatus;
import com.code.ecommercebackend.repositories.OrderRepository;
import com.code.ecommercebackend.services.BaseServiceImpl;
import com.code.ecommercebackend.utils.NotificationHandler;
import com.code.ecommercebackend.utils.SocketHandler;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderServiceImpl extends BaseServiceImpl<Order, String> implements OrderService{
    private final OrderRepository orderRepository;
    private final SocketHandler socketHandler;
    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final NotificationHandler notificationHandler;


    public OrderServiceImpl(MongoRepository<Order, String> repository, OrderRepository orderRepository, SocketHandler socketHandler, KafkaTemplate<String, Object> kafkaTemplate, NotificationHandler notificationHandler) {
        super(repository);
        this.orderRepository = orderRepository;
        this.socketHandler = socketHandler;
        this.kafkaTemplate = kafkaTemplate;
        this.notificationHandler = notificationHandler;
    }

    @Override
    public void confirmReceived(String orderId) throws DataNotFoundException {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new DataNotFoundException("Order not found"));
        List<ProductOrder> productOrders = order.getProductOrders();
        for (ProductOrder productOrder : productOrders) {
            productOrder.setAllowComment(true);
        }
        order.setOrderStatus(OrderStatus.RECEIVED);
        order.setProductOrders(productOrders);
        Notification notification = notificationHandler.saveNotification(order);
        socketHandler.sendNotificationToSocket(notification);
        orderRepository.save(order);
    }

    @Override
    public void confirmShipping(String orderId) throws DataNotFoundException {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new DataNotFoundException("Order not found"));
        List<ProductOrder> productOrders = order.getProductOrders();
        order.setOrderStatus(OrderStatus.SHIPPING);
        order.setProductOrders(productOrders);
        Notification notification = notificationHandler.saveNotification(order);
        socketHandler.sendNotificationToSocket(notification);
        orderRepository.save(order);
    }

    @Override
    public void confirmShippedConfirmation(String orderId) throws DataNotFoundException {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new DataNotFoundException("Order not found"));
        List<ProductOrder> productOrders = order.getProductOrders();
        order.setOrderStatus(OrderStatus.SHIPPED_CONFIRMATION);
        order.setProductOrders(productOrders);
        Notification notification = notificationHandler.saveNotification(order);
        socketHandler.sendNotificationToSocket(notification);
        orderRepository.save(order);
    }

    @Override
    public void confirmCancel(String orderId) throws DataNotFoundException {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new DataNotFoundException("Order not found"));
        order.setOrderStatus(OrderStatus.CANCELLED);
        orderRepository.save(order);
        kafkaTemplate.send("cancel-order-topic", order);

    }


}
