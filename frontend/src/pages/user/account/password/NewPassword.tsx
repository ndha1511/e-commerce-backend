import { useState } from "react";
import { AiFillEye, AiFillEyeInvisible } from "react-icons/ai";
import { validatePassword } from "../../../../utils/validate";
import './password.scss'
import { useCheckLoginQuery, useCreateNewPasswordMutation } from "../../../../services/auth.service";
import ModalLoading from "../../../../components/loading/ModalLoading";
import { useDispatch } from "react-redux";
import { setNotify } from "../../../../rtk/slice/notify-slice";
function NewPassword() {
    const [passwordVisible, setPasswordVisible] = useState(false);
    const [password, setPassword] = useState("");
    const [hasLength, hasUppercase, hasDigit, hasSpecialChar] = validatePassword(password);
    const [confirmPassword, setConfirmPassword] = useState("");
    const [errorConfirmPassword, setErrorConfirmPassword] = useState("");
    const { data: user, isSuccess: loginSuccess } = useCheckLoginQuery();
    const [resetPasswordApi, {isLoading}] = useCreateNewPasswordMutation();
    const email = user?.data?.email;
    const dispatch = useDispatch();
    const togglePasswordVisibility = () => {
        setPasswordVisible(!passwordVisible);
    };
    const handleChangeConfirmPassword = (e: React.ChangeEvent<HTMLInputElement>) => {
        setConfirmPassword(e.target.value);

        setErrorConfirmPassword("");
    }
    const handleResetPassword = async () => {
        if (confirmPassword !== password) {
            setErrorConfirmPassword("Mật khẩu xác nhận không đúng");
            return;
        }
        if (confirmPassword === "") {
            setErrorConfirmPassword("Vui lòng nhập xác nhận mật khẩu");
            return;
        }
        if (hasLength && hasUppercase && hasSpecialChar && hasDigit
            && !errorConfirmPassword) {
            if(email) {
                try {
                    await resetPasswordApi({ email, password, confirmPassword }).unwrap();
                    window.location.href = "/";
                    dispatch(setNotify({
                        message: "Mật khẩu đã được cập nhật thành công",
                        type: "success"
                    }))
                } catch (error) {
                    console.log(error);
                }
            }
        }
    }
    return (
        <div className="shadow-form-email p-4  ">
            <div className="text-center mb-4">
                <h3 className="text-center">CẬP NHẬT MẬT KHẨU</h3>
            </div>

            <form style={{ paddingLeft: 0 }}>
                <div className="mb-3  ">
                    <div className="position-relative">
                        <input
                            type={passwordVisible ? "text" : "password"}
                            className="form-control no-shadow"
                            placeholder="Mật khẩu"
                            value={password}
                            onChange={(e) => setPassword(e.target.value)}
                        />
                        <span
                            className="position-absolute top-50 end-0 translate-middle-y pe-3"
                            style={{ cursor: 'pointer' }}
                            onClick={togglePasswordVisibility}
                        >
                            {passwordVisible ? <AiFillEyeInvisible /> : <AiFillEye />}
                        </span>
                    </div>
                    <div className="mt-2 d-flex flex-column ps-2">
                        <span className={!hasLength ? 'primary' : 'success'}><i className={!hasLength ? 'bi bi-x-circle' : 'bi bi-check-circle'}></i> Mật khẩu phải chứa ít nhất 8 ký tự</span>
                        <span className={!hasUppercase ? 'primary' : 'success'}><i className={!hasUppercase ? 'bi bi-x-circle' : 'bi bi-check-circle'}></i> Có ít nhất 1 ký tự in hoa</span>
                        <span className={!hasDigit ? 'primary' : 'success'}><i className={!hasDigit ? 'bi bi-x-circle' : 'bi bi-check-circle'}></i> Có ít nhất 1 ký tự số</span>
                        <span className={!hasSpecialChar ? 'primary' : 'success'}><i className={!hasSpecialChar ? 'bi bi-x-circle' : 'bi bi-check-circle'}></i> Có ít nhất 1 ký tự đặc biệt @$!%*?&.</span>
                    </div>
                </div>
                <div className="mb-3">
                    <input
                        type={passwordVisible ? "text" : "password"}
                        className="form-control no-shadow"
                        value={confirmPassword}
                        onChange={handleChangeConfirmPassword}
                        placeholder="Nhập lại mật khẩu"
                    />
                    <div className="mt-2 d-flex flex-column ps-2">
                        <span className="primary">{errorConfirmPassword}</span>
                    </div>
                </div>
                <div className="d-grid pt-4">
                    <button type="button" className="button-flex button-hover background-primary text-medium"
                        onClick={handleResetPassword}
                    >Hoàn tất</button>
                </div>
            </form>


            {isLoading && <ModalLoading loading={isLoading} />}

        </div>
    );
}

export default NewPassword;