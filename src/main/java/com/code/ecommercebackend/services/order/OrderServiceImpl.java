package com.code.ecommercebackend.services.order;

import com.code.ecommercebackend.models.Order;
import com.code.ecommercebackend.services.BaseServiceImpl;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Service;

@Service
public class OrderServiceImpl extends BaseServiceImpl<Order, String> implements OrderService{
    public OrderServiceImpl(MongoRepository<Order, String> repository) {
        super(repository);
    }
}
