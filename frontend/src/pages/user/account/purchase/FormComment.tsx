import { Button, Modal } from "react-bootstrap";
import { ProductOrder } from "../../../../models/product-order";
import Rating from "../../../../components/rating/Rating";
import React from "react";
import SimpleBar from "simplebar-react";
import { useCreateCommentMutation } from "../../../../services/comment.service";
import { useCheckLoginQuery } from "../../../../services/auth.service";
import ModalLoading from "../../../../components/loading/ModalLoading";
import { useDispatch } from "react-redux";
import { setNotify } from "../../../../rtk/slice/notify-slice";

interface Props {
    show: boolean;
    handleClose: () => void;
    product: ProductOrder;
    orderId: string;
    attributes: string[];
}

enum FileType {
    IMAGE = 'image',
    VIDEO = 'video',
    OTHER = 'other'
}

interface FileView {
    url: string;
    type: FileType;
}

const FormComment = ({ show, handleClose, product, orderId, attributes }: Props) => {
    const [star, setStar] = React.useState(0);

    const {data: user} = useCheckLoginQuery();

    const [send, {isLoading}] = useCreateCommentMutation();

    const [content, setContent] = React.useState('');

    const clickStar = (star: number) => {
        setStar(star);
    }

    const dispatch = useDispatch();

    const getFileType = (file: File): FileType => {
        const imageExtensions = ['jpg', 'jpeg', 'png', 'gif', 'bmp', 'webp'];
        const videoExtensions = ['mp4', 'mov', 'avi', 'mkv', 'flv', 'wmv'];

        const fileExtension = file.name.split('.').pop()?.toLowerCase();

        if (fileExtension && imageExtensions.includes(fileExtension)) {
            return FileType.IMAGE;
        } else if (fileExtension && videoExtensions.includes(fileExtension)) {
            return FileType.VIDEO;
        }
        return FileType.OTHER;
    };

    const [viewFile, setViewFile] = React.useState<FileView[]>([]);
    const [file, setFile] = React.useState<File[]>([]);

    const sendComment = async () => {
        if(star <= 0) return;
        if(content === '') return;
       
        const productNumId =100;
        const formData: FormData = new FormData();
        for (const f of file) {
            formData.append('files', f);
        }
        formData.append('content', content);
        formData.append('productId', product.productId);
        formData.append('orderId', orderId);
        formData.append('rating', star.toString());
        formData.append('productNumId', productNumId.toString());
        for (const attr of attributes) {
            formData.append('attributes', attr);
        }
        formData.append('userId', user?.data?.id || "");
        try {
            await send(formData).unwrap();
            handleClose();
            dispatch(setNotify({
                type:'success', message: 'Thao tác thành công'
            }))
        } catch (error) {
            console.log(error);
            dispatch(setNotify({
                type:'error', message: 'Thao tác không thành công'
            }))
        }
    }

    return (<Modal show={show} onHide={handleClose} centered>
        <Modal.Header closeButton>
            <h6>Đánh giá sản phẩm</h6>
        </Modal.Header>
        <Modal.Body>
            <SimpleBar style={{
                maxHeight: '500px'
            }}>
                <div>
                    <h6>Bạn cảm thấy sản phẩm này như thế nào</h6>
                    <div className="d-flex gap-3 align-items-center">
                        <img src={product.image} width={100} height={100} />
                        <span>{product.productName}</span>
                        <span>Phân loại: {product.attributes}</span>
                    </div>


                    <div className="d-flex flex-column gap-2 align-items-center">
                        <Rating variant="warning" size="text-large" star={star} setStar={clickStar} />
                        <label htmlFor="video-image" className="p-2 btn-pointer background-primary border-color-primary">
                            Chọn ảnh/video
                        </label>
                        <input style={{
                            display: 'none'
                        }} type="file" id="video-image" multiple onChange={(e) => {
                            if (e.target.files) {
                                const files = e.target.files;
                                const arrView = Array.from(files).map(f => {
                                    return {
                                        url: URL.createObjectURL(f),
                                        type: getFileType(f)
                                    }
                                });
                                setViewFile(prev => [...prev, ...arrView]);
                                setFile(prev => Array.from([...prev, ...files]));
                            }

                        }} />
                        <div className="d-flex gap-2 flex-wrap">
                            {viewFile.map((v, idx) => (
                                <div key={idx} className={`d-flex gap-2 border align-items-center ${v.type === FileType.IMAGE ? 'flex-column' : 'flex-row'}`}>
                                    {v.type === FileType.IMAGE && <img src={v.url} width={100} height={100} />}
                                    {v.type === FileType.VIDEO && <video width={100} height={100} controls>
                                        <source src={v.url} type="video/mp4" />
                                        Your browser does not support the video tag.
                                    </video>}
                                </div>
                            ))}
                        </div>
                        <textarea value={content} onChange={(e) => setContent(e.target.value)}  placeholder="Nhập đánh giá của bạn" rows={5} cols={50}></textarea>
                    </div>
                </div>
            </SimpleBar>
            {isLoading && <ModalLoading loading={isLoading} />}
        </Modal.Body>
        <Modal.Footer>
            <Button variant="secondary" onClick={handleClose}>
                Đóng
            </Button>
            <Button variant="primary" onClick={sendComment}>
                Gửi đánh giá
            </Button>
        </Modal.Footer>
    </Modal>)
}

export default FormComment;