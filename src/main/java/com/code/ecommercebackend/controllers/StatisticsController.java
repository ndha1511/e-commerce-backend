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

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

@RestController
@RequestMapping("${api.prefix}/statistics")
@RequiredArgsConstructor
public class StatisticsController {

    private final StatisticsService statisticsService;

    @GetMapping("/total-order")
    public Response getTotalOrder(@RequestParam(defaultValue = "-2208988800000", required = false) long startDate,
                                  @RequestParam(defaultValue = "4102444800000", required = false) long endDate) {
        LocalDateTime strDate = Instant.ofEpochMilli(startDate)
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();

        LocalDateTime eDate = Instant.ofEpochMilli(endDate)
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();

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
}
