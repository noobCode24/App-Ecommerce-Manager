package com.manager.app_ecommerce.Model.EventBus;

import com.manager.app_ecommerce.Model.ProductModel;

public class DeleteEvent {
    private ProductModel productModel;

    public DeleteEvent(ProductModel productModel) {
        this.productModel = productModel;
    }

    public ProductModel getProductModel() {
        return productModel;
    }

    public void setProductModel(ProductModel productModel) {
        this.productModel = productModel;
    }
}
