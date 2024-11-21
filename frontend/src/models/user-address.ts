import { Address } from "./address";

export interface UserAddress extends Address {
    userId: string;
    receiverName: string;
    phoneNumber: string;
    addressDefault: boolean;
}