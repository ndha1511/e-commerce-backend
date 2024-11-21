
import PromotionRow from "../../../components/promotion/PromotionRow";
import { useEffect, useRef, useState } from "react";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faImage, faPen, faPlus } from "@fortawesome/free-solid-svg-icons";
import { pageQueryHanlder } from "../../../utils/query-handler";
import { useGetCategoriesQuery } from "../../../services/category.service";
import ModalCategoryBrand, { CategoryItems, DeleteCategoryItems } from "./ModalCategoryBrand";
import { useDispatch, useSelector } from "react-redux";
import { RootState } from "../../../rtk/store/store";
import { useCreateBrandMutation } from "../../../services/brand.service";
import { setNotify } from "../../../rtk/slice/notify-slice";
import { clearCategories, removeAll } from "../../../rtk/slice/product-slice";
import { OverlayTrigger } from "react-bootstrap";
import CustomTooltip from "../../../components/tooltip/CustomTooltipProps";

function Brand() {
    const [avt, setAvt] = useState<File>();
    const [url, setUrl] = useState<string>("");
    const [brandName, setBrandName] = useState<string>("");
    const [descriptionBrand, setDescriptionBrand] = useState<string>("");
    const [showCategoryModal, setShowCategoryModal] = useState(false);
    const [categories, setCategories] = useState<CategoryItems[]>([]);
    const params: string = pageQueryHanlder(1, 40, [{ filed: 'parentId', operator: '=', value: 'null' }]);
    const { data } = useGetCategoriesQuery(params);
    const product = useSelector((state: RootState) => state.product);
    console.log(product);
    const chilRef = useRef<DeleteCategoryItems>(null)
    const [btnSubmit, setBtnSubmit] = useState<boolean>(false);
    const dispatch = useDispatch();
    const [createBrand] = useCreateBrandMutation();

    const handleOpenCategoryModal = () => {
        setShowCategoryModal(true);
    }

    const handleCloseCategoryModal = () => {
        setShowCategoryModal(false);
    }

    const handleCategory = (selectedCategories?: CategoryItems[]) => {

        setCategories(selectedCategories || []);
    };

    const handleChangeImage = async (e: React.ChangeEvent<HTMLInputElement>) => {
        const files = e.target.files;
        if (files && files.length > 0) {
            setUrl(URL.createObjectURL(files[0]));
            setAvt(files[0]);
        }
    };

    useEffect(() => {
        return () => {
            URL.revokeObjectURL(url);
        };
    }, [url]);

    const handleChange = (event: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement>) => {
        const { name, value } = event.target;
        if (name === 'brandName') {
            setBrandName(value);
        } else if (name === 'description') {
            setDescriptionBrand(value);
        }
    };

    const handleAdd = async () => {
        setBtnSubmit(true);
        const newFormData = new FormData();
        if (avt) {
            newFormData.append('image', avt);
        }
        newFormData.append('brandName', brandName);
        newFormData.append('description', descriptionBrand);
        product.categories.forEach(category => {
            newFormData.append('categories', category);
        });
        try {
            await createBrand(newFormData).unwrap();
            dispatch(setNotify({
                type: 'success', message: 'Thao tác thành công'
            }));
            setBrandName('');
            setDescriptionBrand('');
            setUrl('');
            setCategories([]); // Reset categories khi hoàn tất
            dispatch(removeAll());
            dispatch(clearCategories());
            if (chilRef.current) {
                chilRef.current.handleClear();
            }
            setBtnSubmit(false);
        } catch (error) {
            console.error("Failed to update user:", error);
            dispatch(setNotify({
                type: 'error', message: 'Thao tác không thành công'
            }));
        }
    }
    const handleRemoveCategory = (id: string) => {
        setCategories((prev) => prev.filter(item => item.id !== id));
        if (chilRef.current) {
            chilRef.current.handleRemoveCategory(id);
        }
    };

    return (
        <div>
            <div className="bg-light p-3 border-radius-small mb-3 mt-3">
                <h6>Thông tin thương hiệu</h6>
                <div className="p-3 bg-white border-raidus-small">
                    <PromotionRow label="Hình ảnh thương hiệu:">
                        <div className="d-flex align-items-center gap-3">
                            <div>
                                {url ? (
                                    <img
                                        src={url}
                                        alt='User Avatar'
                                        className=''
                                        width={600}
                                        height={200}
                                    />
                                ) : null}
                            </div>
                            <input
                                type="file"
                                id="file=brand"
                                style={{ display: 'none' }}
                                onChange={handleChangeImage}
                                accept="image/*"
                            />
                            <OverlayTrigger
                                placement="right"
                                overlay={url.trim() === '' && btnSubmit ? CustomTooltip("Không được để trống!", "img") : <></>}
                                show={url.trim() === ''}
                            >
                                <label htmlFor='file=brand' className='primary' style={{ cursor: 'pointer' }}>
                                    <div className="image-insert-product-seller p-2">
                                        <div className="icon-image-insert">
                                            <FontAwesomeIcon icon={faImage} fontSize={25} />
                                            <FontAwesomeIcon
                                                style={{
                                                    display: 'flex',
                                                    justifyContent: 'center',
                                                    alignItems: 'center',
                                                    fontSize: 12,
                                                    position: 'absolute',
                                                    height: 15,
                                                    width: 15,
                                                    backgroundColor: 'white',
                                                    borderRadius: '50%',
                                                    right: -6,
                                                    bottom: 0,
                                                }}
                                                icon={faPlus}
                                            />
                                        </div>
                                        <span className="w-100 text-center primary">
                                            {url ? 'Thay đổi ' : 'Thêm hình ảnh'}
                                        </span>
                                    </div>
                                </label>
                            </OverlayTrigger>
                        </div>
                    </PromotionRow>
                    <PromotionRow label="Tên thương hiệu:">
                        <OverlayTrigger
                            placement="bottom"
                            overlay={brandName.trim() === '' && btnSubmit ? CustomTooltip("Không được để trống!") : <></>}
                            show={brandName.trim() === ''}
                        >
                            <input
                                className="input-basic-information-seller"
                                name="brandName"
                                value={brandName}
                                placeholder="adidas"
                                onChange={handleChange}
                                type="text"
                            />
                        </OverlayTrigger>
                    </PromotionRow>
                    <PromotionRow label="Ngành hàng:">
                        <OverlayTrigger
                            placement="bottom"
                            overlay={categories.length === 0 && btnSubmit ? CustomTooltip("Không được để trống!") : <></>}
                            show={categories.length === 0}
                        >
                            <div className="border p-3 border-radius-small d-flex justify-content-between align-items-center">

                                <div className="d-flex flex-wrap justify-content-start col-11 ">

                                    {categories.map((category, index) => (


                                        <div key={index} className="border me-3 p-1 mb-2">
                                            <span>{category.name}</span>
                                            <button
                                                onClick={() => handleRemoveCategory(category.id)}
                                                style={{
                                                    marginLeft: '5px',
                                                    background: 'transparent',
                                                    border: 'none',
                                                    color: '#999',
                                                    cursor: 'pointer'
                                                }}
                                            >
                                                ×
                                            </button>
                                        </div>


                                    ))}
                                </div>

                                <div className="col-1 d-flex justify-content-end ">
                                    <div className="brand-shadow d-flex justify-content-center align-items-center" onClick={handleOpenCategoryModal} style={{ cursor: 'pointer', width: 30, height: 20 }} >
                                        <FontAwesomeIcon style={{ right: 20, fontSize: 11 }} icon={faPen} />
                                    </div>
                                </div>
                            </div>
                        </OverlayTrigger>
                    </PromotionRow>
                    <PromotionRow label="Mô tả chi tiết:">
                        <textarea rows={8} cols={50} value={descriptionBrand} onChange={handleChange} name="description" className="textarea-basic-information-seller" />
                    </PromotionRow>
                    <div className="mt-3 w-100 d-flex justify-content-end">
                        <button className="btn-save-all-category-1" style={{ width: 150 }} onClick={handleAdd}>Hoàn tất</button>
                    </div>
                </div>
            </div>
            <ModalCategoryBrand show={showCategoryModal} handleClose={handleCloseCategoryModal} categories={data?.data.items} handleCategory={handleCategory}
                ref={chilRef} />
        </div>
    );
}

export default Brand;
