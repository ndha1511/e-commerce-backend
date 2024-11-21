import { useEffect, useState } from "react";
import { isAbsoluteLocation, isLocation } from "../../utils/location";
import "./menu-fixed.scss";
import { isMobile } from "../../utils/responsive";
import useRedirect from "../../hooks/useRedirect";
import { useGetCartByUserIdQuery } from "../../services/cart.service";
import { useCheckLoginQuery } from "../../services/auth.service";
import { pageQueryHanlder } from "../../utils/query-handler";
import { useGetNotificationsQuery } from "../../services/notification.service";
import NotificationItems from "../../pages/user/notification/NotificationItems";

type Props = {
    fixedSearch: boolean;
}

const MenuFixed = ({ fixedSearch }: Props) => {

    const [showMenu, setShowMenu] = useState<boolean>(true);
    const [showNotification, setShowNotification] = useState<boolean>(false);
    const redirect = useRedirect();
    const { data: user, isSuccess: loginSuccess } = useCheckLoginQuery();
    const { data } = useGetCartByUserIdQuery(user?.data?.id || "", {
        skip: !loginSuccess || !user?.data?.id,
    });
    const paramNotification = pageQueryHanlder(1, 40);
    const { data: dataNotification } = useGetNotificationsQuery({
        id: user?.data?.id || '',
        param: paramNotification,
    },{skip: !loginSuccess});
    console.log(dataNotification);
    console.log(user?.data?.id);
    const unseenCount = dataNotification?.data.items?.filter(item => item.seen === false).length || 0;
    useEffect(() => {
        const container1: HTMLElement | null = document.querySelector('.menu-container');
        const container2: HTMLElement | null = document.querySelector('.menu-container-2');

        const updateHeight = () => {
            if (container1 && container2) {
                container2.style.height = `${container1.offsetHeight}px`;
            }
        };
        const resizeObserver = new ResizeObserver(() => {
            updateHeight();
        });
        if (container1) {
            resizeObserver.observe(container1);
        }
        updateHeight();

        return () => {
            if (container1) {
                resizeObserver.unobserve(container1);
            }
        };
    }, []);

    useEffect(() => {
        const container: HTMLElement | null = document.querySelector('.menu-container');
        const container2: HTMLElement | null = document.querySelector('.menu-container-2');

        if (container && container2) {
            if (showMenu) {
                container.classList.remove('hidden');
                container2.classList.remove('visible');
            } else {
                container.classList.add('hidden');
                container2.classList.add('visible');
            }
        }
    }, [showMenu]);

    return <>
        <div className="menu-container">
            {!isAbsoluteLocation('/') && <div className="menu-item" onClick={() => redirect('/')}>
                <i className="bi bi-shop"></i>
                <span className="text-small">Trang chủ</span>
            </div>}
            {!isLocation('/user') && <div className="menu-item" onClick={() => redirect('/user/account/profile')}>
                <i className="bi bi-person-circle"></i>
                <span className="text-small">Tài khoản</span>
            </div>}
            {!isLocation('/cart') && <div className="menu-item" id="cart-motion-id" onClick={() => redirect('/cart')}>
                <i className="bi bi-bag"></i>
                <span className="text-small">Giỏ hàng</span>
                {data && data?.data.length > 0 &&
                    <span className="badge-item background-primary text-small">{data?.data.length}</span>}
            </div>}
            <div className="menu-item">
                <i className="bi bi-chat-dots"></i>
                <span className="text-small">Tin nhắn</span>
                <span className="badge-item background-primary text-small">2</span>
            </div>
            <div className="menu-item" onClick={() => setShowNotification(!showNotification)}>
                <i className="bi bi-bell"></i>
                <span className="text-small">Thông báo</span>
                {unseenCount !== 0 && <span className="badge-item background-primary text-small">{unseenCount}</span>}
            </div>

            {fixedSearch && <div className="menu-item" onClick={() => window.scrollTo({ top: 0, behavior: 'smooth' })}>
                <i className="bi bi-arrow-up-circle"></i>
                <span className="text-small">Đầu trang</span>
            </div>}

            {isMobile() && <button className="show-menu border text-medium" onClick={() => setShowMenu(false)}>
                <span aria-hidden="true" className="carousel-control-next-icon"></span>
            </button>}
        </div>
        <div className="menu-container-2">
            <button className="show-menu border text-medium" onClick={() => setShowMenu(true)}>
                <span aria-hidden="true" className="carousel-control-prev-icon"></span>
            </button>
        </div>
        {showNotification && (
                <NotificationItems
                    notifications={dataNotification?.data.items || []}
                    isVisible={showNotification}
                    setIsVisible={setShowNotification}
                />
            )}
    </>
}

export default MenuFixed;
