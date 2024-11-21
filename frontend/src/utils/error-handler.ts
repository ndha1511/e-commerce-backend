export const printError = (err: unknown) => {
    if (err instanceof Error) {
        return err.message;
    } else if (typeof err === 'object' && err !== null && 'data' in err) {
        const serverError = err as { data?: { errors?: string } };
        return serverError.data?.errors;
    } else {
        return "Lỗi không xác định";
    }
}
