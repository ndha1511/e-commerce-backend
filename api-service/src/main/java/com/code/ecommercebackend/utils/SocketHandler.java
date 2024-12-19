package com.code.ecommercebackend.utils;

import com.code.ecommercebackend.dtos.response.comment.CommentResponse;
import com.code.ecommercebackend.dtos.response.message.MessageResponse;
import com.code.ecommercebackend.dtos.response.notification.NotificationResponse;
import com.code.ecommercebackend.models.*;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SocketHandler {
    private final SimpMessagingTemplate messagingTemplate;


    public void sendToSocket(Message message) {
        MessageResponse<Message> response = new MessageResponse<>();
        response.setData(message);
        response.setType("message");
        messagingTemplate.convertAndSendToUser(message.getReceiver(),"/queue/messages", response);
    }

    public void sendToSocketSender(Message message) {
        MessageResponse<Message> response = new MessageResponse<>();
        response.setData(message);
        response.setType("message");
        messagingTemplate.convertAndSendToUser(message.getSender(),"/queue/messages", response);
    }

    public void sendCommentToSocket(Comment comment) {
        CommentResponse<Comment> response = new CommentResponse<>();
        response.setData(comment);
        response.setType("comment");
        messagingTemplate.convertAndSend("/topic/"  + comment.getProductId(), response);
    }
    public void sendNotificationToSocket(Notification notification) {
        NotificationResponse<Notification> response = new NotificationResponse<>();
        response.setData(notification);
        response.setType("notification");
        messagingTemplate.convertAndSend("/topic/notification/"  + notification.getUserId(), response);
    }

    public <T> void sendToSocket(T message, String type, String receiver) {
        MessageResponse<T> response = new MessageResponse<>();
        response.setData(message);
        response.setType(type);
        messagingTemplate.convertAndSendToUser(receiver, "/queue/messages", response);

    }


}
