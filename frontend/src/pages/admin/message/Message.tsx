import SimpleBar from "simplebar-react";
import { useCheckLoginQuery } from "../../../services/auth.service";
import { useGetRoomQuery, useUpdateRoomMutation } from "../../../services/room.service";
import { pageQueryHanlder } from "../../../utils/query-handler";
import { Room } from "../../../models/room";
import { useEffect, useState } from "react";
import './message.scss'
import { useGetMessageQuery } from "../../../services/message.service";
import ViewContent from "./ViewContent";
import moment from 'moment';
import anh from '../../../assets/gdtn.jpg';
function Message() {
    const [roomActive, setRoomActive] = useState<string>('');

    const { data: user, isSuccess: loginSuccess } = useCheckLoginQuery();
    const userCurrent = user?.data?.email;
    const paramsRoom = pageQueryHanlder(1, 100);
    const { data: room, isSuccess: roomSuccess, refetch: refetchRoom } = useGetRoomQuery({
        email: user?.data?.email || '',
        param: paramsRoom,
    }, { skip: !loginSuccess || !user?.data?.id })
    const rooms = room?.data.items?.map((room) => {
        return {
            ...room,
            sendDate: moment(room.sendDate, 'YYYY-MM-DD HH:mm:ss').toDate(), // Chuyển đổi thành Date
        };
    });

    // Sắp xếp tăng dần theo thời gian gần nhất
    const sortedRooms = rooms
        ?.filter(room => room.sendDate) // Lọc bỏ phần tử không có sendDate
        .sort((a, b) => {
            const timeA = moment(a.sendDate); // Đọc sendDate dạng ISO
            const timeB = moment(b.sendDate);

            return timeB.diff(timeA); // Sắp xếp giảm dần theo thời gian
        });
    const paramsMessage = pageQueryHanlder(1, 1000);
    const { data: messagesData, refetch, isSuccess } = useGetMessageQuery({
        roomId: roomActive,
        params: paramsMessage,
    }, { skip: !roomSuccess });

    const [updateRoom] = useUpdateRoomMutation();
    const handleUpdateRoom = async (id: string, count: number) => {
        if (count === 0) {
            return;
        }
        try {
            await updateRoom(id);
            refetchRoom();
        } catch (error) {
            console.log(error);
        }
    }
    const handleActive = (id: string) => {
        setRoomActive(id);
    }

    useEffect(() => {
        if (roomSuccess && room?.data.items.length) {
            refetch();
        }
    }, [roomActive]);
    useEffect(() => {
        if (isSuccess) {
            refetch();
        }
    }, [messagesData, isSuccess]);
    console.log(sortedRooms)
    return (
        <div className=" bg-light p-4" style={{ height: '100%', }}>
            <div className="row bg-white" style={{ boxShadow: '0 4px 8px rgba(0, 0, 0, 0.1)' }}>
                <div className="col-3 border-end">
                    <div>
                        <div className='search-message'>
                            <i className="bi bi-search"></i>
                            <input type="text" placeholder='Tìm kiếm ' />
                        </div>
                        <SimpleBar style={{ height: 500 }}>
                            {sortedRooms?.map((item: Room) => (
                                <div key={item.conversationId} className="d-flex align-items-center gap-2 menu-message-items"
                                    onClick={() => { handleActive(item.conversationId); handleUpdateRoom(item.conversationId, item.count) }}
                                >
                                    <img src={item.avatarSender !== null ? item.avatarSender : 'https://cdn.kona-blue.com/upload/kona-blue_com/post/images/2024/09/18/457/avatar-mac-dinh-1.jpg'} width={50} height={50} alt="" />
                                    <div className="d-flex flex-column gap-1">
                                        <span>{item.receiver}</span>
                                        <span className="text-muted">{(item.count === 0 && item.seen === false) ? 'Bạn: ' : ''}{item.lastMessageSender}</span>

                                    </div>
                                    {(item.seen === false && item.count > 0) && <div className='count-message'>{item.count}</div>}
                                </div>
                            ))}
                        </SimpleBar>
                    </div>
                </div>
                <div className="col-9 ">
                    {roomActive !== '' && <ViewContent messagesData={messagesData?.data.items || []}
                        userCurrent={userCurrent || ''}
                        refetch={refetch}
                        refetchRoom={refetchRoom}
                    />}
                    {roomActive === '' && <div className="d-flex justify-content-center align-items-center mt-3">
                        <img src="https://img.freepik.com/premium-photo/speech-bubble-as-cloud-with-blue-border-isolated_113767-1431.jpg" alt="" />
                    </div>}
                </div>
            </div>
        </div>
    );
}

export default Message;