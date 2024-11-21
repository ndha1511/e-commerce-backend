import { createApi } from "@reduxjs/toolkit/query/react";
import { axiosBaseQuery } from "./base-query";
import { BaseResponse } from "../dtos/response/base-response";
import { ProductCartResponse } from "../dtos/response/cart/product-cart-response";
import { Cart } from "../models/cart";
import { AddToCartRequest } from "../dtos/request/cart/add-to-cart-request";
import { UpdateCartRequest } from "../dtos/request/cart/update-cart-request";

const cartApi = createApi({
    reducerPath: 'cartApi',
    baseQuery: axiosBaseQuery(),
    endpoints: (build) => ({
        getCartByUserId: build.query<BaseResponse<ProductCartResponse[]>, string>({
            query: (userId) => ({
                url: '/carts/' + userId,
                method: 'get',
            }),
            keepUnusedDataFor: 180,
        }),
        addToCart: build.mutation<BaseResponse<Cart>, AddToCartRequest>({
            query: (item) => ({
                url: '/carts',
                method: 'post',
                data: item,
            })
        }),
        updateCart: build.mutation<BaseResponse<Cart>, UpdateCartRequest>({
            query: (item) => ({
                url: '/carts',
                method: 'put',
                data: item,
            })
        }),
        deleteCartItem: build.mutation<BaseResponse<null>, { itemId: string, userId: string }>({
            query: ({ itemId, userId }) => ({
                url: `/carts?itemId=${itemId}&userId=${userId}`,
                method: 'delete',
            })
        })
    }),
})

export const {
    useGetCartByUserIdQuery,
    useAddToCartMutation,
    useUpdateCartMutation,
    useDeleteCartItemMutation
} = cartApi;

export default cartApi;