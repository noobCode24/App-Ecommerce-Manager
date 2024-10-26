package com.manager.app_ecommerce.Retrofit;

import com.manager.app_ecommerce.Model.InvoiceModel;
import com.manager.app_ecommerce.Model.MessageModel;
import com.manager.app_ecommerce.Model.UserModel;
import com.manager.app_ecommerce.Model.getProductModel;

import io.reactivex.rxjava3.core.Observable;
import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

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
            @Field("mobile") String mobile,
            @Field("uid") String uid
    );

    @POST("updateToken.php")
    @FormUrlEncoded
    Observable<MessageModel> updateToken(
            @Field("user_id") String user_id,
            @Field("token") String token
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

    @POST("delete.php")
    @FormUrlEncoded
    Observable<MessageModel> deleteProduct(
            @Field("product_id") int product_id
    );

    @POST("updateInvoice.php")
    @FormUrlEncoded
    Observable<MessageModel> updateOrder(
            @Field("id") int id,
            @Field("status") int status
    );

    @POST("insertProduct.php")
    @FormUrlEncoded
    Observable<MessageModel> insertProduct(
            @Field("product_name") String product_name,
            @Field("category_id") int category_id,
            @Field("price") double price,
            @Field("quantity") int quantity,
            @Field("description") String description,
            @Field("image") String image
    );

    @POST("updateProduct.php")
    @FormUrlEncoded
    Observable<MessageModel> updateProduct(
            @Field("product_id") int product_id,
            @Field("product_name") String product_name,
            @Field("category_id") int category_id,
            @Field("price") double price,
            @Field("quantity") int quantity,
            @Field("desc") String desc,
            @Field("image") String image
    );

    @Multipart
    @POST("upload.php")
    Call<MessageModel> uploadFile(@Part MultipartBody.Part file);
}
