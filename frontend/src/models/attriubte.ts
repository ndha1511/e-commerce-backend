export interface Attribute {
    id: string;
    productId: string;
    attributeName: string;
    attributeValues: AttributeValue[];
}

export interface AttributeValue {
    image?: string;
    value: string;
}