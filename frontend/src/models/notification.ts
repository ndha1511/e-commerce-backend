import { BaseModel } from "./base-model";

export interface NotificationModel extends BaseModel{
    userId:string;
    image:string;
    content:string;
    title:string;
    seen:boolean;
    time:Date;
    redirectTo:string;
}