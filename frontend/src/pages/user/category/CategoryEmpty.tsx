import { BsExclamationCircle } from "react-icons/bs";

function CategoryEmpty() {
    return (
        <div className="bg-white p-3  ">
            <div className=" p-3 d-flex align-items-center gap-2 text-meidum err-cate ">
                <BsExclamationCircle />
                <span>Rất tiếc, không tìm thấy sản phẩm phù hợp với lựa chọn của bạn</span>
            </div>
        </div>
    );
}

export default CategoryEmpty;