import { faImage, faPlus, faXmark } from "@fortawesome/free-solid-svg-icons";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { Col, OverlayTrigger, Row, Tooltip, TooltipProps } from "react-bootstrap";
import './sale-info.scss';
import { useEffect, useRef, useState } from "react";
import { ProductAttributeDto } from "../../../dtos/request/product/product-attribute.reques";
import { useDispatch, useSelector } from "react-redux";
import { RootState } from "../../../rtk/store/store";
import { addAttributeDto, addVariantDto, setAttributeValue, setVariantDto } from "../../../rtk/slice/product-slice";
import { VariantDto } from "../../../dtos/request/product/variant.reques";

interface SaleInfoDetailsProps {
    handleCloseAtribute: () => void;
    index: number;
    isSuccess: boolean;
}
const AttributeSaleInfo: React.FC<SaleInfoDetailsProps> = ({ handleCloseAtribute, index, isSuccess }) => {
    const [errorMessages, setErrorMessages] = useState<{ [key: number]: string | null }>({}); // Trạng thái lưu lỗi cho từng ô
    const [isFocused, setIsFocused] = useState(false);
    const [isHidden, setIsHidden] = useState(false);
    const [variantValue, setVariantValue] = useState<number>(0);
    const [flag, setFlag] = useState<boolean>(false);
    const productAttributeDto: ProductAttributeDto[] = useSelector((state: RootState) => state.product.attributesDto);
    const attributeVariant: VariantDto[] = useSelector((state: RootState) => state.product.variantsDto);
    const [previewImage, setPreviewImage] = useState<{ id: number, url: string }[]>([]);

    const inputRef = useRef<HTMLInputElement>(null);
    const dispatch = useDispatch();
    const handleOptionClick = (option: string) => {
        if (index === 0) {
            dispatch(addAttributeDto({
                index: index,
                data: { attributeName: option }
            }));
        } else {
            if (productAttributeDto.length === 0) {
                dispatch(addAttributeDto({
                    index: 0,
                    data: { attributeName: '' }
                }));
            }
            dispatch(addAttributeDto({
                index: index,
                data: { attributeName: option }
            }));
        }
        setIsFocused(false);
        setIsHidden(true);
    };

    const handleOnchangeInput = (e: React.ChangeEvent<HTMLInputElement>) => {
        if (index === 0) {
            dispatch(addAttributeDto({
                index: index,
                data: { attributeName: e.target.value }
            }));
        } else {
            if (productAttributeDto.length === 0) {
                dispatch(addAttributeDto({
                    index: 0,
                    data: { attributeName: '' }
                }));
            }
            dispatch(addAttributeDto({
                index: index,
                data: { attributeName: e.target.value }
            }));
        }
    };

    const handleKeyDown = (e: React.KeyboardEvent<HTMLInputElement>) => {
        if (e.key === 'Enter') {
            setIsFocused(false);
            setIsHidden(true);
        }
    };
    useEffect(() => {
        if (isSuccess) {
            setIsFocused(false);
            setIsHidden(true);
        }
    }, [isSuccess]);

    const [variants, setVariants] = useState([
        { id: 1, img: null as File | null, variant: '' },
    ]);
    useEffect(() => {
        if (productAttributeDto) {
            const attributeVal = productAttributeDto[index]?.attributeValues?.map((value, index) => {
                return {
                    id: index + 1,
                    img: null,
                    variant: value.value
                }
            }) || [];
            attributeVal.push({ id: attributeVal.length + 1, img: null, variant: '' })
            setVariants(attributeVal);
        }
    }, [productAttributeDto])
    const handleVariantChange = (e: React.ChangeEvent<HTMLInputElement>, id: number) => {     
        const updatedVariants = variants.map(attr =>
            attr.id === id ? { ...attr, variant: e.target.value } : attr
        );
        setVariants(updatedVariants);
        const lastVariant = updatedVariants[updatedVariants.length - 1];
        if (lastVariant.variant.trim() !== '') {
            setVariants([...updatedVariants, { id: updatedVariants.length + 1, img: null as File | null, variant: '' }]);
        }

    };

    const handleBlur = (_: React.FocusEvent<HTMLInputElement>, id: number, indexIp: number) => {
        const variant = variants.find(attr => attr.id === id)?.variant;
        if (variant && variant.trim() !== '' && index === 0) {
            const attributeValue1 = [...(productAttributeDto[0]?.attributeValues || [])]
            const indexAttr = attributeValue1.findIndex(value => value.id === id);
            const isVariantExist = attributeValue1.some(value => value.value === variant);
            if (!isVariantExist) {
                if (indexAttr !== -1) {
                    attributeValue1[indexAttr] = {
                        id: id,
                        value: variant,
                        image: attributeValue1[indexAttr].image
                    }
                } else {
                    attributeValue1.push({
                        id: id,
                        value: variant
                    })
                }
                dispatch(setAttributeValue({
                    attributeName: productAttributeDto[0].attributeName,
                    attributeValues: [
                        ...attributeValue1
                    ]
                }))
                setVariantValue(id)
                setFlag(prev => !prev)
                setErrorMessages(prev => ({
                    ...prev,
                    [id]: null // Xóa lỗi nếu không trùng
                }));
            } else {
                const checke = variants.findIndex(vari => vari.variant === variant);

                if (checke === indexIp) {
                    return;
                } else {
                    // Nếu biến thể bị trùng, cập nhật trạng thái lỗi cho ô đó
                    setErrorMessages(prev => ({
                        ...prev,
                        [id]: 'This variant already exists.' // Đặt thông báo lỗi cho biến thể trùng
                    }));
                }

            }



        }
        if (variant && variant.trim() !== '' && index === 1) {
            const attributeValue1 = [...(productAttributeDto[1]?.attributeValues || [])]
            const indexAttr = attributeValue1.findIndex(value => value.id === id);
            if (indexAttr !== -1) {
                attributeValue1[indexAttr] = {
                    id: id,
                    value: variant
                }
            } else {
                attributeValue1.push({
                    id: id,
                    value: variant
                })
            }
            dispatch(setAttributeValue({
                attributeName: productAttributeDto[1]?.attributeName,
                attributeValues: [
                    ...attributeValue1
                ]
            }))
            setVariantValue(id)
            setFlag(prev => !prev)
        }

    };
    useEffect(() => {
        if (variantValue > 0) {
            if (index === 0) {
                const arrVariant = attributeVariant.filter(variant => variant.attributeValue1 === variantValue)
                console.log(arrVariant)
                if (arrVariant.length > 0) {
                    return;
                }
                const uniqueAttribute2 = attributeVariant.filter((item, index, self) =>
                    index === self.findIndex((t) => t.attributeValue2 === item.attributeValue2)
                );
                if (!uniqueAttribute2 || uniqueAttribute2.length === 0) {
                    // Thực hiện dispatch một cách đúng
                    dispatch(addVariantDto({
                        attributeValue1: variantValue,
                        price: 0,
                        quantity: 0
                    }));
                    return;
                }
                const uniqueAttribute1 = attributeVariant.filter((item) => item.attributeValue1)
                const updatedProducts = uniqueAttribute2.map(variant => ({
                    ...variant,
                    attributeValue1: variantValue,
                }))
                const updateVariants = [...uniqueAttribute1, ...updatedProducts];
                dispatch(setVariantDto(updateVariants));
            }
            if (index === 1) {
                const arrVariant = attributeVariant.filter(variant => variant.attributeValue2 === variantValue)
                if (arrVariant.length > 0) {
                    return;
                }
                const uniqueAttribute1 = attributeVariant.filter((item, index, self) =>
                    index === self.findIndex((t) => t.attributeValue1 === item.attributeValue1)
                );
                if (!uniqueAttribute1 || uniqueAttribute1.length === 0) {
                    // Thực hiện dispatch một cách đúng
                    dispatch(addVariantDto({
                        attributeValue2: variantValue,
                        price: 0,
                        quantity: 0
                    }));
                    return;
                }
                const uniqueAttribute2 = attributeVariant.filter((item) => item.attributeValue2)
                const updatedProducts = uniqueAttribute1.map(variant => ({
                    ...variant,
                    attributeValue2: variantValue,
                }))
                const updateVariants = [...uniqueAttribute2, ...updatedProducts];
                dispatch(setVariantDto(updateVariants));
            }
        }
    }, [flag])
    useEffect(() => {
        //  console.log(attributeVariant)
    }, [attributeVariant])
    const attributes = [
        { id: 1, name: 'Màu sắc' },
        { id: 2, name: 'Size' },
        { id: 3, name: 'Giới tính' },
    ]
    const filteredAttributes = attributes.filter(attr =>
        !productAttributeDto?.[0]?.attributeName.includes(attr.name)
    );
    useEffect(() => {

        const imgs = productAttributeDto[0]?.attributeValues;
        if (imgs) {
            const images = imgs.map((img, index) => {
                if (img.image) {
                    // const url: string = URL.createObjectURL(img.image);
                    return {
                        id: index + 1,
                        url: img.image
                    }
                }
                return {
                    id: index + 1,
                    url: ''
                }

            })
            setPreviewImage(images);
        }

    }, [productAttributeDto?.[0]?.attributeValues])
    useEffect(() => {
        // console.log(productAttributeDto) 
    }, [productAttributeDto])
    const handleFileChange = (even: React.ChangeEvent<HTMLInputElement>, id: number) => {
        const newFiles = even.target.files;
        console.log(newFiles)
        if (newFiles) {
            // dispatch(addImage(newFiles[0]))
            const attributeValue1 = [...(productAttributeDto[0].attributeValues) || []]
            if (index === 0) {
                const attributeValues = attributeValue1.find(attr => attr.id === id);
                const indexAttr = attributeValue1.findIndex(value => value.id === id);
                if (indexAttr !== -1) {
                    attributeValue1[indexAttr] = {
                        id: id,
                        value: attributeValues?.value ?? '',
                        image: URL.createObjectURL(newFiles[0])
                    }
                } else {
                    attributeValue1.push({
                        id: id,
                        value: '',
                        image: URL.createObjectURL(newFiles[0])
                    })
                }
                dispatch(setAttributeValue({
                    attributeName: productAttributeDto[0].attributeName,
                    attributeValues: [
                        ...attributeValue1
                    ]
                }))
            }
        }
    };
    const renderTooltip = (props: TooltipProps, message: string | null) => (
        <Tooltip id="button-tooltip" {...props}>
            {message || "Giá trị đã tồn tại"}
        </Tooltip>
    );
    return (
        <div className="bg-light p-3 border-radius-small" >
            <Row>
                <Col md={12}>
                    <div className="d-flex justify-content-between">
                        {/* {isHidden && <span style={{ fontWeight: 600 }}>{productAttributeDto[index]?.attributeName}</span>} */}
                        {isHidden && <span style={{ fontWeight: 600 }}>{productAttributeDto?.[index]?.attributeName}</span>}
                        <div className={`select-search-sale-info bg-white w-50 ${isHidden ? 'hidden-input' : ''}`}>
                            <input
                                ref={inputRef}
                                className={`select-sale-info`}
                                placeholder="Vui lòng chọn"
                                type="text"
                                onFocus={() => setIsFocused(true)}
                                onBlur={() => setTimeout(() => setIsFocused(false), 200)}
                                value={productAttributeDto?.[index]?.attributeName || ""}
                                onChange={handleOnchangeInput}
                                onKeyDown={handleKeyDown} // Capture Enter key event
                            />
                            <div className={`p-1 ps-2 ${isFocused ? 'visible' : 'hidden'}`} style={{ borderLeft: '2px solid rgb(241, 236, 236)' }}>
                                <span>0/14</span>
                            </div>
                            {isFocused && (
                                <div className="suggestions">
                                    {filteredAttributes?.map((attribute) => (
                                        <div className="ats" key={attribute.id} onClick={() => handleOptionClick(attribute.name)}>{attribute.name}</div>
                                    ))}

                                </div>
                            )}
                        </div>
                        <FontAwesomeIcon style={{ fontSize: 20 }} icon={faXmark} onClick={handleCloseAtribute} />
                    </div>
                </Col>
            </Row>
            <hr />
            <Row>
                <Col md={12} className={`d-grid ${productAttributeDto?.[index]?.attributeName ? 'gap-4' : ''}`} style={{ gridTemplateColumns: 'repeat(2, 1fr)' }}>
                    {variants.map((attr, index1) => (
                        <div key={attr.id} className="d-flex align-items-center justify-content-between w-100">
                            {index === 0 &&
                                <div>
                                    {previewImage.map((url, index) => {
                                        return (
                                            url.id === attr.id && url.url !== '' &&
                                            <div key={url.id}>
                                                <img
                                                    src={url.url}
                                                    className="border-radius-small"
                                                    alt={`Preview ${index}`}
                                                    style={{ width: '40px', height: '40px', marginRight: 10 }}
                                                />
                                            </div>
                                        )


                                    })}
                                    {!productAttributeDto?.[0]?.attributeValues?.[index1]?.image && (
                                        <>
                                            <input
                                                type="file"
                                                id={attr.id + 'id'}
                                                style={{ display: 'none' }}
                                                onChange={(e) => { handleFileChange(e, attr.id) }}
                                                accept="image/*"
                                            />
                                            <label htmlFor={attr.id + 'id'} className="d-flex align-items-center primary">
                                                <div className="image-color p-2">
                                                    <div className="icon-image-insert">
                                                        <FontAwesomeIcon icon={faImage} fontSize={15} />
                                                        <FontAwesomeIcon className="icon-plus-image" icon={faPlus} />
                                                    </div>
                                                </div>
                                            </label>
                                        </>
                                    )}
                                </div>}
                            <div className="select-search-sale-info bg-white">
                                <OverlayTrigger
                                    placement="bottom"
                                    overlay={(props) => renderTooltip(props, errorMessages[attr.id])}
                                    show={!!errorMessages[attr.id]}>
                                    <input
                                        className="select-sale-info"
                                        placeholder="Vui lòng nhập"
                                        type="text"
                                        value={attr.variant}
                                        onChange={(e) => { handleVariantChange(e, index1 + 1); }}
                                        onBlur={(e) => handleBlur(e, attr.id, index1)}

                                    />
                                </OverlayTrigger>
                                <div className="p-1 ps-2" style={{ borderLeft: '2px solid rgb(241, 236, 236)' }}>
                                    <span>0/20</span>
                                </div>
                            </div>
                        </div>
                    ))}
                </Col>
            </Row>
        </div>
    );
};

export default AttributeSaleInfo;
