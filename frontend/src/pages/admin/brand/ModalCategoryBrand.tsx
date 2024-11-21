import { faChevronRight } from "@fortawesome/free-solid-svg-icons";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { Col, Modal, Row } from "react-bootstrap";
import '../../../components/seller/modal/category-modal.scss';
import { forwardRef, useEffect, useImperativeHandle, useState } from "react";
import SimpleBar from 'simplebar-react';
import { Category } from "../../../models/category";
import { pageQueryHanlder } from "../../../utils/query-handler";
import { Category as CategoryModel } from "../../../models/category";
import { useLazyGetCategoriesQuery } from "../../../services/category.service";
import { useDispatch } from "react-redux";
import { setCategories } from "../../../rtk/slice/product-slice";

type CategoryModalProps = {
    show: boolean;
    handleClose: () => void;
    categories?: Category[];
    handleCategory: (category?: CategoryItems[]) => void;
}

export interface CategoryItems {
    id: string;
    name: string;
}

export interface DeleteCategoryItems {
    handleRemoveCategory: (id: string) => void;
    handleClear: () => void;
}

const ModalCategoryBrand = forwardRef<DeleteCategoryItems, CategoryModalProps>(
    ({ show, handleClose, categories, handleCategory }, ref) => {
        const [isConfirm, setIsConfirm] = useState<boolean>(true);
        const [categoryId, setCategoryId] = useState<string[]>([]);
        const [categoryId1, setCategoryId1] = useState<string[]>([]);
        const [categoryId2, setCategoryId2] = useState<string[]>([]);
        const [childCategory, setChildCategory] = useState<CategoryModel[]>([]);
        const [childCategory1, setChildCategory1] = useState<CategoryModel[]>([]);
        const [category, setCategory] = useState<CategoryItems[]>([]);
        const dispatch = useDispatch();
        const [getCategory] = useLazyGetCategoriesQuery();
        const paramsParent = pageQueryHanlder(1, 100, categoryId.length > 0
            ? [{ filed: 'parentId', operator: '=', value: categoryId[categoryId.length - 1] }]
            : []
        );
        const paramsParent1 = pageQueryHanlder(1, 100,
            categoryId1.length > 0
                ? [{ filed: 'parentId', operator: '=', value: categoryId1[categoryId1.length - 1] }]
                : []
        );
        const toggleCategory = (id: string, categoryName: string, setCategoryId: React.Dispatch<React.SetStateAction<string[]>>) => {
            // Cập nhật categoryId
            setCategoryId(prev => {
                const updatedIds = prev.includes(id)
                    ? prev.filter(categoryId => categoryId !== id)
                    : [...prev, id];
                setIsConfirm(updatedIds.length === 0);
                return updatedIds;
            });

            // Cập nhật category
            setCategory(prev => {
                const categoryItem: CategoryItems = { id, name: categoryName };
                return prev.find(item => item.id === id)
                    ? prev.filter(item => item.id !== id)
                    : [...prev, categoryItem];
            });
        };

        useEffect(() => {
            const fetchCategories = async () => {
                if (categoryId.length > 0) {
                    try {
                        const result = await getCategory(paramsParent).unwrap();
                        setChildCategory(result.data?.items || []);
                    } catch (error) {
                        console.error(error);
                    }
                    dispatch(setCategories([...categoryId]));
                }

                if (categoryId1.length > 0) {
                    try {
                        const result = await getCategory(paramsParent1).unwrap();
                        setChildCategory1(result.data?.items || []);
                    } catch (error) {
                        console.error(error);
                    }
                    dispatch(setCategories([...categoryId, ...categoryId1]));
                }

                if (categoryId2.length > 0) {
                    dispatch(setCategories([...categoryId, ...categoryId1, ...categoryId2]));
                }
            };

            fetchCategories();
        }, [paramsParent, paramsParent1, categoryId, categoryId1, categoryId2, dispatch]);

        const handleRemoveCategory = (id: string) => {
            setCategory(prev => prev.filter(item => item.id !== id));
            setCategoryId1(prev => prev.filter(categoryId => categoryId !== id));
            setCategoryId2(prev => prev.filter(categoryId => categoryId !== id));
            const updatedCategories = [
                ...categoryId.filter(categoryId => categoryId !== id),
                ...categoryId1.filter(categoryId => categoryId !== id),
                ...categoryId2.filter(categoryId => categoryId !== id),
            ];

            dispatch(setCategories(updatedCategories));
        };
        const handleClear = () => {
            setCategoryId([]);
            setCategoryId1([]);
            setCategoryId2([]);
            setCategory([]);
            setChildCategory([]);
            setChildCategory1([]);
        }
        useImperativeHandle(ref, () => ({
            handleRemoveCategory, handleClear
        }));

        return (
            <Modal show={show} onHide={handleClose} size="xl" centered>
                <Modal.Header closeButton>
                    <Modal.Title>
                        <span className="text-large">Chỉnh sửa loại sản phẩm</span>
                    </Modal.Title>
                </Modal.Header>
                <Modal.Body>
                    <div className="bg-main container-fluid p-3">
                        <Row className="bg-main p-2 custom-row">
                            <Col md={4} className="bg-white col-modal-category">
                                <SimpleBar style={{ maxHeight: 300, marginTop: 10 }}>
                                    {categories?.map((category) => (
                                        <div
                                            key={category.id}
                                            className={`btn-category-list p-1 pe-4 d-flex align-items-center justify-content-between ${categoryId.includes(category.id) ? 'selected' : ''}`}
                                            onClick={() => { toggleCategory(category.id, category.categoryName, setCategoryId) }}
                                        >
                                            <span className="ps-3">{category.categoryName}</span>
                                            <FontAwesomeIcon icon={faChevronRight} />
                                        </div>
                                    ))}
                                </SimpleBar>
                            </Col>
                            <Col md={4} className="bg-white col-modal-category">
                                <SimpleBar style={{ maxHeight: 300, marginTop: 10 }}>
                                    {childCategory.map((category) => (
                                        <div
                                            key={category.id}
                                            className={`btn-category-list p-1 pe-4 d-flex align-items-center justify-content-between ${categoryId1.includes(category.id) ? 'selected' : ''}`}
                                            onClick={() => toggleCategory(category.id, category.categoryName, setCategoryId1)}
                                        >
                                            <span className="ps-3">{category.categoryName}</span>
                                            <FontAwesomeIcon icon={faChevronRight} />
                                        </div>
                                    ))}
                                </SimpleBar>
                            </Col>
                            <Col md={4} className="bg-white bd-tr-radius-sm">
                                <SimpleBar style={{ maxHeight: 300, marginTop: 10 }}>
                                    {childCategory1.map((category) => (
                                        <div key={category.id}>
                                            <div
                                                className={`btn-category-list p-1 pe-4 d-flex align-items-center justify-content-between ${categoryId2.includes(category.id) ? 'selected' : ''}`}
                                                onClick={() => toggleCategory(category.id, category.categoryName, setCategoryId2)}
                                            >
                                                <span className="ps-3">{category.categoryName}</span>
                                            </div>
                                        </div>
                                    ))}
                                </SimpleBar>
                            </Col>
                        </Row>
                    </div>
                </Modal.Body>
                <Modal.Footer>
                    <button
                        className="btn-category-modal-active"
                        disabled={isConfirm}
                        onClick={() => { handleCategory(category); handleClose(); }}
                    >
                        Confirm
                    </button>
                </Modal.Footer>
            </Modal>
        );
    }
);

export default ModalCategoryBrand;

