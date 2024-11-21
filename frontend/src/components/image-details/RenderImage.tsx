const RenderImage = (props: any) => {
    return (
        <img src={props.original} className="image-gallery-image"
        style={{ width: "100%", height: "80vh" }}/>
      );
}

export default RenderImage;