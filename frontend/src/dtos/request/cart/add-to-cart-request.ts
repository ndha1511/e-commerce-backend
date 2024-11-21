import { ProductCart } from "../../../models/cart";

export interface AddToCartRequest {
    userId: string;
    productCart: ProductCart
}