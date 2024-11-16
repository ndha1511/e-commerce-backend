package com.code.ecommercebackend.services.comment;

import com.code.ecommercebackend.dtos.request.comment.CommentRequest;
import com.code.ecommercebackend.exceptions.DataNotFoundException;
import com.code.ecommercebackend.exceptions.FileNotSupportedException;
import com.code.ecommercebackend.exceptions.FileTooLargeException;
import com.code.ecommercebackend.mappers.comment.CommentMapper;
import com.code.ecommercebackend.models.*;
import com.code.ecommercebackend.repositories.*;
import com.code.ecommercebackend.services.BaseServiceImpl;
import com.code.ecommercebackend.services.common.CommonFunction;
import com.code.ecommercebackend.utils.CookieHandler;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class CommentServiceImpl extends BaseServiceImpl<Comment, String> implements CommentService {
    private final CommentMapper commentMapper;
    private final OrderRepository orderRepository;
    private final CommentRepository commentRepository;
    private final ProductRepository productRepository;
    private final CookieHandler cookieHandler;
    private final CommonFunction commonFunction;


    public CommentServiceImpl(MongoRepository<Comment, String> repository,
                              CommentMapper commentMapper,
                              OrderRepository orderRepository,
                              CommentRepository commentRepository,
                              ProductRepository productRepository,
                              CookieHandler cookieHandler,
                              CommonFunction commonFunction) {
        super(repository);
        this.commentMapper = commentMapper;
        this.orderRepository = orderRepository;
        this.commentRepository = commentRepository;
        this.productRepository = productRepository;
        this.cookieHandler = cookieHandler;
        this.commonFunction = commonFunction;

    }


    @Override
    public Comment save(CommentRequest commentRequest, HttpServletRequest request)
            throws DataNotFoundException, FileTooLargeException, FileNotSupportedException, IOException {
        Comment comment = commentMapper.toComment(commentRequest);
        comment.setCommentDate(LocalDateTime.now());
        Order order = orderRepository.findById(commentRequest.getOrderId())
                .orElseThrow(() -> new DataNotFoundException("Order not found"));
        List<ProductOrder> productOrders = order.getProductOrders();
        String token = cookieHandler.getCookie(request, "access_token");
        commonFunction.saveUserBehavior(token, 1,
                commentRequest.getProductNumId(),
                commentRequest.getRating());
        productOrders.forEach(po -> {
            if (po.getProductId().equals(commentRequest.getProductId())) {
                if(po.getAttributes().equals(commentRequest.getAttributes())) {
                    po.setCommented(true);
                }
            }
        });
        calcRating(comment);
        order.setProductOrders(productOrders);
        orderRepository.save(order);
        return commentRepository.save(comment);

    }

    private void calcRating(Comment comment) throws DataNotFoundException {
        Product product = productRepository.findById(comment.getProductId())
                .orElseThrow(() -> new DataNotFoundException("product not found"));
        int numberOfRating = product.getReviews();
        float totalRating = numberOfRating * product.getRating();
        float newTotalRating = totalRating + comment.getRating();
        float newRating = newTotalRating/(numberOfRating + 1);
        product.setRating(newRating);
        product.setReviews(numberOfRating + 1);
        productRepository.save(product);

    }


}
