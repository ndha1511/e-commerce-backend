import { ReactNode } from "react";
import { Col, Row } from "react-bootstrap";

interface PromotionRowProps {
  label: string;
  children: ReactNode;
  className?: string;
}
const PromotionRow: React.FC<PromotionRowProps> = ({ label, children, className = '' }) => (
  <Row className={`d-flex align-items-center mb-3 mt-1 ${className}`}>
    <Col md={2} className="text-end">
      <div><span><span className="primary">*</span>{label}</span></div>
    </Col>
    <Col md={10}>{children}</Col>
  </Row>
);

export default PromotionRow;
