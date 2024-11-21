import { useLocation } from "react-router-dom";

const useGetParam = (key: string) => {
    const location = useLocation();
    const queryParams = new URLSearchParams(location.search);
    return queryParams.get(key);
}

export default useGetParam;