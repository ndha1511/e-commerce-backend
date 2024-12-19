package com.code.ecommercebackend.utils;

import com.code.ecommercebackend.components.LocalDateTimeVN;
import com.code.ecommercebackend.models.Notification;
import com.code.ecommercebackend.models.Order;
import com.code.ecommercebackend.repositories.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class NotificationHandler {

    private final NotificationRepository notificationRepository;

    public Notification saveNotification(Order order){
        Notification notification = new Notification();
        notification.setTime(LocalDateTimeVN.now());
        notification.setTitle("Đơn hàng của bạn đang trong trạng thái"+ order.getOrderStatus());
        notification.setContent("Chưa biết ghi gì ");
        notification.setUserId(order.getUserId());
        notification.setSeen(false);
        notification.setRedirectTo("");
        notificationRepository.save(notification);
        return notification;
    }
}
