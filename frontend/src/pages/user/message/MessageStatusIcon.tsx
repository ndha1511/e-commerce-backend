// src/components/MessageStatusIcon.tsx
import React from "react";
import { Spinner } from "react-bootstrap";

interface MessageStatusIconProps {
  hasError: boolean;
  status: string;
}

const MessageStatusIcon: React.FC<MessageStatusIconProps> = ({ hasError, status }) => {
  return (
    <div className="d-flex justify-content-center align-items-center">
      {hasError ? (
        <i style={{color:'red'}} className="bi bi-exclamation-circle"></i>
      ) : status === 'SENDING' ? (
        <Spinner animation="border" className="small-spinner" role="status">
          <span className="visually-hidden">Loading...</span>
        </Spinner>
      ) : (
        <i className="bi bi-check-lg" style={{ color: 'green', fontSize: 10 }}></i>
      )}
    </div>
  );
};

export default MessageStatusIcon;
