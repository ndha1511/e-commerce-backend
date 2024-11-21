import React, { ReactNode } from 'react';
import './navbar.scss';
import { Col, Collapse, Row } from 'react-bootstrap';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faChevronDown, faChevronRight } from '@fortawesome/free-solid-svg-icons';
import useRedirect from '../../../hooks/useRedirect';

interface Menu {
    name: string;
    path?: string;
    icon?: ReactNode;
    children?: Menu[];
    fontSize?: number;
}

const menu: Menu[] = [
    {
        name: 'Dashboard',
        path: '/admin',
        icon: <i className="bi bi-house-door"></i>
    },
    {
        name: 'QL sản phẩm',
        icon: <i className="bi bi-box-seam"></i>,
        children: [
            {
                name: '● Danh sách sản phẩm',
                path: '/admin/products',
                fontSize: 13
            },
            {
                name: '● Thêm sản phẩm',
                path: '/admin/product/create',
                fontSize: 13
            },
            {
                name: '● Kho sản phẩm',
                path: '/admin/product/stock',
                fontSize: 13
            },
            {
                name: '● Nhập hàng',
                path: '/admin/product/import',
                fontSize: 13
            },
            {
                name: '● Thêm sản phẩm hàng loạt',
                path: '/admin/product/batch',
                fontSize: 13
            },
        ]
    },
    {
        name: 'QL danh mục',
        path: '/admin',
        icon: <i className="bi bi-grid"></i>,
        children: [
            {
                name: '● Danh sách danh mục',
                path: '/admin/categories',
                fontSize: 13
            },
        ]
    },
    {
        name: 'QL thương hiệu',
        path: '/admin',
        icon: <i className="bi bi-patch-check"></i>,
        children: [
            {
                name: '● Thêm thương hiệu',
                path: '/admin/brand',
                fontSize: 13
            },
            {
                name: '● Danh sách thương hiệu',
                path: '/admin/brands',
                fontSize: 13
            },
        ]

    },
    {
        name: 'QL đơn hàng',
        path: '/admin/purchase',
        icon: <i className="bi bi-clipboard-check"></i>
    },
    {
        name: 'QL nhân viên',
        path: '/admin/employee',
        icon: <i className="bi bi-person"></i>,
        children: [
            {
                name: 'Danh sách nhân viên',
                path: '/admin/employees',
                fontSize: 13
            },
            {
                name: 'Tạo tài khoản nhân viên',
                path: '/admin/employee/create',
                fontSize: 13
            },

        ]
    },
    {
        name: 'Khách hàng',
        path: '/admin/customer',
        icon: <i className="bi bi-people"></i>
    },
    {
        name: 'Tin nhắn',
        path: '/admin/messages',
        icon: <i className="bi bi-chat"></i>
    },
    {
        name: 'Thông báo',
        path: '/admin/customer',
        icon: <i className="bi bi-bell"></i>
    },
    {
        name: 'QL khuyến mãi',
        path: '/admin',
        icon: <i className="bi bi-gift"></i>

    },
    {
        name: 'Thống kê',
        path: '/admin',
        icon: <i className="bi bi-graph-up"></i>
    },
]


const MenuItem = ({ name, path, icon, children, fontSize, isChild = false, selectedPath, setSelectedPath, selectedName, openMenu, setOpenMenu }: Menu & { isChild?: boolean; selectedPath?: string; setSelectedPath?: (path: string | undefined) => void, selectedName?: string; setSelectedName?: (path: string | undefined) => void, openMenu: string | undefined; setOpenMenu: (name: string | undefined, path?: string | undefined) => void }) => {
    const [open, setOpen] = React.useState(false);
    const redirect = useRedirect();

    // Xác định nếu menu cha được chọn (kể cả khi có menu con)
    const isActive = selectedName === name;
    const handleClickMenu = (event: React.MouseEvent<HTMLDivElement, MouseEvent>) => {
        if (!isChild) {
            // Nếu là menu cha
            setOpenMenu(name, path);
            if (children) {
                setOpen(!open); // Chỉ toggle mở menu nếu có menu con
            } else {
                redirect(path || '');
            }
        } else {
            event.stopPropagation();
            setSelectedPath && setSelectedPath(path); // Cập nhật selectedPath khi là menu con
            redirect(path || '');
        }
    }

    // Mở menu con nếu menu cha đang được chọn
    React.useEffect(() => {
        setOpen(openMenu === name);
    }, [openMenu, name]);

    return (
        <div className='w-100 pt-1 pb-1 none-select' onClick={(e) => handleClickMenu(e)}>
            <div className={`w-100 ps-3 d-inline-flex align-items-center gap-2 ${isActive ? 'menu-navbar-active' : 'menu-navbar'}`}>
                <Row className='w-100 custom-row-menu p-2 '>
                    <Col className='d-flex justify-content-center' md={2}>
                        <span>{icon}</span>
                    </Col>
                    <Col md={10}>
                        <div className='d-flex justify-content-between align-items-center w-100 pe-1'>
                            <span style={{ fontSize: fontSize || 14, color: isChild && selectedPath === path ? 'rgb(235, 105, 35)' : '' }}>
                                {name}
                            </span>
                            <span>
                                {children ? open ? <FontAwesomeIcon fontSize={10} icon={faChevronDown} /> :
                                    <FontAwesomeIcon fontSize={10} icon={faChevronRight} /> : <></>}
                            </span>
                        </div>
                    </Col>
                </Row>
            </div>
            {children && (
                <Collapse in={open}>
                    <div className='menu-child'>
                        {children.map((item, index) => (
                            <MenuItem
                                key={index}
                                {...item}
                                isChild={true}
                                selectedPath={selectedPath}
                                setSelectedPath={setSelectedPath}
                                openMenu={openMenu}
                                setOpenMenu={setOpenMenu} // Thêm setOpenMenu để quản lý trạng thái
                            />
                        ))}
                    </div>
                </Collapse>
            )}

        </div>
    );
}

const Navbar = () => {
    const [selectedPath, setSelectedPath] = React.useState<string | undefined>(undefined);
    const [selectedName, setSelectedName] = React.useState<string | undefined>(undefined);
    const [openMenu, setOpenMenu] = React.useState<string | undefined>(undefined); // Trạng thái menu cha đang mở

    const handleMenuClick = (name: string | undefined, path: string | undefined) => {
        if (openMenu === name) {
            // Nếu menu đang mở thì đóng lại
            setOpenMenu(undefined);
            setSelectedName(undefined);
        } else {
            // Mở menu mới và đóng menu trước đó
            setOpenMenu(name);
            setSelectedName(name);
            setSelectedPath(path);
        }
    };
    return (
        <div className="w-100 d-flex justify-content-center flex-column gap-2">
            <div className='d-flex  align-items-center gap-4 p-2 title-navbar-top ' >
                <i  style={{ fontSize: 30, color:'white' }} className="bi bi-list-ul"></i>
                <span className='text-large text-white' style={{fontFamily:'Lobster'}}>SOSELL</span>
            </div>
            <div className='w-100 mt-2'>
                {menu.map((item, index) => (
                    <MenuItem
                        key={index}
                        {...item}
                        selectedPath={selectedPath}
                        setSelectedPath={setSelectedPath}
                        selectedName={selectedName}
                        setSelectedName={setSelectedName}
                        openMenu={openMenu}
                        setOpenMenu={handleMenuClick}
                    />
                ))}
            </div>
        </div>
    );
}

export default Navbar;
