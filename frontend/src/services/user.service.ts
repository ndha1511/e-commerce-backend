import { createApi } from "@reduxjs/toolkit/query/react";
import { axiosBaseQuery } from "./base-query";
import { BaseResponse } from "../dtos/response/base-response";
import { EmployeeDto } from "../dtos/request/user/employee-request";
import { PageResponse } from "../dtos/response/page-response";
import { User } from "../models/user";
const userApi = createApi({
    reducerPath: 'user',
    baseQuery: axiosBaseQuery(),
    endpoints: (build) => ({
        updateUser: build.mutation<BaseResponse<null>, {email:string, newUser:FormData}>({
            query: ({ email,newUser }) => ({
                url: '/users/' + email,
                method: 'put',
                headers: {
                    'Content-Type': 'multipart/form-data',
                },
                data: newUser,
            }),
        }),
        createEmployee: build.mutation<BaseResponse<null>, EmployeeDto>({
            query: (newEmployee) => ({
                url: '/users/create-account-emp',
                method: 'post',
                data: newEmployee,
            }),
        }),
        getEmployee: build.query<BaseResponse<PageResponse<User>>, string>({
            query: (params) => ({
                url: '/users/employee?' + params,
                method: 'get',
            }),
            keepUnusedDataFor: 180,
        }),

    })
})

export const {
    useUpdateUserMutation,
    useCreateEmployeeMutation,
    useGetEmployeeQuery,
} = userApi;

export default userApi;