import { useState } from "react";
import { useLazyResetPasswordQuery } from "../../../../services/auth.service";
import { validateEmail } from "../../../../utils/validate";
import ModalLoading from "../../../../components/loading/ModalLoading";
import useRedirect from "../../../../hooks/useRedirect";

const ForgotPassword = () => {
    const [email, setEmail] = useState("");
    const [err, setErr] = useState("");
    const [sendOtp, {isFetching}] = useLazyResetPasswordQuery();
    const redirect = useRedirect();

    const handlerSendOtp = async () => {
        if(email === "") {
            setErr("Vui lòng nhập email");
            return;
        }
        if(!validateEmail(email)) {
            setErr("Email không hợp lệ");
            return;
        }
        try {
            await sendOtp(email).unwrap();
            redirect("/auth/verify-email?email=" + email + "&type=reset-password");
        } catch (error) {
            console.log(error);
            setErr("Email chưa đăng ký tài khoản");
        }
        
    }
    return (
        <div className="card login-card p-4 shadow flex-grow-1">
            <div className="text-center mb-4">
                <h3 className="text-center">QUÊN MẬT KHẨU</h3>
            </div>
            <div className="text-center mb-4">
                <span className="text-center text-medium">Nhập email của bạn</span>
            </div>
            <div className="mb-3">
                <input type="text" className="form-control no-shadow" placeholder="Email"
                 value={email} onChange={(e) => {
                    setEmail(e.target.value);
                    setErr("")}}/>
                <div className="mt-2 d-flex flex-column ps-2">
                    <span className="primary">{err}</span>
                </div>

            </div>
            <div className="d-grid pt-4">
                <button type="button" className="button-flex button-hover background-primary text-medium"
                onClick={handlerSendOtp}>Gửi mã xác thực</button>
            </div>
            {isFetching && <ModalLoading loading={isFetching}/>}

        </div>
    )
}

export default ForgotPassword;