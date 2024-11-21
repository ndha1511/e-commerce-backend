export interface Category{
    id: string;
    categoryName: string;
    image?: string;
    parentId?: string;
    children: number;
    level: number;
    inActive: boolean;
    urlPath:string;

}