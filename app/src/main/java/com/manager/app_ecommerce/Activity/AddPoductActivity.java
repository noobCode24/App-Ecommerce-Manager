package com.manager.app_ecommerce.Activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.textfield.TextInputEditText;
import com.manager.app_ecommerce.R;
import com.manager.app_ecommerce.Retrofit.ApiEcommerce;
import com.manager.app_ecommerce.Retrofit.RetrofitClient;
import com.manager.app_ecommerce.databinding.ActivityAddPoductBinding;
import com.manager.app_ecommerce.utils.Utils;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class AddPoductActivity extends AppCompatActivity {
    private TextView txtCategory;
    private TextInputEditText txtName, txtPrice, txtStockQuantity, txtImage, txtDes;
    private ActivityAddPoductBinding binding;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private ApiEcommerce apiEcommerce;
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
}