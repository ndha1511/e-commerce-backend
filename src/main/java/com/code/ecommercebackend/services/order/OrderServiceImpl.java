package com.code.ecommercebackend.services.order;

import com.code.ecommercebackend.exceptions.DataNotFoundException;
import com.code.ecommercebackend.models.Order;
import com.code.ecommercebackend.models.ProductOrder;
import com.code.ecommercebackend.models.enums.OrderStatus;
import com.code.ecommercebackend.repositories.OrderRepository;
import com.code.ecommercebackend.services.BaseServiceImpl;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderServiceImpl extends BaseServiceImpl<Order, String> implements OrderService{
    private final OrderRepository orderRepository;

    public OrderServiceImpl(MongoRepository<Order, String> repository, OrderRepository orderRepository) {
        super(repository);
        this.orderRepository = orderRepository;
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
        orderRepository.save(order);
    }
}
