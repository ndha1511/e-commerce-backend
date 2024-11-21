import { Product } from "../../../models/product";

export interface VariantResponse {
    id: string;
    product: Product;
    image?: string;
    attributeValue1: string;
    attributeValue2?: string;
    price: number;
    sku?: string;
    quantity: number;
    buyQuantity:number;
}