import { configureStore } from '@reduxjs/toolkit'
import { setupListeners } from '@reduxjs/toolkit/query'
import productApi from '../../services/product.service'
import authApi from '../../services/auth.service'
import categoryApi from '../../services/category.service'
import { createProductSlice } from '../slice/product-slice'
import { Notification } from '../slice/notify-slice'
import variantApi from '../../services/variant.service'
import cartApi from '../../services/cart.service'
import addressApi from '../../services/address.service'
import paymentApi from '../../services/payment.service'
import orderApi from '../../services/order.service'
import inventoryApi from '../../services/inventory.service'
import commentApi from '../../services/comment.service'
import userApi from '../../services/user.service'

import historySearchApi from '../../services/history-search.service'

import brandAPi from '../../services/brand.service'
import messageApi from '../../services/message.service'
import roomApi from '../../services/room.service'
import notificationApi from '../../services/notification.service'


export const store = configureStore({
  reducer: {
    product: createProductSlice.reducer,
    notification: Notification.reducer,
    [productApi.reducerPath]: productApi.reducer,
    [authApi.reducerPath]: authApi.reducer,
    [categoryApi.reducerPath]: categoryApi.reducer,
    [variantApi.reducerPath]: variantApi.reducer,
    [cartApi.reducerPath]: cartApi.reducer,
    [addressApi.reducerPath]: addressApi.reducer,
    [paymentApi.reducerPath]: paymentApi.reducer,
    [orderApi.reducerPath]: orderApi.reducer,
    [inventoryApi.reducerPath]: inventoryApi.reducer,
    [commentApi.reducerPath]: commentApi.reducer,
    [userApi.reducerPath]: userApi.reducer,
    [historySearchApi.reducerPath]: historySearchApi.reducer,
    [brandAPi.reducerPath]: brandAPi.reducer,
    [messageApi.reducerPath]: messageApi.reducer,
    [roomApi.reducerPath]: roomApi.reducer,
    [notificationApi.reducerPath]: notificationApi.reducer,

  },
  middleware: (getDefaultMiddleware) => getDefaultMiddleware()
    .concat(productApi.middleware)
    .concat(authApi.middleware)
    .concat(categoryApi.middleware)
    .concat(variantApi.middleware)
    .concat(cartApi.middleware)
    .concat(addressApi.middleware)
    .concat(paymentApi.middleware)
    .concat(orderApi.middleware)
    .concat(inventoryApi.middleware)
    .concat(commentApi.middleware)
    .concat(userApi.middleware)
    .concat(historySearchApi.middleware)
    .concat(brandAPi.middleware)
    .concat(messageApi.middleware)
    .concat(roomApi.middleware)
    .concat(notificationApi.middleware)

})

setupListeners(store.dispatch)


export type RootState = ReturnType<typeof store.getState>
export type AppDispatch = typeof store.dispatch