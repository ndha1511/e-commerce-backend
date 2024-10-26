package com.code.ecommercebackend.services.comment;

import com.code.ecommercebackend.dtos.request.comment.CommentRequest;
import com.code.ecommercebackend.exceptions.DataNotFoundException;
import com.code.ecommercebackend.exceptions.FileNotSupportedException;
import com.code.ecommercebackend.exceptions.FileTooLargeException;
import com.code.ecommercebackend.mappers.comment.CommentMapper;
import com.code.ecommercebackend.models.Comment;
import com.code.ecommercebackend.models.Order;
import com.code.ecommercebackend.models.Product;
import com.code.ecommercebackend.models.ProductOrder;
import com.code.ecommercebackend.repositories.CommentRepository;
import com.code.ecommercebackend.repositories.OrderRepository;
import com.code.ecommercebackend.repositories.ProductRepository;
import com.code.ecommercebackend.services.BaseServiceImpl;
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

    public CommentServiceImpl(MongoRepository<Comment, String> repository,
                              CommentMapper commentMapper, OrderRepository orderRepository, CommentRepository commentRepository, ProductRepository productRepository) {
        super(repository);
        this.commentMapper = commentMapper;
        this.orderRepository = orderRepository;
        this.commentRepository = commentRepository;
        this.productRepository = productRepository;
    }


    @Override
    public Comment save(CommentRequest commentRequest)
            throws DataNotFoundException, FileTooLargeException, FileNotSupportedException, IOException {
        Comment comment = commentMapper.toComment(commentRequest);
        comment.setCommentDate(LocalDateTime.now());
        Order order = orderRepository.findById(commentRequest.getOrderId())
                .orElseThrow(() -> new DataNotFoundException("Order not found"));
        List<ProductOrder> productOrders = order.getProductOrders();
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
