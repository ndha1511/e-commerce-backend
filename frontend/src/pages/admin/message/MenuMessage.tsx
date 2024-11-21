import SimpleBar from 'simplebar-react';
import './message.scss'
import { Room } from '../../../models/room';

interface ItemProps {
    room: Room[];
}
function MenuMessage({ room }: ItemProps) {

    return (
        <div>
            <div className='search-message'>
                <i className="bi bi-search"></i>
                <input type="text" placeholder='Tìm kiếm ' />
            </div>
            {/* <SimpleBar style={{ height: 500 }}>
                {room.map((item: Room) => (
                    <div key={item.id} className="d-flex align-items-center gap-2 menu-message-items">
                        <img src='' width={50} height={50} alt="" />
                        <div className="d-flex flex-column">
                            <span>{item.receiver}</span>
                            <span className="text-muted">tutu</span>
                        </div>
                        <div className='count-message'>3</div>
                    </div>
                ))}
            </SimpleBar> */}
        </div>
    );
}

export default MenuMessage;