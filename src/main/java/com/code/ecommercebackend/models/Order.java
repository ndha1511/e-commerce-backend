package com.code.ecommercebackend.models;

import com.code.ecommercebackend.models.enums.OrderStatus;
import com.code.ecommercebackend.models.enums.PaymentMethod;
import com.code.ecommercebackend.models.enums.PaymentStatus;
import com.code.ecommercebackend.models.enums.ShippingMethod;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
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
    @Field(name = "total_amount")
    private double totalAmount;
    @Field(name = "voucher_amount")
    private double voucherAmount;
    @Field(name = "shipping_amount")
    private double shippingAmount;
    @Field(name = "shipping_voucher")
    private double shippingVoucher;
    @Field(name = "final_amount")
    private double finalAmount;
    @Field(name = "product_orders")
    private Set<ProductOrder> productOrders;
    @Field(name = "shipping_address")
    private ShippingAddress shippingAddress;


    public void calcFinalAmount() {
        for(ProductOrder productOrder : productOrders) {
            productOrder.calcAmount();
            this.totalAmount += productOrder.getAmount();
        }
        double finalShippingAmount  = this.shippingAmount - this.shippingVoucher;
        this.finalAmount = (this.finalAmount + finalShippingAmount) - (this.voucherAmount);
    }



}
