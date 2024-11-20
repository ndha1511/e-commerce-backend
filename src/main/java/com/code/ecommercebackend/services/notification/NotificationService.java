package com.code.ecommercebackend.services.notification;

import com.code.ecommercebackend.exceptions.DataNotFoundException;
import com.code.ecommercebackend.models.Notification;
import com.code.ecommercebackend.services.BaseService;

public interface NotificationService extends BaseService<Notification, String> {
    void checkSeen(String id) throws DataNotFoundException;
}
