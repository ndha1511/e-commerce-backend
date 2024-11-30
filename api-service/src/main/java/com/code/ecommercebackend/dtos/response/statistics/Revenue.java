package com.code.ecommercebackend.dtos.response.statistics;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Revenue {
    protected double revenue;
    protected double profit;

    public void addRevenue(double revenue, double profit) {
        this.revenue += revenue;
        this.profit += profit;
    }


}
