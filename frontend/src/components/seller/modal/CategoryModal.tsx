import { faAngleRight, faChevronRight } from "@fortawesome/free-solid-svg-icons";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { Col, Modal, Row } from "react-bootstrap";
import './category-modal.scss'
import React, { useEffect, useState } from "react";
import SimpleBar from 'simplebar-react';
import { Category } from "../../../models/category";
import { pageQueryHanlder } from "../../../utils/query-handler";
import { Category as CategoryModel } from "../../../models/category";
import { useLazyGetCategoriesQuery } from "../../../services/category.service";
import { useDispatch, useSelector } from "react-redux";
import { setCategories } from "../../../rtk/slice/product-slice";

type CategoryModalProps = {
    show: boolean;
    handleClose: () => void;
    categories?: Category[];
    handleCategory: (category?: string) => void;
}

function CategoryModal({ show, handleClose, categories, handleCategory }: CategoryModalProps) {
    const [isConfirm, setIsConfirm] = useState<boolean>(true);
    const [categoryId, setCategoryId] = useState<string>('');
    const [categoryId1, setCategoryId1] = useState<string>('');
    const [categoryId2, setCategoryId2] = useState<string>('');
    const paramsParent: string = pageQueryHanlder(1, 100, [{ filed: 'parentId', operator: '=', value: categoryId }]);
    const paramsParent1: string = pageQueryHanlder(1, 100, [{ filed: 'parentId', operator: '=', value: categoryId1 }]);
    const [childCategory, setChildCategory] = React.useState<CategoryModel[]>([]);
    const [childCategory1, setChildCategory1] = React.useState<CategoryModel[]>([]);
    const [getCategory, { isLoading }] = useLazyGetCategoriesQuery();
    const [selectedCategory, setSelectedCategory] = useState<string | null>(null);
    const [selectedCategoryItems, setSelectedCategoryItems] = useState<string | null>(null);
    const [categoryItems, setCategoryItems] = useState<string | null>(null);
    const [category3, setCategory3] = useState<string | null>(null);
    const [category, setCategory] = useState<string | undefined>();
    const dispatch = useDispatch();
    const handleSelectCategoryParent = async (categoryName: string, id: string, children: number) => {
        if (children === 0) {
            setCategory(categoryName)
            setIsConfirm(false);
        } else if (children > 0) {
            setIsConfirm(true);
        }
        setCategoryId(id);
        setCategoryId1('');
        setCategoryId2('');
        setChildCategory1([])
        setSelectedCategory(categoryName);
        setSelectedCategoryItems(categoryName)
    };
    const handleSelectCategoryItem = (categoryName: string, id: string, children: number) => {
        if (children === 0) {
            setIsConfirm(false);
            setCategory(categoryName)
        } else if (children > 0) {
            setIsConfirm(true);
        }
        setCategoryId1(id)
        setCategoryId2('');
        setSelectedCategoryItems(categoryName)
        setCategoryItems(categoryName)

    }
    useEffect(() => {
        const fetchCategory = async () => {
            if (categoryId !== '') {
                try {
                    const result = await getCategory(paramsParent).unwrap();
                    setChildCategory(result.data?.items);
                } catch (error) {
                    console.log(error);
                }
                dispatch(setCategories([categoryId]))
            }
            if (categoryId1 !== '') {
                try {
                    const result = await getCategory(paramsParent1).unwrap();
                    setChildCategory1(result.data?.items);
                } catch (error) {
                    console.log(error);
                }
                dispatch(setCategories([categoryId, categoryId1]))
            }
            if (categoryId2 !== '') {
                dispatch(setCategories([categoryId, categoryId1, categoryId2]))
            }
        };
        fetchCategory();
    }, [paramsParent, paramsParent1, categoryId2])
    return (
        <Modal show={show} onHide={handleClose} size="xl" centered>
            <Modal.Header closeButton>
                <Modal.Title><span className="text-large">Chỉnh sửa loại sản phẩm</span></Modal.Title>
            </Modal.Header>
            <Modal.Body>
                <div className="bg-main container-fluid p-3">
                    <div className="pt-3">
                        <div className="search-category-modal ">
                            <input className="input-search-category-modal" placeholder="Nhập từ khóa tìm kiếm" type="text" />
                            <i className="bi bi-search"></i>
                        </div>
                    </div>
                    <Row className=" bg-main p-2 custom-row ">
                        <Col md={4} className=" bg-white col-modal-category">
                            <SimpleBar style={{ maxHeight: 300, marginTop: 10 }}>
                                {categories?.map((category) => (
                                    <div key={category.id} style={{ width: '100%' }}>
                                        <div
                                            className={`btn-category-list p-1   pe-4 d-flex align-items-center justify-content-between w-100 ${selectedCategory === category.categoryName ? 'selected' : ''}`}
                                            onClick={() => { handleSelectCategoryParent(category.categoryName, category.id, category.children); }}
                                            style={{ width: '100%' }}
                                        >
                                            <span className="ps-3">{category.categoryName}</span>
                                            {category.children > 0 &&
                                                <FontAwesomeIcon
                                                    style={{ color: selectedCategory === category.categoryName ? 'red' : 'rgb(136, 136, 136)' }}
                                                    icon={faChevronRight}
                                                />}
                                        </div>
                                    </div>
                                ))}
                            </SimpleBar>
                        </Col>
                        <Col md={4} className=" bg-white col-modal-category">
                            {selectedCategoryItems &&
                                <SimpleBar style={{ maxHeight: 300, marginTop: 10 }}>
                                    {childCategory?.map((category) => {
                                        return (
                                            <div key={category.id} style={{ width: '100%' }}>
                                                <div
                                                    className={`btn-category-list p-1   pe-4 d-flex align-items-center justify-content-between w-100 ${selectedCategoryItems === category.categoryName ? 'selected' : ''}`}
                                                    onClick={() => handleSelectCategoryItem(category.categoryName, category.id, category.children)}
                                                    style={{ width: '100%' }}
                                                >
                                                    <span className="ps-3">{category.categoryName}</span>
                                                    {category.children > 0 &&
                                                        <FontAwesomeIcon
                                                            style={{ color: selectedCategoryItems === category.categoryName ? 'red' : 'rgb(136, 136, 136)' }}
                                                            icon={faChevronRight}
                                                        />}
                                                </div>
                                            </div>
                                        )
                                    })}
                                </SimpleBar>}
                        </Col>
                        <Col md={4} className=" bg-white bd-tr-radius-sm">
                            {categoryItems &&
                                <SimpleBar style={{ maxHeight: 300, marginTop: 10 }}>
                                    {childCategory1?.map((category) => {
                                        return (
                                            <div key={category.id} style={{ width: '100%' }}>
                                                <div
                                                    className={`btn-category-list p-1   pe-4 d-flex align-items-center justify-content-between w-100 ${category3 === category.categoryName ? 'selected' : ''}`}
                                                    onClick={() => {
                                                        setCategoryId2(category.id);
                                                        category.children === 0 ? setIsConfirm(false) : setIsConfirm(true);
                                                        setCategory3(category.categoryName)
                                                        setCategory(category.categoryName)
                                                    
                                                    }}
                                                    style={{ width: '100%' }}
                                                >
                                                    <span className="ps-3">{category.categoryName}</span>
                                                </div>
                                            </div>
                                        )
                                    }

                                    )}
                                </SimpleBar>}
                        </Col>

                    </Row>
                </div>
            </Modal.Body>
            <Modal.Footer>
                <div className="w-100">
                    <div className="d-flex  justify-content-between align-items-center">
                        <div className="d-flex  justify-content-between align-items-center">
                            <span className="pe-2 text-medium text-muted">Đã chọn :</span>
                            <div className="d-flex   text-medium ">
                                <span>{selectedCategory ? selectedCategory : 'Chưa chọn loại sản phẩm'}</span>
                                <span >{categoryItems ? <span><FontAwesomeIcon icon={faAngleRight} className="ms-1 me-1" />{selectedCategoryItems} </span> : ''}</span>
                            </div>
                        </div>
                        <div className=" ">
                            <button className="btn-category-modal-cancel" onClick={handleClose}>
                                Cancel
                            </button>
                            <button className="btn-category-modal-active" disabled={isConfirm} onClick={() => { handleCategory(category); handleClose() }}>
                                Confirm
                            </button>
                        </div>
                    </div>

                </div>
            </Modal.Footer>
        </Modal>
    );
}

export default CategoryModal;