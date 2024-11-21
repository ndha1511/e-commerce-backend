import './footer.scss'; // File SCSS tùy chỉnh của bạn

const Footer = () => {
  return (
    <div className="footer bg-light pt-5 pb-3">
      <div className="container">
        <div className="footer-content">
          <div className="footer-section">
            <h5 className="fw-bold text-medium">CHĂM SÓC KHÁCH HÀNG</h5>
            <ul className="list-unstyled">
              <li>Trung tâm trợ giúp</li>
              <li>Hướng dẫn mua hàng</li>
              <li>Hướng dẫn bán hàng</li>
              <li>Thanh toán</li>
              <li>Vận chuyển</li>
              <li>Trả hàng & hoàn tiền</li>
              <li>Chăm sóc khách hàng</li>
              <li>Chính sách bảo hành</li>
            </ul>
          </div>
          <div className="footer-section">
            <h5 className="fw-bold text-medium">VỀ DỖ ÁN</h5>
            <ul className="list-unstyled">
              <li>Giới thiệu về Dỗ Án Việt Nam</li>
              <li>Điều khoản Dỗ Án</li>
              <li>Chính sách bảo mật</li>
              <li>Chính hãng</li>
              <li>Kênh người bán</li>
              <li>Liên hệ với truyền thông</li>
            </ul>
          </div>
          <div className="footer-section">
            <h5 className="fw-bold text-medium">THANH TOÁN</h5>
            <div className="d-flex mb-3">
              <img src="visa.png" alt="Visa" className="me-2 img-ft" />
              <img src="mastercard.png" alt="Mastercard" className="me-2 img-ft" />
              <img src="amex.png" alt="American Express" className="me-2 img-ft" />
              <img src="installment.png" alt="Trả góp" className="me-2 img-ft" />
            </div>
            <h5 className="fw-bold">ĐƠN VỊ VẬN CHUYỂN</h5>
            <div className="d-flex">
              <img src="spx.png" alt="SPX" className="me-2 img-ft" />
              <img src="giaohangnhanh.png" alt="Giao Hàng Nhanh" className="me-2 img-ft" />
            </div>
          </div>
          <div className="footer-section">
            <h5 className="fw-bold text-medium">THEO CHÚNG TÔI TRÊN</h5>
            <ul className="list-unstyled">
              <li className="mb-2"><i className="bi bi-facebook me-2"></i> FaceBook</li>
              <li className="mb-2"><i className="bi bi-instagram me-2"></i> Instagram</li>
              <li className="mb-2"><i className="bi bi-linkedin me-2"></i> LinkedIn</li>
            </ul>
          </div>
        </div>
        <hr />
        <div className="footer-bottom text-center mt-4">
          <p className="">Địa chỉ: 251/33 Vườn Lài, An Phú Đông, Q12, tp.HCM</p>
          <p className="">Email: tcm172@gmail.com</p>
          <p>Điện thoại: 0961263780</p>
        </div>
      </div>
    </div>
  );
};

export default Footer;
