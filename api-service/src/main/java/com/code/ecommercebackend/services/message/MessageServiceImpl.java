package com.code.ecommercebackend.services.message;


import com.code.ecommercebackend.components.LocalDateTimeVN;
import com.code.ecommercebackend.dtos.request.message.MessageDto;
import com.code.ecommercebackend.dtos.request.message.MessageKafka;
import com.code.ecommercebackend.exceptions.DataNotFoundException;
import com.code.ecommercebackend.models.Conversation;
import com.code.ecommercebackend.models.Message;
import com.code.ecommercebackend.models.User;
import com.code.ecommercebackend.models.enums.MessageStatus;
import com.code.ecommercebackend.models.enums.MessageType;
import com.code.ecommercebackend.repositories.RoomRepository;
import com.code.ecommercebackend.repositories.UserRepository;
import com.code.ecommercebackend.services.BaseServiceImpl;
import com.code.ecommercebackend.services.room.RoomService;
import com.code.ecommercebackend.utils.SocketHandler;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Set;

@Service
public class MessageServiceImpl extends BaseServiceImpl<Message,String> implements MessageService{


    private final RoomRepository roomRepository;
    private final RoomService roomservice;
    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final SocketHandler socketHandler;
    private final UserRepository userRepository;

    public MessageServiceImpl(MongoRepository<Message,
            String> repository, RoomRepository roomRepository, RoomService roomservice, KafkaTemplate<String, Object> kafkaTemplate, SocketHandler socketHandler, UserRepository userRepository) {
        super(repository);

        this.roomRepository = roomRepository;
        this.roomservice = roomservice;
        this.kafkaTemplate = kafkaTemplate;
        this.socketHandler = socketHandler;
        this.userRepository = userRepository;
    }


    @Override
    public Message sendMessage(MessageDto messageDto) throws DataNotFoundException, IOException {
        Message message = new Message();
        message.setSender(messageDto.getSender());
        message.setReceiver(messageDto.getReceiver());
        message.setMessageType(MessageType.TEXT);
        message.setSendDate(LocalDateTimeVN.now());
        message.setContent(messageDto.getMessage());
        message.setMessageStatus(MessageStatus.SENDING);
        message.setRoomId(roomservice
                .getRoomIdBySenderAndReceiver(messageDto.getSender(),
                        messageDto.getReceiver(),
                        true));

        if(messageDto.getFile() != null){
            message.setMessageType(getMessageType(messageDto.getFile()));
            super.save(message);
            kafkaTemplate.send("send-file-topic", new MessageKafka(message, messageDto.getFile().getBytes()));
        }

        Conversation roomReceiver =roomRepository.findBySenderAndReceiver(message.getReceiver(), message.getSender())
                .orElseThrow(()-> new DataNotFoundException("There is no message with that sender and receiver"));
        roomReceiver.setSeen(false);
        roomReceiver.setCount(roomReceiver.getCount() +1);
        Optional<User> userReceiver = userRepository.findByEmail(message.getReceiver());
        userReceiver.ifPresent(value -> roomReceiver.setAvatarReceiver(value.getAvatar()));
        roomReceiver.setLastMessageSender(message.getContent());
        roomReceiver.setSendDate(LocalDateTimeVN.now());
        Conversation roomSender =roomRepository.findBySenderAndReceiver(message.getSender(), message.getReceiver())
                .orElseThrow(()-> new DataNotFoundException("There is no message with that sender and receiver"));
        roomSender.setSeen(false);
        roomSender.setLastMessageSender(message.getContent());
        Optional<User> userSender = userRepository.findByEmail(message.getSender());
        userSender.ifPresent(value -> roomReceiver.setAvatarReceiver(value.getAvatar()));
        roomSender.setSendDate(LocalDateTimeVN.now());
        roomRepository.save(roomReceiver);
        roomRepository.save(roomSender);
        if(messageDto.getFile() == null){
            message.setMessageStatus(MessageStatus.SENT);
            super.save(message);
            socketHandler.sendToSocket(message);
            socketHandler.sendToSocketSender(message);
        }

        return message;
    }
    private MessageType getMessageType(MultipartFile file) throws DataNotFoundException {
        String filename = file.getOriginalFilename();
        Set<String> IMAGE_EXTENSIONS = Set.of("jpg", "png", "gif", "jpeg", "jfif");
        Set<String> VIDEO_EXTENSIONS = Set.of("mp4", "avi", "mov", "mkv");
        if (filename == null) {
            return MessageType.TEXT;
        }
        String extension = getFileExtension(filename);
        if(IMAGE_EXTENSIONS.contains(extension.toLowerCase())) {
            return MessageType.IMAGE;
        }
        if(VIDEO_EXTENSIONS.contains(extension.toLowerCase())) {
            return MessageType.VIDEO;
        }
        throw new DataNotFoundException("only allow sending image or video");
    }

    private String getFileExtension(String filename) {
        int lastDotIndex = filename.lastIndexOf('.');
        if (lastDotIndex >= 0 && lastDotIndex < filename.length() - 1) {
            return filename.substring(lastDotIndex + 1);
        }
        return "";
    }

}


