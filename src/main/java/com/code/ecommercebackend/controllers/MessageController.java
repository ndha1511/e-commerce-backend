package com.code.ecommercebackend.controllers;


import com.code.ecommercebackend.dtos.request.message.MessageDto;
import com.code.ecommercebackend.dtos.response.Response;
import com.code.ecommercebackend.dtos.response.ResponseSuccess;
import com.code.ecommercebackend.exceptions.DataNotFoundException;
import com.code.ecommercebackend.models.Message;
import com.code.ecommercebackend.services.message.MessageService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("${api.prefix}/messages")
@RequiredArgsConstructor
public class MessageController {
    private final MessageService messageService;

    @PreAuthorize("isAuthenticated()")
    @PostMapping
    public Response createMessage(@Valid @ModelAttribute MessageDto messageDto)
            throws Exception {
        return new ResponseSuccess<>(
                HttpStatus.NO_CONTENT.value(),
                "success",
                messageService.sendMessage(messageDto)
        );
    }

    @GetMapping("/{roomId}")
    public Response getMessage(@PathVariable String roomId,
                               @RequestParam(defaultValue = "1") int pageNo,
                               @RequestParam(defaultValue = "10") int size) throws DataNotFoundException, IOException {
        String roomSearch = "roomId=" + roomId;
        String[] search = {roomSearch};
        String[] sort = {"sendDate:desc"};
        return new ResponseSuccess<>(
                HttpStatus.OK.value(),
                "success",
                messageService.getPageData(pageNo, size, search, sort, Message.class)
        );
    }
}
