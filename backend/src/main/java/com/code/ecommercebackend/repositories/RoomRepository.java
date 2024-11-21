package com.code.ecommercebackend.repositories;

import com.code.ecommercebackend.models.Conversation;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface RoomRepository extends MongoRepository<Conversation, String> {
    List<Conversation> getRoomsBySender(String sender);
    Optional<Conversation> findBySenderAndReceiver(String sender, String receiver);
    List<Conversation> findAllByConversationId(String roomId);


}
