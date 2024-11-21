import { Dropdown, Pagination, Table } from "react-bootstrap";
import { useGetProductsQuery } from "../../../services/product.service";
import { convertPrice } from "../../../utils/convert-price";
import React, { LegacyRef, ReactNode, useState } from "react";
import useRedirect from "../../../hooks/useRedirect";
import './insert-product.scss'
import Select from 'react-select';

function Product() {
    const { data: pageResponse } = useGetProductsQuery();
    const redirect = useRedirect();
    const [selectedProduct, setSelectedProduct] = useState<{value: string, label: string}>({
        value: '',
        label: '',
    }); // Trạng thái tên sản phẩm đã chọn
    const products = pageResponse?.data.items;
    const totalItems = pageResponse?.data.pageSize || 0; // Tổng số sản phẩm
    const itemsPerPage = 10; // Số sản phẩm mỗi trang
    const totalPages = Math.ceil(totalItems / itemsPerPage); // Tổng số trang
    const [currentPage, setCurrentPage] = useState(1); // Trang hiện tại
    const handlePageChange = (page: number) => {
        setCurrentPage(page);
    };

    // Lấy các sản phẩm cho trang hiện tại
    const startIndex = (currentPage - 1) * itemsPerPage;
    // const currentProducts = products?.slice(startIndex, startIndex + itemsPerPage);
    const options = products?.map((product) => ({
        value: product.id,
        label: product.productName
    }));
    const filteredProducts = selectedProduct
        ? products?.filter(product => product.productName === selectedProduct.label)
        : products;

    const displayedProducts = filteredProducts?.slice(startIndex, startIndex + itemsPerPage);

    return (
        <div className=" bg-light p-3">
            <h5>Danh sách sản phẩm</h5>
            <div className="bg-white p-3 border-radius-small">
                <div className="mb-3  d-flex justify-content-between">
                    <div className="d-flex gap-3 w-75">
                        <div className="search-list-product p-2 ">
                            <input className="input-search-list-product" placeholder="Tìm theo tên sản phẩm" type="text" />
                            <i className="bi bi-search"></i>
                        </div>
                        <div className="">
                            <Select
                                id=""
                                options={options}
                                onChange={(e) => {
                                    const label = e?.label || "";
                                    const value = e?.value || "";
                                    setSelectedProduct({
                                        label, value
                                    })
                                }}
                                placeholder="Lọc theo tên sản phẩm"
                            />
                        </div>
                    </div>
                    <div className=" d-flex justify-content-start  gap-3 w-25 ">
                        <button onClick={() => redirect('/admin/product/insert')} className="btn-save-all-category ps-4 pe-4" style={{ display: 'inline-block' }} >Tạo sản phẩm</button>
                        <button onClick={() => redirect('/admin/product/import')} className="btn-save-all-category ps-4 pe-4" >Nhập hàng</button>
                    </div>
                </div>
                <Table className='table-bordered table-responsive  custom-table-product '>
                    <thead>
                        <tr>
                            <th>STT</th>
                            <th>Ảnh</th>
                            <th>Tên sản phẩm</th>
                            <th>Giá bán</th>
                            <th></th>
                        </tr>
                    </thead>
                    <tbody>
                        {displayedProducts?.map((product, index) => (
                            <tr key={product.id}>
                                <td>{index + 1}</td>
                                <td className="pt-1 pb-1"><img src={product.thumbnail} alt='anh' width={60} height={60} /></td>
                                <td >{product.productName.length > 70 ? product.productName.slice(0, 70) + '...' : product.productName}</td>
                                <td>{convertPrice(product.regularPrice)}</td>
                                <td>
                                    <Dropdown>
                                        <Dropdown.Toggle as={CustomToggle}>
                                            <i className="bi bi-three-dots"></i>
                                        </Dropdown.Toggle>

                                        <Dropdown.Menu>
                                            <Dropdown.Item href="#/action-1">Chỉnh sửa</Dropdown.Item>
                                            <Dropdown.Item onClick={() => redirect('attribute?id=' + product.id + '&name=' + product.productName)}>Phân loại</Dropdown.Item>
                                            <Dropdown.Item href="#/action-3">Xóa</Dropdown.Item>
                                        </Dropdown.Menu>
                                    </Dropdown>
                                </td>
                            </tr>
                        ))}

                    </tbody>
                </Table>
                <div className="d-flex justify-content-center align-items-center">
                    <Pagination className="mt-3">
                        <Pagination.First onClick={() => handlePageChange(1)} disabled={currentPage === 1} />
                        <Pagination.Prev onClick={() => handlePageChange(currentPage - 1)} disabled={currentPage === 1} />
                        {[...Array(totalPages)].map((_, index) => (
                            <Pagination.Item key={index + 1} active={index + 1 === currentPage} onClick={() => handlePageChange(index + 1)}>
                                {index + 1}
                            </Pagination.Item>
                        ))}
                        <Pagination.Next onClick={() => handlePageChange(currentPage + 1)} disabled={currentPage === totalPages} />
                        <Pagination.Last onClick={() => handlePageChange(totalPages)} disabled={currentPage === totalPages} />
                    </Pagination>
                </div>
            </div>
        </div>
    );
}
const CustomToggle = React.forwardRef(({ children, onClick }: { children: ReactNode, onClick: (e: React.MouseEvent<HTMLAnchorElement, MouseEvent>) => void }, ref: LegacyRef<HTMLAnchorElement>) => (
    <a
        href="/"
        ref={ref}
        onClick={(e) => {
            e.preventDefault();
            onClick(e);
        }}
        className="avatar-dropdown" // Thêm class cho avatar dropdown
    >
        {children}
    </a>
));
export default Product;