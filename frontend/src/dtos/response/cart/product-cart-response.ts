import { Promotion } from "../../../models/promotion";
import { VariantResponse } from "../variant/variant-response";

export interface ProductCartResponse {
    quantity: number;
    variantResponse: VariantResponse;
    promotion: Promotion;
}