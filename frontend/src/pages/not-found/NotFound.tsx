import { Container } from "react-bootstrap";

const NotFound: React.FC = () => {
    return <Container style={{
        height: "300px",
        display: "flex",
        flexDirection: "column",
        justifyContent: "center",
        alignItems: "center",
    }}>
        <h3>Không tìm thấy trang này</h3>
        <button>Vể trang chủ</button>
    </Container>

}

export default NotFound;