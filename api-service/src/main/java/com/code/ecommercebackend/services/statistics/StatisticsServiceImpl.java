package com.code.ecommercebackend.services.statistics;

import com.code.ecommercebackend.dtos.response.product.ProductSelling;
import com.code.ecommercebackend.dtos.response.statistics.Revenue;
import com.code.ecommercebackend.dtos.response.statistics.RevenueDay;
import com.code.ecommercebackend.dtos.response.statistics.RevenueMonth;
import com.code.ecommercebackend.dtos.response.statistics.TopProductSelling;
import com.code.ecommercebackend.dtos.response.user.UserAmount;
import com.code.ecommercebackend.exceptions.DataNotFoundException;
import com.code.ecommercebackend.models.*;
import com.code.ecommercebackend.repositories.InventoryRepository;
import com.code.ecommercebackend.repositories.ProductRepository;
import com.code.ecommercebackend.repositories.UserRepository;
import com.code.ecommercebackend.repositories.customizations.order.OrderRepositoryCustom;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StatisticsServiceImpl implements StatisticsService{
    private final InventoryRepository inventoryRepository;
    private final OrderRepositoryCustom orderRepositoryCustom;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

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
                    Inventory inventory = inventoryRepository.findById(inventoryOrder.getInventoryId())
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
    public List<ProductSelling> bestSellingProduct(LocalDateTime startDate, LocalDateTime endDate, int top) throws DataNotFoundException {
        Map<String, Long> productCount = new HashMap<>();
        List<Order> orders = orderRepositoryCustom.findByStartDateEndDate(startDate, endDate);
        for (Order order : orders) {
            List<ProductOrder> productResponses = order.getProductOrders();
            for (ProductOrder productOrder : productResponses) {
                String productId = productOrder.getProductId();
                Long quantity = productCount.getOrDefault(productId, 0L);
                productCount.put(productId, quantity + productOrder.getQuantity());
            }
        }
        List<ProductSelling> productSelling = new ArrayList<>();

        Map<String, Long> sortedProductCount = productCount.entrySet().stream()
                .sorted((e1, e2) -> Long.compare(e2.getValue(), e1.getValue()))
                .limit(top)
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (oldValue, newValue) -> oldValue,
                        LinkedHashMap::new
                ));

        for (Map.Entry<String, Long> entry : sortedProductCount.entrySet()) {
            String productId = entry.getKey();
            Product product = productRepository.findById(productId)
                    .orElseThrow(() -> new DataNotFoundException("product not found"));
            Long quantity = entry.getValue();
            ProductSelling productSellingObj = new ProductSelling();
            productSellingObj.setProduct(product);
            productSellingObj.setQuantity(quantity);
            productSelling.add(productSellingObj);
        }

        return productSelling;
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
                    Inventory inventory = inventoryRepository.findById(inventoryOrder.getInventoryId())
                            .orElseThrow(() -> new DataNotFoundException("inventory not found"));
                    profit += productOrder.getAmount() - (inventory.getImportPrice() * inventoryOrder.getQuantity());
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

    @Override
    public List<RevenueDay> revenueDay(LocalDateTime startDate, LocalDateTime endDate) throws DataNotFoundException {
        List<Order> orders = orderRepositoryCustom.findByStartDateEndDate(startDate, endDate);
        List<RevenueDay> revenueDays = new ArrayList<>();
        for (Order order : orders) {
            String dayMonth = order.getCreatedAt().format(DateTimeFormatter.ofPattern("dd-MM"));
            double profit = 0;
            double revenue = order.getTotalAmount() - order.getShippingAmount();
            List<ProductOrder> productResponses = order.getProductOrders();
            for (ProductOrder productOrder : productResponses) {
                List<InventoryOrder> inventoryOrders = productOrder.getInventoryOrders();
                for (InventoryOrder inventoryOrder : inventoryOrders) {
                    Inventory inventory = inventoryRepository.findById(inventoryOrder.getInventoryId())
                            .orElseThrow(() -> new DataNotFoundException("inventory not found"));
                    profit += productOrder.getAmount() - (inventory.getImportPrice() * inventoryOrder.getQuantity());
                }
            }
            RevenueDay revenueDayObj = new RevenueDay();
            revenueDayObj.setDayMonth(dayMonth);
            revenueDayObj.setRevenue(revenue);
            revenueDayObj.setProfit(profit);
            if(!revenueDays.contains(revenueDayObj)) {
                revenueDays.add(revenueDayObj);
            } else {
                int idx = revenueDays.indexOf(revenueDayObj);
                RevenueDay oldObj = revenueDays.get(idx);
                revenueDayObj.addRevenue(oldObj.getRevenue(), oldObj.getProfit());
                revenueDays.set(idx, revenueDayObj);
            }

        }
        return revenueDays;
    }

    @Override
    public List<UserAmount> topUserBuyer(LocalDateTime startDate, LocalDateTime endDate, int top) throws DataNotFoundException {
        Map<String, Double> userCount = new HashMap<>();
        List<Order> orders = orderRepositoryCustom.findByStartDateEndDate(startDate, endDate);
        for (Order order : orders) {
            String userId = order.getUserId();
            Double amount = order.getFinalAmount();
            Double oldAmount = userCount.getOrDefault(userId, 0.0);
            userCount.put(userId, oldAmount + amount);
        }
        Map<String, Double> sortedUserCount = userCount.entrySet().stream()
                .sorted((e1, e2) -> Double.compare(e2.getValue(), e1.getValue()))
                .limit(top)
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (oldValue, newValue) -> oldValue,
                        LinkedHashMap::new
                ));
        List<UserAmount> userAmounts = new ArrayList<>();
        for(Map.Entry<String, Double> entry : sortedUserCount.entrySet()) {
            String userId = entry.getKey();
            Double amount = entry.getValue();
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new DataNotFoundException("user not found"));
            UserAmount userAmount = new UserAmount();
            userAmount.setAmount(amount);
            userAmount.setUser(user);
            userAmounts.add(userAmount);
        }
        return userAmounts;
    }

    @Override
    public List<ProductSelling> topBestSellingAndOutOfStock() throws DataNotFoundException {
        return getProductSelling("top");
    }

    @Override
    public List<ProductSelling> productSlowSelling() throws DataNotFoundException {
        return getProductSelling("");
    }

    private List<ProductSelling> getProductSelling(String mode) throws DataNotFoundException {
        LocalDateTime now = LocalDateTime.now().minusMonths(3);
        List<TopProductSelling> topProductSelling;
        if(mode.equals("top")) {
            topProductSelling = inventoryRepository.getTop5BestSelling(now);
        } else {
            topProductSelling = inventoryRepository.findBottom5ProductsBySalesAndLowImportQuantity(now);
        }
        List<ProductSelling> productSelling = new ArrayList<>();
        for(TopProductSelling p: topProductSelling) {
            Product product = productRepository.findById(p.getId())
                    .orElseThrow(() -> new DataNotFoundException("product not found"));
            ProductSelling pSelling = new ProductSelling();
            pSelling.setProduct(product);
            pSelling.setQuantity(p.getTotalSaleQuantity());
            pSelling.setImportQuantity(p.getTotalImportQuantity());
            productSelling.add(pSelling);
        }

        return productSelling;
    }
}
