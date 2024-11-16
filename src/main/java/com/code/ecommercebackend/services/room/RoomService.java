package com.code.ecommercebackend.services.room;

import com.code.ecommercebackend.exceptions.DataNotFoundException;
import com.code.ecommercebackend.models.Conversation;
import com.code.ecommercebackend.services.BaseService;

import java.util.List;

public interface RoomService extends BaseService <Conversation , Long>{
    List<Conversation> getRoomsBySender(String sender);
    String getRoomIdBySenderAndReceiver(String sender, String receiver, boolean createIfNotExists) throws DataNotFoundException;
}
