import { AttributeValueDto } from "./attribute-value.reques";

export interface ProductAttributeDto {
    attributeName: string;
    attributeValues?: AttributeValueDto[];
}