import { Col, Image, Row } from "react-bootstrap";
import { ProductCartResponse } from "../../../dtos/response/cart/product-cart-response";
import { convertPrice } from "../../../utils/convert-price";


const PaymentItem = ({item} : {item: ProductCartResponse}) => {
    return (
        <div className="border-bottom mb-3 pb-3 order-items">
            <Row>
                <Col xs={2}>
                    <Image 
                    src={item.variantResponse.image || item.variantResponse.product.thumbnail} fluid />
                </Col>
                <Col xs={7}>
                    <p className="mb-1">{item.variantResponse.product.productName}</p>
                    <div className='d-flex'>
                        <div className="primary fw-bold me-2">{convertPrice(item.variantResponse.price)}</div>
                        {/* <div className="text-muted text-decoration-line-through">{item.variantResponse.price}</div> */}
                    </div>
                </Col>
                <Col xs={3} className="text-end">
                    <div>x {item.quantity}</div>
                </Col>
            </Row>
            <div className="mt-2">
                <span style={{ borderRight: '1px black' }} className='p-2'>{item.variantResponse.attributeValue1}</span>
                {item.variantResponse.attributeValue2 && <span className='p-2'>{item.variantResponse.attributeValue2}</span>}
            </div>
        </div>
    )
}

export default PaymentItem;