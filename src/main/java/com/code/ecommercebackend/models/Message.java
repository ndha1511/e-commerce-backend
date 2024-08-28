package com.code.ecommercebackend.models;

import com.code.ecommercebackend.models.enums.MessageType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

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
    private LocalDateTime sendDate;
    private MessageType messageType;
}
