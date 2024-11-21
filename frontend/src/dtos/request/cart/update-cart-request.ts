import { ProductCart } from "../../../models/cart";

export interface UpdateCartRequest {
    userId: string;
    index: number;
    productUpdate: ProductCart;
}