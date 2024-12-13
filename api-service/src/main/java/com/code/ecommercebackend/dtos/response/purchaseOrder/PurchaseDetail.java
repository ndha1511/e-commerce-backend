package com.code.ecommercebackend.dtos.response.purchaseOrder;

import com.code.ecommercebackend.models.PurchaseOrder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class PurchaseDetail {
    private PurchaseOrder purchaseOrder;
    private List<PurchaseItem> purchaseItems;
}
