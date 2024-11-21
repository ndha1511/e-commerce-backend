import { createApi } from "@reduxjs/toolkit/query/react";
import { axiosBaseQuery } from "./base-query";
import { BaseResponse } from "../dtos/response/base-response";
import { RegisterRequest } from "../dtos/request/auth/register.request";
import { User } from "../models/user";

const authApi = createApi({
    reducerPath: 'authApi',
    baseQuery: axiosBaseQuery(),
    endpoints: (build) => ({
        checkEmail: build.query<BaseResponse<null>, string>({
            query: (email: string) => ({
                url: `/auth/check-email?email=${email}`,
                method: 'get',
            })
        }),
        register: build.mutation<BaseResponse<null>, RegisterRequest>({
            query: (registerRequest)  => ({
                url: '/auth/register',
                method: 'post',
                data: registerRequest
            })
        }),
        verifyEmail: build.query<BaseResponse<null>, {email: string, otp: string}>({
            query: ({email, otp}) =>({
                url: `/auth/verify-email?email=${email}&otp=${otp}`,
                method: 'get',
            })
        }),
        sendOtp: build.query<BaseResponse<null>, string>({
            query: (email) => ({
                url: `/auth/send-otp?email=${email}`,
                method: 'get',
            })
        }),
        login: build.mutation<BaseResponse<null>, {username: string, password: string}>({
            query: ({username, password}) => ({
                url: '/auth/login',
                method: 'post',
                data: { username, password },
            })
        }),
        checkLogin: build.query<BaseResponse<User | null>, void> ({
            query: () => ({
                url: '/auth/check-login',
                method: 'get',
                keepUnusedDataFor: 3600 * 24,
            })
        }),
        resetPassword: build.query<BaseResponse<null>, string>({ 
            query: (email) => ({
                url: `/auth/reset-password?email=${email}`,
                method: 'get',
            })
        }),
        verifyEmailResetPassword: build.query<BaseResponse<null>, {email: string, otp: string}>({
            query: ({email, otp}) => ({
                url: `/auth/verify-email-reset-password?email=${email}&otp=${otp}`,
                method: 'get',
            })
        }),
        createNewPassword: build.mutation<BaseResponse<null>, {email: string, password: string, confirmPassword: string}>({
            query: ({email, password, confirmPassword}) => ({
                url: '/auth/create-new-password',
                method: 'post',
                data: { email, password, confirmPassword },
            })
        }),
        changePassword: build.mutation<BaseResponse<null>, {email: string, oldPassword: string, newPassword: string}>({
            query: ({email, oldPassword, newPassword}) => ({
                url: '/auth/change-password',
                method: 'post',
                data: { email, oldPassword, newPassword },
            })
        }),
        logout: build.query<BaseResponse<null>, void>({
            query: () => ({
                url: '/auth/logout',
                method: 'get',
            })
         })
    }),
})

export const {
    useLazyCheckEmailQuery, 
    useRegisterMutation, 
    useLazyVerifyEmailQuery,
    useLazySendOtpQuery,
    useLoginMutation,
    useCheckLoginQuery,
    useLazyResetPasswordQuery,
    useLazyVerifyEmailResetPasswordQuery,
    useCreateNewPasswordMutation,
    useChangePasswordMutation,
    useLazyLogoutQuery
} = authApi;
export default authApi;