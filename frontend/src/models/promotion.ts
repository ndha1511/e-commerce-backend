

export enum DiscountType {
    PERCENT = 'PERCENT',
    AMOUNT = 'AMOUNT',
}

export enum LoopState {
    NO_LOOP = 'NO_LOOP',
    DAILY = 'DAILY',
    WEEKLY = 'WEEKLY',
    MONTHLY = 'MONTHLY',
    ANNUAL = 'ANNUAL',
}



export interface Promotion {
    promotionName: string;
    discountType: DiscountType;
    discountValue: number;
    maxPrice?: number;
    startDate: Date;
    endDate: Date;
    loopType: LoopState;
    applyAll: boolean;
    applyFor?: string[];

}