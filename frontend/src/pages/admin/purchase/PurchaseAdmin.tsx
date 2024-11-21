import React, { useRef, useState } from "react";
import { Pagination, Table } from "react-bootstrap";
import { OrderStatus } from "../../../models/order";
import { pageQueryHanlder } from "../../../utils/query-handler";
import { useGetOrdersQuery } from "../../../services/order.service";
import PurchaseItem from "./PurchaseItem";
import ModalLoading from "../../../components/loading/ModalLoading";
import Tab from "../../../components/seller/insert-product/Tab";
import SimpleBar from "simplebar-react";
import Select from 'react-select'

const PurchaseAdmin = () => {

    const [orderStatus, setOrderStatus] = React.useState(OrderStatus.RECEIVED);
    // default order status is pending
    const param = pageQueryHanlder(1, 40, [{
        filed: "orderStatus",
        operator: "=",
        value: orderStatus
    }], [{ field: "createdAt", order: "desc" }])
    const { data: purchase, isLoading, refetch } = useGetOrdersQuery(param);
    console.log(purchase)
    const totalItems = purchase?.data.pageSize || 0; // Tổng số sản phẩm
    const itemsPerPage = 8; // Số sản phẩm mỗi trang
    const totalPages = Math.ceil(totalItems / itemsPerPage); // Tổng số trang
    const [currentPage, setCurrentPage] = useState(1); // Trang hiện tại
    const simplebarRef = useRef<HTMLDivElement | null>(null);
    const [selectedPurchase, setSelectedPurchase] = useState<any>(null);
    const handlePageChange = (page: number) => {
        setCurrentPage(page);
        if (simplebarRef.current) {
            simplebarRef.current.scrollTop = 0; // Đặt lại vị trí thanh cuộn về đầu trang
        }
    };

    // Lấy các sản phẩm cho trang hiện tại
    const startIndex = (currentPage - 1) * itemsPerPage;
    const currentProducts = purchase?.data.items?.slice(startIndex, startIndex + itemsPerPage);
    // define tabs
    const tabs = [
        "Đang chờ xử lý",
        "Đang giao hàng",
        "Đã giao hàng",
        "Đã nhận hàng",
        "Đã hủy"

    ];

    const [activeTab, setActiveTab] = React.useState("Đang chờ xử lý");

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
            case "Đang chờ xử lý":
            default:
                setOrderStatus(OrderStatus.PENDING);
                break;
        }
    }, [activeTab]);



    const handleTabClick = (tabName: string) => {
        setActiveTab(tabName);
    }
    const options = [
        { value: '', label: 'Tất cả' },
        ...purchase?.data.items?.map((option) => ({
            value: option.id,
            label: option.id
        })) || []
    ];
    const filteredPurchase = selectedPurchase?.value === ''
        ? purchase?.data.items
        : selectedPurchase?.value
            ? purchase?.data.items?.filter(purchase => purchase.id === selectedPurchase.value)
            : purchase?.data.items;


    const displayedPurchases = filteredPurchase?.slice(startIndex, startIndex + itemsPerPage);
    return <div className="profile-container">
        <div className="d-flex justify-content-between align-items-center">
            <span className="text-large">Danh sách đơn hàng</span>
        </div>

        <div className="d-flex">
            <Tab tabNames={tabs} activeTab={activeTab} handleButtonClick={handleTabClick} />
            <div className="w-25">
                <Select options={options} placeholder="Lọc theo mã đơn hàng" onChange={setSelectedPurchase} />
            </div>
        </div>
        <SimpleBar style={{ height: 480 }} scrollableNodeProps={{ ref: simplebarRef }}>
            <div className="w-100 d-flex justify-content-center align-items-center flex-1 ">
                {(currentProducts && currentProducts.length > 0) ?
                    <Table className={`table-bordered table-responsive  ${orderStatus === OrderStatus.PENDING ||
                        orderStatus === OrderStatus.SHIPPING || orderStatus === OrderStatus.SHIPPED_CONFIRMATION
                        ? 'custom-table-purchase-ss' : 'custom-table-purchase'}`}>
                        <thead>
                            <tr>
                                <th>Mã đơn hàng</th>
                                <th>Ngày đặt</th>
                                <th>Thanh toán</th>
                                <th>Tổng tiền</th>
                                {orderStatus === OrderStatus.PENDING || orderStatus === OrderStatus.SHIPPING
                                    || orderStatus === OrderStatus.SHIPPED_CONFIRMATION ? <th>Thao tác</th> : <></>}
                                <th></th>
                            </tr>
                        </thead>
                        <tbody>
                            {displayedPurchases?.map((item, idx) => (
                                <PurchaseItem refetch={refetch} item={item} key={idx} />
                            ))}
                        </tbody>
                    </Table> :
                    <span className="text-medium">Không có dữ liệu</span>

                }

            </div>
            {(currentProducts && currentProducts.length > 0) &&
                <div className="d-flex justify-content-center align-items-center">
                    <Pagination className="">
                        <Pagination.First onClick={() => handlePageChange(1)} disabled={currentPage === 1} />
                        <Pagination.Prev onClick={() => handlePageChange(currentPage - 1)} disabled={currentPage === 1} />
                        {[...Array(totalPages)].map((_, index) => (
                            <Pagination.Item key={index + 1} active={index + 1 === currentPage} onClick={() => handlePageChange(index + 1)}>
                                {index + 1}
                            </Pagination.Item>
                        ))}
                        <Pagination.Next onClick={() => handlePageChange(currentPage + 1)} disabled={currentPage === totalPages} />
                        <Pagination.Last onClick={() => handlePageChange(totalPages)} disabled={currentPage === totalPages} />
                    </Pagination>
                </div>}
        </SimpleBar>
        {isLoading && <ModalLoading loading={isLoading} />}
    </div>
}

export default PurchaseAdmin;