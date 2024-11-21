import { useState } from "react";
import ResetPassword from "../../auth/reset-password/ResetPassword";
import FormEmail from "./FormEmail";
import FormOtp from "./FormOtp";
import NewPassword from "./NewPassword";
import { motion } from "framer-motion";

const Password = () => {
    const [currentStep, setCurrentStep] = useState(0);
    const components = [
        <FormEmail key="form-email" onConfirm={() => setCurrentStep(1)} />,
        <FormOtp key="form-otp" onConfirm={() => setCurrentStep(2)}  />,
        <NewPassword key="form-new-password"  />,
    ];
    return <div className="profile-container d-flex flex-column align-items-center justify-content-center" style={{ position: 'relative' }}>
        <div style={{ position: 'absolute', top: 20, left: 20 }}>
            <span className="text-large">Đổi mật khẩu</span>
        </div>
        <div className=" d-flex justify-content-center -align-items-center" >
            <motion.div
                key={currentStep}
                initial={{ opacity: 0, x: -100 }} // Bắt đầu từ bên trái
                animate={{ opacity: 1, x: 0 }} // Vào vị trí chính
                exit={{ opacity: 0, x: 100 }} // Rời khỏi bên phải
                transition={{ duration: 0.5 }} // Thời gian chuyển đổi
            >
                {components[currentStep]} {/* Hiển thị thành phần hiện tại */}
            </motion.div>
        </div>
    </div>
}

export default Password;