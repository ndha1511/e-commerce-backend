import { ReactNode } from "react";
import Menu from "./menu/Menu";
import "./account.scss";

type Props = {
    children: ReactNode;
}

const Account = ({children} : Props) => {
    return <div className="background-muted pt-4 pb-4">
        <div className="container d-flex">
            <div className="account-left col-md-2 col-3">
                <Menu/>
            </div>
            <div className="d-flex" style={{
                flex: 1,
            }}>{children}</div>
        </div>
    </div>
}

export default Account;