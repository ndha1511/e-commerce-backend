import { Row, Col } from 'react-bootstrap';
import { BsGeoAltFill } from 'react-icons/bs';
import { UserAddress } from '../../models/user-address';
import ModalAddress from '../../pages/user/account/address/ModalAddress';
import { useState } from 'react';

const Address = ({info} : {info: UserAddress}) => {
    const [show, setShow] = useState(false);

    const handleClose = () => setShow(false);
    const handleShow = () => setShow(true);
    return (
        <div className="p-3 bg-white ">
            <Row className="align-items-center">
                <Col md={10}>
                    <div className="d-flex align-items-center mb-2">
                        <BsGeoAltFill color="red" className="me-2" />
                        <h6 className="mb-0 text-danger">Địa Chỉ Nhận Hàng</h6>
                    </div>
                    <div>
                        <strong>{info.receiverName}</strong> {info.phoneNumber}
                    </div>
                    <div className="mt-2">
                        {`${info.addressDetail}, ${info.ward}, ${info.district}, ${info.province}`}
                    </div>

                </Col>
                <Col md={2}> <button className='btn-address' onClick={()=>handleShow()}>Thay đổi</button></Col>
            </Row>
            {show && <ModalAddress show={show} handleClose={handleClose} />}
        </div>
    );
};

export default Address;
