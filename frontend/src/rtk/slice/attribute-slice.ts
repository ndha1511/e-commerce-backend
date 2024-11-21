import { createSlice } from "@reduxjs/toolkit";
import { AttributeResponse } from "../../dtos/response/attribute-response";

const initialState: AttributeResponse = {
    attributes: [],
    variants: [],
}
export const Attribute = createSlice({
    name: 'attribute',
    initialState,
    reducers: {
        addAttribute: (state, action) => {
            state.attributes = action.payload.attributes;
            state.variants = action.payload.variants;
        }
    }
})
export const {addAttribute} = Attribute.actions
export default Attribute.reducer;