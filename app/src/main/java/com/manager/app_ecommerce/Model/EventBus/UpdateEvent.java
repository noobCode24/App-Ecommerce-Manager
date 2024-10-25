package com.manager.app_ecommerce.Model.EventBus;

import com.manager.app_ecommerce.Model.ProductModel;

public class UpdateEvent {
    private ProductModel productModel;

    public UpdateEvent(ProductModel productModel) {
        this.productModel = productModel;
    }

    public ProductModel getProductModel() {
        return productModel;
    }

    public void setProductModel(ProductModel productModel) {
        this.productModel = productModel;
    }
}
