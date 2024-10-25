package com.manager.app_ecommerce.Activity;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.material.textfield.TextInputEditText;
import com.manager.app_ecommerce.Model.MessageModel;
import com.manager.app_ecommerce.Model.ProductModel;
import com.manager.app_ecommerce.R;
import com.manager.app_ecommerce.Retrofit.ApiEcommerce;
import com.manager.app_ecommerce.Retrofit.RetrofitClient;
import com.manager.app_ecommerce.databinding.ActivityAddPoductBinding;
import com.manager.app_ecommerce.utils.Utils;

import java.io.File;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UpdateProduct extends AppCompatActivity {
    private ImageView btnCamera, btnBack;
    private TextView txtCategory;
    private TextInputEditText txtName, txtPrice, txtStockQuantity, txtImage, txtDes;
    private AppCompatButton btnUpdate;
    private ProductModel productModel;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private ApiEcommerce apiEcommerce;
    private String mediaPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_update_product2);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Intent intent = getIntent();
        initView();
        productModel = (ProductModel) intent.getSerializableExtra("update");
        txtDes.setText(productModel.getProduct_desc());
        txtName.setText(productModel.getProduct_name());
        txtPrice.setText(String.valueOf(productModel.getPrice()));
        txtStockQuantity.setText(String.valueOf(productModel.getStock_quantity()));
        txtImage.setText(productModel.getImage());
        switch (productModel.getCategory_id()){
            case 1:
                txtCategory.setText("Category: PC");
                break;
            case 2:
                txtCategory.setText("Category: Phone");
                break;
            case 3:
                txtCategory.setText("Category: Headphone");
                break;
            case 4:
                txtCategory.setText("Category: Gaming");
                break;
        }

        btnCamera.setOnClickListener(v -> {
            ImagePicker.with(UpdateProduct.this)
                    .crop()	    			//Crop image(Optional), Check Customization for more option
                    .compress(1024)			//Final image size will be less than 1 MB(Optional)
                    .maxResultSize(1080, 1080)	//Final image resolution will be less than 1080 x 1080(Optional)
                    .start();
        });

        btnBack.setOnClickListener(v -> {
            finish();
        });
        initControl();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mediaPath = data.getDataString();
        uploadMultipleFiles();
        Log.d("log", "onActivityResult" + mediaPath);
    }

    private String getPath(Uri uri) {
        String result;
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        if (cursor == null) {
            result = uri.getPath();
        } else {
            cursor.moveToFirst();
            int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            result = cursor.getString(index);
            cursor.close();
        }

        return result;
    }

    private void uploadMultipleFiles() {
        Uri uri = Uri.parse(mediaPath);
        // Map is used to multipart the file using okhttp3.RequestBody
        File file = new File(getPath(uri));
        // Parsing any Media type file
        RequestBody requestBody1 = RequestBody.create(MediaType.parse("*/*"), file);
        MultipartBody.Part fileToUpload1 = MultipartBody.Part.createFormData("file", file.getName(), requestBody1);

        Call<MessageModel> call = apiEcommerce.uploadFile(fileToUpload1);
        call.enqueue(new Callback< MessageModel >() {
            @Override
            public void onResponse(Call < MessageModel > call, Response< MessageModel > response) {
                MessageModel serverResponse = response.body();
                if (serverResponse != null) {
                    if (serverResponse.isSuccess()) {
                        txtImage.setText(serverResponse.getName());
                    } else {
                        Toast.makeText(getApplicationContext(), serverResponse.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Log.v("Response", serverResponse.toString());
                }

            }
            @Override
            public void onFailure(Call < MessageModel > call, Throwable t) {
                Log.d("log", t.getMessage());
            }
        });
    }


    private void initControl() {
        btnUpdate.setOnClickListener(v -> {
            updatePro();
        });
    }

    private void initView() {
        apiEcommerce = RetrofitClient.getInstance(Utils.BASE_URL).create(ApiEcommerce.class);
        txtDes = findViewById(R.id.txtDes);
        txtName = findViewById(R.id.txtName);
        txtPrice = findViewById(R.id.txtPrice);
        txtStockQuantity = findViewById(R.id.txtStockQuantity);
        txtImage = findViewById(R.id.txtImage);
        txtCategory = findViewById(R.id.txtCategory);
        btnUpdate = findViewById(R.id.btnUpdate);
        btnCamera = findViewById(R.id.btnCamera);
        btnBack = findViewById(R.id.btnBack);
    }

    private void updatePro() {
        String str_name = txtName.getText().toString().trim();
        String str_price = txtPrice.getText().toString().trim();
        String str_stock = txtStockQuantity.getText().toString().trim();
        String str_image = txtImage.getText().toString().trim();
        String str_des = txtDes.getText().toString().trim();
        
    try{
        double price = Double.parseDouble(str_price);
        int stockQuantity = Integer.parseInt(str_stock);

        compositeDisposable.add(apiEcommerce.updateProduct(productModel.getProduct_id(),str_name,productModel.getCategory_id(), price, stockQuantity, str_des, str_image)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        messageModel -> {
                            if (messageModel.isSuccess()) {
                                Toast.makeText(this, "Sửa sản phẩm thành công", Toast.LENGTH_SHORT).show();
                                finish();
                            } else {
                                Toast.makeText(this, "Sửa sản phẩm thất bại" + messageModel.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        },
                        throwable -> {
                            Toast.makeText(this, "Lỗi: " + throwable.getMessage(), Toast.LENGTH_LONG).show();
                        }
                ));
        }catch (NumberFormatException e){
            Toast.makeText(this, "Định dạng số không hợp lệ", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();
    }
}