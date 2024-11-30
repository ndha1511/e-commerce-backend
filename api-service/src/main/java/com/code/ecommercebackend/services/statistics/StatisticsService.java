package com.code.ecommercebackend.services.statistics;

import com.code.ecommercebackend.dtos.response.product.ProductSelling;
import com.code.ecommercebackend.dtos.response.statistics.Revenue;
import com.code.ecommercebackend.dtos.response.statistics.RevenueDay;
import com.code.ecommercebackend.dtos.response.statistics.RevenueMonth;
import com.code.ecommercebackend.dtos.response.user.UserAmount;
import com.code.ecommercebackend.exceptions.DataNotFoundException;

import java.time.LocalDateTime;
import java.util.List;


public interface StatisticsService {
    long totalOrder(LocalDateTime startDate, LocalDateTime endDate);
    Revenue totalRevenue(LocalDateTime startDate, LocalDateTime endDate) throws DataNotFoundException;
    List<ProductSelling> bestSellingProduct(LocalDateTime startDate, LocalDateTime endDate, int top) throws DataNotFoundException;
    List<RevenueMonth> revenueYear(int year) throws DataNotFoundException;
    List<RevenueDay> revenueDay(LocalDateTime startDate, LocalDateTime endDate) throws DataNotFoundException;
    List<UserAmount> topUserBuyer(LocalDateTime startDate, LocalDateTime endDate, int top) throws DataNotFoundException;
}
