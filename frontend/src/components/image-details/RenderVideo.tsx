const RenderVideo = (props: any) => {
    return (
        
          <video
            className="image-gallery-content"
            style={{ width: "100%", height: "80vh" }}
            src={props.embedUrl}
            autoPlay
            controls
            muted
          />
      );
}

export default RenderVideo;