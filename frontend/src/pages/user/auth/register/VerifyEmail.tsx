import { useEffect, useRef, useState } from "react";
import useGetParam from "../../../../hooks/useGetParam";
import useRedirect from "../../../../hooks/useRedirect";
import "./verify-email.scss";
import { useLazySendOtpQuery, useLazyVerifyEmailQuery, useLazyVerifyEmailResetPasswordQuery } from "../../../../services/auth.service";
import ModalLoading from "../../../../components/loading/ModalLoading";
import { printError } from "../../../../utils/error-handler";

const VerifyEmail = () => {
    const email = useGetParam('email');
    const type = useGetParam('type');
    const redirect = useRedirect();
    const inputRefs = useRef<Array<HTMLInputElement | null>>([]);
    const [otp, setOtp] = useState<string[]>(Array.from({ length: 6 }, () => ''));
    const [verifyEmail, { error, isLoading }] = useLazyVerifyEmailQuery();
    const [sendOtp, { error: otpError, isFetching: otpLoading }] = useLazySendOtpQuery();
    const [verifyEmailResetPwd, { error: resetErr, isFetching: resetFetching}] = useLazyVerifyEmailResetPasswordQuery();
    const [disable, setDisable] = useState(true);



    const handleChangeInput = (e: React.ChangeEvent<HTMLInputElement>, index: number) => {
        const newOtp = [...otp];
        newOtp[index] = e.target.value;
        setOtp(newOtp);
        if (e.target.value.length === 1 && index < 5) {
            inputRefs.current[index + 1]?.focus();
        } else if(e.target.value.length === 0 && index > 0) {
            inputRefs.current[index - 1]?.focus();
        }
    }

    useEffect(() => {
        if(!email) {
            redirect('/');
        }
    }, []);

    useEffect(() => {
        let flag: boolean = false;
        otp.forEach((value) => {
            if (value === '') {
                flag = true;
            }
        });
        if(!flag) setDisable(false)
        else setDisable(true)
    }, [otp]);

    const handleVerify = async () => {
        const otpString = otp.reduce((acc, val) => acc + val, '');
        if(email) {
            if(type === 'reset-password') {
                try {
                    await verifyEmailResetPwd({ email, otp: otpString }).unwrap();
                    redirect("/auth/create-new-password?email=" + email);
                } catch (error) {
                    console.error(error);
                }
            } else {
                try {
                    await verifyEmail({ email, otp: otpString }).unwrap();
                    window.location.href = "/";
                } catch (error) {
                    console.error(error);
                }
            }
        }
        
    }

    const handleSendOtp = async () => {
        if(email) {
            try {
                await sendOtp(email).unwrap();
                setOtp(Array.from({ length: 6 }, () => ''));
                inputRefs.current[0]?.focus();
            } catch (error) {
                console.error(error);
            }
        }
    }

    return (
        <div className="card login-card p-4 shadow flex-grow-1">
            <div className="text-center mb-4">
                <h3 className="text-center">XÁC THỰC EMAIL</h3>
            </div>
            <div className="text-center mb-4">
                <span className="text-center text-medium">Nhập mã otp được gửi tới mail của bạn</span>
            </div>
            <div className="text-center mb-4 input-number-wrapper">
                {otp.map((value, index) =>
                    <input key={index} className="input-number text-medium" maxLength={1} value={value}
                        autoFocus={index === 0}
                        ref={(el) => (inputRefs.current[index] = el)}
                        onChange={(e) => handleChangeInput(e, index)}
                    ></input>)}

            </div>
            <div className="text-center mb-4 input-number-wrapper">
                {error ? <span className="primary">{printError(error)}</span> : <></>}
            </div>
            <div className="text-center mb-4 input-number-wrapper">
                {otpError || resetErr? <span className="primary">Đã xảy ra lỗi, vui lòng thử lại sau</span> : <></>}
            </div>
            <div className="d-grid pt-4">
                <button disabled={disable} type="button" className="btn btn-danger background-primary text-medium"
                onClick={handleVerify}
                >Xác thực</button>
            </div>
            <div className="d-flex pt-4 justify-content-center">
                <span className="text-primary" style={{cursor: 'pointer'}} 
                onClick={handleSendOtp}
                >Gửi lại mã</span>
            </div>
            {isLoading && <ModalLoading loading={isLoading}/>}
            {otpLoading && <ModalLoading loading={otpLoading}/>}
            {resetFetching && <ModalLoading loading={resetFetching}/>}
        </div>
    )
}

export default VerifyEmail;