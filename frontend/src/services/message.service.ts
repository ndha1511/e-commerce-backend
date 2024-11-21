import { createApi } from "@reduxjs/toolkit/query/react";
import { axiosBaseQuery } from "./base-query";
import { BaseResponse } from "../dtos/response/base-response";
import { PageResponse } from "../dtos/response/page-response";
import { Message } from "../models/message";
const messageApi = createApi({
    reducerPath: 'message',
    baseQuery: axiosBaseQuery(),
    endpoints: (build) => ({
        getMessage: build.query<BaseResponse<PageResponse<Message>>, {roomId:string; params:string}>({
            query: (params) => ({
                url: `/messages/${params.roomId}?` + params.params,
                method: 'get',
            }),
            keepUnusedDataFor: 180,
        }),
        createMessage: build.mutation<BaseResponse<Message>, FormData>({
            query: (newMessage) => ({
                url: '/messages',
                method: 'post',
                headers: {
                    'Content-Type': 'multipart/form-data',
                },
                data: newMessage,
            }),
        }),

    })
})

export const {
    useGetMessageQuery,
    useCreateMessageMutation
} = messageApi;

export default messageApi;