import { useNavigate } from "react-router-dom";

const useRedirect = () => {

    const navigate = useNavigate();

    const redirect = (path: string) => {
        navigate(path);
        setTimeout(() => {
            window.scrollTo({
                top: 0,
                behavior: 'instant'
            });
        }, 0);
    };

    return redirect;

}

export default useRedirect;