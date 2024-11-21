import { useState } from 'react';
import { Col, Row } from 'react-bootstrap';
import './category-info.scss'
import SelectCategory from './SelectCategory';
const CategoryInfo = () => {
    const [isDropdownVisible, setDropdownVisible] = useState(false);
    const [isOpen, setIsOpen] = useState(false);
    const [selectedBrand, setSelectedBrand] = useState('');

    const brands = [
        'No brand',
        'AAA JEANS',
        'ADORE DRESS',
        'ADRIENNE VITTADINI',
        'AFTERBEFORE',
        'AFTF BASIC',
        'AJIMAL',
        'AKUBA',
        'ALAMODEvn',
        'ALCADO',
        'ALICE TÔ',
        'No brand',
        'AAA JEANS',
        'ADORE DRESS',
        'ADRIENNE VITTADINI',
        'AFTERBEFORE',
        'AFTF BASIC',
        'AJIMAL',
        'AKUBA',
        'ALAMODEvn',
        'ALCADO',
        'ALICE TÔ',
        // Add more brands here if needed
    ];

    const toggleDropdown = () => {
        setIsOpen(!isOpen);
        setDropdownVisible(!isDropdownVisible);

    };

    const handleBrandClick = (brand:string) => {
        setSelectedBrand(brand);
        setDropdownVisible(false);
    };

    return (
        <div className='container-fluid mt-3 '>
            <Row>
                <Col md={6} className=''>
                    <SelectCategory
                        required='*'
                        lable='Thương hiệu: '
                        isOpen={isOpen}
                        isDropdownVisible={isDropdownVisible}
                        toggleDropdown={toggleDropdown}
                        selectedBrand={selectedBrand}
                        brands={brands}
                        onBrandClick={handleBrandClick}
                    />
                       <SelectCategory
                        lable='Chất liệu: '
                        isOpen={isOpen}
                        isDropdownVisible={isDropdownVisible}
                        toggleDropdown={toggleDropdown}
                        selectedBrand={selectedBrand}
                        brands={brands}
                        onBrandClick={handleBrandClick}
                    />
                         <SelectCategory
                        lable='Mùa: '
                        isOpen={isOpen}
                        isDropdownVisible={isDropdownVisible}
                        toggleDropdown={toggleDropdown}
                        selectedBrand={selectedBrand}
                        brands={brands}
                        onBrandClick={handleBrandClick}
                    />
                </Col>
                <Col md={6} className=''>
                    <SelectCategory
                        lable='Xuất xứ: '
                        isOpen={isOpen}
                        isDropdownVisible={isDropdownVisible}
                        toggleDropdown={toggleDropdown}
                        selectedBrand={selectedBrand}
                        brands={brands}
                        onBrandClick={(e)=>handleBrandClick(e)}
                    />
                         <SelectCategory
                        lable='Mẫu: '
                        isOpen={isOpen}
                        isDropdownVisible={isDropdownVisible}
                        toggleDropdown={toggleDropdown}
                        selectedBrand={selectedBrand}
                        brands={brands}
                        onBrandClick={handleBrandClick}
                    />
                         <SelectCategory
                        lable='Phong cách: '
                        isOpen={isOpen}
                        isDropdownVisible={isDropdownVisible}
                        toggleDropdown={toggleDropdown}
                        selectedBrand={selectedBrand}
                        brands={brands}
                        onBrandClick={handleBrandClick}
                    />
                </Col>
            </Row>
        </div>
    );
};

export default CategoryInfo;
