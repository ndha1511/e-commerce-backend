
import { Modal } from "react-bootstrap";
import BeatLoader from "react-spinners/BeatLoader";



const ModalLoading = ({ color = "#FF0036", loading, size = 10 }: { color?: string, loading: boolean, size?: number }) => {
    return (
        <Modal
            centered
            show={loading}
            className="d-flex"
        >
            <Modal.Body>
                <BeatLoader color={color} loading={loading} size={size} />
            </Modal.Body>
        </Modal>
    )
}

export default ModalLoading;