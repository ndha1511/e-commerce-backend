import { createApi } from "@reduxjs/toolkit/query/react";
import { axiosBaseQuery } from "./base-query";
import { BaseResponse } from "../dtos/response/base-response";
import { PageResponse } from "../dtos/response/page-response";
import { Product } from "../models/product";
import { AttributeResponse } from "../dtos/response/attribute-response";
import { Attribute } from "../models/attriubte";



const productApi = createApi({
    reducerPath: 'productApi',
    baseQuery: axiosBaseQuery(),
    endpoints: (build) => ({
        getProducts: build.query<BaseResponse<PageResponse<Product>>, void>({
            query: () => ({
                url: '/products',
                method: 'get',
            }),
            keepUnusedDataFor: 180,
        }),
        getProductByUrl: build.query<BaseResponse<Product>, string>({
            query: (path) => ({
                url: '/products/' + path,
                method: 'get',
            }),
            keepUnusedDataFor: 180,
        }),
        getProductsPage: build.query<BaseResponse<PageResponse<Product>>, string>({
            query: (params) => ({
                url: '/products?' + params,
                method: 'get',
            }),
            keepUnusedDataFor: 180,
        }),
        getProductByCategory: build.query<BaseResponse<PageResponse<Product>>, string>({
            query: (params) => ({
                url: '/products/category/' + params,
                method: 'get',
            }),
            keepUnusedDataFor: 180,
        }),
        getAttributeById: build.query<BaseResponse<AttributeResponse>, string>({
            query: (id) => ({
                url: '/products/attribute/' + id,
                method: 'get',
            }),
            keepUnusedDataFor: 180,
        }),
        createProduct: build.mutation<BaseResponse<Product>, FormData>({
            query: (newProduct) => ({
                url: '/products',
                method: 'post',
                headers: {
                    'Content-Type': 'multipart/form-data',
                },
                data: newProduct,
            }),
        }),
        createAttribute: build.mutation<BaseResponse<null>, FormData>({
            query: (newAttribute) => ({
                url: '/products/attributes',
                method: 'post',
                headers: {
                    'Content-Type': 'multipart/form-data',
                },
                data: newAttribute,
            }),
        }),
        getAttributeByProductId: build.query<BaseResponse<Attribute[]>, string>({
            query: (productId) => ({
                url: '/products/attributes/' + productId,
                method: 'get',
            })
        }),
        getProductsRecommend: build.query<BaseResponse<Product[]>, {nRecommend?: number}>({
            query: (params) => ({
                url: `/products/recommend?nRecommend=${params.nRecommend ? params.nRecommend : 0}`,
                method: 'get',
            }),
            keepUnusedDataFor: 180,
        })
    }),
});

export const {
    useGetProductsQuery,
    useGetProductByCategoryQuery,
    useCreateProductMutation,
    useGetProductByUrlQuery,
    useCreateAttributeMutation,
    useGetAttributeByIdQuery,
    useGetAttributeByProductIdQuery,
    useGetProductsPageQuery
} = productApi;

export default productApi;