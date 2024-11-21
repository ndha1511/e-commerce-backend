import { AnimatePresence, motion } from "framer-motion";
import './notification-items.scss'
import { NotificationModel } from "../../../models/notification";
import SimpleBar from "simplebar-react";
import moment from 'moment';
import { useCheckSeenMutation } from "../../../services/notification.service";
import { useEffect, useRef, useState } from "react";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faCircle } from "@fortawesome/free-solid-svg-icons";
function NotificationItems({ notifications,isVisible, setIsVisible }: { notifications: NotificationModel[], isVisible:boolean ,setIsVisible: (visible: boolean) => void}) {
    const [checkSeen] = useCheckSeenMutation();
    const notificationRef = useRef<HTMLDivElement>(null);
    const calculateElapsedTime = (notificationTime: Date): string => {
        const notificationMoment = moment(notificationTime, 'YYYY-MM-DD HH:mm:ss'); // Chuyển thời gian thông báo sang đối tượng moment
        const now = moment(); // Thời gian hiện tại
        const duration = moment.duration(now.diff(notificationMoment)); // Tính khoảng thời gian đã trôi qua
        return duration.days() > 0 ? `${duration.days()} ngày trước`
            : duration.hours() > 0 ? `${duration.hours()} giờ trước`
                : `${duration.minutes()} phút trước`;

    };
    const handleSeen = async (notificationId: string) => {
       try {
        await checkSeen(notificationId);
       } catch (error) {
        console.log(error);
       }
    };
    const handleOutsideClick = (event: MouseEvent) => {
        if (notificationRef.current && !notificationRef.current.contains(event.target as Node)) {
            setIsVisible(false); // Đóng khi click ra ngoài
        }
    };
    useEffect(() => {
        document.addEventListener('mousedown', handleOutsideClick);
        return () => {
            document.removeEventListener('mousedown', handleOutsideClick);
        };
    }, []);

    if (!isVisible) return null;
    return (
        <div  ref={notificationRef} className="d-flex flex-column gap-3 justify-content-end" style={{
            position: 'fixed',
            bottom: 200,
            right: 60,
            zIndex: 1000
        }}>
            <AnimatePresence>
                <motion.div
                    initial={{ scale: 0 }}
                    animate={{ scale: 1, }}
                    exit={{ scale: 1 }}
                    transition={{ duration: 0.5 }}
                    className="motion-notification"
                >
                    <div className="border-bottom p-2 mb-1">Thông báo</div>
                    <SimpleBar style={{ height: 300 }}>
                        {notifications.slice().reverse().map((notification) => {
                            const timestamp = calculateElapsedTime(notification.time)
                            return (
                                <div key={notification.id} className="content-notificaton" style={{
                                    backgroundColor: notification.seen === true ? '' : 'rgba(225, 220, 220, 0.3)'
                                }} onClick={()=>handleSeen(notification.id)}>
                                    <img src={notification.image} alt="" width={30} height={30} style={{ marginTop: 4 }} />
                                    <div>
                                        <span>{notification.title}</span>
                                        <div className="d-flex flex-column gap-1">
                                            <span className="text-light-cs">{notification.content}</span>
                                            <span className="text-light-cs" style={{fontSize:10}}>{timestamp}</span>
                                        </div>
                                    </div>
                                    {notification.seen === false && <div className="dot-notifi"><FontAwesomeIcon fontSize={8} color="red" icon={faCircle } /></div>}
                                </div>
                            )
                        })}
                    </SimpleBar>
                </motion.div>
            </AnimatePresence>


        </div>
    );
}

export default NotificationItems;