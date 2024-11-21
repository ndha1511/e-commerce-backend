import { BaseModel } from "./base-model";

export enum MessageType{
    TEXT = 'TEXT',
    IMAGE = 'IMAGE',
    VIDEO = 'VIDEO',
}
export enum MessageStatus{
    SENDING = 'SENDING',
    SENT = 'SENT',
}

export interface Message extends BaseModel{
    sender: string;
    receiver: string;
    content: string;
    sendDate: Date;
    messageType: MessageType;
    messageStatus: MessageStatus;
    roomId: string;
}