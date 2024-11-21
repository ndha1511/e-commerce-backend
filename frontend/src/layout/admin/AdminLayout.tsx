import { ReactNode, useEffect } from "react";
import './admin-layout.scss';
import Header from "./header/Header";
import Navbar from "./navbar/Navbar";
import NotificationButton from "../../components/notify/NotificationButton";
import { useDispatch, useSelector } from "react-redux";
import { RootState } from "../../rtk/store/store";
import { clearNotify } from "../../rtk/slice/notify-slice";

const AdminLayout = ({ children }: { children: ReactNode }) => {
    const notify = useSelector((state:RootState)=>state.notification)
    const dispatch = useDispatch();
    useEffect(() => {
        const timer = setTimeout(() => {
            dispatch(clearNotify())
        }, 3000);

        // Dọn dẹp timeout khi component bị hủy hoặc message thay đổi
        return () => clearTimeout(timer);
    }, [dispatch, notify]);
    return <div className="admin-layout">
        <div className="navbar-admin">
            <Navbar></Navbar>
        </div>
        <div className="admin-layout-left">
            <div className="header-admin">
                <Header></Header>
            </div>
            <div className="content-navbar-admin">{children}</div>           
        </div>
        {notify.type && <NotificationButton type={notify.type} message={notify.message} />}
    </div>
}

export default AdminLayout;