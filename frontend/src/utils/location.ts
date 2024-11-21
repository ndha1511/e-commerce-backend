export const isAbsoluteLocation = (path?: string) : boolean => {
    if (!path) return false;
    return window.location.pathname === path ? true  : false;
}

export const isLocation = (path?: string) : boolean => {
    if(!path) return false;
    return window.location.pathname.startsWith(path) ? true : false;
}

export const redirect = (path: string) => {
    window.location.href = path;
}