import { FaCheckCircle, FaRegCircle } from "react-icons/fa";

function MenuProductInsert() {
    const items = [
        { text: 'Thêm ít nhất 3 hình ảnh', completed: true },
        { text: 'Thêm video sản phẩm', completed: true },
        { text: 'Tên sản phẩm có ít nhất 25~100 kí tự', completed: true },
        { text: 'Thêm ít nhất 100 kí tự hoặc 1 hình ảnh trong mô tả sản phẩm', completed: true },
        { text: 'Thêm thương hiệu', completed: false },
        { text: 'Thêm bảng quy đổi kích cỡ', completed: false },
        { text: 'Sử dụng phân loại màu sắc hoặc kích thước chuẩn hóa', completed: true },
    ];

    return (
        <div className="card shadow border-0" style={{ maxWidth: '400px' }}>
            <div className="card-header bg-primary text-white">
                <span className="text-medium text-white ">Gợi ý điền Thông tin</span>
            </div>
            <div className="d-flex flex-column gap-3 p-3 mt-3">
                {items.map((item, index) => (
                    <div key={index} className="d-flex">
                        <div>
                            {
                                item.completed ? (
                                    <FaCheckCircle className="primary me-2" />
                                ) : (
                                    <FaRegCircle className="text-secondary me-2" />
                                )
                            }
                        </div>
                        < span
                            key={index}
                            className=""
                        >
                            {item.text}
                        </span>
                    </div>

                ))}
            </div>
        </div >
    );
}
export default MenuProductInsert;