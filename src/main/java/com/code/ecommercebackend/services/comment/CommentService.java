package com.code.ecommercebackend.services.comment;

import com.code.ecommercebackend.dtos.request.comment.CommentRequest;
import com.code.ecommercebackend.exceptions.DataNotFoundException;
import com.code.ecommercebackend.exceptions.FileNotSupportedException;
import com.code.ecommercebackend.exceptions.FileTooLargeException;
import com.code.ecommercebackend.models.Comment;
import com.code.ecommercebackend.services.BaseService;

import java.io.IOException;

public interface CommentService extends BaseService<Comment, String> {
    Comment save(CommentRequest commentRequest) throws DataNotFoundException, FileTooLargeException, FileNotSupportedException, IOException;
}
