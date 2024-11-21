import React, { useEffect, useState } from 'react';
import { Form, Row, Col } from 'react-bootstrap';
import './profile.scss';
import { useCheckLoginQuery } from '../../../../services/auth.service';
import { useUpdateUserMutation } from '../../../../services/user.service';
import ModalLoading from '../../../../components/loading/ModalLoading';
import { useDispatch } from 'react-redux';
import { setNotify } from '../../../../rtk/slice/notify-slice';

const Profile: React.FC = () => {
    const [avt, setAvt] = useState<File>();
    const [url, setUrl] = useState<string>("");
    const [isBtn, setIsBtn] = useState<boolean>(false);
    const { data: user, isSuccess: loginSuccess } = useCheckLoginQuery();
    const [trigger, { isLoading }] = useUpdateUserMutation();
    const dispatch = useDispatch();
    const [formData, setFormData] = useState({
        name: '',
        username: '',
        email: '',
        phoneNumber: '',
        gender: 'MALE',
        dateOfBirth: ''
    });
    useEffect(() => {
        if (loginSuccess && user) {
            setFormData({
                name: user?.data?.name || '',
                username: user?.data?.username || '',
                email: user?.data?.email || '',
                phoneNumber: user?.data?.phoneNumber || '',
                gender: user?.data?.gender || 'MALE',
                dateOfBirth: user?.data?.dateOfBirth ? new Date(user?.data?.dateOfBirth).toISOString().split('T')[0] : ''
            });
        }
    }, [loginSuccess, user]);

    const handleChangeImage = async (e: React.ChangeEvent<HTMLInputElement>) => {
        const files = e.target.files;
        if (files && files.length > 0) {
            setUrl(URL.createObjectURL(files[0]));
            setAvt(files[0]);
        }
    };
    const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        setFormData({
            ...formData,
            [e.target.name]: e.target.value
        });
    };
    useEffect(() => {
        return () => {
            URL.revokeObjectURL(url);
        };
    }, [url]);

    // Cập nhật hàm handleSubmit
    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault();
        const formDataToSubmit = new FormData();
        formDataToSubmit.append('name', formData.name);
        formDataToSubmit.append('username', formData.username);
        formDataToSubmit.append('email', formData.email);
        formDataToSubmit.append('phoneNumber', formData.phoneNumber);
        formDataToSubmit.append('gender', formData.gender);
        formDataToSubmit.append('dateOfBirth', formData.dateOfBirth);

        if (avt) {
            formDataToSubmit.append('avatar', avt); // Thêm file ảnh vào FormData
        }
        try {
            await trigger({ email: formData.email, newUser: formDataToSubmit }).unwrap();
            dispatch(setNotify({
                type: 'success', message: 'Thao tác thành công'
            }))
            setIsBtn(false);
        } catch (error) {
            console.error("Failed to update user:", error);
            dispatch(setNotify({
                type: 'error', message: 'Thao tác không thành công'
            }))

        }
    };
    return (
        <div className="profile-container ">
            <div className='d-flex justify-content-between'>
                <span className="text-large">Hồ Sơ Của Tôi</span>
                {!isBtn && <button onClick={() => { setIsBtn(true) }} className='button-flex button-hover background-primary text-medium'>
                    Cập nhật
                </button>}
            </div>
            <div className='d-flex w-100 profile-content flex-wrap '>
                <Form className={`col-12 col-md-6 ${isBtn ? '' : 'pointer-events-none'}  `} >
                    <Row className="mb-3 no-shadow">
                        <Form.Group as={Col} controlId="formEmail">
                            <Form.Label className='text-normal'>Email</Form.Label>
                            <Form.Control
                                className='no-shadow'
                                type="email"
                                name="email"
                                value={formData.email}
                                onChange={handleChange}
                                placeholder="Nhập email"
                                readOnly
                            />
                        </Form.Group>
                    </Row>
                    <Row className="mb-3">
                        <Form.Group as={Col} controlId="formName">
                            <Form.Label className='text-normal'>Tên</Form.Label>
                            <Form.Control
                                className='no-shadow'
                                type="text"
                                name="name"
                                value={formData.name}
                                onChange={handleChange}
                                placeholder="Nhập tên"
                            />
                        </Form.Group>
                    </Row>



                    <Row className="mb-3">
                        <Form.Group as={Col} controlId="formPhone">
                            <Form.Label className='text-normal'>Số điện thoại</Form.Label>
                            <Form.Control
                                className='no-shadow'
                                type="text"
                                name="phoneNumber"
                                value={formData.phoneNumber}
                                onChange={handleChange}
                                placeholder="Nhập số điện thoại"
                            />
                        </Form.Group>
                    </Row>

                    <Row className="mb-3">
                        <Form.Group as={Col} controlId="formGender">
                            <Form.Label className='text-normal'>Giới tính</Form.Label>
                            <div>
                                <Form.Check

                                    className='text-normal'
                                    inline
                                    type="radio"
                                    label="Nam"
                                    name="gender"
                                    value="MALE"
                                    checked={formData.gender === 'MALE'}
                                    onChange={handleChange}
                                />
                                <Form.Check
                                    className='text-normal'
                                    inline
                                    type="radio"
                                    label="Nữ"
                                    name="gender"
                                    value="FEMALE"
                                    checked={formData.gender === 'FEMALE'}
                                    onChange={handleChange}
                                />
                            </div>
                        </Form.Group>
                    </Row>

                    <Row className="mb-3">
                        <Form.Group as={Col} controlId="formDob">
                            <Form.Label className='text-normal'>Ngày sinh</Form.Label>
                            <Form.Control
                                className='no-shadow'
                                type="date"
                                name="dateOfBirth"
                                value={formData.dateOfBirth}
                                onChange={handleChange}
                            />
                        </Form.Group>
                    </Row>
                    {isBtn &&
                        <div className='d-flex gap-3'>
                            <button onClick={(e) => { handleSubmit(e); }} className='button-flex button-hover background-primary text-medium'>
                                Lưu
                            </button>
                        </div>}
                </Form>
                <div className='col-12 col-md-6 d-flex align-items-center justify-content-center flex-column gap-2'>
                    <div>
                        {url ? <img
                            src={url}
                            alt='User Avatar'
                            className='rounded-circle border'
                            width={100}
                            height={100}
                        /> : <img
                            src={user?.data?.avatar ?? 'https://via.placeholder.com/100'}
                            alt='User Avatar'
                            className='rounded-circle border'
                            width={100}
                            height={100}
                        />}
                    </div>
                    <label htmlFor='avatar' className='button-flex button-hover background-primary text-medium gap-2'
                        style={{ cursor: 'pointer' }}
                    >
                        <i className="bi bi-camera-fill"></i> <span>Chọn ảnh</span>
                        <input accept='image/*' id='avatar' type='file' className='hidden-button-file' onChange={handleChangeImage}></input>
                    </label>
                    <span style={{ textAlign: 'center' }} >Dung lượng tối đa 1MB <br></br> Định dạng jpg, png</span>
                </div>
            </div>
            {isLoading && <ModalLoading loading={isLoading} />}
        </div>
    );
};

export default Profile;
