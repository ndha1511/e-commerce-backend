import { DiscountType, Promotion } from "../models/promotion";

export const convertPrice = (price: number = 0) => {
    return new Intl.NumberFormat('vi-VN', { style: 'currency', currency: 'VND' }).format(price);
}

export const calcPromotion = (price: number, promotion?: Promotion) => {
    if(!promotion) return convertPrice(price);
    if(promotion.discountType === DiscountType.AMOUNT) {
        return convertPrice(price - promotion.discountValue)
    }
    return convertPrice(price - (price * promotion.discountValue));
}

export const calcDiscountPrice = (price: number, promotion?: Promotion) => {
    if(!promotion) return convertPrice(0);
    if(promotion.discountType === DiscountType.AMOUNT) {
        return convertPrice(promotion.discountValue);
    }
    return convertPrice(price * promotion.discountValue);
}

export const calcPercentDiscount = (price: number, promotion?: Promotion) => {
    if(!promotion) return 0;
    if(promotion.discountType === DiscountType.PERCENT) {
        return promotion.discountValue * 100;
    }
    return (promotion.discountValue / price) * 100;
}