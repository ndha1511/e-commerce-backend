

import { useParams } from "react-router-dom";
import Select from 'react-select'
import MenuCategory from "./MenuCategory";
import { useGetCategoryByUrlQuery } from "../../../services/category.service";
import { Col, Pagination, Row } from "react-bootstrap";
import { BsChevronRight } from "react-icons/bs";
import CarouseCustom from "../../../components/carousel/CarouseCustom";
import { useGetProductByCategoryQuery } from "../../../services/product.service";
import './category.scss'
import { useState } from "react";
import CategoryEmpty from "./CategoryEmpty";
import SkeletonWrapper from "../../../components/query-wrapper/SkeletonWrapper";
import ListProduct from "../../../components/products/ListProduct";
import QueryWrapper from "../../../components/query-wrapper/QueryWrapper";

function Category() {
    const { categoryPath } = useParams();
    const { data: parentCategory, isSuccess: getCategoriesSuccess, isError: getCategoriesError, error: categoriesError } = useGetCategoryByUrlQuery(categoryPath || '');
    const [activeButton, setActiveButton] = useState<string | null>(null);

    const images = [
        {
            src: ["https://salt.tikicdn.com/ts/tka/a9/ec/4f/e95b916999b2dd40b3a8e2af30e704e8.png", "https://salt.tikicdn.com/ts/tka/99/ce/6a/9c0a7990ddba5207da7cc37b85bdc2f0.png"],
            alt: "First slide",
            caption: "Here is the description for the first slide",
            title: "First Slide",
        },
        {
            src: ["https://salt.tikicdn.com/ts/tka/45/7b/70/fb7c0e1414d55ae6ea43af2883f2d842.png", "https://salt.tikicdn.com/ts/tka/7f/0b/d3/95916a0bd08a84d64206ce6ef9e72010.png"],
            alt: "Second slide",
            caption: "Here is the description for the second slide",
            title: "Second Slide",
        },
        {
            src: ["https://salt.tikicdn.com/ts/tka/a9/ec/4f/e95b916999b2dd40b3a8e2af30e704e8.png", "https://salt.tikicdn.com/ts/tka/99/ce/6a/9c0a7990ddba5207da7cc37b85bdc2f0.png"],
            alt: "Third slide",
            caption: "Here is the description for the third slide",
            title: "Third Slide",
        },
    ]
    const options = [
        { value: 'low', label: 'Thấp' },
        { value: 'medium', label: 'Trung Bình' },
        { value: 'high', label: 'Cao' },
    ];
    const optionRating = [
        { value: '1', label: '⭐' },
        { value: '2', label: '⭐⭐' },
        { value: '3', label: '⭐⭐⭐' },
        { value: '4', label: '⭐⭐⭐⭐' },
        { value: '5', label: '⭐⭐⭐⭐⭐' },
    ];
    const handleSubmit = (buttonName: string) => {
        setActiveButton(buttonName);
    };
    const { data, isSuccess: getProductsSuccess, isError: getProductsError } = useGetProductByCategoryQuery(categoryPath || '');
    const totalItems = data?.data.pageSize || 0; // Tổng số sản phẩm
    const itemsPerPage = 10; // Số sản phẩm mỗi trang
    const totalPages = Math.ceil(totalItems / itemsPerPage); // Tổng số trang
    const [currentPage, setCurrentPage] = useState(1); // Trang hiện tại
    const handlePageChange = (page: number) => {
        setCurrentPage(page);
    };

    // Lấy các sản phẩm cho trang hiện tại
    const startIndex = (currentPage - 1) * itemsPerPage;
    const currentProducts = data?.data.items?.slice(startIndex, startIndex + itemsPerPage);
    return (
       <QueryWrapper queriesError={[getCategoriesError, getProductsError]} error={categoriesError}>
         <div className="p-3 bg-light">
            <SkeletonWrapper queriesStatus={[getCategoriesSuccess]} skWidth={200}>
                <div>
                    <span>Trang chủ <BsChevronRight /> </span>
                    <span>{parentCategory?.data.categoryName}</span>
                </div>
            </SkeletonWrapper>
            <Row className=" custom-row p-2 ">
                <Col md={2} className=" p-2">
                    <SkeletonWrapper queriesStatus={[getCategoriesSuccess]} skHeight={300}>
                        <div className="bg-white border-radius-small">
                            <div className="text-medium p-3 ">Khám phá theo danh mục</div>
                            {parentCategory?.data.children.map((item) => (
                                <MenuCategory key={item.id} item={item} />
                            ))}
                        </div>
                    </SkeletonWrapper>
                </Col>
                <Col md={10} className="p-2">
                    <div className="mb-2">
                        <SkeletonWrapper queriesStatus={[getProductsSuccess]} skHeight={200}>
                            <div className="p-3 bg-white border-radius-small d-flex align-items-center ">
                                <span className="text-large">{parentCategory?.data.categoryName}</span>
                            </div>
                            <div className='mt-1'>
                                <CarouseCustom images={images} height={200} />
                            </div>
                        </SkeletonWrapper>
                    </div>
                    <SkeletonWrapper queriesStatus={[getProductsSuccess]} skHeight={50}>
                        <div className='mt-3 option-filter-user p-3  d-flex gap-3 align-items-center'>
                            <div className="text-muted">Sắp xếp theo</div>
                            <button className={`${activeButton === 'popular' ? 'btn-filter-cate-user-active' : 'btn-filter-cate-user'}`} onClick={() => handleSubmit('popular')}>Phổ biến</button>
                            <button className={`${activeButton === 'latest' ? 'btn-filter-cate-user-active' : 'btn-filter-cate-user'}`} onClick={() => handleSubmit('latest')}>Mới nhất</button>
                            <button className={`${activeButton === 'best-seller' ? 'btn-filter-cate-user-active' : 'btn-filter-cate-user'}`} onClick={() => handleSubmit('best-seller')}>Bán chạy</button>
                            <div style={{ minWidth: 200 }}><Select options={options} placeholder="Giá" /></div>
                            <div style={{ minWidth: 200 }}><Select options={optionRating} placeholder="Đánh giá" /></div>
                        </div>
                    </SkeletonWrapper>
                    <div className='mt-2  pt-1 pb-3'>
                        <ListProduct products={currentProducts} />
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
                </Col>
            </Row>
        </div>
       </QueryWrapper>
    );
}

export default Category;