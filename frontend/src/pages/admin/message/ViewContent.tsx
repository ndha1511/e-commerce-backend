import React, { ChangeEvent, KeyboardEvent, useEffect, useRef, useState } from 'react';
import MessageContent from './MessageContent';
import './message.scss';
import { MessageProps } from '../../user/message/MessageView';
import { Message, MessageStatus, MessageType } from '../../../models/message';
import { useCreateMessageMutation } from '../../../services/message.service';
import moment from 'moment';
import SimpleBar from 'simplebar-react';
interface ViewContentProps {
    messagesData: Message[];
    userCurrent: string;
    refetch: () => void;
    refetchRoom: () => void;
}

const ViewContent: React.FC<ViewContentProps> = ({ messagesData, userCurrent, refetch,refetchRoom }) => {
    const [newMessage, setNewMessage] = useState<string>("");
    const [file, setFile] = useState<File>();
    const [url, setUrl] = useState<string>("");
    const [err, setErr] = useState<string>('');
    const [messages, setMessages] = useState<MessageProps[]>([]);
    const [createMessage] = useCreateMessageMutation();
    const messagesEndRef = useRef<HTMLDivElement | null>(null);
    const userSender = messagesData?.[0]?.sender === userCurrent
        ? messagesData?.[0]?.receiver
        : messagesData?.[0]?.sender;
    useEffect(() => {
        if (messagesData) {
            const formattedMessages: MessageProps[] = messagesData.map((msg, index) => {
                const formattedDate = moment(msg.sendDate, 'YYYY-MM-DD HH:mm:ss').toISOString(); // Chuyển sang định dạng ISO
                const timestamp = moment(formattedDate).format('hh:mm:ss'); // Lấy thời gian theo định dạng HH:mm:ss
                return {
                    id: index + 1, // Đặt `id` là số tăng dần
                    text: msg.content, // Sử dụng `content` hoặc `message`
                    sender: msg.sender,
                    messageType: msg.messageType,
                    messageStatus: msg.messageStatus,
                    timestamp: timestamp
                }
            });
            setMessages(formattedMessages);
        }
    }, [messagesData]);
    const handleInputChange = (e: ChangeEvent<HTMLInputElement>): void => {
        setNewMessage(e.target.value);
    };
    const handleKeyDown = (e: KeyboardEvent<HTMLInputElement>): void => {
        if (e.key === "Enter") {
            e.preventDefault();
            handleSendMessage();
        }
    };
    const handleSendMessage = async (file?: File) => {

        if (newMessage || file) {
            let imageUrl = "";
            if (file) {
                imageUrl = URL.createObjectURL(file);
            }
            const newMsg: MessageProps = {
                id: messages.length + 1,
                text: newMessage || url || '',
                sender: userCurrent || '',
                messageType: getFileType(file),
                messageStatus: MessageStatus.SENDING,
                timestamp: new Date().toLocaleTimeString([], { hour: '2-digit', minute: '2-digit', second: '2-digit', hour12: false }),  // Lấy thời gian hiện tại và chuyển thành chuỗi
            };
            setMessages([newMsg, ...messages]);
            try {
                const newFormData = new FormData();
                if (newMessage) {
                    newFormData.append('message', newMessage);
                }
                if (file) {
                    newFormData.append('file', file);
                }
                newFormData.append('sender', userCurrent || '');
                newFormData.append('receiver', userSender);
                const res = await createMessage(newFormData).unwrap();
                refetchRoom();
                if (res.data.content) {
                    refetch();
                    setUrl('')
                    setFile(undefined);
                }
                setErr('');
                setNewMessage("");
            } catch (error) {
                console.log(error);
                if (file) {
                    setErr(url);
                    setUrl('');
                    setFile(undefined);
                    return;
                } else {
                    setErr(newMessage)
                }
                setNewMessage("");
            }
        }
    };
    function getFileType(file?: File): MessageType {
        // Kiểm tra nếu file là kiểu MIME (như khi upload qua input)
        if (file && file.type) {
            if (file.type.startsWith("image/")) {
                return MessageType.IMAGE;
            } else if (file.type.startsWith("video/")) {
                return MessageType.VIDEO;
            }

        }
        return MessageType.TEXT;
    }
    const handleChangeImage = async (e: React.ChangeEvent<HTMLInputElement>) => {
        const files = e.target.files;
        if (files && files.length > 0) {
            const imageUrl = URL.createObjectURL(files[0]);
            setUrl(imageUrl);
            setFile(files[0]);

        }
    };
    useEffect(() => {
        handleSendMessage(file);
    }, [url, file])
    const scrollToBottom = (smooth: boolean = false): void => {
        if (messagesEndRef.current !== null) { // Kiểm tra null trước khi gọi
            setTimeout(() => {
                messagesEndRef.current!.scrollIntoView({
                    behavior: smooth ? 'smooth' : 'auto',
                });
            }, 100); // Thời gian chờ 100ms (bạn có thể điều chỉnh thời gian theo nhu cầu)
        }
    };
    useEffect(() => {
        scrollToBottom(true); // Sử dụng `smooth` khi có tin nhắn mới
    }, [messages]);
    return (
        <div className='view-content'>
            <div className="sender-message-title">
                <img
                    src="https://cdn.kona-blue.com/upload/kona-blue_com/post/images/2024/09/18/457/avatar-mac-dinh-1.jpg"
                    alt="User Avatar"
                />
                <span>{userSender}</span>
            </div>
            <SimpleBar style={{ height: 430 }}>
                <div className="message-content-list">

                    {messages.map((message) => (
                        <div
                            key={message.id}
                            className={`message-${message.sender === userCurrent ? 'sender' : 'receiver'}`}
                        >
                            <MessageContent
                                message={message}
                                userCurrent={userCurrent}
                                err={err}
                            />
                        </div>
                    ))}

                </div>
                <div ref={messagesEndRef} />
            </SimpleBar>
            <div className="message-content-footer pb-2">
                <div className="footer-motion-admin">
                    <label htmlFor="file-message" style={{ cursor: 'pointer' }}>
                        <i className="bi bi-camera"></i>
                        <input id="file-message" type="file"
                            className="hidden-button-file"
                            onChange={(e) => handleChangeImage(e)}
                        />
                    </label>
                    <input type="text" placeholder="Nhập tin nhắn"
                        value={newMessage}
                        onChange={(e) => handleInputChange(e)}
                        onKeyDown={handleKeyDown} />
                    <i
                        className="bi bi-send"
                        onClick={() => handleSendMessage()}
                        style={{ rotate: '45deg', cursor: 'pointer' }}
                    ></i>
                </div>
            </div>
        </div>
    );
};

export default ViewContent;
