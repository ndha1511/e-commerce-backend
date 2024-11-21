import Skeleton from 'react-loading-skeleton'
import 'react-loading-skeleton/dist/skeleton.css'
import { SkeletonWrapperProps } from "../types";


const SkeltetonWrapper: React.FC<SkeletonWrapperProps> = (props) => {
    const {children, queriesStatus, skeleton: SkeletonCustom, skWidth, skHeight} = props;
    const allQueriesSuccess = queriesStatus.every(val => val === true);
    if(!allQueriesSuccess) {
        if(SkeletonCustom) {
            return <SkeletonCustom/>
        }
        return <Skeleton style={{
            height: skHeight || "100%",
            width: skWidth || "100%"
        }}/>;
    }

    return children;
    

}

export default SkeltetonWrapper;