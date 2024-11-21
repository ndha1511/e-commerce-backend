import { createApi } from "@reduxjs/toolkit/query/react";
import { axiosBaseQuery } from "./base-query";
import { BaseResponse } from "../dtos/response/base-response";
import { Brand } from "../models/brand";
import { PageResponse } from "../dtos/response/page-response";
const brandAPi = createApi({
    reducerPath: 'brand',
    baseQuery: axiosBaseQuery(),
    endpoints: (build) => ({
        createBrand: build.mutation<BaseResponse<Brand>, FormData>({
            query: (newBrand) => ({
                url: '/brands',
                method: 'post',
                headers: {
                    'Content-Type': 'multipart/form-data',
                },
                data: newBrand,
            }),
        }),
        getBrands: build.query<BaseResponse<PageResponse<Brand>>, string>({
            query: (params) => ({
                url: '/brands?'+params,
                method: 'get',
            }),
        }),

    })
})

export const {
    useCreateBrandMutation,
    useGetBrandsQuery,
} = brandAPi;

export default brandAPi;