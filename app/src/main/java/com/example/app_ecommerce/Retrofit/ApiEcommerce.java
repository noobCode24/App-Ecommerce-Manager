package com.example.app_ecommerce.Retrofit;

import com.example.app_ecommerce.Model.UserModel;
import com.example.app_ecommerce.Model.getProductModel;

import io.reactivex.rxjava3.core.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface ApiEcommerce {
    @GET("getProduct.php")
    Observable<getProductModel> getProduct();

    @POST("getProductByCategory.php")
    @FormUrlEncoded
    Observable<getProductModel> getProductByCategory(
            @Field("loai") int loai
    );

    @POST("register.php")
    @FormUrlEncoded
    Observable<UserModel> register(
            @Field("user_name") String user_name,
            @Field("email") String email,
            @Field("pass") String pass,
            @Field("mobile") String mobile
    );

    @POST("login.php")
    @FormUrlEncoded
    Observable<UserModel> login(
            @Field("email") String email,
            @Field("pass") String pass
    );
}
