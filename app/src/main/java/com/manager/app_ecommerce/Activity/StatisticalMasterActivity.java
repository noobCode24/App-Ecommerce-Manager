package com.manager.app_ecommerce.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.manager.app_ecommerce.R;

import soup.neumorphism.NeumorphCardView;

public class StatisticalMasterActivity extends AppCompatActivity {
    private NeumorphCardView CardProduct, CardMonth;
    private ImageView btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_statistical_master);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        initView();
        intControl();
    }

    private void intControl() {
        CardProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StatisticalMasterActivity.this, StatisticalActivity.class);
                startActivity(intent);
            }
        });

        btnBack.setOnClickListener(v -> {
           finish();
        });

        CardMonth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StatisticalMasterActivity.this, MonthlyStatisticsActivity.class);
                startActivity(intent);
                }
        });
    }

    private void initView() {
        CardProduct = findViewById(R.id.CardProduct);
        CardMonth = findViewById(R.id.CardMonth);
        btnBack = findViewById(R.id.btnBack);
    }
}