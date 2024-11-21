import { useState } from "react";
import { motion, AnimatePresence } from "framer-motion";
import { Col, Row } from "react-bootstrap";

export default function App() {
  const [x, setX] = useState(0);
  const [y, setY] = useState(0);
  const [rotate, setRotate] = useState(0);
  const [isVisible, setIsVisible] = useState(false);

  const handleAdd = () => {
    setIsVisible(true);
    const cartElement = document.getElementById("cart-motion-id"); // Lấy phần tử giỏ hàng bằng ID
    if (cartElement) {
      const iconRect = cartElement.getBoundingClientRect();
      setX(iconRect.left + window.scrollX - 1225); // Căn giữa biểu tượng
      setY(iconRect.top + window.scrollY - 140); // Điều chỉnh vị trí cho phù hợp
      setRotate(10);
    }
  };

  return (
    <div className="example d-flex justify-content-between" style={{ border: '1px solid black', position: 'relative' }}>
      <div style={{ position: "relative", border: '1px solid black', marginLeft: 1200 }}>
        <AnimatePresence>
          <div>abccabcbabasf</div>
          <div>abccabcbabasf</div>
          <div>abccabcbabasf</div>
          <div>abccabcbabasf</div>
          <div>abccabcbabasf</div>
          <div>abccabcbabasf</div>
          <div>abccabcbabasf</div>    <div>abccabcbabasf</div>
          <div>abccabcbabasf</div>
          <div>abccabcbabasf</div>
          <div>abccabcbabasf</div>
          <div>abccabcbabasf</div>
          <div>abccabcbabasf</div>
          <div>abccabcbabasf</div>
          <div>abccabcbabasf</div>
          <div>abccabcbabasf</div>
          <div>abccabcbabasf</div>
          <div>abccabcbabasf</div>
          <div>abccabcbabasf</div>
          <div>abccabcbabasf</div>    <div>abccabcbabasf</div>
          <div>abccabcbabasf</div>
          <div>abccabcbabasf</div>
          <div>abccabcbabasf</div>
          <div>abccabcbabasf</div>
          <div>abccabcbabasf</div>
          <div>abccabcbabasf</div>
          <div>abccabcbabasf</div>
          <div>abccabcbabasf</div>    <div>abccabcbabasf</div>
          <div>abccabcbabasf</div>
          <div>abccabcbabasf</div>
          <div>abccabcbabasf</div>    <div>abccabcbabasf</div>
          <div>abccabcbabasf</div>
          <div>abccabcbabasf</div>
          <div>abccabcbabasf</div>
          <div>abccabcbabasf</div>
          <div>abccabcbabasf</div>
          <div>abccabcbabasf</div>
          <div>abccabcbabasf</div>
          <div>abccabcbabasf</div>

          {isVisible && (
            <motion.div
              className="box border"
              initial={{ opacity: 1, y: 0 }}
              animate={{
                x,
                y,
                rotate,
                scale: [1, 0.5],
                opacity: [1, 0],
              }}
              exit={{ opacity: 0 }}
              transition={{ type: "spring", stiffness: 100, damping: 40, duration: 1.5 }}
              style={{
                opacity: "50%",
                position: "absolute",
                top: 0,
                left: 0,
                zIndex: 9999,
              }}
              onAnimationComplete={() => setIsVisible(false)}
            >
              <img
                src="https://dongphuchaianh.vn/wp-content/uploads/2022/02/ao-polo-nam-quan-7-routine.jpg"
                width={50}
                height={50}
              />
              <Row className="mt-3 border ">
                <Col>
                  <div className="d-flex  ">
                    <img
                      src=''
                      alt="variant"
                      className="img-fluid img-ft"
                    />
                    <div className="ps-1">
                      <span>1</span> <br />
                      <span>2</span>
                    </div>
                  </div>
                </Col>
                <Col>
                  <div className="ps-1">
                    <span>Kho hàng: </span> <br />

                  </div>
                </Col>
              </Row>
            </motion.div>
          )}
        </AnimatePresence>

        {/* Hình ảnh tĩnh hiển thị ban đầu */}
        <img
          src="https://dongphuchaianh.vn/wp-content/uploads/2022/02/ao-polo-nam-quan-7-routine.jpg"
          width={50}
          height={50}
          style={{
            position: 'absolute',
            top: 0,
            left: 0,
            zIndex: 9998, // Đảm bảo hình ảnh tĩnh hiển thị dưới hoạt ảnh
            opacity: isVisible ? 0 : 1, // Ẩn hình ảnh tĩnh khi hoạt ảnh xuất hiện
            transition: 'opacity 0.5s', // Hiệu ứng chuyển tiếp
          }}
        />
      </div>
      <button onClick={handleAdd}>Thêm</button>
      <div
        id="motionCart"
        className="border"
        style={{
          padding: '10px',
          height: 60,
          display: 'inline-block',
          position: 'fixed', // Giữ vị trí cố định
          right: 200,
          zIndex: 1000, // Đảm bảo giỏ hàng hiển thị phía trên
        }}
      >
        <i className="bi bi-cart" style={{ fontSize: 24, color: 'black' }}></i>
      </div>
    </div >
  );
}




// import React, { useState } from 'react';
// import { motion, AnimatePresence } from 'framer-motion';

// const MyComponent = () => {
//     const [isVisible, setIsVisible] = useState(false);

//     return (
//         <div>
//             <button onClick={() => setIsVisible(!isVisible)}>
//                 {isVisible ? 'Ẩn' : 'Hiện'}
//             </button>
//             <AnimatePresence>
//                 {isVisible && (
//                     <motion.div
//                         initial={{ opacity: 0, y: -20 }} // trạng thái ban đầu
//                         animate={{ opacity: 1, y: 0 }} // trạng thái khi xuất hiện
//                         exit={{ opacity: 0, y: 20 }} // trạng thái khi biến mất
//                         transition={{ duration: 0.3 }} // thời gian chuyển tiếp
//                     >
//                         <h1>Chào mừng bạn đến với Framer Motion!</h1>
//                     </motion.div>
//                 )}
//             </AnimatePresence>
//         </div>
//     );
// };

// export default MyComponent;
