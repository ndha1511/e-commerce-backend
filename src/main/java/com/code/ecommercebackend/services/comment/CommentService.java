package com.code.ecommercebackend.services.comment;

import com.code.ecommercebackend.dtos.request.comment.CommentReplyRequest;
import com.code.ecommercebackend.dtos.request.comment.CommentRequest;
import com.code.ecommercebackend.exceptions.DataNotFoundException;
import com.code.ecommercebackend.exceptions.FileNotSupportedException;
import com.code.ecommercebackend.exceptions.FileTooLargeException;
import com.code.ecommercebackend.models.Comment;
import com.code.ecommercebackend.services.BaseService;
import jakarta.servlet.http.HttpServletRequest;

import java.io.IOException;

public interface CommentService extends BaseService<Comment, String> {
    Comment save(CommentRequest commentRequest, HttpServletRequest request) throws DataNotFoundException, FileTooLargeException, FileNotSupportedException, IOException;
    Comment reply(String commentId, CommentReplyRequest commentReplyRequest) throws DataNotFoundException, IOException;
}
