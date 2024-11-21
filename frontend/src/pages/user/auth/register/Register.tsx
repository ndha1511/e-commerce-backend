import { useEffect, useState } from "react";
import { AiFillEye, AiFillEyeInvisible } from "react-icons/ai";
import { Link } from "react-router-dom";
import { validateEmail, validatePassword } from "../../../../utils/validate";
import { useLazyCheckEmailQuery, useRegisterMutation } from "../../../../services/auth.service";
import useDebounce from "../../../../hooks/useDebounce";
import ModalLoading from "../../../../components/loading/ModalLoading";
import { redirect } from "../../../../utils/location";

const Register = () => {
    const [passwordVisible, setPasswordVisible] = useState(false);
    const [password, setPassword] = useState("");
    const [email, setEmail] = useState("");
    const [hasLength, hasUppercase, hasDigit, hasSpecialChar] = validatePassword(password);
    const [errorEmail, setErrorEmail] = useState("");
    const [checkEmail, { error: emailExists }] = useLazyCheckEmailQuery();
    const debounceEmail = useDebounce(email, 1000);
    const [confirmPassword, setConfirmPassword] = useState("");
    const [errorConfirmPassword, setErrorConfirmPassword] = useState("");
    const [registerRequest, { isLoading }] = useRegisterMutation();


    const togglePasswordVisibility = () => {
        setPasswordVisible(!passwordVisible);
    };

    const handleChangeEmail = (e: React.ChangeEvent<HTMLInputElement>) => {
        setEmail(e.target.value);
        if (e.target.value === "") {
            setErrorEmail("Vui lòng nhập email");
            return;
        }
        if (!validateEmail(e.target.value)) {
            setErrorEmail("Email không hợp lệ");
        } else {
            setErrorEmail("");
        }
    }

    const handleChangeConfirmPassword = (e: React.ChangeEvent<HTMLInputElement>) => {
        setConfirmPassword(e.target.value);
       
        setErrorConfirmPassword("");
    }

    const handleRegister = async () => {
        if(email === "") {
            setErrorEmail("Vui lòng nhập email");
            return;
        }
        if(!validateEmail(email)) {
            setErrorEmail("Email không hợp lệ");
            return;
        }
        if(confirmPassword !== password) {
            setErrorConfirmPassword("Mật khẩu xác nhận không đúng");
            return;
        }
        if (confirmPassword === "") {
            setErrorConfirmPassword("Vui lòng nhập xác nhận mật khẩu");
            return;
        }
        if (hasLength && hasUppercase && hasSpecialChar && hasDigit
            && !emailExists && !errorConfirmPassword) {
            try {
                await registerRequest({ email, password, confirmPassword }).unwrap();
                redirect('/auth/verify-email?email=' + email);
            } catch (error) {
                console.log(error);
            }
        }
    }

    useEffect(() => {
        if (debounceEmail && validateEmail(debounceEmail)) {
            checkEmail(debounceEmail);
        }
    }, [debounceEmail])

    return (
        <div className="card login-card p-4 shadow flex-grow-1">
            <div className="text-center mb-4">
                <h3 className="text-center">ĐĂNG KÝ</h3>
            </div>
            <form>
                <div className="mb-3">
                    <input type="text" className="form-control no-shadow" placeholder="Email"
                        onChange={handleChangeEmail} value={email} />
                    <div className="mt-2 d-flex flex-column ps-2">
                        <span className="primary">{errorEmail}</span>
                    </div>
                    {emailExists ? <div className="mt-2 d-flex flex-column ps-2">
                        <span className="primary">Email đã tồn tại</span>
                    </div> : <></>}
                </div>
                <div className="mb-3">
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
                        onClick={handleRegister}
                    >Tạo tài khoản</button>
                </div>
                <div className="d-flex pt-4 justify-content-between">
                    <div>
                        <span>Bạn đã có tài khoản? </span><Link to={"/auth/login"}>Đăng nhập</Link>
                    </div>
                    <Link to={"/"}>Về trang chủ</Link>
                </div>
            </form>
            {isLoading && <ModalLoading loading={isLoading} />}
        </div>
    )
}

export default Register;