import { useEffect, useState } from "react";

function useChangeFile(maxImages: number, initFile: File[], initPreviewUrl: (string | ArrayBuffer | null)[]) {
    const [files, setFiles] = useState<File[]>(initFile);
    const [previewUrls, setPreviewUrls] = useState<(string | ArrayBuffer | null)[]>(initPreviewUrl);
    const shouldHideInput = files.length >= maxImages;

    useEffect(()=>{
        if(previewUrls.length === 0){
            setFiles([]);
        }
    },[previewUrls])
    // Handle file input change event
    const handleFileChange = (event: React.ChangeEvent<HTMLInputElement>) => {
        const newFiles = event.target.files;
        if (newFiles) {
            const newFileArray = Array.from(newFiles);
            const availableSpace = maxImages - files.length;
            if (availableSpace > 0) {
                const filesToAdd = newFileArray.slice(0, availableSpace);
                setFiles((prev) => [...prev, ...filesToAdd]);

                // Array to hold the new preview URLs
                const readPromises = filesToAdd.map((file) => {
                    return new Promise<string | ArrayBuffer | null>((resolve) => {
                        const reader = new FileReader();
                        reader.onloadend = () => {
                            resolve(reader.result);
                        };
                        reader.readAsDataURL(file);
                    });
                });

                // Resolve all promises and update the preview URLs state
                Promise.all(readPromises).then((newUrls) => {
                    setPreviewUrls((prev) => [...prev, ...newUrls]);
                });
            }
        }
    };

    // Handle deleting an image by index
    const handleDeleteImage = (index: number) => {
        setFiles((prev) => prev.filter((_, i) => i !== index));
        setPreviewUrls((prev) => prev.filter((_, i) => i !== index));
    };

    // Return as an object instead of merging in an array for better destructuring and type safety
    return { files, previewUrls, handleFileChange, shouldHideInput, handleDeleteImage,setPreviewUrls };
}

export default useChangeFile;
