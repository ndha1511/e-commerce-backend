
import { faAngleDown, faAngleUp, faCirclePlus, faPenToSquare, faTrash } from "@fortawesome/free-solid-svg-icons";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import React from "react";
import { Collapse, Table } from "react-bootstrap";
import CreateCategoryModal from "./CreateCategoryModal";
import useGetParam from "../../../hooks/useGetParam";
import { useGetCategoriesQuery, useLazyGetCategoriesQuery } from "../../../services/category.service";
import { pageQueryHanlder } from "../../../utils/query-handler";
import ClipLoader from "react-spinners/ClipLoader";
import { Category as CategoryModel } from "../../../models/category";
import SimpleBar from "simplebar-react";

interface ItemProps {
    category: CategoryModel;
}

const CategoryItem = ({ category }: ItemProps) => {
    const [open, setOpen] = React.useState<boolean>(false);

    const [getCategory, { isLoading }] = useLazyGetCategoriesQuery();
    const [showModal, setShowModal] = React.useState(false);
    const [childCategory, setChildCategory] = React.useState<CategoryModel[]>([]);
    const paramsChild: string = pageQueryHanlder(1, 100, [{ filed: 'parentId', operator: '=', value: category.id }]);

    const openCollapse = async () => {
        setOpen(!open);
        if (!open) { // Only fetch when opening
            try {
                const result = await getCategory(paramsChild).unwrap();
                setChildCategory(result.data?.items);
            } catch (error) {
                console.log(error);
            }
        }
    };

    const handleClose = () => {
        setShowModal(false);
    };

    const refetch = async () => {
        await getCategory(paramsChild).refetch();
    };
    return (
        <React.Fragment key={category.id}>

            <tr>
                <td>
                    {category.image && <img src={category.image} alt="category" width={50} height={50} />}
                </td>
                <td><h6>{category.categoryName}</h6></td>
                <td><h6>{category.children}</h6></td>
                <td>
                    <span className="text-large cursor-pointer text-success">
                        {category.inActive ? <i className="bi bi-toggle-off"></i> : <i className="bi bi-toggle-on"></i>}
                    </span>
                </td>
                <td>
                    <div className="d-flex align-items-center gap-3 justify-content-center">
                        <span className="text-medium cursor-pointer secondary" data-toggle="tooltip" title="chỉnh sửa">
                            <FontAwesomeIcon icon={faPenToSquare} />
                        </span>
                        <span className="text-medium cursor-pointer text-success" data-toggle="tooltip" title="thêm danh mục con" onClick={() => setShowModal(true)}>
                            <FontAwesomeIcon icon={faCirclePlus} />
                        </span>
                        <span className="text-medium cursor-pointer primary" data-toggle="tooltip" title="xóa">
                            <FontAwesomeIcon icon={faTrash} />
                        </span>
                    </div>
                </td>
                <td>
                    {category.children > 0 && (
                        <span onClick={openCollapse} className="text-primary text-large cursor-pointer">
                            {open ? <FontAwesomeIcon icon={faAngleUp} /> : <FontAwesomeIcon icon={faAngleDown} />}
                        </span>
                    )}
                </td>
            </tr>
            <tr>
                <td colSpan={6} className="p-0">
                    <Collapse in={open}>
                        <div className=" p-3">
                            <Table className="mb-0 table-bordered table-responsive custom-table-category-cs ">
                                <thead>
                                    <tr className='text-center'>
                                        <th>Hình ảnh</th>
                                        <th>Danh mục</th>
                                        <th>Danh mục con</th>
                                        <th>Trạng thái</th>
                                        <th>Các chức năng</th>
                                        <th></th>
                                    </tr>
                                </thead>
                                <tbody>
                                    {childCategory.map((item) => (
                                        <CategoryItem key={item.id} category={item} />
                                    ))}
                                </tbody>
                            </Table>
                        </div>
                    </Collapse>
                </td>
            </tr>
            {showModal && <CreateCategoryModal refetch={refetch} show={showModal} handleClose={handleClose} parentId={category.id} />}
        </React.Fragment>
    );
};

const Category = () => {
    const pageParams = useGetParam('page');
    const [show, setShow] = React.useState(false);
    const [page, setPage] = React.useState<number>(pageParams ? Number(pageParams) : 1);

    const { data, isFetching, refetch } = useGetCategoriesQuery(pageQueryHanlder(page, 20, [{ filed: 'parentId', operator: ':', value: 'null' }]));

    const handleClose = () => {
        setShow(false);
    };

    return (
        <div className="d-flex flex-column  bg-light">
            <div className="p-3 border">
                <SimpleBar style={{ height: 600 }}>
                    <div className="d-flex align-items-center justify-content-between loading-container">
                        <h5>Danh sách danh mục</h5>
                    </div>
                    <div className="d-flex mt-2 mb-3  justify-content-between">
                        <div className="search-category-admin">
                            <input className="input-search-category-admin" placeholder="Nhập từ khóa tìm kiếm" type="text" />
                            <i className="bi bi-search"></i>
                        </div>
                        <button onClick={() => setShow(true)} className="button-hover button-flex background-primary">
                            <i className="bi bi-plus-lg"></i> Thêm danh mục
                        </button>
                    </div>
                    <div className="d-flex flex-column gap-3  flex-1">
                        <Table className='table-bordered table-responsive custom-table-category'>
                            <thead>
                                <tr className='text-center'>
                                    <th>Hình ảnh</th>
                                    <th>Danh mục</th>
                                    <th>Danh mục con</th>
                                    <th>Trạng thái</th>
                                    <th>Các chức năng</th>
                                    <th></th>
                                </tr>
                            </thead>
                            <tbody>
                                {data?.data.items.map((item) => (
                                    <CategoryItem key={item.id} category={item} />
                                ))}
                            </tbody>
                        </Table>
                        {isFetching && <div className="loading-content"><ClipLoader color={'red'} loading={isFetching} /></div>}
                    </div>
                </SimpleBar>
            </div>
            {show && <CreateCategoryModal refetch={refetch} show={show} handleClose={handleClose} />}
        </div>
    );
};

export default Category;





