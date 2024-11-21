import { PaymentMethod } from "../dtos/request/payment/order-request";
import { BaseModel } from "./base-model";
import { ProductOrder } from "./product-order";
import { UserAddress } from "./user-address";

export enum OrderStatus {
    PENDING = "PENDING",
    SHIPPING = "SHIPPING",
    SHIPPED_CONFIRMATION = "SHIPPED_CONFIRMATION", 
    RECEIVED = "RECEIVED",
    CANCELLED = "CANCELLED",
    AWAITING_PICKUP = "AWAITING_PICKUP",
    ERROR = "ERROR",
}

export enum PaymentStatus {
    PAID = "PAID",
    UNPAID = "UNPAID",
}

export interface Order extends BaseModel {
    userId: string;
    paymentMethod: PaymentMethod;
    orderStatus: OrderStatus;
    paymentStatus?: PaymentStatus;
    totalAmount: number;
    voucherAmount: number;
    shippingAmount: number;
    finalAmount: number;
    productOrders: ProductOrder[];
    shippingAddress: UserAddress;
    paymentDate?: Date;
    note: string;


}