package com.code.ecommercebackend.models;

import com.code.ecommercebackend.models.enums.OrderStatus;
import com.code.ecommercebackend.models.enums.PaymentMethod;
import com.code.ecommercebackend.models.enums.PaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.List;


@Document(collection = "orders")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Order extends BaseModel {
    @Field(name = "user_id")
    private String userId;
    @Field(name = "payment_method")
    private PaymentMethod paymentMethod;
    @Field(name = "order_status")
    private OrderStatus orderStatus;
    @Field(name = "payment_status")
    private PaymentStatus paymentStatus;
    @Field(name = "total_amount")
    private double totalAmount;
    @Field(name = "voucher_amount")
    private double voucherAmount;
    @Field(name = "shipping_amount")
    private double shippingAmount;
    @Field(name = "final_amount")
    private double finalAmount;
    @Field(name = "product_orders")
    private List<ProductOrder> productOrders;
    @Field(name = "shipping_address")
    private UserAddress shippingAddress;
    private String note;

    public void calcFinalAmount() {
        this.finalAmount = (this.totalAmount - this.voucherAmount) + this.shippingAmount;
    }

}
