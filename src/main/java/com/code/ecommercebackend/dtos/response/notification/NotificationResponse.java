package com.code.ecommercebackend.dtos.response.notification;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NotificationResponse<T> {
    private T data;
    private String type;
}
