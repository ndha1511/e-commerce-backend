package com.code.ecommercebackend.controllers;

import com.code.ecommercebackend.dtos.response.Response;
import com.code.ecommercebackend.dtos.response.ResponseSuccess;
import com.code.ecommercebackend.exceptions.DataNotFoundException;
import com.code.ecommercebackend.models.Notification;
import com.code.ecommercebackend.services.notification.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("${api.prefix}/notifications")
@RequiredArgsConstructor
public class NotificationController {
    private final NotificationService notificationService;

    @GetMapping("/{userId}")
    public Response getNotifications( @PathVariable String userId ,
                                      @RequestParam(defaultValue = "1") int pageNo,
                              @RequestParam(defaultValue = "40") int size,
                              @RequestParam(required = false) String[] sort) {
        String userSearch = "userId=" + userId;
        String[] search = {userSearch};
        return new ResponseSuccess<>(
                HttpStatus.OK.value(),
                "success",
                notificationService.getPageData(pageNo,size,search,sort, Notification.class)
        );
    }
    @PutMapping("/{id}")
    public Response checkSeen(@PathVariable String id) throws DataNotFoundException {
        notificationService.checkSeen(id);
        return new ResponseSuccess<>(
                HttpStatus.OK.value(),
                "success"
        );
    }
}
