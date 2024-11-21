import { createApi } from "@reduxjs/toolkit/query/react";
import { axiosBaseQuery } from "./base-query";
import { BaseResponse } from "../dtos/response/base-response";
import { Fee } from "../dtos/response/payment/fee";
import { OrderRequest } from "../dtos/request/payment/order-request";
import { Order } from "../models/order";

const paymentApi = createApi({
    reducerPath: 'paymentApi',
    baseQuery: axiosBaseQuery(),
    endpoints: (build) => ({
        getFee: build.query<BaseResponse<Fee>, {pickProvince: string, pickDistrict: string, province: string, district: string, weight: number, value: number}>({
            query: (data) => ({
                url: `/payment/fee?prick_province=${data.pickProvince}
                &prick_district=${data.pickDistrict}&province=${data.province}
                &district=${data.district}&weight=${data.weight}&value=${data.value}`,
                method: 'get',
            }),
            keepUnusedDataFor: 180,
        }),
        createOrder: build.mutation<BaseResponse<Order>, OrderRequest>({
            query: (data) => ({
                url: '/payment/order',
                method: 'post',
                data: data,
            })
        }),
        getPayment: build.query<BaseResponse<string>, {orderId: string; amount: number}>({
            query: (param) => ({
                url: `/payment/vnp?orderId=${param.orderId}&amount=${param.amount}`,
                method: 'get',
            })
        })
    })

})

export const { 
    useLazyGetFeeQuery, 
    useCreateOrderMutation,
    useLazyGetPaymentQuery
} = paymentApi;

export default paymentApi;