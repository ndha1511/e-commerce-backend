package com.code.ecommercebackend.services.message;

import com.code.ecommercebackend.dtos.request.message.MessageDto;
import com.code.ecommercebackend.exceptions.DataNotFoundException;
import com.code.ecommercebackend.models.Message;
import com.code.ecommercebackend.services.BaseService;

import java.io.IOException;

public interface MessageService  extends BaseService<Message,String> {
    Message sendMessage(MessageDto messageDto) throws DataNotFoundException, IOException;
}
