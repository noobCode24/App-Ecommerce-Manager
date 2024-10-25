package com.manager.app_ecommerce.Activity;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.material.textfield.TextInputEditText;
import com.manager.app_ecommerce.Model.MessageModel;
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

public class AddPoductActivity extends AppCompatActivity {
    private TextView txtCategory;
    private TextInputEditText txtName, txtPrice, txtStockQuantity, txtImage, txtDes;
    private ActivityAddPoductBinding binding;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private ApiEcommerce apiEcommerce;
    private String mediaPath;
    int loai = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddPoductBinding.inflate(getLayoutInflater());
        apiEcommerce = RetrofitClient.getInstance(Utils.BASE_URL).create(ApiEcommerce.class);
        EdgeToEdge.enable(this);
        setContentView(binding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        initView();
        initData();
    }

    private void initData() {
        String productType = getIntent().getStringExtra("PRODUCT_TYPE");
        if (productType != null) {
            txtCategory.setText("Category: " + productType);
        }
        loai = getCategoryId(productType);

        binding.btnAdd.setOnClickListener(v -> {
            addProduct();
        });

        // thuc hien chuc nang them anh
        binding.btnCamera.setOnClickListener(v -> {
            ImagePicker.with(AddPoductActivity.this)
                    .crop()	    			//Crop image(Optional), Check Customization for more option
                    .compress(1024)			//Final image size will be less than 1 MB(Optional)
                    .maxResultSize(1080, 1080)	//Final image resolution will be less than 1080 x 1080(Optional)
                    .start();
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mediaPath = data.getDataString();
        uploadMultipleFiles();
        Log.d("log", "onActivityResult" + mediaPath);
    }

    private void addProduct() {
        String str_name = txtName.getText().toString().trim();
        String str_price = txtPrice.getText().toString().trim();
        String str_stock = txtStockQuantity.getText().toString().trim();
        String str_image = txtImage.getText().toString().trim();
        String str_des = txtDes.getText().toString().trim();

        if(TextUtils.isEmpty(str_name) || TextUtils.isEmpty(str_price) || TextUtils.isEmpty(str_stock) || TextUtils.isEmpty(str_image) || TextUtils.isEmpty(str_des)){
            Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_LONG).show();
        } else {
            try{
                double price = Double.parseDouble(str_price);
                int stockQuantity = Integer.parseInt(str_stock);

                compositeDisposable.add(apiEcommerce.insertProduct(str_name, loai, price, stockQuantity, str_des, str_image)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                messageModel -> {
                                    if (messageModel.isSuccess()) {
                                        Toast.makeText(this, "Thêm sản phẩm thành công", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(this, "Thêm sản phẩm thất bại", Toast.LENGTH_SHORT).show();
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
    }

    private void initView() {
        txtCategory = findViewById(R.id.txtCategory);
        txtName = findViewById(R.id.txtName);
        txtPrice = findViewById(R.id.txtPrice);
        txtStockQuantity = findViewById(R.id.txtStockQuantity);
        txtImage = findViewById(R.id.txtImage);
        txtDes = findViewById(R.id.txtDes);
    }

    private int getCategoryId(String productType) {
        switch (productType) {
            case "PC":
                return 1;
            case "Phone":
                return 2;
            case "HeadPhone":
                return 3;
            case "Gaming":
                return 4;
            default:
                return 0; // Trả về 0 nếu lấy tất cả sản phẩm
        }
    }

    @Override
    protected void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();
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
    // Uploading Image/Video
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
}