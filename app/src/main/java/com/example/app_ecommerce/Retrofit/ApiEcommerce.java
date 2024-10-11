package com.example.app_ecommerce.Retrofit;

import com.example.app_ecommerce.Model.getProductModel;

import io.reactivex.rxjava3.core.Observable;
import retrofit2.http.GET;

public interface ApiEcommerce {
    @GET("getProduct.php")
    Observable<getProductModel> getProduct();
}
