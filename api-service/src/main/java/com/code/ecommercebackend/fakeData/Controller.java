package com.code.ecommercebackend.fakeData;

import com.code.ecommercebackend.dtos.request.payment.OrderItem;
import com.code.ecommercebackend.dtos.request.payment.OrderRequest;
import com.code.ecommercebackend.exceptions.DataNotFoundException;
import com.code.ecommercebackend.models.User;
import com.code.ecommercebackend.models.UserAddress;
import com.code.ecommercebackend.models.Variant;
import com.code.ecommercebackend.models.enums.PaymentMethod;
import com.code.ecommercebackend.repositories.UserRepository;
import com.code.ecommercebackend.repositories.VariantRepository;
import com.code.ecommercebackend.services.payment.PaymentServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Random;

@RestController
@RequiredArgsConstructor
@RequestMapping("/fake")
public class Controller {

    private final UserRepository userRepository;
    private final VariantRepository variantRepository;
    private final PaymentServiceImpl paymentServiceImpl;

    @GetMapping
    public String hello() throws DataNotFoundException {
        fakeOrder();
        return "Hello World";
    }
    public void fakeOrder() throws DataNotFoundException {
        List<User> users = userRepository.findAll();
        Random rand = new Random();
        for(int i = 0; i < 1; i ++) {
            User randomUser = users.get(rand.nextInt(users.size()));
            UserAddress userAddress = new UserAddress();
            userAddress.setPhoneNumber("0981972551");
            userAddress.setReceiverName("Hoang Anh");
            userAddress.setDistrict("Huyện Củ Chi");
            userAddress.setProvince("Hồ Chí Minh");
            userAddress.setWard("Xã Phạm Văn Cội");
            userAddress.setAddressDetail("527, Bùi Thị Điệt");
            OrderRequest orderRequest = new OrderRequest();
            orderRequest.setUserId(randomUser.getId());
            orderRequest.setPaymentMethod(PaymentMethod.COD);
            orderRequest.setDeliveryFee(30000);
            OrderItem orderItem = new OrderItem();
            orderItem.setQuantity(1);
            orderItem.setVariantId("673cc65a070eee68e20d328a");
            List<OrderItem> orderItems = List.of(orderItem);
            orderRequest.setOrderItems(orderItems);
            orderRequest.setOrderFrom("product");
            orderRequest.setUserAddress(userAddress);
            paymentServiceImpl.order(orderRequest);
        }
    }
}
