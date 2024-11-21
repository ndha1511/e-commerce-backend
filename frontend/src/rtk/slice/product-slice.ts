import { createSlice } from "@reduxjs/toolkit";
import { CreateProductDto } from "../../dtos/request/product/create-product.request";

const initialState: CreateProductDto = {
    productName: '',
    regularPrice: 0,
    categories: [],
    brandId: '',
    city: '',
    thumbnailIndex: 0,
    images: [],
    attributesDto: [],
    variantsDto: [],
    description: '',
    tag: [],
}
export const createProductSlice = createSlice({
    name: 'createProduct',
    initialState,
    reducers: {
        setProductName: (state, action) => {
            state.productName = action.payload;
        },
        setDescription: (state, action) => {
            state.description = action.payload;
        },
        setRegularPrice: (state, action) => {
            state.regularPrice = action.payload;
        },
        setCategories: (state, action) => {
            state.categories = action.payload;
        },
        addCategories: (state, action) => {
            state.categories.push(action.payload);
        },
        setBrandId: (state, action) => {
            state.brandId = action.payload;
        },
        setCity: (state, action) => {
            state.city = action.payload;
        },
        setThumbnailIndex: (state, action) => {
            state.thumbnailIndex = action.payload;
        },
        addImage: (state, action) => {
            state.images.push(action.payload);
        },
        setVideo: (state, action) => {
            state.video = action.payload;
        },
        addAttributeDto: (state, action) => {
            state.attributesDto[action.payload.index] = (action.payload.data);
        },
        setAttributeDto: (state, action) => {
            state.attributesDto = action.payload;
        },
        setAttributeValue: (state, action) => {
            const index = state.attributesDto.findIndex(item => item.attributeName === action.payload.attributeName);
            if (index !== -1) {
                state.attributesDto[index] = action.payload;
            }
        },
        addVariantDto: (state, action) => {
            state.variantsDto = [...state.variantsDto, action.payload];
        },
        setVariantDto: (state, action) => {
            state.variantsDto = action.payload;
        },
        addTag: (state, action) => {
            state.tag?.push(action.payload);
        },
        // Remove reducers
        removeImage: (state, action) => {
            state.images = state.images.filter((_, index) => index !== action.payload);
        },
        removeAttributeDto: (state, action) => {
            state.attributesDto = state.attributesDto.filter((_, index) => index !== action.payload);
        },
        removeVariantDto: (state, action) => {
            state.variantsDto = state.variantsDto.filter((_, index) => index !== action.payload);
        },
        removeTag: (state, action) => {
            state.tag = state.tag?.filter((_, index) => index !== action.payload);
        },
        removeAll: (state) => {
            state = initialState
            return state;
        },
        clearCategories: (state) => {
            state.categories = [];
        },
    }
})
export const { setProductName, setRegularPrice, setDescription,setAttributeDto,
    setCategories, setBrandId, setCity, addCategories,
    setThumbnailIndex, addImage, setVideo, setAttributeValue,
    clearCategories,
    addAttributeDto, addVariantDto, addTag, setVariantDto,
    removeImage, removeAttributeDto, removeVariantDto, removeTag,removeAll } = createProductSlice.actions
export default createProductSlice.reducer;