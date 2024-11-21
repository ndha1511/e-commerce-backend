import { useEffect, useRef, useState } from "react";
import SideBar from "../side-bar/SideBar";
import "./header.scss";
import useDebounce from "../../../hooks/useDebounce";
import { useCheckLoginQuery } from "../../../services/auth.service";
import PopoverSearch from "./PopoverSearch";

type Props = {
    fixedSearch: boolean;
}

const Header = ({ fixedSearch }: Props) => {

    const [isOpenPopover, setIsOpenPopover] = useState(false);
    const [textSearch, setTextSearch] = useState("");
    const debouncedSearch = useDebounce(textSearch, 500);
    const { data: userData } = useCheckLoginQuery();
    const popoverRef = useRef<HTMLDivElement | null>(null);
    const inputRef = useRef<HTMLInputElement | null>(null);

    useEffect(() => {
        const handleClickOutside = (event: MouseEvent) => {
            if (
                popoverRef.current &&
                !popoverRef.current.contains(event.target as Node) &&
                inputRef.current &&
                !inputRef.current.contains(event.target as Node)
            ) {
                setIsOpenPopover(false);
            }
        };

        if (isOpenPopover) {
            document.addEventListener('mousedown', handleClickOutside);
        } else {
            document.removeEventListener('mousedown', handleClickOutside);
        }

        // Cleanup event listener on unmount
        return () => {
            document.removeEventListener('mousedown', handleClickOutside);
        };
    }, [isOpenPopover]);


    return <>
        <SideBar />
        <div className={fixedSearch ? `search fixed-search` : `search`}>
            <div className="search-arround"></div>
            <div className="search-center">
                <label htmlFor="search-input" className="search-wrapper">
                    <input
                        ref={inputRef}
                        value={textSearch}
                        onFocus={() => setIsOpenPopover(true)}
                        onChange={(e) => setTextSearch(e.target.value)}
                        id="search-input"
                        className="search-input col-8"
                        placeholder="Tìm kiếm sản phẩm"
                    />
                    <button className="search-button background-primary col-2 col-md-1 button-hover" data-toggle="tooltip" title="Tìm kiếm">
                        <i className="bi bi-search"></i>
                    </button>
                </label>

                {isOpenPopover && (
                    <div ref={popoverRef} style={{
                        position: 'absolute',
                        width: "100%",
                        minHeight: "100px",
                        background: '#fff',
                        boxShadow: "-2px 0 30px 2px rgba(97, 105, 119, 0.18)",
                        top: "100%",
                        borderRadius: '5px',
                        zIndex: 999,
                        marginTop: "2px"
                    }}>
                        <PopoverSearch user={userData?.data} content={debouncedSearch} />
                    </div>
                )}


            </div>
            <div className="search-arround">
            </div>
        </div>
    </>
}

export default Header;