import { Carousel } from "react-bootstrap";
import './carousel-home.scss'
interface CarouselImage {
  src: string[]; 
  alt: string;
  caption?: string;
  title?: string;
}
type ObjectFit = 'fill' | 'contain' | 'cover' | 'none' | 'scale-down';
interface CarouseCustomProps {
  images: CarouselImage[]; // Nhận mảng ảnh từ props
  height?: number; // Chiều cao tùy chọn
  objectFit?: ObjectFit; // Kiểu hiển thị hình ảnh
}

const CarouseCustom: React.FC<CarouseCustomProps> = ({
  images,
  height = 285, // Giá trị mặc định
  objectFit = 'cover' // Giá trị mặc định
}) => {
  return (
    <div className="home-carousel">
      <Carousel fade>
        {images.map((image, index) => (
          <Carousel.Item key={index} >
            <div className="d-flex gap-3">
            {image.src.map((imgSrc, imgIndex) => (
                <img
                  key={imgIndex}
                  className=" border-radius-medium"
                  style={{ width:'100%',   height: height, objectFit: objectFit }} // Sử dụng props
                  src={imgSrc}
                  alt={`${image.alt} - ${imgIndex + 1}`}
                />
              ))}
            </div>
            <Carousel.Caption className="carousel-caption-custom">
              <h3>{image.title}</h3>
              <p>{image.caption}</p>
            </Carousel.Caption>
          </Carousel.Item>
        ))}
      </Carousel>
    </div>
  );
};

export default CarouseCustom;
