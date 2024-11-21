import { useState } from "react";
import useValidText from "../../../hooks/useValidText";
import { DiscountType } from "../../../models/promotion";
import { valueOf } from "../../../utils/enum-handler";
import './create-promotion.scss'
import { OverlayTrigger } from "react-bootstrap";
import CustomTooltip from "../../../components/tooltip/CustomTooltipProps";
import Select from 'react-select';
import { convertPrice } from "../../../utils/convert-price";
import PromotionRow from "../../../components/promotion/PromotionRow";

const CreatePromotion = () => {

    const { value: promotionName, setValue: setPromotionName, err: errPromotionName } = useValidText();
    const [discountType, setDiscountType] = useState<DiscountType | undefined>(DiscountType.PERCENT);
    const [loopType, setLoopType] = useState(null);
    const [applyType, setApplyType] = useState(null);
    const discountOptions = [
        { value: DiscountType.PERCENT, label: 'Phần trăm' },
        { value: DiscountType.AMOUNT, label: 'Giá cố định' },
    ];
    const loopOptions = [
        { value: 'no-repeat', label: 'Không lặp lại' },
        { value: 'daily', label: 'Hàng ngày' },
        { value: 'weekly', label: 'Hàng tuần' },
        { value: 'monthly', label: 'Hàng tháng' },
        { value: 'yearly', label: 'Hàng năm' },
    ];
    const applyOptions = [
        { value: 'all-store', label: 'Toàn cửa hàng' },
        { value: 'by-category', label: 'Theo danh mục' },
        { value: 'by-brand', label: 'Theo thương hiệu' },
        { value: 'by-product', label: 'Theo sản phẩm' },
    ];
    const handleDiscountChange = (selectedOption: any) => {
        setDiscountType(valueOf(DiscountType,selectedOption))
    };
    const handleLoopChange = (selectedOption: any) => {
        setLoopType(selectedOption);
    };
    const handleApplyChange = (selectedOption: any) => {
        setApplyType(selectedOption);
    };
    return <div className="d-flex flex-column p-3 bg-light">
        <div className="d-flex align-items-center justify-content-between loading-container">
            <h5>Tạo chương trình khuyến mãi</h5>
        </div>
        <div className="bg-white p-3 border-radius-small mb-3 mt-3">
            <h6>Thông tin chương trình khuyến mãi</h6>
            <div className="p-3">
                <PromotionRow label="Tên chương trình khuyến mãi:">
                    <OverlayTrigger
                        placement="bottom"
                        overlay={errPromotionName.trim() !== '' ? CustomTooltip(errPromotionName) : <></>}
                        show={errPromotionName.trim() !== ''}
                    >
                        <input
                            className="input-basic-information-seller"
                            value={promotionName || ''}
                            onChange={(e) => setPromotionName(e.target.value)}
                            placeholder="Tên khuyến mãi"
                            type="text"
                        />
                    </OverlayTrigger>
                </PromotionRow>
                
                <PromotionRow label="Chọn loại mã giảm giá">
                    <div className="w-25">
                        <Select
                            value={discountOptions.find(option => option.value === discountType)} // Tìm giá trị hiện tại
                            onChange={handleDiscountChange}
                            options={discountOptions}
                            placeholder="Chọn loại giảm giá"
                        />
                    </div>
                </PromotionRow>
                <PromotionRow label="Số tiền giảm:">
                    <input className={`input-basic-information-seller }`}
                        placeholder={convertPrice(100000)} type="text" />
                </PromotionRow>
                <PromotionRow label="Ngày bắt đầu:">
                <input id="start-date" name="start-date" type="datetime-local"></input>
                </PromotionRow>
                <PromotionRow label="Ngày kết thúc:">
                    <input id="end-date" name="end-date" type="datetime-local"></input>
                </PromotionRow>
                <PromotionRow label="Lặp lại">
                    <div className="w-25">
                        <Select
                            id="loop-state"
                            value={loopType}
                            onChange={handleLoopChange}
                            options={loopOptions}
                            placeholder="Chọn tần suất lặp lại"
                        />
                    </div>
                </PromotionRow>
            </div>
        </div>
        <div className="bg-white p-3 border-radius-small mb-3 ">
            <h6>Điều kiện khuyến mãi</h6>
            <div className="p-3">
                <PromotionRow label="Loại điều kiện khuyến mãi:" >
                    <div className="d-flex gap-3">
                        <div className="d-flex align-items-center gap-2">
                            <input className="radio-insert-product-seller" type="radio" name="condition-type" id="condition-type-1"
                            />
                            <label htmlFor="condition-type-1">Giá tối thiểu</label>
                        </div>
                        <div className="d-flex align-items-center gap-2">
                            <input className="radio-insert-product-seller" type="radio" name="condition-type" id="condition-type-2"
                            />
                            <label htmlFor="condition-type-2">Số lượng mua tối thiểu</label>
                        </div>
                    </div>
                </PromotionRow>
                <PromotionRow label="Phạm vi áp dụng: ">
                    <div className="w-25">
                        <Select
                            id="apply-type"
                            value={applyType}
                            onChange={handleApplyChange}
                            options={applyOptions}
                            placeholder="Chọn phạm vi áp dụng"
                        />
                    </div>
                </PromotionRow>
                <PromotionRow label="Giá tối thiểu:">
                    <input className={`input-basic-information-seller }`}
                        placeholder={convertPrice(100000)} type="text" />
                </PromotionRow>
                <PromotionRow label="Số lượng mua tối thiểu:">
                    <input className={`input-basic-information-seller }`}
                        placeholder='5 sản phẩm' type="text" />
                </PromotionRow>

            </div>
        </div>
        <div className="p-2  d-flex justify-content-end">
            <button className="btn-save-all-category p-2" style={{ width: '12%' }} >Hoàn tất</button>
        </div>
    </div>
}

export default CreatePromotion;