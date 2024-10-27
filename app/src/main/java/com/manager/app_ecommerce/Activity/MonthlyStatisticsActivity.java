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

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
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

public class MonthlyStatisticsActivity extends AppCompatActivity {
    private ImageView btnBack;
    private BarChart barchart;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private ApiEcommerce apiEcommerce;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_monthly_statistics);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        apiEcommerce = RetrofitClient.getInstance(Utils.BASE_URL).create(ApiEcommerce.class);
        initView();
        intControl();
        getSettingData();
        getDataBarChart();
    }

    private void getDataBarChart() {
        compositeDisposable.add(apiEcommerce.monthlyStatistics()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        statisticalModel ->{
                            if (statisticalModel.isSuccess()){
                                List<BarEntry> list = new ArrayList<>();
                                Log.d("API Data", "Received data: " + statisticalModel.getResult().toString());
                                for(int i = 0; i < statisticalModel.getResult().size(); i++){
                                    double totalbymonth  = statisticalModel.getResult().get(i).getTotalbymonth();
                                    int month = statisticalModel.getResult().get(i).getMonth();
                                    Log.d("BarEntry", "totalbymonth " + totalbymonth + ", mounth: " + month);
                                    list.add(new BarEntry(month, (float) totalbymonth));
                                }
                                if (list.isEmpty()) {
                                    Log.e("PieChart", "No entries to display");
                                }

                                BarDataSet barDataSet = new BarDataSet(list, "Thống kê");
                                barDataSet.setColors(ColorTemplate.MATERIAL_COLORS);
                                barDataSet.setValueTextSize(14f);
                                barDataSet.setValueTextColor(Color.BLACK);

                                BarData data = new BarData(barDataSet);
                                barchart.setData(data);
                                barchart.animateXY(2000, 2000); // xoay
                                barchart.invalidate();
                            } else {
                                Log.e("API Data", "No data received or success is false");
                            }
                        },
                        throwable -> {
                            Log.e("API Error", "Error fetching data: " + throwable.getMessage());
                        }
                ));
    }

    private void getSettingData() {
        barchart.getDescription().setEnabled(false);
        barchart.setDrawValueAboveBar(false);
        XAxis xAxis = barchart.getXAxis();
        xAxis.setAxisMinimum(1);
        xAxis.setAxisMaximum(12);
        YAxis yAxisright = barchart.getAxisRight();
        yAxisright.setAxisMinimum(0);
        YAxis yAxisleft = barchart.getAxisLeft();
        yAxisleft.setAxisMinimum(0);
    }

    private void intControl() {
        btnBack.setOnClickListener(v -> {
            finish();
        });
    }

    private void initView() {
        btnBack = findViewById(R.id.btnBack);
        barchart = findViewById(R.id.barchart);
    }
}