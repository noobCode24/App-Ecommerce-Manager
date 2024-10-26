package com.manager.app_ecommerce.Activity;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.manager.app_ecommerce.Adapter.InvoiceAdapter;
import com.manager.app_ecommerce.Model.EventBus.InviceEvent;
import com.manager.app_ecommerce.Model.Invoice;
import com.manager.app_ecommerce.R;
import com.manager.app_ecommerce.Retrofit.ApiEcommerce;
import com.manager.app_ecommerce.Retrofit.RetrofitClient;
import com.manager.app_ecommerce.utils.Utils;
import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class PurchaseHistoryActivity extends AppCompatActivity {
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private ApiEcommerce apiEcommerce;
    private RecyclerView recyclerViewPurchaseHistory;
    private ImageView btnBack;
    private Invoice invoice;
    private TextView txtHistoryEmpty;
    private AlertDialog dialog;
    int status;

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
        compositeDisposable.add(apiEcommerce.getInvoice(0 )
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



    private void showDialog() {
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_invoice, null);
        Spinner spinner = view.findViewById(R.id.spinner_dialog);
        AppCompatButton btndongy = view.findViewById(R.id.dongy_dialog);
        List<String> list = new ArrayList<>();
        list.add("Chờ xác nhận");
        list.add("Đã xác nhận");
        list.add("Đang giao hàng");
        list.add("Đã giao hàng");
        list.add("Đã hủy");
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, list);
        spinner.setAdapter(adapter);
        spinner.setSelection(invoice.getStatus());
//        bat su kien khi thay doi
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                status = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        btndongy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateInvoice();
            }
        });
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(view);
        dialog = builder.create();
        dialog.show();
    }

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void eventInvoice(InviceEvent event){
        if (event != null ){
            invoice = event.getInvoice();
            showDialog();
        }
    }

    private void updateInvoice(){
        compositeDisposable.add(apiEcommerce.updateOrder(invoice.getId(), status)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        messageModel -> {
                            if (messageModel.isSuccess()){
                                getInvoice();
                                dialog.dismiss();
                            }
                        },
                        throwable -> {
                            Log.e("API Error", throwable.getMessage());
                        }
                ));
    }

    @Override
    protected void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();
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