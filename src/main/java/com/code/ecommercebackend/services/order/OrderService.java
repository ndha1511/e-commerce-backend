package com.code.ecommercebackend.services.order;

import com.code.ecommercebackend.exceptions.DataNotFoundException;
import com.code.ecommercebackend.models.Order;
import com.code.ecommercebackend.services.BaseService;

public interface OrderService extends BaseService<Order, String> {
    void confirmReceived(String orderId) throws DataNotFoundException;
}
