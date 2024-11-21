// src/components/TextMessage.tsx
import React from "react";
import MessageStatusIcon from "./MessageStatusIcon";

interface TextMessageProps {
    text: string;
    timestamp: string;
    status: string;
    isSender: boolean;
    hasError: boolean;
}

const TextMessage: React.FC<TextMessageProps> = ({ text, timestamp, status, isSender, hasError }) => {
    return (
        <span>
            {text}
            <div className="message-time" style={{ justifyContent: isSender ? 'end' : 'start' }}>
                {timestamp}
                {isSender && <MessageStatusIcon hasError={hasError} status={status} />}
            </div>
        </span>
    );
};

export default TextMessage;
