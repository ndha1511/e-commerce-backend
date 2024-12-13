package com.code.ecommercebackend.events;

import com.code.ecommercebackend.components.LocalDateTimeVN;
import com.code.ecommercebackend.models.BaseModel;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.BeforeConvertEvent;
import org.springframework.stereotype.Component;


@Component
public class BaseModelListener extends AbstractMongoEventListener<BaseModel> {
    @Override
    public void onBeforeConvert(BeforeConvertEvent<BaseModel> event) {
        BaseModel model = event.getSource();
        if(model.getCreatedAt() == null) {
            model.setCreatedAt(LocalDateTimeVN.now());
        }
        model.setUpdatedAt(LocalDateTimeVN.now());
    }

}
