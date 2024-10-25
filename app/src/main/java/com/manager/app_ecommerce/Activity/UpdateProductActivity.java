package com.manager.app_ecommerce.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.manager.app_ecommerce.Adapter.CategoryAdapter;
import com.manager.app_ecommerce.Model.EventBus.DeleteEvent;
import com.manager.app_ecommerce.Model.EventBus.UpdateEvent;
import com.manager.app_ecommerce.Model.ProductModel;
import com.manager.app_ecommerce.R;
import com.manager.app_ecommerce.Retrofit.ApiEcommerce;
import com.manager.app_ecommerce.Retrofit.RetrofitClient;
import com.manager.app_ecommerce.utils.Utils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class UpdateProductActivity extends AppCompatActivity {
    private ImageView btnBack;
    private TextView txtEmpty;
    private ApiEcommerce apiEcommerce;
    private List<ProductModel> productList;
    private CategoryAdapter adapter;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private RecyclerView rvProducts;
    private ProductModel updateProduct1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_update_product);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        apiEcommerce = RetrofitClient.getInstance(Utils.BASE_URL).create(ApiEcommerce.class);
        initView();
        initControl();
        initData();
    }

    private void initData() {
        String productType = getIntent().getStringExtra("PRODUCT_TYPE");
        int loai = getCategoryId(productType);
        compositeDisposable.add(apiEcommerce.getProductByCategory(loai)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        getProductModel -> {
                            if(getProductModel.isSuccess()) {
                                productList = getProductModel.getResult();
                                adapter = new CategoryAdapter(this,productList, false);
                                rvProducts.setAdapter(adapter);
                                hindAndShowRV(true);
                            }else{
                                hindAndShowRV(true);
                            }
                        },
                        throwable -> {
                            Toast.makeText(getApplicationContext(), "Không thể kết nối được với server" + throwable.getMessage(), Toast.LENGTH_LONG).show();
                        }
                ));
    }
    private void hindAndShowRV(boolean b) {
        if (b) {
            rvProducts.setVisibility(View.VISIBLE);
            txtEmpty.setVisibility(View.GONE);
        } else {
            rvProducts.setVisibility(View.GONE);
            txtEmpty.setVisibility(View.VISIBLE);
        }
    }

    private void initControl() {
        btnBack.setOnClickListener(v -> {
            finish();
        });
    }

    private void initView() {
        btnBack = findViewById(R.id.btnBack);
        txtEmpty = findViewById(R.id.txtEmpty);
        rvProducts = findViewById(R.id.rvProducts);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this, 2);
        rvProducts.setLayoutManager(layoutManager);
        rvProducts.setHasFixedSize(true);
    }

    private int getCategoryId(String productType) {
        switch (productType) {
            case "PC":
                return 1;
            case "Phone":
                return 2;
            case "Headphone":
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

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        if (item.getGroupId() == 1) {
            UpdatePro();
        }
        return super.onContextItemSelected(item);
    }

    private void UpdatePro() {
        Intent intent = new Intent(UpdateProductActivity.this, UpdateProduct.class);
        intent.putExtra("update", updateProduct1);
        startActivity(intent);
    }

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void onUpdateEvent(UpdateEvent event) {
        if(event != null){
            updateProduct1 = event.getProductModel();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }
}