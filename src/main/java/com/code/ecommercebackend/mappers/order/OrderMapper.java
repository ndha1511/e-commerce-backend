package com.code.ecommercebackend.mappers.order;

import com.code.ecommercebackend.dtos.request.payment.OrderRequest;
import com.code.ecommercebackend.models.Order;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface OrderMapper {
    Order toOrder(OrderRequest orderRequest);
}
