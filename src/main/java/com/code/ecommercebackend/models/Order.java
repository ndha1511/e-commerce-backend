package com.code.ecommercebackend.models;

import com.code.ecommercebackend.models.enums.OrderStatus;
import com.code.ecommercebackend.models.enums.PaymentMethod;
import com.code.ecommercebackend.models.enums.PaymentStatus;
import com.code.ecommercebackend.models.enums.ShippingMethod;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Set;

@Document(collection = "orders")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Order extends BaseModel {
    @DocumentReference
    @Field(name = "shop_id")
    private Shop shop;
    @Field(name = "user_id")
    private String userId;
    @Field(name = "payment_method")
    private PaymentMethod paymentMethod;
    @Field(name = "shipping_method")
    private ShippingMethod shippingMethod;
    @Field(name = "order_status")
    private OrderStatus orderStatus;
    @Field(name = "payment_status")
    private PaymentStatus paymentStatus;
    private double totalAmount;
    private double shopVoucher;
    private double appVoucher;
    private double shippingAmount;
    private double shippingVoucher;
    private double finalAmount;
    private Set<ProductOrder> productOrders;
    private ShippingAddress shippingAddress;


    public void calcFinalAmount() {
        for(ProductOrder productOrder : productOrders) {
            productOrder.calcAmount();
            this.totalAmount += productOrder.getAmount();
        }
        double finalShippingAmount  = this.shippingAmount - this.shippingVoucher;
        this.finalAmount = (this.finalAmount + finalShippingAmount) - (this.shopVoucher + this.appVoucher);
    }



}
