package com.code.ecommercebackend.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;
import java.util.List;

@Document(collection = "comments")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Comment extends BaseModel {
    private String content;
    @Field(name = "comment_date")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private LocalDateTime commentDate;
    @Field(name = "comment_media")
    private List<CommentMedia> commentMedia;
    private int rating;
    @Field(name = "product_id")
    private String productId;
    private List<String> attributes;
    @DocumentReference
    @Field(name = "user_id")
    private User user;
    @Field(name = "reply_comment")
    private String replyComment;
}
