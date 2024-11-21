package com.code.ecommercebackend.services.message;

import com.code.ecommercebackend.dtos.request.message.MessageKafka;
import com.code.ecommercebackend.exceptions.DataNotFoundException;
import com.code.ecommercebackend.models.Conversation;
import com.code.ecommercebackend.models.Message;
import com.code.ecommercebackend.models.enums.MessageStatus;
import com.code.ecommercebackend.repositories.MessageRepository;
import com.code.ecommercebackend.repositories.RoomRepository;
import com.code.ecommercebackend.utils.S3Upload;
import com.code.ecommercebackend.utils.SocketHandler;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;


import java.io.IOException;

@Component
@RequiredArgsConstructor
public class MessageListener {
    private final S3Upload s3Upload;
    private final ObjectMapper objectMapper;
    private final MessageRepository messageRepository;
    private final SocketHandler socketHandler;
    private final RoomRepository roomRepository;

    @KafkaListener(topics = "send-file-topic", groupId = "send-file-topic")
    public void uploadFileAsync(ConsumerRecord<String, byte[]> record) throws IOException, DataNotFoundException {
        MessageKafka messageKafka = getMessage(record);
        Message message = messageKafka.getMessage();
        byte[] fileResource = messageKafka.getFile();

        message.setContent(s3Upload.uploadFile(fileResource, "abc"));
        message.setMessageStatus(MessageStatus.SENT);
        messageRepository.save(message);
        Conversation roomReceiver =roomRepository.findBySenderAndReceiver(message.getReceiver(), message.getSender())
                .orElseThrow(()-> new DataNotFoundException("There is no message with that sender and receiver"));
        roomReceiver.setLastMessageSender(message.getMessageType());
        Conversation roomSender =roomRepository.findBySenderAndReceiver(message.getSender(), message.getReceiver())
                .orElseThrow(()-> new DataNotFoundException("There is no message with that sender and receiver"));
        roomSender.setLastMessageSender(message.getMessageType());
        roomRepository.save(roomReceiver);
        roomRepository.save(roomSender);
        socketHandler.sendToSocket(message);
        socketHandler.sendToSocketSender(message);
    }


    private MessageKafka getMessage(ConsumerRecord<String, byte[]> record) throws IOException {
        byte[] messageBytes = record.value();
        return objectMapper.readValue(messageBytes, MessageKafka.class);
    }
}
