export enum ConditionType {
    MINIMUM_PRICE = 'MINIMUM_PRICE',
    MINIMUM_QUANTITY = 'MINIMUM_QUANTITY',
    LIMIT_QUANTITY = 'LIMIT_QUANTITY',
}

export interface Condition {
    conditionType: ConditionType;
    minPrice: number;
    buyQuantity: number;
}