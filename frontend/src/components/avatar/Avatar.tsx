interface Props {
    name: string;
    url?: string;
    width?: number;
    height?: number;
}

const Avatar = ({ name, url, width = 30, height = 30}: Props) => {
    return <div className="d-inline-flex gap-1">
        {url ? <img src={url} alt={name} width={width} height={height} /> :
            <i className="bi bi-person-circle"></i>}
        <span className="side-bar-item">{name}</span>
    </div>
}

export default Avatar;