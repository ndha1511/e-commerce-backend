import { createApi } from "@reduxjs/toolkit/query/react";
import { axiosBaseQuery } from "./base-query";
import { BaseResponse } from "../dtos/response/base-response";
import { PageResponse } from "../dtos/response/page-response";
import { NotificationModel } from "../models/notification";
const notificationApi = createApi({
    reducerPath: 'notifications',
    baseQuery: axiosBaseQuery(),
    endpoints: (build) => ({
        getNotifications: build.query<BaseResponse<PageResponse<NotificationModel>>, {id: string, param: string}>({
            query: (params) => ({
                url: `/notifications/${params.id}?` + params.param,
                method: 'get',
            }),
            keepUnusedDataFor: 180,
        }),
        checkSeen: build.mutation<BaseResponse<null>, string>({
            query: (id) => ({
                url: '/notifications/'+id,
                method: 'put',
            }), 
        }),

    })
})

export const {
    useGetNotificationsQuery,useCheckSeenMutation
} = notificationApi;

export default notificationApi;