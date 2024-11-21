import React from "react";
import { OrderStatus } from "../../../../models/order";
import { useCheckLoginQuery } from "../../../../services/auth.service";
import { useGetOrdersByUserIdQuery } from "../../../../services/order.service";
import { pageQueryHanlder } from "../../../../utils/query-handler";
import Tab from "../../../../components/seller/insert-product/Tab";
import ModalLoading from "../../../../components/loading/ModalLoading";
import PurchaseItem from "./PurchaseItem";
import { Table } from "react-bootstrap";

const Purchase = () => {
    const { data: user } = useCheckLoginQuery();

    const [orderStatus, setOrderStatus] = React.useState(OrderStatus.RECEIVED);

    // default order status is pending
    const param = pageQueryHanlder(1, 40, [{
        filed: "orderStatus",
        operator: "=",
        value: orderStatus
    }], [{ field: "createdAt", order: "desc" }])

    const [paramState, setParamState] = React.useState(param);

    const { data: purchase, isLoading, refetch } = useGetOrdersByUserIdQuery(
        {
            userId: user?.data?.id || "",
            param: paramState
        }, {
        skip: !user?.data?.id
    });

    // define tabs
    const tabs = [
        "Đang chờ xử lý",
        "Đang giao hàng",
        "Đã giao hàng",
        "Đã nhận hàng",
        "Đã hủy"

    ];

    const [activeTab, setActiveTab] = React.useState("Đã nhận hàng");

    React.useEffect(() => {
        switch (activeTab) {
            case "Đang giao hàng":
                setOrderStatus(OrderStatus.SHIPPING);
                break;
            case "Đã giao hàng":
                setOrderStatus(OrderStatus.SHIPPED_CONFIRMATION);
                break;
            case "Đã nhận hàng":
                setOrderStatus(OrderStatus.RECEIVED);
                break;
            case "Đã hủy":
                setOrderStatus(OrderStatus.CANCELLED);
                break;
            default:
                setOrderStatus(OrderStatus.PENDING);
                break;
        }
    }, [activeTab]);

    React.useEffect(() => {
        setParamState(param);
    }, [param]);

    const handleTabClick = (tabName: string) => {
        setActiveTab(tabName);
    }

    return <div className="profile-container">
        <div className="d-flex justify-content-between align-items-center">
            <span className="text-large">Đơn hàng đã mua</span>
        </div>
        <Tab tabNames={tabs} activeTab={activeTab} handleButtonClick={handleTabClick} />
        <div className="w-100 d-flex justify-content-center align-items-center flex-1">
            {(purchase?.data?.items && purchase?.data?.items.length > 0) ?
                <Table className={`table-bordered table-responsive  ${orderStatus === OrderStatus.PENDING ? 'custom-table-purchase-ss' : 'custom-table-purchase'}`}>
                    <thead>
                        <tr>
                            <th>Mã đơn hàng</th>
                            <th>Ngày đặt</th>
                            <th>Thanh toán</th>
                            <th>Tổng tiền</th>
                            {orderStatus === OrderStatus.PENDING ?   <th>Thao tác</th> : <></>} 
                            <th></th>
                        </tr>
                    </thead>
                    <tbody>
                        {purchase?.data?.items.map((item, idx) => (
                           <PurchaseItem refetch={refetch} item={item} key={idx}/>
                        ))}
                    </tbody>
                </Table> :
                <span className="text-medium">Không có dữ liệu</span>
            }
        </div>
        {isLoading && <ModalLoading loading={isLoading} />}
    </div>
}

export default Purchase;