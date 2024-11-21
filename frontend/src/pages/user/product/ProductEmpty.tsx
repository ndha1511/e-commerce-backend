function ProductEmpty() {
    return (
        <div className="d-flex justify-content-center">
            <div className="d-flex  justify-content-center p-3  gap-2 text-large custom-background-pr-ep">
                <i className="bi bi-exclamation-circle"></i><span>Đang hết hàng</span>
            </div>
        </div>
    );
}

export default ProductEmpty;