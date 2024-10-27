package com.manager.app_ecommerce.Activity;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.manager.app_ecommerce.R;
import com.manager.app_ecommerce.Retrofit.ApiEcommerce;
import com.manager.app_ecommerce.Retrofit.RetrofitClient;
import com.manager.app_ecommerce.utils.Utils;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class StatisticalActivity extends AppCompatActivity {
    private ImageView btnBack;
    private PieChart pieChart;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private ApiEcommerce apiEcommerce;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_statistical);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        apiEcommerce = RetrofitClient.getInstance(Utils.BASE_URL).create(ApiEcommerce.class);
        initView();
        intControl();
        getData();
    }

    private void getData() {
        List<PieEntry> list = new ArrayList<>();
        compositeDisposable.add(apiEcommerce.Statistical()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                   statisticalModel -> {
                       if (statisticalModel.isSuccess()){
                           Log.d("API Data", "Received data: " + statisticalModel.getResult().toString());
                           for(int i = 0; i < statisticalModel.getResult().size(); i++){
                                String nameProduct = statisticalModel.getResult().get(i).getProduct_name();
                                int total = statisticalModel.getResult().get(i).getTotal();
                               Log.d("PieEntry", "Product: " + nameProduct + ", Total: " + total);
                               list.add(new PieEntry(total,nameProduct));
                           }
                           if (list.isEmpty()) {
                               Log.e("PieChart", "No entries to display");
                           }

                           PieDataSet pieDataSet = new PieDataSet(list, "Thống kê");
                           PieData data = new PieData();
                           data.setDataSet(pieDataSet);
                           data.setValueTextSize(12f);
                           data.setValueFormatter(new PercentFormatter());
                           pieDataSet.setColors(ColorTemplate.MATERIAL_COLORS);

                           pieChart.setData(data);
                           pieChart.animateXY(2000, 2000); // xoay
                           pieChart.setUsePercentValues(true);
                           pieChart.getDescription().setEnabled(false);
                           pieChart.invalidate();
                       } else {
                           Log.e("API Data", "No data received or success is false");
                       }
                   },
                    throwable -> {
                        Log.e("API Error", "Error fetching data: " + throwable.getMessage());
                    }
                ));
    }

    private void intControl() {
        btnBack.setOnClickListener(v -> finish());
    }

    private void initView() {
        btnBack = findViewById(R.id.btnBack);
        pieChart = findViewById(R.id.piechar);
    }
}