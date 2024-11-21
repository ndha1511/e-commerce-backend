import Avatar from '../../../components/avatar/Avatar';
import Dropdown from '../../../components/dropdown/Dropdown';
import useRedirect from '../../../hooks/useRedirect';
import { useCheckLoginQuery, useLazyLogoutQuery } from '../../../services/auth.service';
import { isAbsoluteLocation } from '../../../utils/location';
import './side-bar.scss';


const Account = ({username, avatar} : {username: string, avatar?: string}) => {
    const redirect = useRedirect();
    const [logoutHandler] = useLazyLogoutQuery();

    const dropDownItems = [
        {
            item: <span className='side-bar-item'>Thông tin tài khoản</span>,
            event: () => redirect('/user/account/profile')
        },
        {
            item: <span className='side-bar-item'>Đơn hàng đã mua</span>,
            event: () => redirect('/user/purchase')
        },
        {
            item: <span className='side-bar-item'>Đăng xuất</span>,
            event: async () => {
                try {
                    await logoutHandler().unwrap();
                    window.location.reload();
                } catch (error) {
                    console.log(error);
                }
            }
        }
    ]
    return <>
        <Dropdown dropDownItem={dropDownItems}>
            <div className='side-bar-item'>
                <Avatar name={username} url={avatar}/>
            </div>
        </Dropdown>
    </>
}

const SideBar = () => {
    const redirect = useRedirect();
    const { data } = useCheckLoginQuery();
    return <div className='side-bar-container'>
        <div className='container side-bar-container'>
            <div className={`side-bar-content`}>
                <span className={`side-bar-item ${isAbsoluteLocation('/') ? 'primary' : ''}`}
                    onClick={() => redirect('/')}
                >
                    <i className="bi bi-house-fill"></i>
                    Trang chủ
                </span>
                <span className='side-bar-item'>
                    Kênh người bán
                </span>
            </div>
            <div className='side-bar-content'>
                <span className='side-bar-item'>
                    <i className="bi bi-bell-fill"></i>
                    Thông báo
                </span>
                <span className={`side-bar-item ${isAbsoluteLocation('/cart') ? 'primary' : ''}`}
                    onClick={() => redirect('/cart')}
                >
                    <i className="bi bi-cart-fill"></i>
                    Giỏ hàng
                </span>
                {data?.data ? <Account username={data.data.username}/> : <>
                    <span className='side-bar-item' onClick={() => redirect('/auth/login')}>Đăng nhập</span>
                    <span className='side-bar-item' onClick={() => redirect('/auth/register')}>Đăng ký</span>
                </>}
            </div>
        </div>
    </div>
}

export default SideBar;