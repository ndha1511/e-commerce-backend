package com.code.ecommercebackend.mappers.comment;

import com.code.ecommercebackend.dtos.request.comment.CommentRequest;
import com.code.ecommercebackend.exceptions.DataNotFoundException;
import com.code.ecommercebackend.exceptions.FileNotSupportedException;
import com.code.ecommercebackend.exceptions.FileTooLargeException;
import com.code.ecommercebackend.models.Comment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.io.IOException;

@Mapper(componentModel = "spring", uses = CommentMapperHelper.class)
public interface CommentMapper {
    @Mapping(source = "files", target = "commentMedia", qualifiedByName = "uploadCommentFile")
    @Mapping(source = "userId", target = "user", qualifiedByName = "mapCommentUser")
    Comment toComment(CommentRequest commentRequest) throws FileTooLargeException, FileNotSupportedException, IOException, DataNotFoundException;
}
