package com.code.ecommercebackend.dtos.response.statistics;


import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Getter
@Setter
public class RevenueDay extends Revenue {
    private String dayMonth;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RevenueDay that = (RevenueDay) o;
        return Objects.equals(dayMonth, that.dayMonth);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(dayMonth);
    }
}
