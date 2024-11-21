// src/components/MessageContent.tsx
import React, { useEffect, useState } from "react";
import './message.scss'
import {  MessageType } from "../../../models/message";
import { MessageProps } from "./MessageView";
import ImageMessage from "./ImageMessage";
import TextMessage from "./TextMessage";
import VideoMessage from "./VideoMessage";

interface MessageTextProps {
  message: MessageProps;
  userCurrent?: string;
  err: string;
}

const MessageContent: React.FC<MessageTextProps> = ({ message, userCurrent, err }) => {
  const [errors, setErrors] = useState<string[]>([]);

  useEffect(() => {
    if (err && message.text && err === message.text && !errors.includes(err)) {
      setErrors([...errors, err]);
    }
  }, [err, message.text, errors]);

  const isSender = message.sender === userCurrent;
  const hasError = errors.includes(message.text);

  return (
    <div className={`message-text-${isSender ? "sender" : "receiver"}`}>
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
  );
};

export default MessageContent;
