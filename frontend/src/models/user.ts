import { BaseModel } from "./base-model";

export enum Gender {
    MALE = "MALE",
    FEMALE = "FEMALE"
}

export interface User extends BaseModel {
    username: string;
    email: string;
    name?: string;
    phoneNumber?: string;
    gender?: Gender;
    dateOfBirth: Date;
    avatar?: string;
    
}