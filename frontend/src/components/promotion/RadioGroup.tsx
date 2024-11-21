import React from "react";
import { PromotionType } from "../../models/promotion";

interface Option {
  id: string;
  value: PromotionType;
  label: string;
}

interface RadioGroupProps {
  options: Option[];
  selectedValue: PromotionType;
  onChange: (value: PromotionType) => void; // Hàm onChange nhận PromotionType
}

const RadioGroup: React.FC<RadioGroupProps> = ({ options, selectedValue, onChange }) => (
  <div className="d-flex gap-3">
    {options.map(({ id, value, label }) => (
      <div key={id} className="d-flex align-items-center gap-2">
        <input
          className="radio-insert-product-seller"
          type="radio"
          name="promotion-type"
          id={id}
          checked={selectedValue === value}
          onChange={() => onChange(value)} // Trả về giá trị PromotionType
        />
        <label htmlFor={id}>{label}</label>
      </div>
    ))}
  </div>
);

export default RadioGroup;
