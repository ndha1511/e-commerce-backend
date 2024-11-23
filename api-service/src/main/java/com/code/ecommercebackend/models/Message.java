package com.code.ecommercebackend.models;

import com.code.ecommercebackend.models.enums.MessageStatus;
import com.code.ecommercebackend.models.enums.MessageType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;

@Document(collection = "messages")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Message extends BaseModel {
    private String sender;
    private String receiver;
    private Object content;
    @Field(name = "send_date")
    private LocalDateTime sendDate;
    @Field(name = "message_type")
    private MessageType messageType;
    @Field(name = "message_status")
    private MessageStatus messageStatus;
    @Field(name = "room_id")
    private String roomId;
}
