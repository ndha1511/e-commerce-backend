import AccessDenied from "../../pages/access-denied/AccessDenied";
import NotFound from "../../pages/not-found/NotFound";
import { QueryWrapperProps } from "../types";

const QueryWrapper: React.FC<QueryWrapperProps> = (props) => {
    const {children, queriesError, error} = props;
    const notError = queriesError.every(val => val === false);
    if(notError) {
        return children;
    } else {
        if(error && typeof error === 'object' && 'status' in error) {
            console.log(error.status);
            if(error.status === 404) {
                return <NotFound/>
            }
            if(error.status === 403) {
                return <AccessDenied/>
            }
        }
        return <div>Error: {queriesError.join(', ')}</div>
    }

}

export default QueryWrapper;