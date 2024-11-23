package com.code.ecommercebackend.services.order;

import com.code.ecommercebackend.exceptions.DataNotFoundException;
import com.code.ecommercebackend.models.Notification;
import com.code.ecommercebackend.models.Order;
import com.code.ecommercebackend.models.ProductOrder;
import com.code.ecommercebackend.models.enums.OrderStatus;
import com.code.ecommercebackend.repositories.NotificationRepository;
import com.code.ecommercebackend.repositories.OrderRepository;
import com.code.ecommercebackend.services.BaseServiceImpl;
import com.code.ecommercebackend.utils.SocketHandler;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class OrderServiceImpl extends BaseServiceImpl<Order, String> implements OrderService{
    private final OrderRepository orderRepository;
    private final SocketHandler socketHandler;
    private final NotificationRepository notificationRepository;

    public OrderServiceImpl(MongoRepository<Order, String> repository, OrderRepository orderRepository, SocketHandler socketHandler, NotificationRepository notificationRepository) {
        super(repository);
        this.orderRepository = orderRepository;
        this.socketHandler = socketHandler;
        this.notificationRepository = notificationRepository;
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
        Notification notification =  saveNotification(order);
        socketHandler.sendNotificationToSocket(notification);
        orderRepository.save(order);
    }

    @Override
    public void confirmShipping(String orderId) throws DataNotFoundException {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new DataNotFoundException("Order not found"));
        List<ProductOrder> productOrders = order.getProductOrders();
        for (ProductOrder productOrder : productOrders) {
            productOrder.setAllowComment(true);
        }
        order.setOrderStatus(OrderStatus.SHIPPING);
        order.setProductOrders(productOrders);
        Notification notification =  saveNotification(order);
        socketHandler.sendNotificationToSocket(notification);
        orderRepository.save(order);
    }

    @Override
    public void confirmShippedConfirmation(String orderId) throws DataNotFoundException {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new DataNotFoundException("Order not found"));
        List<ProductOrder> productOrders = order.getProductOrders();
        for (ProductOrder productOrder : productOrders) {
            productOrder.setAllowComment(true);
        }
        order.setOrderStatus(OrderStatus.SHIPPED_CONFIRMATION);
        order.setProductOrders(productOrders);
        Notification notification =  saveNotification(order);
        socketHandler.sendNotificationToSocket(notification);
        orderRepository.save(order);
    }

    @Override
    public void confirmCancel(String orderId) throws DataNotFoundException {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new DataNotFoundException("Order not found"));
        List<ProductOrder> productOrders = order.getProductOrders();
        for (ProductOrder productOrder : productOrders) {
            productOrder.setAllowComment(true);
        }
        order.setOrderStatus(OrderStatus.CANCELLED);
        order.setProductOrders(productOrders);
        Notification notification =  saveNotification(order);
        socketHandler.sendNotificationToSocket(notification);
        orderRepository.save(order);
    }

    public Notification saveNotification(Order order){
        Notification notification = new Notification();
        notification.setTime(LocalDateTime.now());
        notification.setTitle("Đơn hàng của bạn đang trong trạng thái"+ order.getOrderStatus());
        notification.setContent("Chưa biết ghi gì ");
        notification.setUserId(order.getUserId());
        notification.setSeen(false);
        notification.setRedirectTo("");
        notificationRepository.save(notification);
        return notification;
    }
}
