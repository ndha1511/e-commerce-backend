package com.code.ecommercebackend.services.room;


import com.code.ecommercebackend.exceptions.DataNotFoundException;
import com.code.ecommercebackend.models.Conversation;
import com.code.ecommercebackend.repositories.RoomRepository;
import com.code.ecommercebackend.repositories.UserRepository;
import com.code.ecommercebackend.services.BaseServiceImpl;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RoomServiceImpl extends BaseServiceImpl<Conversation, String> implements RoomService {

    private final RoomRepository roomRepository;
    private final UserRepository userRepository;

    public RoomServiceImpl(MongoRepository<Conversation, String> repository, RoomRepository roomRepository, UserRepository userRepository) {
        super(repository);
        this.roomRepository = roomRepository;
        this.userRepository = userRepository;
    }

    @Override
    public List<Conversation> getRoomsBySender(String sender) {
        return List.of();
    }

    @Override
    public String getRoomIdBySenderAndReceiver(String sender, String receiver, boolean createIfNotExists) throws DataNotFoundException {
        String roomId = concatRoomId(sender, receiver);
        Optional<Conversation> optional = roomRepository.findBySenderAndReceiver(sender, receiver);
        if (!userRepository.existsByEmail(sender)) throw new DataNotFoundException("sender not found");
        if (!userRepository.existsByEmail(receiver)) throw new DataNotFoundException("receiver not found");
        if (optional.isEmpty()) {
            if (!createIfNotExists) {
                throw new DataNotFoundException("Room not found");
            }
            saveRoom(roomId, sender, receiver);
            saveRoom(roomId, receiver, sender);
        } else {
            roomId = optional.get().getConversationId();
        }
        return roomId;
    }

    @Override
    public List<Conversation> updateRoomById(String roomId) throws DataNotFoundException {
        List<Conversation> conversations = roomRepository.findAllByConversationId(roomId);
        if (conversations == null || conversations.isEmpty()) {
            throw new DataNotFoundException("No conversations found for room id: " + roomId);
        }
        conversations.forEach(conversation -> {
            conversation.setSeen(true);
            conversation.setCount(0);
        });
        roomRepository.saveAll(conversations);
        return conversations;
    }


    private String concatRoomId(String sender, String receiver) {
        return sender + "_" + receiver;
    }

    private void saveRoom(String roomId, String sender, String receiver) {
        Conversation roomChat = new Conversation();
        roomChat.setConversationId(roomId);
        roomChat.setSender(sender);
        roomChat.setReceiver(receiver);
        roomRepository.save(roomChat);
    }
}

