import { createSlice, PayloadAction } from "@reduxjs/toolkit";

type NotificationType = 'error' | 'success' | 'warning' | 'info';

interface Notification {
  type?: NotificationType;
  message: string;
}

const initialState: Notification = {
    message: '',
}
export const Notification = createSlice({
    name: 'notification',
    initialState,
    reducers: {
        setNotify: (state, action: PayloadAction<Notification>) => {
            // Cập nhật state bằng cách sao chép thuộc tính từ action.payload
            return {
                ...state,
                ...action.payload,
            };
        },
        // Có thể thêm một hàm để reset thông báo
        clearNotify: (state) => {
            state.type = undefined; // Hoặc loại nào đó
            state.message = '';
        },
    }
})
export const {setNotify,clearNotify  } = Notification.actions
export default Notification.reducer;