import { createApi } from "@reduxjs/toolkit/query/react";
import { axiosBaseQuery } from "./base-query";
import { BaseResponse } from "../dtos/response/base-response";
import { VariantResponse } from "../dtos/response/variant/variant-response";

const variantApi = createApi({
    reducerPath: 'variantApi',
    baseQuery: axiosBaseQuery(),
    endpoints: (build) => ({
        getVariants: build.query<BaseResponse<VariantResponse>, {productId: string; attr1: string; attr2?: string;}>({
            query: ({productId, attr1, attr2}) => ({
                url: '/variants?productId=' + productId + '&attr1=' + attr1 + (attr2 ? '&attr2=' + attr2 : ''),
                method: 'get',
            }),
            keepUnusedDataFor: 180,
        }),
        getVariantsByProductId: build.query<BaseResponse<VariantResponse[]>, string>({
            query: (productId) => ({
                url: '/variants/' + productId,
                method: 'get',
            }),
            keepUnusedDataFor: 180,
        }),
        getVariantsByProductIAndAttVl1: build.query<BaseResponse<VariantResponse[]>, { productId: string; attVl1: string }>({
            query: ({ productId, attVl1 }) => ({
                url: `/variants/${productId}/byAttVal?attVl1=${attVl1}`,
                method: 'get',
            }),
            keepUnusedDataFor: 180,
        }),
    }),
   
})

export const {
    useLazyGetVariantsQuery,
    useGetVariantsByProductIdQuery,
    useGetVariantsByProductIAndAttVl1Query
} = variantApi;

export default variantApi;