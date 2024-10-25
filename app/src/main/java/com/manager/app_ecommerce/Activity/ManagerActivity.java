package com.manager.app_ecommerce.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.manager.app_ecommerce.R;

import soup.neumorphism.NeumorphCardView;

public class ManagerActivity extends AppCompatActivity {
    private NeumorphCardView cardPC, cardPhone, cardHeadPhone, cardGamming, cardAccount;
    private ImageView btnBack;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_manager);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initView();
        initControl();
    }

    private void initControl() {
        cardPC.setOnClickListener(v -> {
            Intent intent = new Intent(ManagerActivity.this, WorkingWithPC.class);
            startActivity(intent);
        });

        cardPhone.setOnClickListener(v -> {
            Intent intent = new Intent(ManagerActivity.this, WorkingWithPhone.class);
            startActivity(intent);
        });

        cardHeadPhone.setOnClickListener(v -> {
            Intent intent = new Intent(ManagerActivity.this, WorkingWithHeadphone.class);
            startActivity(intent);
        });

        cardGamming.setOnClickListener(v -> {
            Intent intent = new Intent(ManagerActivity.this, WorkingWithGaming.class);
            startActivity(intent);
        });

        cardAccount.setOnClickListener(v -> {
            Intent intent = new Intent(ManagerActivity.this, WorkingWithAccount.class);
            startActivity(intent);
        });
        btnBack.setOnClickListener(v -> {
            finish();
        });
    }

    private void initView() {
        cardPC = findViewById(R.id.CardPC);
        cardPhone = findViewById(R.id.CardPhone);
        cardHeadPhone = findViewById(R.id.CardHeadPhone);
        cardGamming = findViewById(R.id.CardGamming);
        cardAccount = findViewById(R.id.CardAccount);
        btnBack = findViewById(R.id.btnBack);
    }
}