import { createApi } from "@reduxjs/toolkit/query/react";
import { axiosBaseQuery } from "./base-query";
import { BaseResponse } from "../dtos/response/base-response";
import { PageResponse } from "../dtos/response/page-response";
import { Category } from "../models/category";
import { CategoryResponse } from "../dtos/response/category/category-response";

const categoryApi = createApi({
    reducerPath: 'categoryApi',
    baseQuery: axiosBaseQuery(),
    endpoints: (build) => ({
        getCategories: build.query<BaseResponse<PageResponse<Category>>, string>({
            query: (params) => ({
                url: '/categories?' + params,
                method: 'get',
            }),
            keepUnusedDataFor: 180,
        }),
        getCategoryByUrl: build.query<BaseResponse<CategoryResponse>, string>({
            query: (params) => ({
                url: '/categories/' + params,
                method: 'get',
            }),
            keepUnusedDataFor: 180,
        }),
        getListCategory: build.query<BaseResponse<Category[]>, string[]>({
            query: (params) => ({
                url: '/categories/list?categoryId=' + params.join(';'),
                method: 'get',
            })
        }),
        createCategory: build.mutation<BaseResponse<Category>, FormData>({
            query: (category) => ({
                url: '/categories',
                method: 'post',
                headers: {
                    'Content-Type': 'multipart/form-data',
                },
                data: category,
            }),
        })
    }),
});

export const { 
    useGetCategoriesQuery,
    useGetCategoryByUrlQuery,
    useLazyGetCategoriesQuery,
    useCreateCategoryMutation,
    useGetListCategoryQuery
} = categoryApi;

export default categoryApi;