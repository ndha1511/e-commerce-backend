package com.code.ecommercebackend.services.statistics;

import com.code.ecommercebackend.dtos.response.statistics.Revenue;
import com.code.ecommercebackend.dtos.response.statistics.RevenueMonth;
import com.code.ecommercebackend.exceptions.DataNotFoundException;
import com.code.ecommercebackend.models.*;
import com.code.ecommercebackend.repositories.InventoryRepository;
import com.code.ecommercebackend.repositories.customizations.order.OrderRepositoryCustom;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class StatisticsServiceImpl implements StatisticsService{
    private final InventoryRepository inventoryRepository;
    private final OrderRepositoryCustom orderRepositoryCustom;

    @Override
    public long totalOrder(LocalDateTime startDate, LocalDateTime endDate) {
        return orderRepositoryCustom.countByStartDateEndDate(startDate, endDate);
    }

    @Override
    public Revenue totalRevenue(LocalDateTime startDate, LocalDateTime endDate) throws DataNotFoundException {
        List<Order> orders = orderRepositoryCustom.findByStartDateEndDate(startDate, endDate);

        double revenue = 0;
        double profit = 0;

        for (Order order : orders) {
            double currentRevenue = order.getFinalAmount() - order.getShippingAmount();
            revenue += currentRevenue;
            List<ProductOrder> productResponses = order.getProductOrders();
            for (ProductOrder productOrder : productResponses) {
                List<InventoryOrder> inventoryOrders = productOrder.getInventoryOrders();
                for (InventoryOrder inventoryOrder : inventoryOrders) {
                    Inventory inventory = inventoryRepository.findById(inventoryOrder.getId())
                            .orElseThrow(() -> new DataNotFoundException("Inventory not found"));
                    profit += productOrder.getAmount() - (inventory.getImportPrice() * inventoryOrder.getQuantity());
                }
            }

        }
        Revenue revenueObj = new Revenue();
        revenueObj.setRevenue(revenue);
        revenueObj.setProfit(profit);
        return revenueObj;
    }

    @Override
    public List<Product> bestSellingProduct(LocalDateTime startDate, LocalDateTime endDate, int top) {
        return List.of();
    }

    @Override
    public List<RevenueMonth> revenueYear(int year) throws DataNotFoundException {
        List<Order> orders = orderRepositoryCustom
                .getOrderInYear(year);
        List<RevenueMonth> revenueMonths = new ArrayList<>();
        for (Order order : orders) {
            int month = order.getCreatedAt().getMonth().getValue();
            double profit = 0;
            double revenue = order.getTotalAmount() - order.getShippingAmount();
            List<ProductOrder> productResponses = order.getProductOrders();
            for (ProductOrder productOrder : productResponses) {
                List<InventoryOrder> inventoryOrders = productOrder.getInventoryOrders();
                for (InventoryOrder inventoryOrder : inventoryOrders) {
                    Inventory inventory = inventoryRepository.findById(inventoryOrder.getId())
                            .orElseThrow(() -> new DataNotFoundException("inventory not found"));
                    profit += inventory.getImportPrice() * inventoryOrder.getQuantity();
                }

            }
            RevenueMonth revenueMonthObj = new RevenueMonth();
            revenueMonthObj.setMonth(month);
            revenueMonthObj.setRevenue(revenue);
            revenueMonthObj.setProfit(profit);
            if(!revenueMonths.contains(revenueMonthObj)) {
                revenueMonths.add(revenueMonthObj);
            } else {
                int idx = revenueMonths.indexOf(revenueMonthObj);
                RevenueMonth oldObj = revenueMonths.get(idx);
                revenueMonthObj.addRevenue(oldObj.getRevenue(), oldObj.getProfit());
                revenueMonths.set(idx, revenueMonthObj);
            }

        }
        return revenueMonths;
    }
}
