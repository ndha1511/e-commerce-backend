import { createApi } from "@reduxjs/toolkit/query/react";
import { axiosBaseQuery } from "./base-query";
import { BaseResponse } from "../dtos/response/base-response";
import { PageResponse } from "../dtos/response/page-response";
import { Room } from "../models/room";
const roomApi = createApi({
    reducerPath: 'room',
    baseQuery: axiosBaseQuery(),
    endpoints: (build) => ({
        getRoom: build.query<BaseResponse<PageResponse<Room>>, {email : string ; param :string}>({
            query: (params) => ({
                url: `/rooms/${params.email}?` + params.param,
                method: 'get',
            }),
            keepUnusedDataFor: 180,
        }),
        updateRoom: build.mutation<BaseResponse<PageResponse<Room>>, string>({
            query: (roomId) => ({
                url: `/rooms/roomId/` + roomId,
                method: 'put',
            }),
        }),

    })
})

export const {
    useGetRoomQuery,
    useUpdateRoomMutation
} = roomApi;

export default roomApi;