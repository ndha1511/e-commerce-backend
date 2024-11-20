package com.code.ecommercebackend.services.notification;

import com.code.ecommercebackend.exceptions.DataNotFoundException;
import com.code.ecommercebackend.models.Notification;
import com.code.ecommercebackend.repositories.NotificationRepository;
import com.code.ecommercebackend.services.BaseServiceImpl;
import com.code.ecommercebackend.utils.SocketHandler;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Service;

@Service
public class NotificationServiceImpl extends BaseServiceImpl<Notification, String> implements  NotificationService {
    private final NotificationRepository notificationRepository;
    private final SocketHandler socketHandler;

    public NotificationServiceImpl(MongoRepository<Notification, String> repository, NotificationRepository notificationRepository, SocketHandler socketHandler) {
        super(repository);
        this.notificationRepository = notificationRepository;
        this.socketHandler = socketHandler;
    }

    @Override
    public void checkSeen(String id) throws DataNotFoundException {
        Notification notification = notificationRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("Notification not found"));
        notification.setSeen(true);
        socketHandler.sendNotificationToSocket(notification);
        notificationRepository.save(notification);
    }
}
