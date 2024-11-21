import { BaseModel } from "./base-model";

export interface Cart extends BaseModel {
    userId: string;
    productCarts: ProductCart[];
}

export interface ProductCart {
    variantId: string;
    quantity: number;
}