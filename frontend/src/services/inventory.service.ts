import { createApi } from "@reduxjs/toolkit/query/react";
import { axiosBaseQuery } from "./base-query";
import { BaseResponse } from "../dtos/response/base-response";
import { CreateInventoryRequest } from "../dtos/request/inventory/create-inventory-request";

const inventoryApi = createApi({
    reducerPath: 'inventoryApi',
    baseQuery: axiosBaseQuery(),
    endpoints: (build) => ({
        createInventory: build.mutation<BaseResponse<null>,CreateInventoryRequest >({
            query: (newInventory) => ({
                url: '/inventories',
                method: 'post',
                data: newInventory,
            }),
        }),
    }),
   
})

export const {
    useCreateInventoryMutation,
} = inventoryApi;

export default inventoryApi;