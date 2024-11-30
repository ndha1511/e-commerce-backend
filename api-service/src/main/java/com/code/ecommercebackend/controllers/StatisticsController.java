package com.code.ecommercebackend.controllers;

import com.code.ecommercebackend.dtos.response.Response;
import com.code.ecommercebackend.dtos.response.ResponseSuccess;
import com.code.ecommercebackend.services.statistics.StatisticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


import java.time.LocalDate;
import java.time.LocalDateTime;


@RestController
@RequestMapping("${api.prefix}/statistics")
@RequiredArgsConstructor
public class StatisticsController {

    private final StatisticsService statisticsService;

    @GetMapping("/total-order")
    public Response getTotalOrder(LocalDate startDate,
                                  LocalDate endDate) {
        LocalDateTime strDate = startDate.atStartOfDay();

        LocalDateTime eDate = endDate.atStartOfDay();

        return new ResponseSuccess<>(
                HttpStatus.OK.value(),
                "success",
                statisticsService.totalOrder(strDate, eDate)
        );
    }

    @GetMapping("/revenue-in-year")
    public Response getRevenueInYear(@RequestParam int year)
            throws Exception {
        return new ResponseSuccess<>(
                HttpStatus.OK.value(),
                "success",
                statisticsService.revenueYear(year)
        );
    }

    @GetMapping("/revenue-day")
    public Response getRevenueDay(LocalDate startDate,
                                  LocalDate endDate) throws Exception {
        LocalDateTime strDate = startDate.atStartOfDay();

        LocalDateTime eDate = endDate.atStartOfDay();
        return new ResponseSuccess<>(
                HttpStatus.OK.value(),
                "success",
                statisticsService.revenueDay(strDate, eDate)
        );
    }

    @GetMapping("/total-revenue")
    public Response getTotalRevenue(LocalDate startDate,
                                    LocalDate endDate) throws Exception {
        LocalDateTime strDate = startDate.atStartOfDay();

        LocalDateTime eDate = endDate.atStartOfDay();
        return new ResponseSuccess<>(
                HttpStatus.OK.value(),
                "success",
                statisticsService.totalRevenue(strDate, eDate)
        );

    }

    @GetMapping("/top-product")
    public Response getTopProduct(LocalDate startDate,
                                  LocalDate endDate,
                                  @RequestParam(required = false, defaultValue = "10") int topN) throws Exception {
        LocalDateTime strDate = startDate.atStartOfDay();

        LocalDateTime eDate = endDate.atStartOfDay();
        return new ResponseSuccess<>(
                HttpStatus.OK.value(),
                "success",
                statisticsService.bestSellingProduct(strDate, eDate, topN)
        );
    }

    @GetMapping("/top-user")
    public Response getTopUser(LocalDate startDate,
                               LocalDate endDate,
                               @RequestParam(required = false, defaultValue = "10") int topN) throws Exception {
        LocalDateTime strDate = startDate.atStartOfDay();

        LocalDateTime eDate = endDate.atStartOfDay();
        return new ResponseSuccess<>(
                HttpStatus.OK.value(),
                "success",
                statisticsService.topUserBuyer(strDate, eDate, topN)
        );
    }
}
