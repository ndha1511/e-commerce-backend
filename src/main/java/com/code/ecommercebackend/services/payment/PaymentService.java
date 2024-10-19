package com.code.ecommercebackend.services.payment;

import com.code.ecommercebackend.dtos.request.payment.OrderRequest;
import com.code.ecommercebackend.dtos.response.payment.Fee;
import com.code.ecommercebackend.exceptions.DataNotFoundException;
import com.code.ecommercebackend.models.Order;
import jakarta.servlet.http.HttpServletRequest;

public interface PaymentService {
    Fee calcFee(String pickProvince,
                String pickDistrict,
                String province,
                String district,
                int weight, int value);
    Order order(OrderRequest orderRequest) throws DataNotFoundException;
    String payment(HttpServletRequest req);
    boolean paymentSuccess(HttpServletRequest req) throws DataNotFoundException;
}
