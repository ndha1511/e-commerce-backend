package com.code.ecommercebackend.services.statistics;

import com.code.ecommercebackend.dtos.response.statistics.Revenue;
import com.code.ecommercebackend.dtos.response.statistics.RevenueMonth;
import com.code.ecommercebackend.exceptions.DataNotFoundException;
import com.code.ecommercebackend.models.Product;

import java.time.LocalDateTime;
import java.util.List;


public interface StatisticsService {
    long totalOrder(LocalDateTime startDate, LocalDateTime endDate);
    Revenue totalRevenue(LocalDateTime startDate, LocalDateTime endDate) throws DataNotFoundException;
    List<Product> bestSellingProduct(LocalDateTime startDate, LocalDateTime endDate, int top);
    List<RevenueMonth> revenueYear(int year) throws DataNotFoundException;

}
