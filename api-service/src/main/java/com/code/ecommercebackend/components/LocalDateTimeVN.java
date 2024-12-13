package com.code.ecommercebackend.components;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

public class LocalDateTimeVN {

    public static LocalDateTime now() {
        LocalDateTime now = LocalDateTime.now();
        ZonedDateTime vietnamTime = now.atZone(ZoneId.of("Asia/Ho_Chi_Minh"));
        return vietnamTime.toLocalDateTime();
    }

}
