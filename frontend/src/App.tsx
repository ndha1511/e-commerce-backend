import { ReactNode, useEffect, useState } from "react";
import { useCheckLoginQuery } from "./services/auth.service";
import { connect, isConnected, stompClient } from "./websocket/websocket-config";
import { Message } from "stompjs";
import { pageQueryHanlder } from "./utils/query-handler";
import { useGetRoomQuery } from "./services/room.service";
import { useGetMessageQuery } from "./services/message.service";
import { useGetCommentsQuery } from "./services/comment.service";
import { useGetNotificationsQuery } from "./services/notification.service";

const App = ({ children }: { children: ReactNode }) => {
    const {data, isSuccess} = useCheckLoginQuery();
    const [connectFinish, setConnectFinish] = useState<boolean>(false);
    
    const { data: user, isSuccess: loginSuccess } = useCheckLoginQuery();
    const paramsRoom = pageQueryHanlder(1, 100);
    const { data: room, isSuccess: roomSuccess, refetch: refetchRoom } = useGetRoomQuery({
        email: user?.data?.email || '',
        param: paramsRoom,
    }, { skip: !loginSuccess || !user?.data?.id })
    const paramsMessage = pageQueryHanlder(1, 1000);
    const { data: messagesData, refetch } = useGetMessageQuery({
        roomId: room?.data?.items?.[0]?.conversationId || '',
        params: paramsMessage,
    }, { skip: !roomSuccess });



    //////Notification
    const paramNotification = pageQueryHanlder(1,40);
    const { data: dataNotification,refetch:notificationRefetch } = useGetNotificationsQuery({
        id: user?.data?.id || '',
        param: paramNotification,
    },{skip: !loginSuccess});
    ///////////
    useEffect(() => {
        if(isSuccess) {
            if(data.data) {
                connect(onConnected, onError);
            } else {
                setConnectFinish(true);
            }
        } else {
            setConnectFinish(true);
        }
    }, [isSuccess]);

    const onConnected = () => {
        console.log("Connected to websocket server");
        if (isConnected() && stompClient) {
            stompClient.subscribe("/user/" + data?.data?.email + "/queue/messages", onMessageReceived);
            stompClient.subscribe("/topic/notification/" + data?.data?.id , onNotification);
        }
        setConnectFinish(true);
    }

    const onMessageReceived = (message: Message) => {
        const messageResponse = JSON.parse(message.body);
        console.log(messageResponse);
        refetch();
        refetchRoom();
    }
    const onNotification = (notification: Message) => {
        const notificationResponse = JSON.parse(notification.body);
        console.log(notificationResponse);
        notificationRefetch();
    }


    const onError = () => {
        console.log("Error connecting to websocket server");
    }

    return connectFinish ? children : <></>;
}

export default App;