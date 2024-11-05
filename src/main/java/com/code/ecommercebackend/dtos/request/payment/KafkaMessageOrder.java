package com.code.ecommercebackend.dtos.request.payment;

import com.code.ecommercebackend.models.Order;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class KafkaMessageOrder {
    private Order order;
    private OrderRequest orderRequest;

}
