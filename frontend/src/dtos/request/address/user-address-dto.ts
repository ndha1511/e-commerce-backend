import { AddressDto } from "./address-dto";

export interface UserAddressDto extends AddressDto {
    userId: string;
    receiverName: string;
    phoneNumber: string;
    addressDefault: boolean;
    id?: string;
}