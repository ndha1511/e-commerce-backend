package com.code.ecommercebackend.repositories.customizations.order;

import com.code.ecommercebackend.models.Order;

import java.time.LocalDateTime;
import java.util.List;

public interface OrderRepositoryCustom {
    List<Order> getOrderInYear(int year);
    long countByStartDateEndDate(LocalDateTime startDate, LocalDateTime endDate);
    List<Order> findByStartDateEndDate(LocalDateTime startDate, LocalDateTime endDate);
}
