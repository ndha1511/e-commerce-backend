package com.code.ecommercebackend.controllers;


import com.code.ecommercebackend.dtos.response.Response;
import com.code.ecommercebackend.dtos.response.ResponseSuccess;
import com.code.ecommercebackend.models.Conversation;
import com.code.ecommercebackend.services.room.RoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("${api.prefix}/rooms")
@RequiredArgsConstructor
public class RoomController {
    private final RoomService roomService;

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{email}")
    public Response getRooms(@PathVariable String email,
                            @RequestParam(defaultValue = "1") int pageNo,
                             @RequestParam(defaultValue = "40") int size
                        )
            {
        String emailSearch = "sender=" + email;
        String[] search = {emailSearch};
        String[] sort = {"isSeen:asc"};
        return new ResponseSuccess<>(
                HttpStatus.NO_CONTENT.value(),
                "success",
                roomService.getPageData(pageNo,size,search,sort, Conversation.class)
        );
    }
}
