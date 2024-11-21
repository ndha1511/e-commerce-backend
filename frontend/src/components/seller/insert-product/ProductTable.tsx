
import { useEffect, useState } from 'react';
import { Table } from 'react-bootstrap';
import { ProductAttributeDto } from '../../../dtos/request/product/product-attribute.reques';
import { VariantDto } from '../../../dtos/request/product/variant.reques';
import { useDispatch, useSelector } from 'react-redux';
import { RootState } from '../../../rtk/store/store';
import './product-table.scss'
import { setVariantDto } from '../../../rtk/slice/product-slice';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faImage } from '@fortawesome/free-solid-svg-icons';
interface GroupedDataItem {
    attributeValue1: number;
    attributeValue2: number;
    price: number;
    quantity: number;
}
const ProductTable = () => {
    const [groupedData, setGroupedData] = useState<{ [key: string]: GroupedDataItem[] }>({});
    const productAttributeDto: ProductAttributeDto[] = useSelector((state: RootState) => state.product.attributesDto);
    const variants: VariantDto[] = useSelector((state: RootState) => state.product.variantsDto);
    const dispatch = useDispatch();
    useEffect(() => {
        const groupedVariants = variants?.reduce((acc, curr) => {
            const key = curr.attributeValue1?.toString(); // Đảm bảo key là chuỗi
            // nhóm theo attributeValue1
            if (!acc[key]) {
                acc[key] = [];
            }
            acc[key].push({
                attributeValue1: curr.attributeValue1 || 0, // Thêm attributeValue1 vào đối tượng
                attributeValue2: curr.attributeValue2 || 0,
                price: curr.price || 0,
                quantity: curr.quantity || 0,
            });
            return acc;
        }, {} as { [key: string]: GroupedDataItem[] });

        setGroupedData(groupedVariants);
    }, [variants]);
    const handleChange = (attrVal1: string, attrVal2: number, field: 'price' | 'quantity', value: number) => {
        const newVariant = [...variants];
        const someValue: number = Number(attrVal1);
        const itemIndex = newVariant.findIndex((vart) => {
            if (attrVal2) {
                return vart.attributeValue1 === someValue && vart.attributeValue2 === attrVal2;
            }
            return vart.attributeValue1 === someValue;
        });
        console.log(itemIndex)
        if (itemIndex !== -1) {
            const updatedItem = { ...newVariant[itemIndex], [field]: value };
            console.log(updatedItem)
            newVariant[itemIndex] = updatedItem;
            dispatch(setVariantDto(newVariant));
        }
    };
    useEffect(() => {
        // console.log(variants)
        // console.log(productAttributeDto)
    }, [variants])
    return (
        <div className=" mt-3">
            <Table className='table-bordered table-responsive  custom-table' >
                <thead >
                    <tr className='text-center' >
                        <th>{productAttributeDto?.[0]?.attributeName ? productAttributeDto[0]?.attributeName : 'Phân loại 1'}</th>
                        <th>{productAttributeDto?.[1]?.attributeName ? productAttributeDto[1]?.attributeName : 'Phân loại 2'}</th>
                        <th>Giá bán</th>
                        <th>Phân loại SKU</th>
                    </tr>
                </thead>
                <tbody>

                    {groupedData&& Object.entries(groupedData).map(([attrVal1, items]) => {
                        return (
                            <tr key={attrVal1} className='text-center'>
                                <td >
                                    {productAttributeDto[0]?.attributeValues
                                        ?.filter(attr => attrVal1 === String(attr.id) && attr.value !== '')
                                        .map(attr => (
                                            <div className='attr' key={attr.id} style={{ border: 'none' }}>
                                                {productAttributeDto[0]?.attributeValues?.filter(attr => attr.id === Number(attrVal1))
                                                    .map((item) => (
                                                        <div key={item.id}>
                                                            <div>{attr.value}</div>
                                                            {item.image &&
                                                                <div key={item.id} className='mt-1'>
                                                                    <img
                                                                        src={attr.image}
                                                                        className="border-radius-small"
                                                                        alt=''
                                                                        style={{ width: '60px', height: '60px' }}
                                                                    />
                                                                </div>}
                                                            {!item.image &&
                                                                <label className="d-flex align-items-center primary mt-1">
                                                                    <div className="image-color-cs p-2">
                                                                        <div className="icon-image-insert">
                                                                            <FontAwesomeIcon icon={faImage} fontSize={30} />
                                                                            {/* <FontAwesomeIcon className="icon-plus-image" icon={faPlus} /> */}
                                                                        </div>
                                                                    </div>
                                                                </label>}
                                                        </div>
                                                    ))}

                                            </div>
                                        ))}
                                </td>


                                <td >
                                    {items.map((item) => (
                                        <div key={item.attributeValue2} className="attr">
                                            {productAttributeDto[1]?.attributeValues?.map((attr) => (
                                                item.attributeValue2 === attr.id ? attr.value : null
                                            ))}

                                        </div>
                                    ))}
                                </td>
                                <td>
                                    {items.map((item) => (
                                        <div key={item.attributeValue2} className="attr">
                                            <div className="select-search-table-info">
                                                <div
                                                    className="p-1 pe-2"
                                                >
                                                    <span>₫</span>
                                                </div>
                                                <input
                                                    className="select-table-info"
                                                    style={{ padding: 0, paddingLeft: 10 }}
                                                    placeholder="Giá nhập"
                                                    type="text"
                                                    value={item.price}
                                                    onChange={(e) => handleChange(attrVal1, item.attributeValue2, 'price', Number(e.target.value))}
                                                />
                                            </div>

                                        </div>

                                    ))}
                                </td>
                              
                                <td>
                                    {items.map((item) => (
                                        <div key={item.attributeValue2} className="attr">
                                            <div className="select-search-table-info">
                                                <input
                                                    className="select-table-info"
                                                    style={{ padding: 5, paddingLeft: 10 }}
                                                    placeholder="Kho hàng"
                                                    type="text"
                                                    value={item.quantity}
                                                    onChange={(e) => handleChange(attrVal1, item.attributeValue2, 'quantity', Number(e.target.value))}

                                                />
                                            </div>
                                        </div>
                                    ))}
                                </td>
                            </tr>

                        );
                    })}
                </tbody>

            </Table>
        </div>
    );
};

export default ProductTable;
