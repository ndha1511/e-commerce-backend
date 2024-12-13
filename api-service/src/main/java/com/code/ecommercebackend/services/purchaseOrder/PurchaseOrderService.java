package com.code.ecommercebackend.services.purchaseOrder;

import com.code.ecommercebackend.dtos.response.purchaseOrder.PurchaseDetail;
import com.code.ecommercebackend.exceptions.DataNotFoundException;
import com.code.ecommercebackend.models.PurchaseOrder;
import com.code.ecommercebackend.services.BaseService;
import com.fasterxml.jackson.core.JsonProcessingException;

public interface PurchaseOrderService extends BaseService<PurchaseOrder, String> {
    PurchaseDetail findByPurchaseOrderId(String purchaseOrderId) throws DataNotFoundException, JsonProcessingException;
}
