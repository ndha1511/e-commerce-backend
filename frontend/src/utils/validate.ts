const emailPattern = /^[a-z0-9._%+-]+@[a-z0-9.-]+\.[a-z]{2,4}$/


export function validateEmail(email: string): boolean {
    return emailPattern.test(email);
}

export function validatePassword(password: string) {
    const hasLength = password.length >= 8;
    const hasUppercase = /[A-Z]/.test(password); 
    const hasDigit = /\d/.test(password);    
    const hasSpecialChar = /[@$!%*?&.]/.test(password); 
    return [
        hasLength,
        hasUppercase,
        hasDigit,
        hasSpecialChar
    ];
    
}

