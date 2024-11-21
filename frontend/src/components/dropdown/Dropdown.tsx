import { ReactNode } from "react";
import "./dropdown.scss";

interface Props {
    dropDownItem: {
        item: ReactNode;
        event: () => void;
    }[];
    children: ReactNode;
}

const Dropdown = ({dropDownItem, children} : Props) => {

    return <div className="dropdown-wrapper">
        {children}
        <div className="menu-dropdown border-radius-small">
            <div className="triangle-up"></div>
            <ul>
                {dropDownItem.map((item, index) => (
                    <li key={index} onClick={item.event}>
                        {item.item}
                    </li>
                ))}
            </ul>
        </div>
    </div>
}

export default Dropdown;