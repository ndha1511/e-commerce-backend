import React, { useEffect, useState } from 'react';
import { Container, Row, Col, Form, Button } from 'react-bootstrap';
import CartItem from './CartItem';
import { useGetCartByUserIdQuery } from '../../../services/cart.service';
import { useCheckLoginQuery } from '../../../services/auth.service';
import { convertPrice } from '../../../utils/convert-price';
import useRedirect from '../../../hooks/useRedirect';
import ModalLoading from '../../../components/loading/ModalLoading';
import CartEmpty from './CartEmpty';


const Cart: React.FC = () => {
    const redirect = useRedirect();
    const { data: user, isSuccess: loginSuccess } = useCheckLoginQuery();
    const { data, refetch, isLoading } = useGetCartByUserIdQuery(user?.data?.id || "", {
        skip: !loginSuccess || !user?.data?.id,
    });
    const [isFooterFixed, setIsFooterFixed] = useState(true);
    const [isHeaderFixed, setIsHeaderFixed] = useState(false);
    const [selectVariant, setSelectVariant] = useState<string[]>([]);
    const [checked, setChecked] = useState(false);
    const [totalPrice, setTotalPrice] = useState(0);

    const addSelectVariant = (variantId: string) => {
        if (!selectVariant.includes(variantId)) {
            setSelectVariant([...selectVariant, variantId]);
        }
    }

    const removeSelectVariant = (variantId: string) => {
        const index = selectVariant.indexOf(variantId);
        if (index > -1) {
            setSelectVariant([...selectVariant.slice(0, index), ...selectVariant.slice(index + 1)]);
        }
    }

    const linkToPayment = () => {
        const variant = selectVariant.reduce((acc, variant) => {
            return acc !== '' ? acc + ';' + variant : variant;
        }, '');
        redirect('/payment?select-variant=' + variant + '&from=cart');
    }


    const handleScroll = () => {
        const cartItemsHeight = document.getElementById('cart-items')?.offsetHeight || 0;
        const scrollPosition = window.scrollY + window.innerHeight;

        // Điều kiện để cố định header khi cuộn xuống
        if (window.scrollY > 150) { // Giá trị 150 là tùy chỉnh, bạn có thể điều chỉnh để phù hợp với yêu cầu thực tế
            setIsHeaderFixed(true);
        } else {
            setIsHeaderFixed(false);
        }

        // Điều kiện để di chuyển footer khi cuộn hết danh sách sản phẩm
        if (scrollPosition >= cartItemsHeight + 150) {
            setIsFooterFixed(false);
        } else {
            setIsFooterFixed(true);
        }
    };

    useEffect(() => {
        if (checked) {
            setSelectVariant(data?.data.map(v => v.variantResponse.id) || []);
        } else {
            setSelectVariant([]);
        }
    }, [checked]);

    useEffect(() => {
        let total = 0;
        selectVariant.forEach(variantId => {
            const variant = data?.data.find(v => v.variantResponse.id === variantId);
            if (variant) {
                total += variant.variantResponse.price * variant.quantity;
            }
        });
        setTotalPrice(total);
    }, [selectVariant, data]);

    useEffect(() => {
        window.addEventListener('scroll', handleScroll);
        return () => window.removeEventListener('scroll', handleScroll);
    }, []);
    return (
        <Container className="bg-light p-3 border-radius-small">
            <div className={`cart-header border p-3 bg-white ${isHeaderFixed ? 'fixed-header container' : ''}`}>
                <Row className="align-items-center">
                    <Col md={4} >
                        <div className='d-flex  align-items-center'>
                            <Form.Check type="checkbox" className='checkbox-cart me-4'
                                checked={checked}
                                onChange={() => setChecked(prev => !prev)} />
                            <span style={{ fontSize: 16 }} >Sản Phẩm</span>
                        </div>
                    </Col>

                    <Col md={2} className="text-center">
                        <span>Đơn Giá</span>
                    </Col>
                    <Col md={2} className="text-center">
                        <span>Số Lượng</span>
                    </Col>
                    <Col md={2} className="text-center">
                        <span>Số Tiền</span>
                    </Col>
                    <Col md={2} className="text-center">
                        <span>Thao Tác</span>
                    </Col>
                </Row>
            </div>
            <div id="cart-items" className="">
                {data?.data.length === 0 &&
                    <div className='bg-white d-flex justify-content-center ' style={{ height: 200 }}>
                        <CartEmpty />
                    </div>}


                {
                    data?.data.map((item, index) => (
                        <CartItem
                            refetch={refetch}
                            key={index}
                            item={item}
                            index={index}
                            addVariant={addSelectVariant}
                            removeVariant={removeSelectVariant}
                            selectVariant={selectVariant}

                        />
                    ))
                }
            </div >

            <div className={`p-3 pt-2 border-top bg-white ${isFooterFixed ? 'fixed-footer container' : ''}`}>
                <Row className="align-items-center p-3">
                    <Col xs={6} md={6} className="d-flex align-items-center">
                        <Form.Check type="checkbox" className="checkbox-cart me-4"
                            checked={checked}
                            onChange={() => setChecked(prev => !prev)}
                        />
                        <span className='text-medium'>Chọn Tất Cả ({data?.data.length})</span>
                        <Button variant="link" className="ms-3 primary p-0">Xóa</Button>
                    </Col>
                    <Col xs={12} md={6} className="text-end mt-3 mt-md-0">
                        <div className='d-flex justify-content-end align-items-center w-100'>
                            {selectVariant.length > 0 && <div className="text-medium me-4">
                                Tổng thanh toán ({selectVariant.length} sản phẩm):
                                <span className="primary fw-bold ms-2">{convertPrice(totalPrice)}</span>
                            </div>}
                            <button
                                disabled={selectVariant.length <= 0}
                                className="btn-buy button-flex button-hover background-primary text-large "
                                style={{ border: 'none' }}
                                onClick={linkToPayment}
                            >Thanh toán</button>

                        </div>
                    </Col>
                </Row>
            </div>
            {isLoading && <ModalLoading loading={isLoading} />}
        </Container >
    );
};

export default Cart;
