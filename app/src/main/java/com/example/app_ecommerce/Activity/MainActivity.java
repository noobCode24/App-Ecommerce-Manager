package com.example.app_ecommerce.Activity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.app_ecommerce.Adapter.ProductAdapter;
import com.example.app_ecommerce.Model.ProductModel;
import com.example.app_ecommerce.R;
import com.example.app_ecommerce.Retrofit.ApiEcommerce;
import com.example.app_ecommerce.Retrofit.RetrofitClient;
import com.example.app_ecommerce.utils.Utils;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {
    private ProductAdapter productAdapter;
    private List<ProductModel> productList;

    private RecyclerView rvPopularProducts;
    private CompositeDisposable compositeDisposable;
    private ApiEcommerce apiEcommerce;
    private ConstraintLayout categoryPC, categoryPhone, categoryHeadPhone, categoryGaming;
    private TextView seeAll;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        apiEcommerce = RetrofitClient.getInstance(Utils.BASE_URL).create(ApiEcommerce.class);
        Anhxa();

        if (isConnected(this)){
            getProducts();
            getEventClick();
        } else {
            Toast.makeText(getApplicationContext(), "Không có Internet, vui lòng kết nối!", Toast.LENGTH_LONG).show();
        }
    }

    private void getProducts() {
        compositeDisposable.add(apiEcommerce.getProduct()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        getProductModel -> {
                            if(getProductModel.isSuccess()) {
                                productList = getProductModel.getResult();
                                productAdapter = new ProductAdapter(getApplicationContext(),productList);
                                rvPopularProducts.setAdapter(productAdapter);
                            }
                        },
                        throwable -> {
                            Toast.makeText(getApplicationContext(), "Không thể kết nối được với server" + throwable.getMessage(), Toast.LENGTH_LONG).show();
                        }
                ));
    }

    private void Anhxa() {
        rvPopularProducts = findViewById(R.id.rvAllProducts);
        // Sử dụng LinearLayoutManager cho trượt ngang
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        rvPopularProducts.setLayoutManager(layoutManager);
        rvPopularProducts.setHasFixedSize(true);

        //khoi tao list
        productList = new ArrayList<>();
        compositeDisposable = new CompositeDisposable();

        //khoi tao category
        categoryPC = findViewById(R.id.categoryPC);
        categoryPhone = findViewById(R.id.categoryPhone);
        categoryHeadPhone = findViewById(R.id.categoryHeadPhone);
        categoryGaming = findViewById(R.id.categoryGaming);
        seeAll = findViewById(R.id.tvSeeAll);
    }

    private void getEventClick() {
        categoryPC. setOnClickListener(v -> {
            openCategoryActivity("PC");
        });

        // Xử lý click cho category Phone
        categoryPhone.setOnClickListener(v -> {
            openCategoryActivity("Phone");
        });

        // Xử lý click cho category HeadPhone
        categoryHeadPhone.setOnClickListener(v -> {
            openCategoryActivity("HeadPhone");
        });

        // Xử lý click cho category Gaming
        categoryGaming.setOnClickListener(v -> {
            openCategoryActivity("Gaming");
        });

        // Xử lý click cho "See All"
        seeAll.setOnClickListener(v -> {
            openCategoryActivity("All");
        });
    }

    // Mở categoryActivity với tham số category
    private void openCategoryActivity(String category){
        Intent intent = new Intent(MainActivity.this, CategoryActivity.class);
        intent.putExtra("category", category);
        startActivity(intent);
    }
    // kiem tra thiet bị co kết nối với internet hay không để lay du lieu tu db
    private boolean isConnected (Context context){
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifi = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI); // them quyen
        NetworkInfo mobile = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if (wifi != null && wifi.isConnected() ||(mobile != null && mobile.isConnected())) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    protected void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();
    }
}