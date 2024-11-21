import { useEffect, useState } from 'react';
import { Message, MessageType } from '../../../models/message';
import ImageMessage from '../../user/message/ImageMessage';
import { MessageProps } from '../../user/message/MessageView';
import TextMessage from '../../user/message/TextMessage';
import VideoMessage from '../../user/message/VideoMessage';
import './message.scss'

interface MessagesProps {
    message: MessageProps;
    userCurrent: string;
    err: string;
}
function MessageContent({ message, userCurrent, err }: MessagesProps) {
    const [errors, setErrors] = useState<string[]>([]);
    useEffect(() => {
        if (err && message.text && err === message.text && !errors.includes(err)) {
            setErrors([...errors, err]);
        }
    }, [err, message.text, errors]);

    const isSender = message.sender === userCurrent;
    const hasError = errors.includes(message.text);
    return (
        <div className='message-content-container'>
            <div className={`message-text-${message.sender === userCurrent ? "sender" : "receiver"}`}>
                {message.messageType === MessageType.IMAGE ? (
                    <ImageMessage
                        src={message.text}
                        timestamp={message.timestamp}
                        status={message.messageStatus}
                        isSender={isSender}
                        hasError={hasError}
                    />
                ) : message.messageType === MessageType.TEXT ? (
                    <TextMessage
                        text={message.text}
                        timestamp={message.timestamp}
                        status={message.messageStatus}
                        isSender={isSender}
                        hasError={hasError}
                    />
                ) : message.messageType === MessageType.VIDEO ? (
                    <VideoMessage
                        src={message.text}
                        timestamp={message.timestamp}
                        status={message.messageStatus}
                        isSender={isSender}
                        hasError={hasError}
                    />
                ) : null}

            </div>

        </div>
    );
}

export default MessageContent;