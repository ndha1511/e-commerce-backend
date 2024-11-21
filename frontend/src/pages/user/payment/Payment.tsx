import { Container, Row, Col, Button, Form } from 'react-bootstrap';
import Address from '../../../components/address/Address';
import './payment.scss'
import { BsCheckSquareFill } from 'react-icons/bs';
import SimpleBar from 'simplebar-react';
import 'simplebar-react/dist/simplebar.min.css';
import useGetParam from '../../../hooks/useGetParam';
import { useCheckLoginQuery } from '../../../services/auth.service';
import { useGetCartByUserIdQuery } from '../../../services/cart.service';
import PaymentItem from './PaymentItem';
import { useGetAddressByUserIdQuery, useGetAddressQuery } from '../../../services/address.service';
import { useEffect, useState } from 'react';
import ModalAddress from '../../../components/address/ModalAddress';
import { useCreateOrderMutation, useLazyGetFeeQuery, useLazyGetPaymentQuery } from '../../../services/payment.service';
import { convertPrice } from '../../../utils/convert-price';
import { OrderItem, OrderRequest, PaymentMethod } from '../../../dtos/request/payment/order-request';
import ModalLoading from '../../../components/loading/ModalLoading';
import CreateAddressModal from '../../../components/address/CreateAddressModal';

