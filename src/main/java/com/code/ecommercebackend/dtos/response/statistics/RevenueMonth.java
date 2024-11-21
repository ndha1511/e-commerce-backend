package com.code.ecommercebackend.dtos.response.statistics;

import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Getter
@Setter
public class RevenueMonth {
    private int month;
    private double profit;
    private double revenue;

    public void addRevenue(double revenue, double profit) {
        this.revenue += revenue;
        this.profit += profit;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RevenueMonth that = (RevenueMonth) o;
        return month == that.month;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(month);
    }
}
