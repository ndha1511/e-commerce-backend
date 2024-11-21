export interface CategoryResponse{
    id: string;
    categoryName:string;
    image?: string;
    urlPath?: string;
    children: CategoryResponse[];
}