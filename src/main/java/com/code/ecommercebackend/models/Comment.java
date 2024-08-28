package com.code.ecommercebackend.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bson.types.ObjectId;
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
    private LocalDateTime commentDate;
    @Field(name = "comment_media")
    private List<CommentMedia> commentMedia;
    private int rating;
    @Field(name = "product_id")
    private String productId;
    @DocumentReference
    @Field(name = "user_id")
    private User user;
}
