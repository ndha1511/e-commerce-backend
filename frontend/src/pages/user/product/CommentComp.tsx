import { useState } from 'react';
import { Card, Col, Image, Pagination, ProgressBar, Row } from "react-bootstrap";
import './comment.scss';
import Rating from '../../../components/rating/Rating';
import { Comment, MediaType } from '../../../models/comment';
import Avatar from '../../../components/avatar/Avatar';
import { useReplyCommentMutation } from '../../../services/comment.service';


const CommentComp = ({ comments }: { comments: Comment[] }) => {
    const totalReviews = 16;
    const [isReply, setIsReply] = useState<string | null>(null);
    const [textReply, setTextReply] = useState<string>('');
    const handleOpenReply = (commentId: string) => {
        setIsReply(isReply === commentId ? null : commentId); 
    };
    const [reply] = useReplyCommentMutation();

    const handleChangeReply = (e:React.ChangeEvent<HTMLInputElement>) => {
        setTextReply(e.target.value);
    }   

    const handleReply = async (commentId: string) => {
        console.log(commentId);
        console.log(textReply);
        const newFormData = new FormData();
        newFormData.append('content',textReply);
        try {
            await reply({
                commentId: commentId,
                formData: newFormData
            });
            setIsReply(null);
        } catch (error) {
            alert('khoong thành công')
        }
    }
    const ratings = [
        { stars: 5, count: 13 },
        { stars: 4, count: 2 },
        { stars: 3, count: 0 },
        { stars: 2, count: 1 },
        { stars: 1, count: 0 },
    ];

    const averageRating = 4.7;

    const [selectedFilter, setSelectedFilter] = useState('Tất Cả');

    const filters = [
        { label: 'Tất Cả', count: null },
        { label: '5 Sao', count: '8,9k' },
        { label: '4 Sao', count: 653 },
        { label: '3 Sao', count: 263 },
        { label: '2 Sao', count: 94 },
        { label: '1 Sao', count: 196 },
        { label: 'Có Bình Luận', count: '3,6k' },
        { label: 'Có Hình Ảnh / Video', count: '1,3k' }
    ];

    const handleFilterClick = (filterLabel: string) => {
        setSelectedFilter(filterLabel);
    };

    const active = 2;
    const items = [];
    for (let number = 1; number <= 5; number++) {
        items.push(
            <Pagination.Item key={number} active={number === active}>
                {number}
            </Pagination.Item>,
        );
    }

    return (
        <div className='bg-white p-3  border-radius-medium mt-3'>
            <div className='ms-2 mt-1'> <h6>Khách hàng đánh giá</h6></div>
            <div className='p-3'>
                <Row className='row-rt'>
                    <Col md={4} className="">
                        <div>
                            <div className="d-flex align-items-center">
                                <h2 className="mb-0">{averageRating}</h2>
                                <div className="ms-2">
                                    {[...Array(5)].map((_, index) => (
                                        <span style={{ fontSize: 30 }} key={index} className="text-warning">&#9733;</span>
                                    ))}
                                </div>
                            </div>
                            <p className="mb-2 text-muted">({totalReviews} đánh giá)</p>
                            {ratings.map((rating, index) => (
                                <div key={index} className="d-flex align-items-center mb-1">
                                    <div className="me-2" style={{ width: '20%' }}>
                                        {[...Array(rating.stars)].map((_, starIndex) => (
                                            <span key={starIndex} className="text-warning">&#9733;</span>
                                        ))}
                                        {[...Array(5 - rating.stars)].map((_, starIndex) => (
                                            <span key={starIndex} className="text-muted">&#9733;</span>
                                        ))}
                                    </div>
                                    <div className="w-100">
                                        <ProgressBar
                                            now={(rating.count / totalReviews) * 100}
                                            variant="danger"
                                            style={{ height: '10px', marginTop: 8, marginLeft: 6 }}
                                        />
                                    </div>
                                    <div className="ms-2">{rating.count}</div>
                                </div>
                            ))}
                        </div>
                    </Col>
                    <Col md={8} className=" ">
                        <Row className="row-center">
                            {filters.map((filter, index) => (
                                <Col key={index} xs={6} md={3} className="mb-2 ">
                                    <button
                                        onClick={() => handleFilterClick(filter.label)}
                                        className={`w-100 btn-comment border-radius-small bg-white p-1 d-flex align-items-center justify-content-center ${selectedFilter === filter.label ? 'selected' : ''}`}
                                    >
                                        {filter.label} {filter.count && `(${filter.count})`}
                                    </button>
                                </Col>
                            ))}
                        </Row>
                    </Col>
                </Row>
            </div>


            {comments.slice().reverse().map((value, idx) => {
                return (
                    <Card key={idx} style={{ border: 'none' }} className="mb-1">
                        <Card.Body>
                            <Row className="align-items-center">
                                <Col className="">
                                    <div className='d-flex gap-3'>
                                        <Avatar name={value?.user?.name || ""} url={value?.user?.avatar} />
                                        <div>
                                            <h6 className="mb-0">{value?.user?.username}</h6>
                                            <div className="d-flex align-items-center">
                                                <Rating variant='warning' star={value.rating} />
                                            </div>
                                            <small className="text-muted">
                                                {value.commentDate.toString()} | Phân loại hàng: {value.attributes}
                                            </small>
                                        </div>
                                    </div>
                                </Col>
                            </Row>
                            <Card.Text className="mt-2">
                           
                            </Card.Text>
                            <div className='d-flex align-items-center gap-1'>
                                    <div className="d-flex gap-3">
                                        {value.content}
                                        {value.commentMedia?.map((img, imgIndex) => (
                                            <div key={imgIndex} className="">
                                                {img.mediaType === MediaType.IMAGE ?
                                                    <Image className='img-cm' src={img.path} thumbnail /> :
                                                    <video src={img.path}></video>
                                                }
                                            </div>
                                        ))}
                                    </div>
                                    <button className='btn-reply' onClick={()=>handleOpenReply(value.id)}>Phản hồi</button>
                                </div>
                            {isReply === value.id &&
                                <div className=' d-flex flex-column'>
                                    <div className='d-flex align-items-end'>
                                        <Avatar name={value?.user?.name || ""} url={value?.user?.avatar} />
                                        <input onChange={(e)=>handleChangeReply(e)} type="text" placeholder='Phản hồi' className='input-reply' />
                                    </div>
                                    <div className='d-flex justify-content-end gap-2 mt-1'>
                                        <button className='btn-cancel' onClick={() => setIsReply(null)}>Hủy</button>
                                        <button className='btn-send' onClick={()=>handleReply(value.id)}>Phản hồi</button>
                                    </div>
                                </div>
                            }
                            {value.replyComment ? <div className="bg-light p-3 mt-3 custom-background">
                                <span>Phản Hồi Của Shop
                                    
                                </span>
                                <Card.Text className='text-muted'>
                                    {value.replyComment}
                                </Card.Text>
                            </div> : <></>}
                        </Card.Body>
                        <hr />
                    </Card>)
            })}
            <div className='w-100 d-flex justify-content-end '>
                <Pagination>{items}</Pagination>
            </div>

        </div>
    );
}

export default CommentComp;
