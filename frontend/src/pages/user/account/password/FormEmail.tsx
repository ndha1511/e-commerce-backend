
import { useState } from 'react';
import { useCheckLoginQuery, useLazyResetPasswordQuery } from '../../../../services/auth.service';
import './password.scss'
import { useDispatch } from 'react-redux';
import { setNotify } from '../../../../rtk/slice/notify-slice';
import { OverlayTrigger } from 'react-bootstrap';
import CustomTooltip from '../../../../components/tooltip/CustomTooltipProps';
import ModalLoading from '../../../../components/loading/ModalLoading';
interface FormEmailProps {
    onConfirm: () => void;
}
function FormEmail({ onConfirm }: FormEmailProps) {
    const [trigger, { isLoading }] = useLazyResetPasswordQuery();
    const { data: user, isSuccess: loginSuccess } = useCheckLoginQuery();
    const email = user?.data?.email;
    const [btnSubmit, setBtnSubmit] = useState<boolean>(false);
    const dispatch = useDispatch();
    const handleConfirm = async () => {
        setBtnSubmit(true)
        if (email) {
            try {
                onConfirm();
                await trigger(email);
            
            } catch (error) {
                dispatch(setNotify({
                    message: 'Thao tác không thành công',
                    type: 'error'
                }))
            }
        }
    };
    return (
        <div className=' d-flex flex-column justify-content-center align-items-center  gap-1 p-5 shadow-form-email'>
            <i className="bi bi-shield-check"></i>
            <span>Để tăng cường bảo mật cho tài khoản của bạn, hãy xác minh thông tin bằng Email.</span>
                <div className="input-group mt-3 mb-3">
                    <span className="input-group-text" id="basic-addon1">
                        <i className="bi bi-envelope-fill"></i>
                    </span>
                    <input type="email" className="form-control no-shadow" readOnly value={email} aria-label="Email" aria-describedby="basic-addon1" />
                </div>
            <button type="button" className="btn btn-custom-orange" onClick={handleConfirm}>Xác nhận</button> {/* Nút xác nhận */}
            {isLoading && <ModalLoading loading={isLoading} />}
        </div>

    );
}

export default FormEmail;