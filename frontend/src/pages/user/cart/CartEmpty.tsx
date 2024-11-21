import useRedirect from "../../../hooks/useRedirect";

function CartEmpty() {
    const redirect = useRedirect();
    return (
        <div className="d-flex justify-content-center flex-column align-items-center">
            <div className="d-flex  justify-content-center p-3  gap-2 text-large ">
                <i className="bi bi-cart"></i><span>Bạn chưa có sản phẩm nào</span>
            </div>
            <button className="border-color-primary p-2 background-primary" onClick={()=>redirect('/')}>Thêm sản phẩm ở đây</button>
        </div>
    );
}

export default CartEmpty;