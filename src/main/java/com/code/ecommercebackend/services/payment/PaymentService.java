package com.code.ecommercebackend.services.payment;

import com.code.ecommercebackend.dtos.request.payment.OrderRequest;
import com.code.ecommercebackend.dtos.response.payment.Fee;
import com.code.ecommercebackend.exceptions.DataNotFoundException;
import com.code.ecommercebackend.models.Order;


public interface PaymentService {
    Fee calcFee(String pickProvince,
                String pickDistrict,
                String province,
                String district,
                int weight, int value);
    Order order(OrderRequest orderRequest) throws DataNotFoundException;
}
