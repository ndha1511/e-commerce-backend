import { Button, Col, Row } from "react-bootstrap";
import { useGetAttributeByProductIdQuery } from "../../../services/product.service";
import { useState } from "react";

interface Props {
    productId: string;
    attributeValue1: string;
    attributeValue2?: string;
    updateAttribute: (attr1: string, attr2?: string) => void;
    close: () => void;
}

const ToggleAttribute = ({ productId, attributeValue1, attributeValue2, updateAttribute, close }: Props) => {
    const { data } = useGetAttributeByProductIdQuery(productId);
    const [stateAttrVal1, setStateAttrVal1] = useState(attributeValue1);
    const [stateAttrVal2, setStateAttrVal2] = useState(attributeValue2 || '');

    const changeOption = (newValue: string, index: number) => {
        if (index === 0) {
            setStateAttrVal1(newValue);
        } else {
            setStateAttrVal2(newValue);
        }
    }

    const handleUpdate = () => {
        if ((stateAttrVal1 !== attributeValue1) || (stateAttrVal2 !== attributeValue2)) {
            if (!attributeValue2) {
                updateAttribute(stateAttrVal1);
            } else {
                updateAttribute(stateAttrVal1, stateAttrVal2);
            }
        }
        close();
    }



    return (
        <div className="variant-options">
            <div className="options-container p-3 border bg-white">
                {data?.data.map((attribute, index) => {
                    let selectColor = '';
                    if (index === 0) {
                        selectColor = stateAttrVal1;
                    } else {
                        selectColor = stateAttrVal2;
                    }
                    return <div key={index}>
                        <Row className="mb-3">
                            <Col xs={4}><strong>{attribute.attributeName}</strong></Col>
                            <Col xs={8} className="d-flex gap-2">
                                {attribute.attributeValues.map((val, index1) => (
                                    <Button
                                        key={index1}
                                        variant={selectColor === val.value ? 'danger' : 'outline-secondary'}
                                        className={`text-normal variant-button ${selectColor === val.value ? 'selected' : ''}`}
                                        onClick={() => changeOption(val.value, index)}
                                    >
                                        {val.value}
                                    </Button>
                                ))}
                            </Col>
                        </Row>

                    </div>
                })}
                <div className="d-flex justify-content-between mt-4">
                    <Button variant="light" onClick={close}>
                        TRỞ LẠI
                    </Button>
                    <Button variant="danger" onClick={handleUpdate}>
                        XÁC NHẬN
                    </Button>
                </div>
            </div>
        </div>
    )
}

export default ToggleAttribute;