import { useState } from 'react';
import './Login.scss'; // Optional, if you want to add custom styles
import { FaFacebook, FaGoogle } from 'react-icons/fa';
import { AiFillEye, AiFillEyeInvisible } from 'react-icons/ai'; // Import icon mới
import { Link } from 'react-router-dom';
import { useLoginMutation } from '../../../../services/auth.service';
import ModalLoading from '../../../../components/loading/ModalLoading';
import useGetParam from '../../../../hooks/useGetParam';



function Login() {
  const [passwordVisible, setPasswordVisible] = useState(false);
  const [username, setusername] = useState("");
  const [password, setpassword] = useState("");
  const [error, setError] = useState("");
  const [loginRequest, { isLoading }] = useLoginMutation();
  const redirectUrl = useGetParam('redirect-url');

  const togglePasswordVisibility = () => {
    setPasswordVisible(!passwordVisible);
  };

  const handleLogin = async () => {
      if(username && password) {
        setError("");
        try {
          await loginRequest({ username, password }).unwrap();
          if(redirectUrl) {
            window.location.href = `/${redirectUrl}`
          } else { 
            window.location.href = `/`; 
          }
          
        } catch (error) {
          console.log(error);
          setError("Tên đăng nhập hoặc mật khẩu không chính xác");
        }
      } else {
        setError("Vui lòng nhập thông tin")
      }
  }

  return (
    <div className="card login-card p-4 shadow flex-grow-1">
      <div className="text-center mb-4">
        <h3 className="text-center">ĐĂNG NHẬP</h3>
      </div>
      <form>
        <div className="mb-3">
          <input type="text" className="form-control no-shadow"
            placeholder="Email/Số điện thoại/Tên đăng nhập"
            value={username}
            onChange={e => {setusername(e.target.value); setError("")}}
          />
        </div>
        <div className="mb-3 position-relative">
          <input
            type={passwordVisible ? "text" : "password"}
            className="form-control no-shadow"
            placeholder="Mật khẩu"
            value={password}
            onChange={e => {setpassword(e.target.value); setError("")}}
          />
          <span
            className="position-absolute top-50 end-0 translate-middle-y pe-3"
            style={{ cursor: 'pointer' }}
            onClick={togglePasswordVisibility}
          >
            {passwordVisible ? <AiFillEyeInvisible /> : <AiFillEye />}
          </span>
        </div>
        <div className="mb-3 position-relative ps-2">
          <span className='primary'>{error}</span>
        </div>
        <div className="d-grid pt-4">
          <button type="button" onClick={handleLogin} className="button-flex button-hover background-primary text-medium">Đăng nhập</button>
        </div>
        <div className="text-end mt-3">
          <Link to="/auth/forgot-password" className="a-disable-default">Quên mật khẩu</Link>
        </div>
        <hr />
        <div className="d-flex justify-content-center">
          <button className="btn btn-outline-primary me-3 btn-social">
            <FaFacebook className="me-2" /> Facebook
          </button>
          <button className="btn btn-outline-danger btn-social">
            <FaGoogle className="me-2" /> Google
          </button>
        </div>
        <p className="text-center mt-3">
          Bạn mới biết đến nền tảng? <Link to={`/auth/register${redirectUrl ? '?redirect-url=' + encodeURIComponent(redirectUrl) : ''}`} className="text-danger">Đăng ký</Link>
        </p>
        <p className="text-center mt-3">
          <Link to="/" >Về trang chủ</Link>
        </p>
      </form>
      {isLoading && <ModalLoading loading={isLoading}/>}
    </div>
  );
}

export default Login;
