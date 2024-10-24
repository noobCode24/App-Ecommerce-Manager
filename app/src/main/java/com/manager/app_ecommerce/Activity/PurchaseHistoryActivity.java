package com.manager.app_ecommerce.Activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.manager.app_ecommerce.Adapter.InvoiceAdapter;
import com.manager.app_ecommerce.R;
import com.manager.app_ecommerce.Retrofit.ApiEcommerce;
import com.manager.app_ecommerce.Retrofit.RetrofitClient;
import com.manager.app_ecommerce.utils.Utils;
import com.google.gson.Gson;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class PurchaseHistoryActivity extends AppCompatActivity {
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private ApiEcommerce apiEcommerce;
    private RecyclerView recyclerViewPurchaseHistory;
    private ImageView btnBack;

    private TextView txtHistoryEmpty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_purchase_history);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initView();
        initControl();
        getInvoice();

    }

    private void initControl() {
        btnBack.setOnClickListener(v -> {finish();});
    }

    private void getInvoice() {
        compositeDisposable.add(apiEcommerce.getInvoice(Utils.user_current.getUser_id())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        invoiceModel -> {
                            // Log toàn bộ dữ liệu trả về từ API
                            Log.d("API Response", new Gson().toJson(invoiceModel));

                            if (invoiceModel.getResult() != null && !invoiceModel.getResult().isEmpty()) {
                                if (invoiceModel.getResult().get(0).getItem() != null && !invoiceModel.getResult().get(0).getItem().isEmpty()) {
                                    // Hiển thị sản phẩm nếu có
                                    InvoiceAdapter invoiceAdapter = new InvoiceAdapter(getApplicationContext(), invoiceModel.getResult());
                                    recyclerViewPurchaseHistory.setAdapter(invoiceAdapter);
                                    hindAndShowData(true);
                                } else {
                                    hindAndShowData(false);
                                }
                            } else {
                                hindAndShowData(false);
                            }
                        },
                        throwable -> {
                            Log.e("API Error", throwable.getMessage());
                        }
                ));
    }
    private void initView() {
        apiEcommerce = RetrofitClient.getInstance(Utils.BASE_URL).create(ApiEcommerce.class);
        recyclerViewPurchaseHistory = findViewById(R.id.recyclerViewPurchaseHistory);
        btnBack = findViewById(R.id.btnBack);
        txtHistoryEmpty = findViewById(R.id.txtHistoryEmpty);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerViewPurchaseHistory.setLayoutManager(layoutManager);
    }

    private void hindAndShowData(boolean hasData) {
        if (hasData) {
            recyclerViewPurchaseHistory.setVisibility(View.VISIBLE);
            txtHistoryEmpty.setVisibility(View.GONE);
        } else {
            recyclerViewPurchaseHistory.setVisibility(View.GONE);
            txtHistoryEmpty.setVisibility(View.VISIBLE);
        }
    }


    @Override
    protected void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();
    }
}