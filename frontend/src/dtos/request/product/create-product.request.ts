import { Tag } from "../../../models/tag";
import { ProductAttributeDto } from "./product-attribute.reques";
import { VariantDto } from "./variant.reques";

export interface CreateProductDto {
    productName: string;
    regularPrice: number;
    categories: string[];
    brandId: string;
    city: string;
    thumbnailIndex: number;
    images: string[];
    video?: string;
    attributesDto: ProductAttributeDto[];
    variantsDto: VariantDto[];
    description: string;
    tag?: Tag[];
}