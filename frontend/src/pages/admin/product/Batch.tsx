import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { OverlayTrigger } from "react-bootstrap";
import CustomTooltip from "../../../components/tooltip/CustomTooltipProps";
import { useState } from "react";
import { faPen } from "@fortawesome/free-solid-svg-icons";
import CategoryModal from "../../../components/seller/modal/CategoryModal";
import { useGetCategoriesQuery } from "../../../services/category.service";
import { pageQueryHanlder } from "../../../utils/query-handler";

const Batch = () => {
    const [showCategoryModal, setShowCategoryModal] = useState(false);
    const [category, setCategory] = useState<string | null>('');
    const params: string = pageQueryHanlder(1, 40, [{ filed: 'parentId', operator: '=', value: 'null' }]);
    const { data } = useGetCategoriesQuery(params);

    const handleOpenCategoryModal = () => {
        setShowCategoryModal(true);
    }

    const handleCategory = (categoryModal?: string) => {
        if (categoryModal) {
            setCategory(categoryModal);
        }
    }

    

    return <div className=" bg-light p-3 pe-3">
        <div className=" d-flex justify-content-between">
            <div className="d-flex gap-1"><h5>Thêm sản phẩm hàng loạt</h5>
            </div>
        </div>
        <div className="bg-white p-2 border-radius-small mt-3">
            <div className="mt-1 mb-3 d-flex align-items-center gap-2 ">
                <OverlayTrigger
                    placement="bottom"
                    overlay={category?.trim() === '' ? CustomTooltip("Không được để trống!") : <></>}
                    show={category?.trim() === ''}
                >
                    <div className={`category-seller `} onClick={handleOpenCategoryModal}>
                        <input readOnly className={`input-basic-information-seller ${category?.trim() === '' ? 'border-err' : ''}`} value={category || ""} placeholder="Chọn ngành hàng" type="text" />
                        <FontAwesomeIcon style={{ position: 'absolute', right: 20 }} icon={faPen} />
                    </div>
                </OverlayTrigger>
                <button className="btn btn-primary">Tải mẫu excel</button>
            </div>
            <div className="mt-1 mb-3 d-flex align-items-center justify-content-center">
                <button className="btn btn-success">Tải lên file excel</button>
            </div>
        </div>
        {showCategoryModal && <CategoryModal show={showCategoryModal} handleClose={() => setShowCategoryModal(false)} categories={data?.data.items} handleCategory={handleCategory} />}
    </div>
}

export default Batch;