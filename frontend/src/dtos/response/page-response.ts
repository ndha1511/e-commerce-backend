export interface PageResponse<T> {
    pageNumber: number;
    pageSize: number;
    totalPage: number;
    items: T[];

}