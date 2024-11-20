package com.code.ecommercebackend.controllers;

import com.code.ecommercebackend.dtos.request.comment.CommentReplyRequest;
import com.code.ecommercebackend.dtos.request.comment.CommentRequest;
import com.code.ecommercebackend.dtos.response.Response;
import com.code.ecommercebackend.dtos.response.ResponseSuccess;
import com.code.ecommercebackend.models.Comment;
import com.code.ecommercebackend.services.comment.CommentService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("${api.prefix}/comments")
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;

    @GetMapping("/{productId}")
    public Response getComments(@RequestParam(defaultValue = "1") int pageNo,
                                @RequestParam(defaultValue = "40") int size,
                                @RequestParam(required = false) String[] search,
                                @RequestParam(required = false) String[] sort,
                                @PathVariable String productId) {
        List<String> searchList = new ArrayList<>();
        searchList.add("productId=" + productId);
        if(search != null) {
            searchList.addAll(Arrays.asList(search));
        }
        search = searchList.toArray(new String[0]);
        return new ResponseSuccess<>(
                HttpStatus.OK.value(),
                "success",
                commentService.getPageData(pageNo, size, search, sort, Comment.class)
        );

    }

    @PostMapping
    public Response createComment(@ModelAttribute @Valid CommentRequest commentRequest, HttpServletRequest request)
    throws Exception {
        return new ResponseSuccess<>(
                HttpStatus.CREATED.value(),
                "success",
                commentService.save(commentRequest, request)
        );
    }
    @PutMapping("/{commentId}")
    public Response replyComment(@PathVariable String commentId , @ModelAttribute CommentReplyRequest commentReplyRequest)
            throws Exception {
        return new ResponseSuccess<>(
                HttpStatus.CREATED.value(),
                "success",
                commentService.reply(commentId, commentReplyRequest)
        );
    }

}
