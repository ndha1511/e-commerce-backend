import { faChevronLeft, faChevronRight } from "@fortawesome/free-solid-svg-icons";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import ImageGallery from "react-image-gallery";
import './style-image-gallery.scss';
import {  useRef } from "react";

type Image = {
    original: string;
    thumbnail: string;
    embedUrl?: string;
};

export type Props = {
    images: Image[];
    startIndex?: number;
};

const ImageDetails = ({ images, startIndex = 0 }: Props) => {
    const galleryRef = useRef<ImageGallery>(null);
    return (
        <div style={{ width: "100%", flex: 1 }}>
            <ImageGallery
                ref={galleryRef}
                startIndex={startIndex}
                renderLeftNav={(onClick, disabled) => (
                    <button
                        className="image-gallery-icon image-gallery-left-nav text-large"
                        onClick={onClick}
                        disabled={disabled}
                    >
                        <FontAwesomeIcon icon={faChevronLeft} />
                    </button>
                )}
                renderRightNav={(onClick, disabled) => (
                    <button
                        className="image-gallery-icon image-gallery-right-nav text-large"
                        onClick={onClick}
                        disabled={disabled}
                    >
                        <FontAwesomeIcon icon={faChevronRight} />
                    </button>
                )}
                items={images}
            />
        </div>
    );
};

export default ImageDetails;
