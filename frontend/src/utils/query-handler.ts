export interface SearchType {
    filed: string;
    operator: '<' | '>' | '<=' | '>=' | '=' | '!' | '-' | ':';
    value: string;
}

export interface SortType {
    field: string;
    order: 'asc' | 'desc';
}


export const pageQueryHanlder = (page: number = 1, size: number = 40, 
    search?: SearchType[], sort?: SortType[]): string => {
    
    let query = `pageNo=${page}&size=${size}`;
    
    if (search && search.length > 0) {
        const searchString: string[] = search.map(s => s.filed + s.operator + s.value);
        searchString.forEach(s => query += `&search=${s}`)
    }
    
    if (sort && sort.length > 0) {
        const sortString: string[] = sort.map(s => s.field + ':' + s.order);
        sortString.forEach(s => query += `&sort=${s}`)
    }

    return query;


}