import CarouselHome from '../../../components/carousel/CarouselHome';
import CategoriesCarousel from '../../../components/categories/CategoriesCarousel ';
import ListProduct from '../../../components/products/ListProduct';
import Rating from '../../../components/rating/Rating';
import { useGetProductsQuery } from '../../../services/product.service';
import './home.scss';

const Home = () => {
   
    const { data } = useGetProductsQuery();

    return <div className='container home-container'>
        <div className='carousel-home'>
            <CarouselHome />
        </div>
        <div className='category'>
            <CategoriesCarousel />
        </div>
        <div className='suggested'>
            <Rating star={1.5} variant={'warning'} size='text-medium' />
        </div>

        {/* <ListProduct products={data?.data.items}/> */}
        
    </div>
}

export default Home;