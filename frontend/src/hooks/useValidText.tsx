import { useEffect, useState } from "react";

export interface Pattern {
    regExp?: RegExp;
    message?: string;
}

const useValidText = (pattern?: Pattern) => {
    const [value, setValue] = useState<string | null>(null);
    const [err, setErr] = useState<string>('');

    useEffect(() => {
        if(value && value !== '') {
            if(pattern && pattern.regExp) {
                if(!pattern.regExp.test(value)) {
                    setErr(pattern.message || 'Dữ liệu không hợp lệ');
                } else {
                    setErr('');
                }
            }
            setErr('');
            return;
        }
        if(value === '') {
            setErr('Vui lòng nhập vào trường này');
            return;
        } 
    }, [value])

    return {value, setValue, err};
}

export default useValidText;