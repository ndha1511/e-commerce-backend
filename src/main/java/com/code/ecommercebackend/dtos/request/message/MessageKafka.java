package com.code.ecommercebackend.dtos.request.message;

import com.code.ecommercebackend.models.Message;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
public class MessageKafka {
    Message message;
    byte[] file;

}
