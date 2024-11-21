import { useRef, useState } from "react";
import ModalLoading from "../../../../components/loading/ModalLoading";
import useGetParam from "../../../../hooks/useGetParam";
import './password.scss'
import { useCheckLoginQuery, useLazyResetPasswordQuery, useLazyVerifyEmailResetPasswordQuery } from "../../../../services/auth.service";
import { useDispatch } from "react-redux";
import { setNotify } from "../../../../rtk/slice/notify-slice";

export interface FormOtp {
    onConfirm: () => void;
}
function FormOtp({ onConfirm }: FormOtp) {
    const [otp, setOtp] = useState<string[]>(Array.from({ length: 6 }, () => ''));
    const inputRefs = useRef<Array<HTMLInputElement | null>>([]);
    const { data: user, isSuccess: loginSuccess } = useCheckLoginQuery();
    const email = user?.data?.email;
    const [verifyEmailResetPwd, { isLoading,error }] = useLazyVerifyEmailResetPasswordQuery();
    const [sendOtp, { isLoading: otpLoading }] = useLazyResetPasswordQuery();
    const [disable, setDisable] = useState(true);
    const dispatch = useDispatch();
    const handleChangeInput = (e: React.ChangeEvent<HTMLInputElement>, index: number) => {
        setDisable(false);
        const newOtp = [...otp];
        newOtp[index] = e.target.value;
        setOtp(newOtp);
        if (e.target.value.length === 1 && index < 5) {
            inputRefs.current[index + 1]?.focus();
        } else if (e.target.value.length === 0 && index > 0) {
            inputRefs.current[index - 1]?.focus();
        }
    }

    const handleSendOtp = async () => {
        if (email) {
            try {
                await sendOtp(email);
                setOtp(Array.from({ length: 6 }, () => ''));
            } catch (error) {
                dispatch(setNotify({
                    message: 'Thao tác không thành công',
                    type: 'error'
                }))
            }
        }
    }
    const handleVerify = async () => {
        const otpString = otp.reduce((acc, val) => acc + val, '');
        if (email) {

            try {
                await verifyEmailResetPwd({ email, otp: otpString }).unwrap();
                onConfirm();
            } catch (error) {
                console.error(error);
            }

        }

    }
    return (
        <div className=" shadow-form-email p-5">
            <div className="text-center mb-4">
                <h3 className="text-center">XÁC THỰC EMAIL</h3>
            </div>
            <div className="text-center mb-4">
                <span className="text-center text-medium">Nhập mã otp được gửi tới mail của bạn</span>
            </div>
            <div className="text-center mb-4 input-number-wrapper ">
                {otp.map((value, index) =>
                    <input key={index} className="input-number text-medium" maxLength={1} value={value}
                        autoFocus={index === 0}
                        ref={(el) => (inputRefs.current[index] = el)}
                        onChange={(e) => handleChangeInput(e, index)}
                    ></input>)}

            </div>
            <div className="text-center">
               {error ?  <span className="primary">Otp không chính xác!</span> :<></>}
            </div>
            <div className="d-grid pt-4">
                <button disabled={disable} type="button" className="btn btn-danger background-primary text-medium"
                    onClick={handleVerify}
                >Xác thực</button>
            </div>
            <div className="d-flex pt-4 justify-content-center">
                <span className="text-primary" style={{ cursor: 'pointer' }}
                    onClick={handleSendOtp}
                >Gửi lại mã</span>
            </div>
            {isLoading && <ModalLoading loading={isLoading} />}
            {otpLoading && <ModalLoading loading={otpLoading} />}
        </div>
    );
}

export default FormOtp;