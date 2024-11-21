import { User } from "./user";

export enum MediaType {
    IMAGE = 'IMAGE',
    VIDEO = 'VIDEO',
}

export interface CommentMedia {
    mediaType: MediaType;
    path: string;
}

export interface Comment {
    id: string;
    content: string;
    commentDate: Date;
    commentMedia: CommentMedia[];
    rating: number;
    productId: string;
    attributes: string[];
    user: User;
    replyComment: string;
}