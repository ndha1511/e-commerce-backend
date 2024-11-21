package com.code.ecommercebackend.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;

@Document(collection = "conversations")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Conversation extends BaseModel {
    @Field(name = "conversation_id")
    private String conversationId;
    private String sender;
    private String receiver;
    @Field(name = "receiver_name")
    private String receiverName;
    @Field(name = "send_date")
    private LocalDateTime sendDate;
    private  Object lastMessageSender;
    private int count;
    private String avatarSender;
    private String avatarReceiver;
    private boolean seen;
}
