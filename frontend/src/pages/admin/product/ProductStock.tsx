import { Collapse, Pagination, Table } from "react-bootstrap";
import './insert-product.scss'
import { useGetAttributeByIdQuery, useGetProductsQuery } from "../../../services/product.service";
import React, { useState } from "react";
import { Product } from "../../../models/product";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faAngleDown, faAngleUp } from "@fortawesome/free-solid-svg-icons";
import { useGetVariantsByProductIdQuery } from "../../../services/variant.service";
import SimpleBar from "simplebar-react";
import Select from 'react-select';
function ProductStock() {
    const { data } = useGetProductsQuery();
    const products = data?.data.items;
    const totalItems = data?.data.pageSize || 0; // Tổng số sản phẩm
    const itemsPerPage = 10; // Số sản phẩm mỗi trang
    const totalPages = Math.ceil(totalItems / itemsPerPage); // Tổng số trang
    const [currentPage, setCurrentPage] = useState(1); // Trang hiện tại
    const handlePageChange = (page: number) => {
        setCurrentPage(page);
        // Gọi lại API với trang mới nếu cần
        // useGetProductsQuery({ page, limit: itemsPerPage });
    };

    // Lấy các sản phẩm cho trang hiện tại
    const startIndex = (currentPage - 1) * itemsPerPage;
    const currentProducts = products?.slice(startIndex, startIndex + itemsPerPage);
    return (
        <div className=" bg-light p-3 pe-3">
            <div className=" d-flex justify-content-between">
                <div className="d-flex gap-1"><h5>Kho Sản Phẩm </h5>
                </div>
                <button className="btn-stock">Lịch sử tồn kho</button>
            </div>
            <div className="bg-white p-2 border-radius-small mt-3">
                <div className=" mt-1 mb-3 d-flex justify-content-between">
                    <div className="search-list-product p-2 ">
                        <input className="input-search-list-product" placeholder="Nhập từ khóa tìm kiếm" type="text" />
                        <i className="bi bi-search"></i>
                    </div>
                    <div className="">
                        <Select
                            id=""
                            placeholder="Lọc theo số lượng bán"
                        />
                    </div>
                </div>
                <SimpleBar style={{ height: 450 }}>
                    <Table className='table-bordered table-responsive  custom-table-product-stock '>
                        <thead>
                            <tr>
                                <th></th>
                                <th>ẢNH ĐẠI DIỆN</th>
                                <th>TÊN SẢN PHẨM</th>
                                <th>SỐ LƯỢNG HÀNG TỒN</th>
                                <th>ĐÃ BÁN</th>
                                <th>TRẠNG THÁI</th>
                            </tr>
                        </thead>
                        <tbody>
                            {currentProducts?.map((product) => (
                                <ProductStockItems key={product.id} product={product} />
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
                </SimpleBar>
            </div>
        </div>
    );
}

export default ProductStock;

interface ProductStockItems {
    product: Product;
}
function ProductStockItems({ product }: ProductStockItems) {
    const [open, setOpen] = React.useState<boolean>(false);
    const { data } = useGetVariantsByProductIdQuery(product.id || '');
    const { data: attributes } = useGetAttributeByIdQuery(product.id || '');
    console.log(attributes)
    const openCollapse = async () => {
        setOpen(!open);

    };
    const sizeOrder: Record<string, number> = {
        S: 1,
        M: 2,
        L: 3,
        XL: 4,
        XXL: 5,
        XXXL: 6,
    };

    // Hàm chuyển đổi đơn vị GB và TB
    const convertToGB = (value: string): number => {
        if (!value) return 0;
        const numericValue = parseFloat(value); // Lấy phần số
        if (value.includes('TB')) {
            return numericValue * 1024; // 1TB = 1024GB
        }
        return numericValue; // Nếu là GB
    };

    // Hàm sắp xếp cho attributeValue2
    const sortAttributeValue2 = (val: string): number => {
        if (/^\d+GB$/.test(val)) {
            return convertToGB(val);
        } else if (/^\d+TB$/.test(val)) {
            return convertToGB(val);
        } else if (sizeOrder[val] !== undefined) {
            return sizeOrder[val]; // Sử dụng thứ tự kích thước
        } else {
            return Infinity; // Đưa các giá trị không xác định vào cuối
        }
    };
    return (
        <React.Fragment >
            <tr key={product.id}>
                <td>
                    <span onClick={openCollapse} className="text-orange text-large cursor-pointer">
                        {open ? <FontAwesomeIcon icon={faAngleUp} /> : <FontAwesomeIcon icon={faAngleDown} />}
                    </span>
                </td>
                <td className="pt-1 pb-1"><img src={product.thumbnail} alt='anh' width={60} height={60} /></td>
                <td >{product.productName.length > 70 ? product.productName.slice(0, 70) + '...' : product.productName}</td>
                <td>{product.totalQuantity}</td>
                <td>{product.buyQuantity}</td>
                <td>
                    <div className="inActiveProduct">
                        {product.InActive ? 'Ngưng hoạt động' : 'Đang bán'}
                    </div></td>
            </tr>
            <tr>
                <td colSpan={6} className="">
                    <SimpleBar style={{ height: open ? 400 : 0, }}>
                        <Collapse in={open}>
                            <div className=" p-3">

                                <Table className="mb-0 table-bordered table-responsive  custom-table-product-stock-cs ">
                                    <thead className="sticky-header">
                                        <tr className='text-center'>
                                            <th>Tên sản phẩm</th>
                                            <th>{attributes?.data.attributes?.[0]?.attributeName}</th>
                                            <th>{attributes?.data.attributes?.[1]?.attributeName}</th>
                                            <th>SỐ LƯỢNG HÀNG TỒN</th>
                                            <th>ĐÃ BÁN</th>
                                        </tr>
                                    </thead>

                                    <tbody>

                                        {data?.data
                                            .slice() // Tạo bản sao của mảng
                                            .sort((a, b) => {
                                                // Sắp xếp theo attributeValue1
                                                const compareValue1 = a.attributeValue1.localeCompare(b.attributeValue1);
                                                if (compareValue1 !== 0) return compareValue1; // Nếu khác nhau, sắp xếp theo attributeValue1

                                                // Sắp xếp theo attributeValue2
                                                const valA = sortAttributeValue2(a.attributeValue2 || '');
                                                const valB = sortAttributeValue2(b.attributeValue2 || '');
                                                return valA - valB; // Sắp xếp theo giá trị đã được xác định
                                            }).map((item) => (
                                                <tr key={item.id}>
                                                    <td>{item.product.productName}</td>
                                                    <td>
                                                        <div className=" text-start d-inline-block p-2  " >
                                                            <div className="d-flex align-items-center  justify-content-center gap-1">
                                                                {item.image && <img src={item.image} alt={item.image} width={50} height={50} />}
                                                                <div className={`${item.image ? "color-stock " : ''}`}><span className="truncate-text">{item.attributeValue1}</span></div>
                                                            </div>
                                                        </div>
                                                    </td>
                                                    <td>{item.attributeValue2}</td>

                                                    <td>{item.quantity}</td>
                                                    <td>{item.buyQuantity}</td>

                                                </tr>
                                            ))}
                                    </tbody>

                                </Table>

                            </div>
                        </Collapse>
                    </SimpleBar>
                </td>
            </tr>
        </React.Fragment>
    );
}
