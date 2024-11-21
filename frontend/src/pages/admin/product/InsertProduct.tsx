import { Col, OverlayTrigger, Row } from "react-bootstrap";
import './insert-product.scss'
import { useEffect, useState } from "react";
import ImgAndVideo from "../../../components/seller/insert-product/ImgAndVideo";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faPen } from "@fortawesome/free-solid-svg-icons";
import CategoryModal from "../../../components/seller/modal/CategoryModal";
import { useGetCategoriesQuery } from "../../../services/category.service";
import { useDispatch, useSelector } from "react-redux";
import { RootState } from "../../../rtk/store/store";
import { removeAll, setDescription, setProductName, setRegularPrice } from "../../../rtk/slice/product-slice";
import { pageQueryHanlder } from "../../../utils/query-handler";
import { useCreateProductMutation } from "../../../services/product.service";
import { setNotify } from "../../../rtk/slice/notify-slice";
import ModalLoading from "../../../components/loading/ModalLoading";
import CustomTooltip from "../../../components/tooltip/CustomTooltipProps";
import useRedirect from "../../../hooks/useRedirect";


function InsertProduct() {
    const [showCategoryModal, setShowCategoryModal] = useState(false);
    const [addProductClick, setAddProductClick] = useState(false);
    const [video, setVideo] = useState<File>();
    const [category, setCategory] = useState<string | null>('');
    const products = useSelector((state: RootState) => state.product)
    const params: string = pageQueryHanlder(1, 40, [{ filed: 'parentId', operator: '=', value: 'null' }]);
    const [trigger, { isLoading }] = useCreateProductMutation();
    const product = useSelector((state: RootState) => state.product);
    const [productName, setName] = useState<string>('');
    const [weight, setWeight] = useState<number | null>(null);
    const [regularPrice, setRigularPrice] = useState<number>(0);
    const dispatch = useDispatch();
    const redirect =   useRedirect()

    const handleClickAdd = () => {
        setAddProductClick(true);
    }

    const handleCategory = (categoryModal?: string) => {
        if (categoryModal) {
            setCategory(categoryModal);
        }
    }
    const handleCloseCategoryModal = () => {
        setShowCategoryModal(false);
    }
    const handleOpenCategoryModal = () => {
        setShowCategoryModal(true);
    }
    const fetchBlobAndCreateFile = async (url: string[]): Promise<File[]> => {
        try {
            const fileArr: File[] = [];
            for (let i = 0; i < url.length; i++) {
                const response = await fetch(url[i]);
                const blob = await response.blob();
                const newFile = new File([blob], 'image.png', { type: blob.type });
                fileArr.push(newFile);
                // URL.revokeObjectURL(url[i]);
            }
            return fileArr;
        } catch (error) {
            console.log(error);
            return [];
        }
    };
    const handleChange = (value: number) => {
        setRigularPrice(value);
        dispatch(setRegularPrice(value))
    }
    const handleAddProduct = async () => {
        if(product.images.length === 0 || productName === '' || category === '' || regularPrice === null || weight === null) {
            setAddProductClick(true);
            return;
        }
        const file: File[] = await fetchBlobAndCreateFile(product.images);
        const formData = new FormData();
        
        if (file) {
            for (let i = 0; i < file.length; i++) {
                formData.append('images', file[i]);
            }
        }
        if (product.video) {
            if(video){
                formData.append('video', video);
            }
        }
        
        try {
            formData.append('productName', product.productName);
            formData.append('brandId', product.brandId);
            product.categories.forEach(category => {
                formData.append('categories', category);
            });
            formData.append('regularPrice', product.regularPrice.toString());
            formData.append('weight', weight.toString());
            formData.append('description', product.description);
            const res =  await trigger(formData).unwrap();
            dispatch(setNotify({
                type: 'success', message: 'Thao tác thành công'
            }))
            setAddProductClick(false);
            setCategory('')
            setWeight(null);
            setDescription('');
            dispatch(removeAll())
            redirect('/admin/products/attribute?id=' + res.data.id + '&name=='+ res.data.productName)
        } catch (error) {
            console.log(error);
            dispatch(setNotify({
                type: 'error', message: 'Thao tác không thành công'
            }))
           
        }
    }

    const videoUrl = product.video;
    useEffect(() => {
        const fetchBlobAndCreateFile = async () => {
            try {
                const response = await fetch(videoUrl || '');
                const blob = await response.blob();
                const newVideo = new File([blob], 'video.mp4', { type: blob.type });
                setVideo(newVideo);
            } catch (error) {
                console.error('Error fetching blob:', error);
            }
        };
        fetchBlobAndCreateFile();
        // Giải phóng URL blob khi component unmount
        return () => {
            if (videoUrl) {
                URL.revokeObjectURL(videoUrl);
            }

        };
    }, [videoUrl]);
    const { data } = useGetCategoriesQuery(params);
    return (
        <div className="w-100 container-fluid">
            <Row>
                <Col md={12} className="border bg-light p-3">
                    <div className="bg-white p-4 border-radius-medium" style={{ width: '95%' }}>
                        <h4>Thông tin cơ bản</h4>
                        <ImgAndVideo addProductClick={addProductClick} />
                        <div className="d-flex flex-column gap-4"> <Row>
                            <Col md={2} className="text-end">
                                <div className="mt-2"> <span><span className="primary">*</span> Tên sản phẩm :</span></div>
                            </Col>
                            <Col md={10}>
                                <OverlayTrigger
                                    placement="bottom"
                                    overlay={productName.trim() === '' ? CustomTooltip("Không được để trống!") : <></>}
                                    show={productName.trim() === '' && addProductClick}
                                >
                                    <input className={`input-basic-information-seller ${productName.trim() === '' && addProductClick ? 'border-err' : ''}`}
                                        value={products.productName ? products.productName : ''}
                                        onChange={(e) => {
                                            dispatch(setProductName(e.target.value));
                                            setName(e.target.value)
                                        }}
                                        placeholder="Tên sản phẩm + Thương hiệu + Model" type="text" />
                                </OverlayTrigger>

                            </Col>
                        </Row>
                            <Row>
                                <Col md={2} className="text-end">
                                    <div className="mt-2"><span><span className="primary" >*</span> Ngành hàng:</span></div>
                                </Col>
                                <Col md={10}>
                                    <OverlayTrigger
                                        placement="bottom"
                                        overlay={category?.trim() === '' ? CustomTooltip("Không được để trống!") : <></>}
                                        show={category?.trim() === '' && addProductClick}
                                    >
                                        <div className={`category-seller `} onClick={handleOpenCategoryModal}>
                                            <input readOnly className={`input-basic-information-seller ${category?.trim() === '' && addProductClick ? 'border-err' : ''}`} value={category || ""} placeholder="Chọn ngành hàng" type="text" />
                                            <FontAwesomeIcon style={{ position: 'absolute', right: 20 }} icon={faPen} />
                                        </div>
                                    </OverlayTrigger>
                                </Col>
                            </Row>
                            <Row>
                                <Col md={2} className="text-end">
                                    <div className="mt-2"><span><span className="primary">*</span>Giá:</span></div>
                                </Col>
                                <Col md={10}>
                                    <OverlayTrigger
                                        placement="bottom"
                                        overlay={regularPrice === 0 ? CustomTooltip("Không được để trống!") : <></>}
                                        show={regularPrice === 0 && addProductClick}
                                    >
                                        <div className={`select-search-sale-info ${regularPrice === 0 && addProductClick ? 'border-err' : ''}`} >
                                            <div className="p-2 " style={{ borderRight: '2px solid rgb(241, 236, 236)' }}>
                                                <span >₫</span>
                                            </div>
                                            <input
                                                className="select-sale-info"
                                                placeholder="Giá nhập"
                                                type="text"
                                                value={products.regularPrice || ''}
                                                onChange={(e) => handleChange(Number(e.target.value))}
                                            />
                                        </div>
                                    </OverlayTrigger>

                                </Col>
                            </Row>
                            <Row>
                                <Col md={2} className="text-end">
                                    <div className="mt-2"><span><span className="primary">*</span>Cân nặng:</span></div>
                                </Col>
                                <Col md={10}>
                                    <OverlayTrigger
                                        placement="bottom"
                                        overlay={weight === null ? CustomTooltip("Không được để trống!") : <></>}
                                        show={weight === null && addProductClick}
                                    >
                                        <div className={`select-search-sale-info ${weight === null && addProductClick ? 'border-err' : ''}`} >
                                            <div className=" p-2" style={{ borderRight: '2px solid rgb(241, 236, 236)' }}>
                                                <span >gr</span>
                                            </div>
                                            <input
                                                className="select-sale-info"
                                                placeholder="Cân nặng"
                                                type="text"
                                                value={weight !== null ? String(weight) : ''}
                                                onChange={(e) => { setWeight(Number(e.target.value)) }}
                                            />
                                        </div>
                                    </OverlayTrigger>
                                </Col>
                            </Row>
                            <Row>
                                <Col md={2} className="text-end">
                                    <div className="mt-2"><span><span className="primary">*</span> Mô tả sản phẩm:</span></div>
                                </Col>
                                <Col md={10}>
                                    <textarea rows={8} cols={50} value={product.description} onChange={(e) => dispatch(setDescription(e.target.value))} className="textarea-basic-information-seller" />
                                </Col>
                            </Row>


                        </div>
                        <div className=" mt-3 w-100 d-flex justify-content-end">
                            <button className="btn-save-all-category-1" onClick={() => { handleAddProduct(); handleClickAdd() }}>Thêm sản phẩm</button>
                        </div>
                    </div>

                    {/* <div className="bg-white  border-radius-medium mt-3 p-3" style={{ width: '95%' }}>
                        <h4>Thông tin chi tiết</h4>
                        <CategoryInfo />
                    </div>
                    <div className="bg-white  border-radius-medium mt-3 p-3" style={{ width: '95%' }}>
                        <h4>Thông tin bán hàng</h4>
                        <SaleInfo />
                    </div> */}
                    {/* <div className="bg-white border-radius-medium mt-3 p-3" style={{ width: '95%' }}>
                   
                    </div> */}

                </Col>
            </Row>
            {isLoading && <ModalLoading loading={isLoading} />}
            <CategoryModal show={showCategoryModal} handleClose={handleCloseCategoryModal} categories={data?.data.items} handleCategory={handleCategory} />
        </div>
    );
}

export default InsertProduct;