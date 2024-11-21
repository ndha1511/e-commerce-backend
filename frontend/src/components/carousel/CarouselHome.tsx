import { Carousel } from "react-bootstrap";

const CarouselHome = () => {
  const images = [
    {
      src: "https://www.matbao.net/uploads/news/trilm/files/Ban%20hang%20tren%20san%20thuong%20mai%20dien%20tu%204.jpg",
      alt: "First slide",
      caption: "Here is the description for the first slide",
      title: "First Slide",
    },
    {
      src: "https://phuongnamvina.com/img_data/images/san-thuong-mai-dien-tu-la-gi.jpg",
      alt: "Second slide",
      caption: "Here is the description for the second slide",
      title: "Second Slide",
    },
    {
      src: "https://cdn4.vieclam24h.vn/wp-content/uploads/2024/05/20145024/carousel-la-gi-01.webp?_gl=1*194zhae*_gcl_au*MjQxMzcyMTU2LjE3MjQyNTc2MzQ.*_ga*MTY0NzY5NzQxMC4xNzI0MjU3NjM0*_ga_1QF2GYW1WQ*MTcyNDI1NzYzNC4xLjAuMTcyNDI1NzYzNC42MC4wLjA.",
      alt: "Third slide",
      caption: "Here is the description for the third slide",
      title: "Third Slide",
    },
  ];

  return (
    <div className="home-carousel">
      <Carousel fade>
        {images.map((image, index) => (
          <Carousel.Item key={index}>
            <img
              className="d-block w-100 border-radius-medium"
              style={{width: '100%', height: 285,objectFit:'cover'}}
              src={image.src}
              alt={image.alt}
            />
            <Carousel.Caption>
              <h3>{image.title}</h3>
              <p>{image.caption}</p>
            </Carousel.Caption>
          </Carousel.Item>
        ))}
      </Carousel>
    </div>
  );
};

export default CarouselHome;
