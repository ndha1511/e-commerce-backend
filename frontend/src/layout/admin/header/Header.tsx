import useRedirect from "../../../hooks/useRedirect";

const Header = () => {
    const redirect =   useRedirect();
    return <div className=" p-2 ps-3" style={{backgroundColor:'rgb(244, 110, 39)',height:64}} onClick={()=>redirect('/')}>
        <div className=" p-1 d-flex gap-2 cursor-pointer" >
            <img src="https://cdn.pixabay.com/photo/2024/06/04/01/57/desktop-wallpaper-4k-8807689_1280.jpg" alt=""
                width={40} height={40} />
            <div className="d-flex flex-column text-white ">
                <span className="text-white">SẢN PHẨM CHẤT LƯỢNG</span>
                <span className="text-white opacity-75">abc123@gmail.com.vn</span>
            </div>
        </div>
    </div>
}

export default Header;