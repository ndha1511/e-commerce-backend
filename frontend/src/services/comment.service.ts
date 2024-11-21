import { createApi } from "@reduxjs/toolkit/query/react";
import { axiosBaseQuery } from "./base-query";
import { PageResponse } from "../dtos/response/page-response";
import { BaseResponse } from "../dtos/response/base-response";
import { Comment } from "../models/comment";

const commentApi = createApi({
    reducerPath: 'commentApi',
    baseQuery: axiosBaseQuery(),
    endpoints: (build) => ({
        getComments: build.query<BaseResponse<PageResponse<Comment>>, { productId: string; params: string }>({
            query: ({ productId, params }) => ({
                url: `/comments/${productId}?${params}`,
            })
        }),
        createComment: build.mutation<BaseResponse<Comment>, FormData>({
            query: (comment) => ({
                url: '/comments',
                method: 'POST',
                data: comment,
                headers: {
                    'Content-Type':'multipart/form-data'
                }
            })
        }),
        replyComment: build.mutation<BaseResponse<Comment>, {commentId: string, formData:FormData}>({
            query: (comment) => ({
                url: '/comments/'+ comment.commentId,
                method: 'put',
                headers: {
                    'Content-Type':'multipart/form-data'
                },
                data: comment.formData
            })
        }),
    })
       
})

export const { 
    useGetCommentsQuery,
    useCreateCommentMutation,
    useReplyCommentMutation
} = commentApi;

export default commentApi;