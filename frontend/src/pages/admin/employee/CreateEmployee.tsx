import { useEffect, useState } from "react";
import { Button, Form, InputGroup } from "react-bootstrap";
import { FaCheck, FaEnvelope, FaLock, FaUser, FaEye, FaEyeSlash } from "react-icons/fa"; // Các biểu tượng gốc
import { useCreateEmployeeMutation } from "../../../services/user.service";
import { useDispatch } from "react-redux";
import { setNotify } from "../../../rtk/slice/notify-slice";

function CreateEmployee() {
    const [createEmployee] = useCreateEmployeeMutation();
    const dispatch = useDispatch();
    const [formData, setFormData] = useState({
        email: '',
        name: '',
        password: '',
        confirmPassword: '',
    });

    const [showPassword, setShowPassword] = useState(false);
    const [showConfirmPassword, setShowConfirmPassword] = useState(false);
    const [disable, setDisable] = useState(true);

    const handleChange = (e: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement | HTMLSelectElement>) => {
        const { name, value } = e.target;
        setFormData({ ...formData, [name]: value });
    };

    useEffect(() => {
        if (formData.email !== '' && formData.name !== '' && formData.password !== '' && formData.confirmPassword !== '') {
            console.log('123')
            setDisable(false);
        } else {
            setDisable(true);
        }
    }, [formData])
    const handleSubmit = async (e:React.FormEvent<HTMLFormElement>) => {
        e.preventDefault();

        try {
            await createEmployee({
                email: formData.email,
                name: formData.name,
                password: formData.password,
            }).unwrap();
         dispatch(setNotify({
             type: "success",
             message: "Thêm nhân viên thành công"
         }))
        } catch (error) {
            console.log(error);
            dispatch(setNotify({
                type: "error",
                message: "Thêm nhân viên không thành công"
            }))
        }
    };

    return (
        <div className="bg-light p-3">
            <span className="text-medium">Thêm tài khoản nhân viên</span>
            <div className="border border-radius-small bg-white p-5 mt-3 d-flex align-items-center justify-content-center">
                <Form onSubmit={(e)=>handleSubmit(e)} className="border p-4 w-50 border-radius-small shadow-all">
                    <Form.Group controlId="formEmail" className="mb-3">
                        <Form.Label>Email</Form.Label>
                        <InputGroup>
                            <InputGroup.Text style={{ border: "1px solid #ccc", background: "transparent" }}>
                                <FaEnvelope style={{ color: "orange" }} />
                            </InputGroup.Text>
                            <Form.Control
                                className="no-shadow"
                                type="email"
                                placeholder="Nhập email"
                                name="email"
                                value={formData.email}
                                onChange={(e)=>handleChange(e)}
                                required
                            />
                        </InputGroup>
                    </Form.Group>

                    <Form.Group controlId="formName" className="mb-3">
                        <Form.Label>Tên</Form.Label>
                        <InputGroup>
                            <InputGroup.Text style={{ border: "1px solid #ccc", background: "transparent" }}>
                                <FaUser style={{ color: "orange" }} />
                            </InputGroup.Text>
                            <Form.Control
                                className="no-shadow"
                                type="text"
                                placeholder="Nhập tên"
                                name="name"
                                value={formData.name}
                                onChange={handleChange}
                                required
                            />
                        </InputGroup>
                    </Form.Group>

                    <Form.Group controlId="formPassword" className="mb-3">
                        <Form.Label>Mật khẩu</Form.Label>
                        <InputGroup>
                            <InputGroup.Text style={{ border: "1px solid #ccc", background: "transparent" }}>
                                <FaLock style={{ color: "orange" }} />
                            </InputGroup.Text>
                            <Form.Control
                                className="no-shadow"
                                type={showPassword ? "text" : "password"}
                                placeholder="Nhập mật khẩu"
                                name="password"
                                value={formData.password}
                                onChange={handleChange}
                                required
                            />
                            <InputGroup.Text onClick={() => setShowPassword(!showPassword)} style={{ cursor: "pointer", border: "1px solid #ccc", background: "transparent" }}>
                                {showPassword ? <FaEyeSlash style={{ color: "orange" }} /> : <FaEye style={{ color: "orange" }} />}
                            </InputGroup.Text>
                        </InputGroup>
                    </Form.Group>

                    <Form.Group controlId="formConfirmPassword" className="mb-3">
                        <Form.Label>Nhập lại mật khẩu</Form.Label>
                        <InputGroup>
                            <InputGroup.Text style={{ border: "1px solid #ccc", background: "transparent" }}>
                                <FaCheck style={{ color: "orange" }} />
                            </InputGroup.Text>
                            <Form.Control
                                className="no-shadow"
                                type={showConfirmPassword ? "text" : "password"}
                                placeholder="Nhập lại mật khẩu"
                                name="confirmPassword"
                                value={formData.confirmPassword}
                                onChange={handleChange}
                                isInvalid={!!formData.confirmPassword && formData.password !== formData.confirmPassword}
                                required
                            />
                            <InputGroup.Text onClick={() => setShowConfirmPassword(!showConfirmPassword)} style={{ cursor: "pointer", border: "1px solid #ccc", background: "transparent" }}>
                                {showConfirmPassword ? <FaEyeSlash style={{ color: "orange" }} /> : <FaEye style={{ color: "orange" }} />}
                            </InputGroup.Text>
                            <Form.Control.Feedback type="invalid">
                                Mật khẩu xác nhận không khớp.
                            </Form.Control.Feedback>
                        </InputGroup>
                    </Form.Group>

                    <div className="d-flex justify-content-end">
                        <Button
                            disabled={disable}
                            style={{ backgroundColor: 'red', borderColor: 'red' }} // Thiết lập màu đỏ
                            type="submit"
                        >
                            Đăng ký
                        </Button>
                    </div>
                </Form>
            </div>
        </div>
    );
}

export default CreateEmployee;
