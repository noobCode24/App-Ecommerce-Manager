package com.manager.app_ecommerce.Retrofit;

import com.manager.app_ecommerce.Model.InvoiceModel;
import com.manager.app_ecommerce.Model.UserModel;
import com.manager.app_ecommerce.Model.getProductModel;

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

    @POST("sendEmail.php")
    @FormUrlEncoded
    Observable<UserModel> resetPass(
            @Field("email") String email
    );

    @POST("invoice.php")
    @FormUrlEncoded
    Observable<UserModel> createOrder(
            @Field("email") String email,
            @Field("mobile") String mobile,
            @Field("address") String address,
            @Field("quantity") int quantity,
            @Field("total_amount") double total_amount,
            @Field("users_id") int users_id,
            @Field("detail") String detail
    );

    @POST("getInvoice.php")
    @FormUrlEncoded
    Observable<InvoiceModel> getInvoice(
            @Field("users_id") int users_id
    );

    @POST("searchByCategory.php")
    @FormUrlEncoded
    Observable<getProductModel> searchByCategory(
            @Field("category_id") int category_id,
            @Field("search") String search
    );

    @POST("search.php")
    @FormUrlEncoded
    Observable<getProductModel> search(
            @Field("search") String search
    );
}
