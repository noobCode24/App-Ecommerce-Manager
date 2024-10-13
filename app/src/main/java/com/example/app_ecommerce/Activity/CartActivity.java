package com.example.app_ecommerce.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.app_ecommerce.Adapter.CartAdapter;
import com.example.app_ecommerce.R;
import com.example.app_ecommerce.utils.Utils;

public class CartActivity extends AppCompatActivity {
    private ImageView btnBack;
    private RecyclerView recyclerViewCart;
    private TextView txt_subtotal, txt_delivery, txt_tax, txt_total;
    private Button btnOrder;
    private CartAdapter cartAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_cart);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initView();
        initControl();
    }

    private void initControl() {
        // Xử lý sự kiện nhấn nút quay lại
        btnBack.setOnClickListener(v -> finish());

        // Cài đặt RecyclerView
        recyclerViewCart.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerViewCart.setLayoutManager(layoutManager);

        updateCartView();
    }

    private void updateCartView() {
        if (Utils.ShoppingCartList.isEmpty()) {
            // Ẩn tất cả các phần tử trong ScrollView trừ tiêu đề giỏ hàng
            recyclerViewCart.setVisibility(View.GONE);
        } else {
            // Hiển thị danh sách giỏ hàng
            recyclerViewCart.setVisibility(View.VISIBLE); // Hiển thị RecyclerView
            cartAdapter = new CartAdapter(getApplicationContext(), Utils.ShoppingCartList);
            recyclerViewCart.setAdapter(cartAdapter);
        }
    }

    private void initView() {
        btnBack = findViewById(R.id.btnBack);
        txt_subtotal = findViewById(R.id.txt_subtotal);
        txt_delivery = findViewById(R.id.txt_delivery);
        txt_tax = findViewById(R.id.txt_tax);
        txt_total = findViewById(R.id.txt_total);
        btnOrder = findViewById(R.id.btnOrder);
        recyclerViewCart = findViewById(R.id.recyclerViewCart);
    }
}