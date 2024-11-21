import { Attribute } from "./attriubte";
import { BaseModel } from "./base-model";
import { Promotion } from "./promotion";
import { Tag } from "./tag";

export interface Product extends BaseModel {
    productName: string;
    regularPrice: number;
    urlPath: string;
    categories: string[];
    brandId?: string;
    totalQuantity: number;
    thumbnail: string;
    reviews: number;
    buyQuantity: number;
    rating: number;
    description: string;
    weight: number;
    tags: Tag[];
    images: string[];
    video: string;
    likes: number;
    promotion?: Promotion;
    attributes?: Attribute[];

}