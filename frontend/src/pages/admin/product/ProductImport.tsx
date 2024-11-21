import { Col, Row, Table } from "react-bootstrap";
import { convertPrice } from "../../../utils/convert-price";
import { ChangeEvent, useState } from "react";
import { pageQueryHanlder } from "../../../utils/query-handler";
import { useGetProductsPageQuery } from "../../../services/product.service";
import useDebounce from "../../../hooks/useDebounce";
import SimpleBar from "simplebar-react";
import './insert-product.scss'
import { useGetVariantsByProductIdQuery } from "../../../services/variant.service";
import ModalLoading from "../../../components/loading/ModalLoading";
import { InventoryDto } from "../../../dtos/request/inventory/inventory-request";
import { useCreateInventoryMutation } from "../../../services/inventory.service";
import { useDispatch } from "react-redux";
import { setNotify } from "../../../rtk/slice/notify-slice";

function ProductImport() {
    const [searchKeyword, setSearchKeyword] = useState<string>('');
    const [open, setOpen] = useState<boolean>(false);
    const debounce = useDebounce(searchKeyword, 500)
    const params: string = pageQueryHanlder(1, 100, [{ filed: 'productName', operator: ':', value: debounce }]);
    const { data } = useGetProductsPageQuery(params);
    const [inputQuantity, setInputQuantity] = useState<number>(0);
    const [inputPrice, setInputPrice] = useState<number>(0);
    const [productId, setProductId] = useState<string>('')
    const { data: variants, isLoading } = useGetVariantsByProductIdQuery(productId || '', { skip: !productId });
    const [quantities, setQuantities] = useState<{ [key: string]: number }>({});
    const [prices, setPrices] = useState<{ [key: string]: number }>({});
    const [trigger, { isLoading: addInventory }] = useCreateInventoryMutation();
    const [isApplyAll, setIsApplyAll] = useState<boolean>(true);
    const dispatch = useDispatch();
    const handleSearchProduct = (e: ChangeEvent<HTMLInputElement>) => {
        if (e.target.value !== '') {
            setOpen(true);
        } else {
            setOpen(false);
        }
        setSearchKeyword(e.target.value);
    }
    const handleSelect = (productId: string) => {
        setOpen(false);
        setSearchKeyword('');
        setProductId(productId);
    }
    const handleQuantityChange = (variantId: string, value: number) => {
        setQuantities((prevQuantities) => ({
            ...prevQuantities,
            [variantId]: value,
        }));
    }

    const handlePriceChange = (variantId: string, value: number) => {
        setPrices((prevPrices) => ({
            ...prevPrices,
            [variantId]: value,
        }));
    };
    const totalAmount = Object.keys(quantities).reduce((acc, id) => {
        return acc + (quantities[id] || 0) * (prices[id] || 0);
    }, 0);
    const handleAddInventory = async () => {
        const newInventory: InventoryDto[] = [];
        if (variants?.data) {
            console.log(variants?.data)
            for (let i = 0; i < variants?.data.length; i++) {
                newInventory.push({
                    productId: productId,
                    variantId: variants.data[i].id,
                    importQuantity: quantities[variants.data[i].id] || 0,
                    importPrice: prices[variants.data[i].id] || 0,
                })
            }

        }
        try {
            await trigger({ inventories: newInventory }).unwrap();
            dispatch(setNotify({
                type: 'success', message: 'Thao tác thành công'
            }))
        } catch (error) {
            console.error(error)
            dispatch(setNotify({
                type: 'error', message: 'Thao tác không thành công'
            }))
        }
    }
    const handleDelete = (varId: string) => {
        setQuantities((prevQuantities) => ({
            ...prevQuantities,
            [varId]: 0,
        }));
        setPrices((prevPrices) => ({
            ...prevPrices,
            [varId]: 0,
        }));
    }
    const handleQuantityAllChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        const newValue = Number(e.target.value);
        setInputQuantity(newValue);
        if (newValue === 0 && inputPrice === 0) {
            setIsApplyAll(true);
        } else {
            setIsApplyAll(false);
        }
    };

    const handlePriceAllChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        const newValue = Number(e.target.value);
        setInputPrice(newValue);
        if (newValue === 0 && inputQuantity === 0) {
            setIsApplyAll(true);
        } else {
            setIsApplyAll(false);
        }
    };
    const handleApplyAll = () => {
        const newQuantity = Number(inputQuantity) || 0;
        const newPrice = Number(inputPrice) || 0;

        if (variants?.data) {
            const updatedQuantities = { ...quantities };
            const updatedPrices = { ...prices };
            for (let i = 0; i < variants.data.length; i++) {
                const variantId = variants.data[i].id;
                if (newQuantity > 0) {
                    updatedQuantities[variantId] = newQuantity;
                }
                if (newPrice > 0) {
                    updatedPrices[variantId] = newPrice;
                }
            }
            setQuantities(updatedQuantities);
            setPrices(updatedPrices);
        }
        setInputQuantity(0);
        setInputPrice(0);
        setIsApplyAll(true)
    };
    return (
        <div className="p-3 bg-light">
            <div className=" d-flex justify-content-between">
                <h5>Thông tin sản phẩm</h5>
            </div>
            <div className="mt-3 bg-white border-radius-small p-3">
                <div className=" mb-3 d-flex justify-content-between gap-3">
                    <div className="search-list-product p-2 pe-3">
                        <input className="input-search-list-product"
                            value={searchKeyword} onChange={(e) => { handleSearchProduct(e) }}
                            placeholder="Nhập từ khóa tìm kiếm" type="text" />
                        <i className="bi bi-search"></i>
                        {open && <div className=" bg-white search-result">
                            <SimpleBar style={{ minHeight: 100 }}>
                                {data?.data?.items?.length && data.data.items.length > 0 ? (
                                    data.data.items.map((item) => (
                                        <div className=" search-result-cs" onClick={() => { handleSelect(item.id) }} key={item.id}>
                                            <div className="d-flex align-items-center">
                                                {item.thumbnail && <img src={item.thumbnail} alt={item.thumbnail} width={60} height={60} />}
                                                <div className="d-flex flex-column justify-content-center  " style={{ fontSize: 12 }}>
                                                    <span className="truncate-text ps-2">{item.productName}</span>
                                                    <span className="truncate-text ps-2 text-muted" >skit-hela</span>
                                                    <span className="truncate-text ps-2">{item.weight} gr</span>
                                                </div>
                                            </div>
                                            <span style={{ fontSize: 12 }}>
                                                Tồn kho: <strong>{item.totalQuantity}</strong>
                                            </span>

                                        </div>
                                    ))
                                ) : (
                                    <div className="text-center mt-4 d-flex align-items-center justify-content-center gap-2">
                                        <i style={{ fontSize: 25 }} className="bi bi-search"></i>
                                        Không tìm thấy kết quả phù hợp</div>
                                )}
                            </SimpleBar>
                        </div>}
                    </div>
                    {variants?.data && variants.data.length > 0 && (
                        <>
                            <div className="select-search-sale-info w-25">
                                <input
                                    className="select-sale-info no-spinner"
                                    placeholder="Số lượng"
                                    type="number"
                                    value={inputQuantity || convertPrice(0)}
                                    onChange={handleQuantityAllChange}
                                />
                            </div>
                            <div className="select-search-sale-info w-25">
                                <div className="p-1 pe-2" style={{ borderRight: '2px solid rgb(241, 236, 236)' }}>
                                    <span>₫</span>
                                </div>
                                <input
                                    className="select-sale-info no-spinner"
                                    placeholder="Giá nhập"
                                    type="number"
                                    value={inputPrice || convertPrice(0)}
                                    onChange={handlePriceAllChange}
                                />
                            </div>
                            <div className="d-flex w-25">
                                <button
                                    disabled={isApplyAll}
                                    onClick={handleApplyAll}
                                    className="btn-save-all-category"
                                >
                                    Áp dụng tất cả
                                </button>
                            </div>
                        </>
                    )}

                </div>
                <Table className='table-bordered table-responsive  custom-table-product-stock-import '>
                    <thead>
                        <tr>
                            <th>SKU</th>
                            <th>TÊN SẢN PHẨM</th>
                            <th>MÀU SẮC</th>
                            <th>DUNG LƯỢNG</th>
                            <th>SỐ LƯỢNG</th>
                            <th>GIÁ NHẬP</th>
                            <th>TỔNG</th>
                            <th></th>
                        </tr>
                    </thead>

                    {variants && variants.data && variants?.data?.length > 0 ? (
                        <tbody>
                            {variants?.data.map((variant, index) => (
                                <tr key={index}>
                                    <td>{variant.sku}</td>
                                    <td>
                                        {variant.product.productName.length > 70
                                            ? variant.product.productName.slice(0, 70) + "..."
                                            : variant.product.productName}
                                    </td>
                                    <td className="pt-1 pb-1">
                                        <div className="d-flex justify-content-center gap-2 align-items-center">
                                            {variant.image && <img src={variant.image} alt="anh" width={50} height={50} />}
                                            <div className={`${variant.image ? 'text-start' : 'text-center'}`} style={{ minWidth: 50 }}>
                                                {variant.attributeValue1}
                                            </div>
                                        </div>
                                    </td>
                                    <td>{variant.attributeValue2}</td>
                                    <td className="p-3">
                                        <div className="p-1 border border-radius-small">
                                            <input
                                                className="no-spinner"
                                                style={{ outline: "none", border: "none" }}
                                                type="number"
                                                value={quantities[variant.id] || convertPrice(0)}
                                                onChange={(e) => handleQuantityChange(variant.id, Number(e.target.value))}
                                            />
                                        </div>
                                    </td>
                                    <td className="p-3">
                                        <div className="p-1 border border-radius-small">
                                            <input
                                                className="no-spinner"
                                                style={{ outline: "none", border: "none" }}
                                                type="number"
                                                value={prices[variant.id] || convertPrice(0)}
                                                onChange={(e) => handlePriceChange(variant.id, Number(e.target.value))}
                                            />
                                        </div>
                                    </td>
                                    <td>{(quantities[variant.id] || 0) * (prices[variant.id] || 0)}</td>
                                    <td>
                                        <i className="bi bi-trash3" onClick={() => { handleDelete(variant.id) }} style={{ cursor: 'pointer' }}></i>
                                    </td>
                                </tr>
                            ))}
                        </tbody>
                    ) : (<></>)}



                </Table>
                {variants?.data.length && variants?.data.length > 0 &&
                    <div className="  d-flex justify-content-end pe-4">
                        <Row className=" w-25" >
                            <Col md={6}>
                                <div className="d-flex flex-column">
                                    <p>Số lượng</p>
                                    <p>Tạm tính</p>
                                    <p>Tax</p>
                                    <h5>Tổng cộng</h5>
                                </div>
                            </Col>
                            <Col md={6}>
                                <div className="d-flex flex-column text-end">
                                    <p>{Object.values(quantities).reduce((acc, quantity) => acc + quantity, 0)} sản phẩm</p>
                                    <p>{convertPrice(totalAmount)}</p>
                                    <p>{convertPrice(totalAmount * 0.1)}</p>
                                    <p>{convertPrice(totalAmount - totalAmount * 0.1)}</p>
                                </div>
                            </Col>
                        </Row>
                    </div>}
                {variants?.data && variants.data.length > 0 &&
                    <div className="p-2  d-flex justify-content-start">
                        <button onClick={() => handleAddInventory()} className="btn-save-all-category p-2" style={{ width: '12%' }} >Hoàn tất</button>
                    </div>}
            </div>
            {isLoading || addInventory && <ModalLoading loading={isLoading || addInventory} />}
        </div>
    );
}

export default ProductImport;