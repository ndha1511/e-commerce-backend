import { Container } from "react-bootstrap";

const AccessDenied: React.FC = () => {
    return <Container style={{
        height: "300px",
        display: "flex",
        flexDirection: "column",
        justifyContent: "center",
        alignItems: "center",
    }}>
        <h3>Bạn không có quyển truy cập trang này</h3>
        <button>Vể trang chủ</button>
    </Container>

}

export default AccessDenied;