import { Collapse, Table } from "react-bootstrap";

import '..//../user/account/purchase/purchase.scss'
import { ProductOrder } from "../../../models/product-order";
import React from "react";
import { convertPrice } from "../../../utils/convert-price";
import ModalLoading from "../../../components/loading/ModalLoading";
import { Order, OrderStatus, PaymentStatus } from "../../../models/order";
import { PaymentMethod } from "../../../dtos/request/payment/order-request";
import { useLazyGetPaymentQuery } from "../../../services/payment.service";
import { useConfirmActionMutation, useConfirmReceivedMutation } from "../../../services/order.service";
import { useDispatch } from "react-redux";
import { setNotify } from "../../../rtk/slice/notify-slice";

const ProductItem = ({ product, orderId, item }: { product: ProductOrder, orderId: string, item: Order }) => {
    const [showFormComment, setShowFormComment] = React.useState(false);
    const handleClose = () => {
        setShowFormComment(false);
    }
    return <>
        <tr>
            <td>
                <img src={product.image} width={100} height={100} />
            </td>
            <td>
                <span>{product.productName}</span>
            </td>
            <td>
                <div>{product.attributes.map((attr, idx) =>
                (
                    <div key={idx}>
                        <span>{attr}</span>
                    </div>
                ))}</div>
            </td>
            <td>{convertPrice(product.price)}</td>
            <td>{product.quantity}</td>
            <td>{convertPrice(product.amount)}</td>
            {item.orderStatus === OrderStatus.RECEIVED && (
                (product.allowComment && !product.commented) && (
                    <td>
                        <button className="inActiveProduct" onClick={() => setShowFormComment(true)}>
                            Xem đánh giá sản phẩm
                        </button>
                    </td>
                )
            )}
        </tr>
    </>


}

const PurchaseItem = ({ item, refetch }: { item: Order, refetch: () => void }) => {
    const [trigger] = useConfirmActionMutation();
    const [getUrlPayment] = useLazyGetPaymentQuery();
    const [open, setOpen] = React.useState(false);
    const [confirmReceived, { isLoading: confirmLoading }] = useConfirmReceivedMutation();
    const dispatch = useDispatch();
    const handlerPayment = async () => {
        try {
            const result = await getUrlPayment({
                orderId: item.id,
                amount: item.finalAmount
            }).unwrap();
            location.href = result.data;
        } catch (error) {
            console.log(error);
        }
    }
    const updatePurchase = async (orderStatus: OrderStatus) => {
        try {
            switch (orderStatus) {
                case OrderStatus.RECEIVED:
                    await confirmReceived(item.id);
                    refetch();
                    break;
                default:
                    break;
            }
        } catch (error) {
            console.log(error);
        }
    }
    const handleConfirm = async (action: 'shipping' | 'cancel' | 'shipped-confirmation' | 'received') => {
        try {
            await trigger({
                orderId: item.id,
                action: action
            }).unwrap();
            refetch();
            dispatch(setNotify({
                type: 'success', message: 'Thao tác thành công'
            }))
        } catch (error) {
            console.log(error);
            dispatch(setNotify({
                type: 'error', message: 'Thao tác không thành công'
            }))
        }
    }
    return <>
        <tr className="">
            <td className="p-3">{item.id}</td>
            <td>{new Date(item.createdAt).toLocaleString()}</td>
            <td>
                {item.paymentMethod === PaymentMethod.COD ? "Thanh toán khi nhận hàng" :
                    item.paymentStatus === PaymentStatus.UNPAID ? <button onClick={handlerPayment}>Thanh toán</button> : "Đã thanh toán"}
            </td>
            <td>{convertPrice(item.finalAmount)}</td>
            {item.orderStatus === OrderStatus.PENDING || item.orderStatus === OrderStatus.SHIPPED_CONFIRMATION || item.orderStatus === OrderStatus.SHIPPING ?
                <td>
                    {(item.orderStatus === OrderStatus.PENDING) && <button className="inActiveProduct" onClick={() => handleConfirm('shipping')}>Xác nhận </button>}
                    {(item.orderStatus === OrderStatus.SHIPPING) && <button className="inActiveProduct" onClick={() => handleConfirm('shipped-confirmation')}>Xác nhận </button>}
                    {(item.orderStatus === OrderStatus.SHIPPED_CONFIRMATION) && <button className="inActiveProduct" onClick={() => updatePurchase(OrderStatus.RECEIVED)}>Xác nhận</button>}
                </td> : <></>
            }

            <td>
                <div className="d-flex justify-content-center">
                    <div onClick={() => setOpen(prev => !prev)}>
                        {open ? <i className="bi bi-chevron-up btn-pointer"></i> : <i className="bi bi-chevron-down btn-pointer"></i>}
                    </div>

                </div>
            </td>

        </tr>
        <tr>
            <td colSpan={7} >
                <Collapse in={open}>
                    <div className="p-3">
                        <Table className="mb-0 table-bordered table-responsive custom-table-purchase-cs" >
                            <thead>
                                <tr>
                                    <th>Hình ảnh</th>
                                    <th>Tên sản phẩm</th>
                                    <th>Phân loại</th>
                                    <th>Đơn giá</th>
                                    <th>Số lượng</th>
                                    <th>Thành tiền</th>
                                    {(item.orderStatus === OrderStatus.RECEIVED) && <th></th>}
                                </tr>
                            </thead>
                            <tbody>
                                {item.productOrders.map((p, idx) => (
                                    <ProductItem item={item} orderId={item.id} product={p} key={idx} />
                                ))}
                                <tr >
                                    <td colSpan={item.orderStatus !== OrderStatus.RECEIVED ? 5 : 6} style={{ border: 'none' }}></td>
                                    <td colSpan={1} style={{ width: '20%', border: 'none' }} >
                                        <div className="d-flex gap-2">
                                            <div className=" text-start">
                                                <p>Tổng tiền:</p>
                                                <p>Phí vận chuyển:</p>
                                                <p>Giảm giá:</p>
                                                <p>Số tiền phải trả:</p>
                                            </div>
                                            <div className="text-start">
                                                <p> {convertPrice(item.totalAmount)}</p>
                                                <p> {convertPrice(item.shippingAmount)}</p>
                                                <p>{convertPrice(item.voucherAmount)}</p>
                                                <p> {convertPrice(item.finalAmount)}</p>
                                            </div>
                                        </div>

                                    </td>
                                </tr>
                            </tbody>
                        </Table>

                    </div>

                </Collapse>
            </td>
        </tr>

        {confirmLoading && <ModalLoading loading={confirmLoading} />}
    </>
}

export default PurchaseItem;