export interface ProductOrder {
    productId: string;
    productName: string;
    attributes: string[];
    price: number;
    quantity: number;
    amount: number;
    image: string;
    allowComment: boolean;
    commented: boolean;
}