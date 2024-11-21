// src/components/ImageMessage.tsx
import React from "react";
import MessageStatusIcon from "./MessageStatusIcon";

interface ImageMessageProps {
  src: string;
  timestamp: string;
  status: string;
  isSender: boolean;
  hasError: boolean;
}

const ImageMessage: React.FC<ImageMessageProps> = ({ src, timestamp, status, isSender, hasError }) => {
  return (
    <div className="d-flex flex-column gap-1">
      <img
        className="img-message-sender"
        src={src}
        alt="Sent Image"
        style={{ maxWidth: '60%' }}
      />
      <div className="message-time" style={{ justifyContent: isSender ? 'end' : 'start' }}>
        {timestamp}
        {isSender && <MessageStatusIcon hasError={hasError} status={status} />}
      </div>
    </div>
  );
};

export default ImageMessage;
