import React from 'react';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faChevronDown } from '@fortawesome/free-solid-svg-icons';
import SimpleBar from 'simplebar-react';
import { Col, Row } from 'react-bootstrap';
import './category-info.scss'
interface DropdownProps {
    required?: string,
    lable: string,
    isOpen: boolean;
    isDropdownVisible: boolean;
    toggleDropdown: () => void;
    selectedBrand: string;
    brands: string[];
    onBrandClick: (brand: string) => void;
}

const SelectCategory: React.FC<DropdownProps> = ({ required, lable, isOpen, isDropdownVisible, toggleDropdown, selectedBrand, brands, onBrandClick }) => {
    return (
        <div className='mt-3'>
            <Row>
                <Col md={3} className=' p-1 justify-content-end d-flex align-items-center'>
                    <span><span className='primary'>{required}</span>{lable}</span>
                </Col>
                <Col md={9} className='p-1'>
                    <div className="select-search-category-info" onClick={toggleDropdown}>
                        <input
                            readOnly
                            className="select-category-info"
                            value={selectedBrand ? selectedBrand : ''}
                            placeholder="Vui lòng chọn"
                            type="text"
                        />
                        <FontAwesomeIcon icon={faChevronDown} className={isOpen ? 'rotate-icon' : ''} />
                        {isDropdownVisible && (
                            <div className="option-category-info">
                                <div className="w-100" style={{ padding: '10px 10px 2px 10px' }}>
                                    <div className="search-category-info">
                                        <input
                                            className="input-category-info"
                                            placeholder="Vui lòng nhập"
                                            type="text"
                                        />
                                        <i className="bi bi-search"></i>
                                    </div>
                                </div>
                                <div className="w-100 mt-2 list-option">
                                    <SimpleBar style={{ maxHeight: 200 }}>
                                        {brands.map((brand, index) => (
                                            <button
                                                key={index}
                                                className="w-100 btn-select-category-info"
                                                onClick={() => onBrandClick(brand)}
                                            >
                                                {brand}
                                            </button>
                                        ))}
                                    </SimpleBar>
                                </div>
                            </div>
                        )}
                    </div>

                </Col>
            </Row>
        </div>
    );
};

export default SelectCategory;
