package com.code.ecommercebackend.controllers;

import com.code.ecommercebackend.dtos.response.Response;
import com.code.ecommercebackend.dtos.response.ResponseSuccess;
import com.code.ecommercebackend.models.User;
import com.code.ecommercebackend.services.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("${api.prefix}/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/{id}")
    public Response getUserById(@PathVariable String id)
    throws Exception {
        return new ResponseSuccess<>(
                HttpStatus.OK.value(),
                "success",
                userService.findById(id)
        );
    }

    @GetMapping
    public Response getAllUsers(@RequestParam(defaultValue = "1") int pageNo,
                                @RequestParam(defaultValue = "40") int size,
                                @RequestParam(required = false) String[] search,
                                @RequestParam(required = false) String[] sort) {
        return new ResponseSuccess<>(
                HttpStatus.OK.value(),
                "success",
                userService.getPageData(pageNo, size, search, sort, User.class)
        );
    }

}
