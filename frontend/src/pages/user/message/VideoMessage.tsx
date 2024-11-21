// src/components/VideoMessage.tsx
import React from "react";
import MessageStatusIcon from "./MessageStatusIcon";

interface VideoMessageProps {
  src: string;
  timestamp: string;
  status: string;
  isSender: boolean;
  hasError: boolean;
}

const VideoMessage: React.FC<VideoMessageProps> = ({ src, timestamp, status, isSender, hasError }) => {
  return (
    <div className="d-flex flex-column gap-1">
      <video src={src} height={180} style={{ minWidth: '150px' }} controls />
      <div className="message-time" style={{ justifyContent: isSender ? 'end' : 'start' }}>
        {timestamp}
        {isSender && <MessageStatusIcon hasError={hasError} status={status} />}
      </div>
    </div>
  );
};

export default VideoMessage;
