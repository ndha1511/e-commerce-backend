import { Product } from "./product";

export interface Variant{
    id: string;
    product: Product;
    attributeValue1: string;
    attributeValue2: string;
    price: number;
    sku: string;
}