package com.code.ecommercebackend.services.comment;

import com.code.ecommercebackend.dtos.request.comment.CommentReplyRequest;
import com.code.ecommercebackend.dtos.request.comment.CommentRequest;
import com.code.ecommercebackend.exceptions.DataNotFoundException;
import com.code.ecommercebackend.exceptions.FileNotSupportedException;
import com.code.ecommercebackend.exceptions.FileTooLargeException;
import com.code.ecommercebackend.mappers.comment.CommentMapper;
import com.code.ecommercebackend.models.*;
import com.code.ecommercebackend.repositories.*;
import com.code.ecommercebackend.repositories.NotificationRepository;
import com.code.ecommercebackend.services.BaseServiceImpl;
import com.code.ecommercebackend.services.common.CommonFunction;
import com.code.ecommercebackend.utils.CookieHandler;
import com.code.ecommercebackend.utils.S3Upload;
import com.code.ecommercebackend.utils.SocketHandler;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class CommentServiceImpl extends BaseServiceImpl<Comment, String> implements CommentService {
    private final CommentMapper commentMapper;
    private final OrderRepository orderRepository;
    private final CommentRepository commentRepository;
    private final ProductRepository productRepository;
    private final CookieHandler cookieHandler;
    private final CommonFunction commonFunction;
    private final SocketHandler socketHandler;
    private final S3Upload s3Upload;
    private final NotificationRepository notificationRepository;


    public CommentServiceImpl(MongoRepository<Comment, String> repository,
                              CommentMapper commentMapper,
                              OrderRepository orderRepository,
                              CommentRepository commentRepository,
                              ProductRepository productRepository,
                              CookieHandler cookieHandler,
                              CommonFunction commonFunction, SocketHandler socketHandler, S3Upload s3Upload, NotificationRepository notificationRepository) {
        super(repository);
        this.commentMapper = commentMapper;
        this.orderRepository = orderRepository;
        this.commentRepository = commentRepository;
        this.productRepository = productRepository;
        this.cookieHandler = cookieHandler;
        this.commonFunction = commonFunction;
        this.socketHandler = socketHandler;
        this.s3Upload = s3Upload;
        this.notificationRepository = notificationRepository;
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
                (float) commentRequest.getRating());
        productOrders.forEach(po -> {
            if (po.getProductId().equals(commentRequest.getProductId())) {
                if (po.getAttributes().equals(commentRequest.getAttributes())) {
                    po.setCommented(true);
                }
            }
        });
        calcRating(comment);
        order.setProductOrders(productOrders);
        orderRepository.save(order);
        socketHandler.sendCommentToSocket(comment);
        Optional<Product> optionalProduct = productRepository.findById(comment.getProductId());

        Notification notification = new Notification();
        notification.setUserId(commentRequest.getUserId());
//        optionalProduct.ifPresent(product -> {
//            notification.setImage(product.getThumbnail());
//        });
        if (!productOrders.isEmpty()) {
            // Lấy hình ảnh đầu tiên
            String image = productOrders.get(0).getImage();
            notification.setImage(image);
        }
        notification.setTitle("Bạn vừa đánh giá sản phẩm có mã " + "SP12");
        notification.setTime(LocalDateTime.now());
        notification.setSeen(false);
        notification.setContent(commentRequest.getContent());
        notificationRepository.save(notification);
        socketHandler.sendNotificationToSocket(notification);
        return commentRepository.save(comment);

    }

    @Override
    public Comment reply(String commentId, CommentReplyRequest commentReplyRequest) throws DataNotFoundException, IOException {
        Comment commentReply = commentRepository.findById(commentId)
                .orElseThrow(() -> new DataNotFoundException("Comment not found"));
        if (commentReplyRequest.getFiles() != null) {
            commentReply.setReplyComment(s3Upload.uploadFile(commentReplyRequest.getFiles()));
        } else {
            commentReply.setReplyComment(commentReplyRequest.getContent());
        }
        socketHandler.sendCommentToSocket(commentReply);
        return commentRepository.save(commentReply);
    }


    private void calcRating(Comment comment) throws DataNotFoundException {
        Product product = productRepository.findById(comment.getProductId())
                .orElseThrow(() -> new DataNotFoundException("product not found"));
        int numberOfRating = product.getReviews();
        float totalRating = numberOfRating * product.getRating();
        float newTotalRating = totalRating + comment.getRating();
        float newRating = newTotalRating / (numberOfRating + 1);
        product.setRating(newRating);
        product.setReviews(numberOfRating + 1);
        productRepository.save(product);

    }


}