const Payment = () => {
    const selectVariant = useGetParam('select-variant');
    const variantIds = selectVariant?.split(";") || []; 
    const {data: user, isSuccess: loginSuccess} = useCheckLoginQuery();
    const {data: cart, refetch: cartRefetch} = useGetCartByUserIdQuery(user?.data?.id || "", {
        skip: !loginSuccess || !user?.data?.id,
    });
    const {data: address, refetch: addressRefetch,isSuccess} = useGetAddressByUserIdQuery(user?.data?.id || "", {
        skip: !loginSuccess || !user?.data?.id,
    });
    console.log(address)
    const {data: shopAddress} = useGetAddressQuery();
    const itemViews = cart?.data.filter(item => variantIds.includes(item.variantResponse.id)) || [];
    const [modalAdd, setModalAdd] = useState(false);
    const [getFee] = useLazyGetFeeQuery();
    const [fee, setFee] = useState(0);
    const [amount, setAmount] = useState(0);
    const [selectAddress, setSelectAddress] = useState(0);
    const orderFrom = useGetParam("from");
    const [paymentMethod, setPaymentMethod] = useState<PaymentMethod>(PaymentMethod.COD);
    const [createOrder, {isLoading}] = useCreateOrderMutation();
    const [getUrlPayment] = useLazyGetPaymentQuery();
    useEffect(() => {
        const getPrice = async () => {
            if(address?.data && shopAddress?.data && itemViews.length > 0) {
                const totalPrice = itemViews.reduce((acc, item) => {
                    return acc + (item.variantResponse.price * item.quantity);
                }, 0);
                const weight = itemViews.reduce((acc, item) => {
                    return acc + (item.variantResponse.product.weight * item.quantity)
                }, 0);
                try {
                    const fee = await getFee({
                        pickDistrict: shopAddress.data.district,
                        pickProvince: shopAddress.data.province,
                        district: address.data[selectAddress].district,
                        province: address.data[selectAddress].province,
                        weight: weight,
                        value: totalPrice
                    }).unwrap();
                    setFee(fee.data.fee);
                } catch (error) {
                    console.log(error);
                }
                
                setAmount(totalPrice);
            }
        }
        getPrice();
    }, [address, shopAddress, cart])

    const handlerOrder = async () => {
        const orderItem: OrderItem[] = itemViews.map(i => {
            return {
                variantId: i.variantResponse.id,
                quantity: i.quantity
            }
        });
        if(address && address.data?.[selectAddress]) {
            const orderRequest: OrderRequest = {
                userId: user?.data?.id || "",
                userAddress: address?.data[selectAddress],
                orderItems: orderItem,
                paymentMethod: paymentMethod,
                note: "",
                orderFrom: orderFrom || "",
                deliveryFee: fee
            }
            try {
                const response = await createOrder(orderRequest).unwrap();
                const newOrder = response.data;
                cartRefetch();
                if(newOrder.paymentMethod === PaymentMethod.ATM) {
                    const responsePayment = await getUrlPayment({
                        orderId: newOrder.id,
                        amount: newOrder.finalAmount
                    }).unwrap();
                    location.href = responsePayment.data;
                }
            } catch (error) {
                console.log(error);
                
            }
        }

    }
    useEffect(() => {
        if (isSuccess && address?.data) {
            const index = address?.data.findIndex(addr => addr.addressDefault);
            if (index !== -1) {
               setSelectAddress(index);
            }
        }
    }, [isSuccess, address]);
    return (
        <Container className="p-2 bg-light border rounded ">
            <Row className="">
                <Col md={8} className='border'>
                    {(address?.data && address?.data.length > 0) ? <Address info={address.data[selectAddress]} /> :
                        <div className="p-3 bg-white ">
                            <button onClick={() => setModalAdd(true)} className='button-flex button-hover background-primary'>Thêm địa chỉ nhận hàng</button>
                        </div>
                    }
                    <div className=' p-3 '>
                        <Row className="bg-white border border-radius-small">
                            {/* Lời nhắn */}
                            <Col md={6} className='border p-2'>
                                <Form.Group className="mb-3">
                                    <Form.Label className="fw-bold">Lời nhắn:</Form.Label>
                                    <Form.Control
                                        as="textarea"
                                        placeholder="Lưu ý cho Người bán..."
                                        rows={1} // Đặt số dòng ban đầu
                                    />
                                </Form.Group>
                            </Col>
                            <Col md={6} className='border p-2'>
                                <div className='mb-3'>
                                    <span className="fw-bold ">Thông tin vận chuyển:</span>
                                </div>
                                <div className='d-flex flex-column gap-2'>
                                    <span>Gửi từ: {`${shopAddress?.data.addressDetail}, 
                                    ${shopAddress?.data.ward}, ${shopAddress?.data.district}, ${shopAddress?.data.province}`}</span>
                                    <span>Đơn vị vận chuyển: Giao hàng tiết kiệm</span>
                                    <span>Phí vận chuyển: {convertPrice(fee)} (bao gồm cả phí bảo hiểm đơn hàng)</span>
                                </div>
                            
                            </Col>
                        </Row>
                    </div>
                    <div className=' p-3 bg-white border border-radius-small '>
                        <div>
                            <span>Phương thức thanh toán: </span>
                            <div className='d-flex mt-2'>
                                <Button className='payment-button me-3' 
                                    active={paymentMethod === PaymentMethod.COD ? true: false}
                                    onClick={() => setPaymentMethod(PaymentMethod.COD)}
                                 >
                                    Thanh toán khi nhận hàng
                                </Button>
                                <Button className='payment-button'
                                     active={paymentMethod === PaymentMethod.ATM ? true: false}
                                     onClick={() => setPaymentMethod(PaymentMethod.ATM)}
                                >
                                    Chuyển khoản ngân hàng
                                </Button>
                            </div>
                        </div>
                    </div>
                </Col>
                <Col md={4} className='border'>
                    <div className='p-3 bg-white border border-radius-small '>
                        <div className="d-flex align-items-center mb-2 pb-3 border-bottom">
                            <BsCheckSquareFill className="text-primary me-2" />
                            <h6 className="mb-0">Thông tin đơn hàng</h6>
                        </div>
                        <SimpleBar className='order-payment'>
                            <div>     
                                {itemViews.map((item, index) => (
                                   <PaymentItem key={index} item={item}/>
                                ))}</div>
                        </SimpleBar >
                        <div className="mb-2">
                            <span className="text-muted">Voucher ưu đãi của sàn: </span>
                            <span className="primary fw-bold ms-2">15k</span>
                            <span className="info fw-bold ms-2 btn-pointer">Chọn voucher khác</span>
                        </div>
                        <div className="p-3 bg-white border rounded">
                            <Row className="mb-2">
                                <Col xs={6} className="text-muted">Tổng tiền hàng</Col>
                                <Col xs={6} className="text-end">{convertPrice(amount)}</Col>
                            </Row>
                            <Row className="mb-2">
                                <Col xs={6} className="text-muted">Phí vận chuyển</Col>
                                <Col xs={6} className="text-end">{convertPrice(fee)}</Col>
                            </Row>
                            <Row className="mb-2">
                                <Col xs={6} className="text-muted">Tổng cộng Voucher giảm giá:</Col>
                                <Col xs={6} className="text-end">0</Col>
                            </Row>
                            <hr />
                            <Row className="mb-3">
                                <Col xs={6} className="fw-bold">Tổng thanh toán</Col>
                                <Col xs={6} className="text-end primary" style={{ fontSize: '1.5rem' }}>{convertPrice(amount + fee)}</Col>
                            </Row>
                            <button 
                                onClick={handlerOrder}
                                className="w-100 button-flex button-hover background-primary text-large " style={{ border: 'none' }}>Đặt hàng</button>
                        </div>
                    </div>
                </Col>
            </Row>
            {modalAdd && <CreateAddressModal  show={modalAdd} handleClose={() => setModalAdd(false)} refetch={addressRefetch}/>}  
            {isLoading && <ModalLoading loading={isLoading}/>}                  
        </Container>
    );
};

export default Payment;
