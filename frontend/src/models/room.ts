

export interface Room  {
    conversationId: string;
    sender: string;
    receiver: string;
    receiverName: string;
    sendDate:Date;
    lastMessageSender: string;
    count: number;
    avatarSender:string;
    avatarReceiver:string;
    seen:boolean;
}