import { Collapse } from "react-bootstrap";
import { useState } from "react";
import { BsChevronDown, BsChevronUp } from "react-icons/bs";
import './category.scss'
import { CategoryResponse } from "../../../dtos/response/category/category-response";

interface Item {
    item: CategoryResponse
}
function MenuCategory({ item }: Item) {
    const [open, setOpen] = useState<boolean>(false);
    const [isSelected, setIsSelected] = useState<boolean>(false);
    const handleClick = (event: React.MouseEvent<HTMLDivElement, MouseEvent>) => {
        setIsSelected(!isSelected);
        setOpen(!open)
        event.stopPropagation();
    }
    return (
        <div onClick={(e) => handleClick(e)}>
            <div className={` cursor-pointer menu-cate-user
                ${item.children ? 'd-flex border-top w-100 justify-content-between align-items-center p-3' : '  p-2 ps-4'}
                 ${isSelected ? 'selected-cate-user' : ''}
                `}>
                <span> {item.categoryName}</span>
                {item.children && (open ? <BsChevronUp /> : <BsChevronDown />)}
            </div>
            {item.children && (
                <Collapse in={open}>
                    <div key={item.id} className=''>
                        {item.children.map((item) => (
                            <MenuCategory
                                key={item.id}
                                item={item}
                            />
                        ))}
                    </div>
                </Collapse>
            )}
        </div>
    );
}

export default MenuCategory;