import { faPlus } from "@fortawesome/free-solid-svg-icons";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { Col, Row } from "react-bootstrap";
import './sale-info.scss';
import AttributeSaleInfo from "./AttibuteSaleInfo";
import { useEffect, useState } from "react";
import ProductTable from "./ProductTable";
import { useDispatch, useSelector } from "react-redux";
import { RootState } from "../../../rtk/store/store";
import { setAttributeDto, setAttributeValue, setRegularPrice, setVariantDto } from "../../../rtk/slice/product-slice";
import useGetParam from "../../../hooks/useGetParam";
import { useCreateAttributeMutation, useGetAttributeByIdQuery } from "../../../services/product.service";
import { setNotify } from "../../../rtk/slice/notify-slice";
import { VariantDto } from "../../../dtos/request/product/variant.reques";



function SaleInfo() {

    const products = useSelector((state: RootState) => state.product);
    const idProduct = useGetParam('id');
    const { data, isSuccess } = useGetAttributeByIdQuery(idProduct || '');
    const variants = useSelector((state: RootState) => state.product.variantsDto);
    const dispatch = useDispatch();
    const [showAttributeSaleInfo1, setShowAttributeSaleInfo1] = useState(false);
    const [regularPrice, setRigularPrice] = useState<number>(0);
    const quantity = 0;
    const [isApplyAll, setIsApplyAll] = useState<boolean>(true);
    const [trigger] = useCreateAttributeMutation();
    const productName = useGetParam('name');
    const handleCloseAtribute = () => {
        setShowAttributeSaleInfo1(false);
    };
    const handleCloseAtribute1 = () => {
        setShowAttributeSaleInfo1(false);
    };
    const handleChange = (value: number) => {
        setRigularPrice(value);
        dispatch(setRegularPrice(value))
    }
   
    const handleApplyAll = () => {
        const variantNew = [...variants].map((variant) => ({
            ...variant,
            price: regularPrice !== 0 ? regularPrice : variant.price,
            quantity: quantity !== 0 ? quantity : variant.quantity,
        }));
        dispatch(setVariantDto(variantNew))
    }
    useEffect(() => {
        if (regularPrice !== 0 || quantity !== 0) {
            setIsApplyAll(false);
        } else {
            setIsApplyAll(true);
        }
    }, [regularPrice, quantity])
    useEffect(() => {
        if (isSuccess) {
            const variants = data.data.variants;
            const attributes = data?.data.attributes;
            dispatch(setAttributeDto(attributes))

            const variantNew = variants?.map((v) => {
                const variantDto: VariantDto = {
                    price: v.price,
                    attributeValue1: 0,
                    quantity: 0
                }
                const indexNew = attributes[0].attributeValues.findIndex((val) => val.value === v.attributeValue1)
                if (indexNew !== -1) {
                    variantDto.attributeValue1 = indexNew + 1;
                }
                const indexNew1 = attributes[1]?.attributeValues.findIndex((val) => val.value === v.attributeValue2);
                if (indexNew1 !== -1) {
                    variantDto.attributeValue2 = indexNew1 + 1;
                }
                return variantDto;
            })
            console.log(variantNew)
            console.log(variants)
            for (let i = 0; i < attributes?.length; i++) {
                const attVal = attributes[i].attributeValues;


                const newAttrVal = attVal.map((val, i) => {
                    return {
                        id: i + 1,
                        value: val.value,
                        image: val.image
                    }
                })
                dispatch(setAttributeValue({
                    attributeName: attributes[i].attributeName,
                    attributeValues: newAttrVal
                }));
            }
            dispatch(setVariantDto(variantNew))
        }
    }, [isSuccess])
    console.log(products)
    const fetchBlobAndCreateFile = async (url: string) => {
        try {
            const response = await fetch(url || '');
            const blob = await response.blob();
            const newImg = new File([blob], 'image.jfif', { type: blob.type });
            URL.revokeObjectURL(url);
            return newImg;
        } catch (error) {
            console.error('Error fetching blob:', error);
        }
    };
    const handleSave = async () => {
        const formData = new FormData();
        const attributes = products.attributesDto;
        const variants = products.variantsDto;
        try {
            formData.append('productId', idProduct || '');
            for (let i = 0; i < attributes.length; i++) {
                const attVal = attributes[i].attributeValues;
                formData.append(`attributes[${i}].attributeName`, attributes[i].attributeName)
                if (attVal && attVal.length > 0) {
                    for (let j = 0; j < attVal.length; j++) {
                        formData.append(`attributes[${i}].attributeValues[${j}].value`, attVal[j].value)
                        if (attVal[j].image) {
                            const img = await fetchBlobAndCreateFile(attVal[j].image || '');
                            formData.append(`attributes[${i}].attributeValues[${j}].image`, img || '')
                        }
                    }
                }
            }
            for (let i = 0; i < variants.length; i++) {
                formData.append(`variants[${i}].price`, variants[i].price.toString())
                const variantVal = attributes[0].attributeValues?.find((attr) => attr.id === variants[i].attributeValue1);
                if (variantVal) {
                    formData.append(`variants[${i}].attributeValue1`, variantVal.value)
                }
                const variantVal1 = attributes[1].attributeValues?.find((attr) => attr.id === variants[i].attributeValue2);
                if (variantVal1) {
                    formData.append(`variants[${i}].attributeValue2`, variantVal1.value)
                }
            }
            await trigger(formData).unwrap();
            dispatch(setNotify({
                type: 'success', message: 'Thao tác thành công'
            }))
        } catch (error) {
            console.log(error);
            dispatch(setNotify({
                type: 'error', message: 'Thao tác thất bại'
            }))
        }
    }
    return (<div className="container-fluid ">
        <div>

            <h5>Phân loại sản phẩm</h5>
            <div className="product-name-attribute">
                <span className="text-medium text-white ms-3">
                    Sản phẩm: {productName ?
                        (productName.length > 100 ? productName.slice(0, 100) + '...' : productName)
                        : 'Tên sản phẩm không có'}
                </span>
            </div>


            <Row className="mt-3">
                <Col md={12} className=''>
                    <Row>
                        <Col md={2} className="">
                            <div className=' p-1 justify-content-end d-flex mt-2 '>
                                <span><span className='primary'>*</span>Phân loại hàng: </span>
                            </div>

                        </Col>
                        <Col md={10}>
                            <>
                                <AttributeSaleInfo
                                    handleCloseAtribute={handleCloseAtribute}
                                    index={0}
                                    isSuccess={data?.data.attributes?.[0] !== undefined}
                                />
                                <div className={`${showAttributeSaleInfo1 === false ? 'p-3' : ''} w-100 bg-light mt-3 border-radius-small `}>
                                    {data?.data.attributes?.[1] ?
                                        <></>
                                        :
                                        showAttributeSaleInfo1 === false &&
                                        <button className="btn-classify w-25"
                                            onClick={() => { setShowAttributeSaleInfo1(true) }}>
                                            <FontAwesomeIcon icon={faPlus} /> Thêm phân loại 2</button>
                                    }
                                    {(showAttributeSaleInfo1 || data?.data.attributes?.[1]) &&
                                        <>
                                            <AttributeSaleInfo
                                                handleCloseAtribute={handleCloseAtribute1}
                                                index={1}
                                                isSuccess={data?.data.attributes[1] !== undefined}
                                            />
                                        </>
                                    }
                                </div>
                            </>
                        </Col>

                    </Row>
                    <Row className="mt-3">
                        <Col md={2} className="">
                            <div className=' p-1 justify-content-end d-flex mt-2 '>
                                <span className="text-end">Danh sách phân loại hàng : </span>
                            </div>
                        </Col>
                        <Col md={10}>
                            <Row>
                                <Col md={9} className="d-flex gap-4">
                                    <div className="select-search-sale-info" >
                                        <div className=" p-1 pe-2" style={{ borderRight: '2px solid rgb(241, 236, 236)' }}>
                                            <span >₫</span>
                                        </div>
                                        <input
                                            className="select-sale-info"
                                            placeholder="Giá bán"
                                            type="text"
                                            value={products.regularPrice || ''}
                                            onChange={(e) => handleChange(Number(e.target.value))}
                                        />
                                    </div>
                                </Col>
                                {/* <Col md={2} className="d-flex">
                                    <div className="select-search-sale-info" >
                                        <input
                                            className="select-sale-info"
                                            placeholder="Kho hàng"
                                            type="text"
                                            onChange={(e) => handleChangeQuantity(Number(e.target.value))}
                                        />
                                    </div>
                                </Col> */}
                                <Col md={3} className="d-flex">
                                    <button disabled={isApplyAll} onClick={() => handleApplyAll()} className="btn-save-all-category">Áp dụng tất cả phân loại</button>
                                </Col>
                            </Row>
                            <ProductTable />

                        </Col>
                    </Row>
                </Col>
            </Row>

        </div>
        <div className="p-2  d-flex justify-content-end">
            <button onClick={handleSave} className="btn-save-all-category p-2" style={{ width: '12%' }} >Hoàn tất</button>
        </div>
    </div>);
}

export default SaleInfo;