package com.manager.app_ecommerce.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.manager.app_ecommerce.R;

public class WorkingWithHeadphone extends AppCompatActivity {
    private LinearLayout LayoutAdd, LayoutDelete, LayoutFix;
    private ImageView btnBack;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_working_with_head_phone);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initView();
        initControl();
    }

    private void initControl() {
        LayoutAdd.setOnClickListener(v -> {
            Intent intent = new Intent(WorkingWithHeadphone.this, AddPoductActivity.class);
            intent.putExtra("PRODUCT_TYPE", "Headphone");
            startActivity(intent);
        });
        LayoutDelete.setOnClickListener(v -> {
            Intent intent = new Intent(WorkingWithHeadphone.this, DeleteProductActivity.class);
            intent.putExtra("PRODUCT_TYPE", "Headphone");
            startActivity(intent);
        });
        LayoutFix.setOnClickListener(v -> {
            Intent intent = new Intent(WorkingWithHeadphone.this, UpdateProductActivity.class);
            intent.putExtra("PRODUCT_TYPE", "Headphone");
            startActivity(intent);
        });
        btnBack.setOnClickListener(v -> {
            finish();
        });
    }

    private void initView() {
        LayoutAdd = findViewById(R.id.LayoutAdd);
        btnBack = findViewById(R.id.btnBack);
        LayoutDelete = findViewById(R.id.LayoutDelete);
        LayoutFix = findViewById(R.id.LayoutFix);
    }
}