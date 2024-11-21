
import { Carousel } from 'react-bootstrap';
import './CategoriesCarousel .scss'; // Optional: Create a separate CSS file for additional styling.
import { useGetCategoriesQuery } from '../../services/category.service';
import { pageQueryHanlder } from '../../utils/query-handler';
import useRedirect from '../../hooks/useRedirect';
import QueryWrapper from '../query-wrapper/SkeletonWrapper';

const CategoriesCarousel = () => {
  const redirect = useRedirect();
  const params: string = pageQueryHanlder(1, 1000, [{ filed: 'parentId', operator: '=', value: 'null' }]);
  const { data: parentCategory, isSuccess: getCategoriesSuccess } = useGetCategoriesQuery(params);
  const itemsPerSlide = 20; // Number of items per slide
  const totalSlides = Math.ceil((parentCategory?.data.items.length || 0) / itemsPerSlide);
  const renderCarouselItems = () => {
    const slides = [];
    for (let i = 0; i < totalSlides; i++) {
      slides.push(
        <Carousel.Item key={i}>
          <div className="d-flex  align-items-center flex-wrap items-categories">
            { parentCategory?.data.items.slice(i * itemsPerSlide, (i + 1) * itemsPerSlide).map((category, idx) => (
              <div className="category-item text-center  " onClick={() => redirect('/' + category.urlPath)} key={idx}>
                <img src={category.image} alt={category.categoryName} className="category-icon" />
                <p>{category.categoryName}</p>
              </div>
            ))}
          </div>
        </Carousel.Item>
      );
    }
    return slides;
  };

  return (
    <QueryWrapper queriesStatus={[getCategoriesSuccess]}>
      <div className="categories-carousel">
        <div className='title-categories'>
          <span >Danh mục</span>
        </div>

        <Carousel interval={null} indicators={false} controls={true}>
          {renderCarouselItems()}
        </Carousel>

      </div >
    </QueryWrapper>

  );
};

export default CategoriesCarousel;
