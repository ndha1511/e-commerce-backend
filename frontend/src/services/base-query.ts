import type { BaseQueryFn } from '@reduxjs/toolkit/query'
import axios from 'axios'
import type { AxiosRequestConfig, AxiosError } from 'axios'
import { ErrorResponse } from '../dtos/response/error-response'

const axiosInstance = axios.create({
  baseURL: import.meta.env.VITE_BASE_URL,
})


axiosInstance.interceptors.response.use(
  (response) => {
    return response
  },
  async (error: AxiosError) => {
    if (error.response?.status === 401) {
      const config: AxiosRequestConfig<unknown> | undefined = error.config;
      if (config) {
        console.log('start refresh token.............');
        try {
          await axiosInstance.get("/auth/refresh-token", config);
          return axiosInstance(config);
        } catch (error) {
          return Promise.reject(error);
        }
      }
    }
    return Promise.reject(error)
  }
)

export const axiosBaseQuery = (): BaseQueryFn<
  {
    url: string
    method?: AxiosRequestConfig['method']
    data?: AxiosRequestConfig['data']
    params?: AxiosRequestConfig['params']
    headers?: AxiosRequestConfig['headers']
  },
  unknown,
  unknown
> =>
  async ({ url, method, data, params, headers }) => {
    try {
      const result = await axiosInstance({
        url,
        method,
        data,
        params,
        headers,
        withCredentials: true
      })
      return { data: result.data }
    } catch (axiosError) {
      const err = axiosError as AxiosError
      const errorResponse: ErrorResponse = {
        status: err.response?.status,
        data: err.response?.data || err.message,
      }
      return {
        error: errorResponse,
      }
    }
  }

