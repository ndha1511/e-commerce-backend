import { UserAddress } from "../../../models/user-address";

export interface OrderItem {
    variantId: string;
    quantity: number;
}

export enum PaymentMethod {
    COD = "COD",
    ATM = "ATM"
}

export interface OrderRequest {
    orderItems: OrderItem[];
    userId: string;
    voucherId?: string;
    paymentMethod: PaymentMethod;
    userAddress: UserAddress;
    orderFrom?: string;
    note: string;
    deliveryFee: number;
}