package com.example.app_ecommerce.Activity;

import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.app_ecommerce.Adapter.CategoryAdapter;
import com.example.app_ecommerce.Adapter.ProductAdapter;
import com.example.app_ecommerce.Model.ProductModel;
import com.example.app_ecommerce.R;
import com.example.app_ecommerce.Retrofit.ApiEcommerce;
import com.example.app_ecommerce.Retrofit.RetrofitClient;
import com.example.app_ecommerce.utils.Utils;

import java.security.PrivateKey;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class CategoryActivity extends AppCompatActivity {
    private RecyclerView rvAllProducts;
    private ApiEcommerce apiEcommerce;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private List<ProductModel> productList;
    private CategoryAdapter categoryAdapter;
    private int currentPage = 1;
    private int totalItems = 0; // Tổng số sản phẩm đã tải
    private static final int ITEMS_PER_PAGE = 6; // Số sản phẩm trên mỗi trang



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_category);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        apiEcommerce = RetrofitClient.getInstance(Utils.BASE_URL).create(ApiEcommerce.class);
        Anhxa();
        getData();

    }

    private void getData() {
        String category = getIntent().getStringExtra("category");

        if (category.equals("All")){
            // Lấy tất cả sản phẩm
            compositeDisposable.add(apiEcommerce.getProduct()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                            getProductModel -> {
                                if(getProductModel.isSuccess()) {
                                    // Cập nhật danh sách sản phẩm ở đây
                                    productList = getProductModel.getResult();
                                    categoryAdapter = new CategoryAdapter(this,productList);
                                    rvAllProducts.setAdapter(categoryAdapter);
                                    // Cập nhật adapter ở đây
                                }
                            },
                            throwable -> {
                                Toast.makeText(getApplicationContext(), "Không thể kết nối được với server" + throwable.getMessage(), Toast.LENGTH_LONG).show();
                            }
                    ));
        } else {
            int loai = getCategoryId(category);
            compositeDisposable.add(apiEcommerce.getProductByCategory(loai)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                            getProductModel -> {
                                if(getProductModel.isSuccess()) {
                                    productList = getProductModel.getResult();
                                    categoryAdapter = new CategoryAdapter(this,productList);
                                    rvAllProducts.setAdapter(categoryAdapter);
                                }
                            },
                            throwable -> {
                                Toast.makeText(getApplicationContext(), "Không thể kết nối được với server" + throwable.getMessage(), Toast.LENGTH_LONG).show();
                            }
                    ));
        }
    }

    private int getCategoryId(String category) {
        switch (category) {
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

    private void Anhxa() {
        rvAllProducts = findViewById(R.id.rvAllProducts);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this, 2);
        rvAllProducts.setLayoutManager(layoutManager);
        rvAllProducts.setHasFixedSize(true);
    }

    @Override
    protected void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();
    }
}