export interface CommentRequest {
    content: string;
    files: File[];
    rating: number;
    productId: string;
    attributes: string[];
    userId: string;
    orderId: string;
}