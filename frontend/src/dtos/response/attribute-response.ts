import { Attribute } from "../../models/attriubte";
import { Variant } from "../../models/variant";

export interface AttributeResponse {
    attributes: Attribute[];
    variants: Variant[];
}