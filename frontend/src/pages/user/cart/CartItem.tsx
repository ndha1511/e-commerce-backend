import React, {useState } from 'react';
import { Row, Col, Form, Button, Dropdown, Image } from 'react-bootstrap';
import './cart-item.scss';
import { ProductCartResponse } from '../../../dtos/response/cart/product-cart-response';
import { convertPrice } from '../../../utils/convert-price';
import { Link } from 'react-router-dom';
import { useDeleteCartItemMutation, useUpdateCartMutation } from '../../../services/cart.service';
import { UpdateCartRequest } from '../../../dtos/request/cart/update-cart-request';
import { useCheckLoginQuery } from '../../../services/auth.service';
import ToggleAttribute from './ToggleAttribute';
import { useLazyGetVariantsQuery } from '../../../services/variant.service';

interface CartItemProps {
    item: ProductCartResponse;
    refetch: () => void;
    index: number;
    addVariant: (variantId: string) => void;
    removeVariant: (variantId: string) => void;
    selectVariant: string[]
}

const CartItem: React.FC<CartItemProps> = ({ item, refetch, index, addVariant, removeVariant, selectVariant }) => {
    const {data: user} = useCheckLoginQuery();
    const [showOptions, setShowOptions] = useState(false);
    const quantityInStock = item.variantResponse.quantity;
    const [getVariant] = useLazyGetVariantsQuery();
    const [trigger] = useUpdateCartMutation();
    const [deleteItem] = useDeleteCartItemMutation();
    const checked = selectVariant.findIndex(v => v === item.variantResponse.id) !== -1;


    const toggleOptions = () => {
        setShowOptions(!showOptions)
    };

    const increment = async () => {
        if (item.quantity < quantityInStock) {
            await updateCart({
                userId: user?.data?.id || '',
                index: index,
                productUpdate: {
                    variantId: item.variantResponse.id,
                    quantity: item.quantity + 1
                }
            });
        }
    }

    const decrement = async () => {
        if (item.quantity > 1) {
            await updateCart({
                userId: user?.data?.id || '',
                index: index,
                productUpdate: {
                    variantId: item.variantResponse.id,
                    quantity: item.quantity - 1
                }
            });
        }
    }

    const updateAttribute = async (attr1: string, attr2?: string) => {
        try {
            const rs = await getVariant({
                productId: item.variantResponse.product.id,
                attr1: attr1, 
                attr2: attr2
            }).unwrap();
            const variant = rs.data;
            await updateCart({
                userId: user?.data?.id || '',
                index: index,
                productUpdate: {
                    variantId: variant.id,
                    quantity: 1
                }
            });
           
        } catch (error) {
            console.log(error);
        }
    }

    const updateCart = async (newItem: UpdateCartRequest) => {
        try {
            await trigger(newItem).unwrap();
            refetch();
        } catch (error) {
            console.log(error);
        }
    }

    const handleCheck = () => {
        if(checked) {
            removeVariant(item.variantResponse.id)
        } else {
            addVariant(item.variantResponse.id)
        }
    }

    const handleDelete = async () => {
        try {
            await deleteItem({
                userId: user?.data?.id || '',
                itemId: item.variantResponse.id
            });
            refetch();
        } catch (error) {
            console.log(error);
        }
    }

    return (
        <div>
            <div className="cart-item border-bottom p-3 pl-3 pt-5 pr-3 pb-5 bg-white">
                <Row className="align-items-center">
                    <Col xs={12} md={4} className="d-flex align-items-center mb-3 mb-md-0">
                        <Form.Check type="checkbox" className="checkbox-cart me-3" checked={checked} 
                            onChange={handleCheck}
                        />
                        <div className="d-flex align-items-center">
                            <Image src={item.variantResponse.image || item.variantResponse.product.thumbnail} fluid className="cart-item-image me-3" />
                            <div className="w-100">
                                <Link to={'/product/' + item.variantResponse.product.urlPath}>
                                    <p>
                                        {item.variantResponse.product.productName.split(" ").slice(0, 10).join(" ") + (item.variantResponse.product.productName.split(" ").length > 10 ? "..." : "")}
                                    </p>
                                </Link>
                                <div className="variant-selector" style={{ position: 'relative', cursor: 'pointer' }}>
                                    <div className="selected-variant text-muted" onClick={toggleOptions}>
                                        <span>Phân Loại Hàng:</span>
                                        <i
                                            className={`bi ${showOptions ? 'bi-caret-up-fill' : 'bi-caret-down-fill'} ms-2`}
                                            style={{ transition: 'transform 0.3s ease-in-out' }}
                                        ></i>
                                        <br />
                                        <span>{item.variantResponse.attributeValue1} {item.variantResponse.attributeValue2}</span>

                                    </div>

                                    {showOptions && <ToggleAttribute updateAttribute={updateAttribute} close={toggleOptions} productId={item.variantResponse.product.id} 
                                    attributeValue1={item.variantResponse.attributeValue1} attributeValue2={item.variantResponse.attributeValue2}/>}
                                </div>
                            </div>
                        </div>
                    </Col>

                    <Col xs={6} md={2} className="text-center mb-3 mb-md-0">
                       {item.promotion ? <>
                        <p className="text-muted mb-1"><s>{convertPrice(item.variantResponse.price)}</s></p>
                        <p className="primary fw-bold mb-1">{convertPrice(item.variantResponse.price)}</p>
                       </> : <p className="primary fw-bold mb-1">{convertPrice(item.variantResponse.price)}</p> }
                    </Col>
                    <Col xs={6} md={2} className="text-center mb-3 mb-md-0">
                        <div className="d-flex justify-content-center align-items-center mb-2">
                            <Button variant="light" size="sm" onClick={decrement}>-</Button>
                            <span className="px-3">{item.quantity}</span>
                            <Button variant="light" size="sm" onClick={increment}>+</Button>
                        </div>
                        <div className="d-flex justify-content-center align-items-center">
                            Kho hàng: {quantityInStock > 0 ? quantityInStock : 'Hết hàng'}
                        </div>
                    </Col>
                    <Col xs={6} md={2} className="text-center mb-3 mb-md-0">
                        <p className="primary fw-bold mb-1">{convertPrice(item.variantResponse.price * item.quantity)}</p>
                    </Col>
                    <Col xs={6} md={2} className="text-end d-flex justify-content-center align-items-center flex-column">
                        <Button variant="link" className="primary p-0" onClick={handleDelete}>Xóa</Button>
                        <Dropdown align="end">
                            <Dropdown.Toggle variant="link" className="text-muted p-0">
                                Tìm sản phẩm tương tự
                            </Dropdown.Toggle>
                            <Dropdown.Menu>
                                <Dropdown.Item href="#">Sản phẩm tương tự 1</Dropdown.Item>
                                <Dropdown.Item href="#">Sản phẩm tương tự 2</Dropdown.Item>
                            </Dropdown.Menu>
                        </Dropdown>
                    </Col>
                </Row>
            </div>
        </div>
    );
};

export default CartItem;
