package com.example.app_ecommerce.Activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.app_ecommerce.Model.ProductModel;
import com.example.app_ecommerce.R;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;

public class DetailActivity extends AppCompatActivity {
    TextView txt_title, txt_price, txt_desc, txt_rate, txt_soldquantity;
    ImageView img_pic, btnAddCart, btnBack;
    Button btnBuyNow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_detail);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        initView();
        ActionBack();
        initData();
    }

    private void initData() {
        ProductModel productModel = (ProductModel) getIntent().getSerializableExtra("Detail");
        txt_title.setText(productModel.getProduct_name());
        txt_desc.setText(productModel.getProduct_desc());
        DecimalFormat decimalFormatWithDecimal = new DecimalFormat("$#,##0.00"); // Định dạng với 2 chữ số thập phân
        DecimalFormat decimalFormatWithoutDecimal = new DecimalFormat("$#,##0"); // Định dạng không có chữ số thập phân

        // Kiểm tra nếu giá là số nguyên hay không
        if (productModel.getPrice() % 1 == 0) {
            // Hiển thị giá không có phần thập phân
            txt_price.setText(decimalFormatWithoutDecimal.format(productModel.getPrice()));
        } else {
            // Hiển thị giá có phần thập phân
            txt_price.setText(decimalFormatWithDecimal.format(productModel.getPrice()));
        }

        // Định dạng và hiển thị số lượng bán
        txt_soldquantity.setText(formatSoldQuantity(productModel.getSold_quantity()));

        // Định dạng và hiển thị rate
        DecimalFormat decimalFormatRating = new DecimalFormat("#,##0.0");
        txt_rate.setText(decimalFormatRating.format(productModel.getRating()));

        int imageResourceId = getResources().getIdentifier(productModel.getImage(), "drawable", getPackageName());

        if (imageResourceId != 0) {
            // Nếu có ảnh trong drawable, hiển thị nó
            img_pic.setImageResource(imageResourceId);
        } else {
            // Nếu không, lấy ảnh từ internet bằng Picasso
            Picasso.get().load(productModel.getImage()).into(img_pic);
        }
    }

    private String formatSoldQuantity(int quantity) {
        if (quantity >= 1000000) {
            // Nếu số lượng lớn hơn 1 triệu, hiển thị với đơn vị "m" (ví dụ: 1,2m)
            return String.format("%.1fm", quantity / 1000000.0);
        } else if (quantity >= 1000) {
            // Nếu số lượng lớn hơn 1 ngàn, hiển thị với đơn vị "k" (ví dụ: 1,1k)
            return String.format("%.1fk", quantity / 1000.0);
        } else {
            // Nếu nhỏ hơn 1000, hiển thị như bình thường
            return String.valueOf(quantity);
        }
    }

    private void ActionBack() {
        btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initView() {
        txt_title = findViewById(R.id.txt_title);
        txt_price = findViewById(R.id.txt_price);
        txt_desc = findViewById(R.id.txt_desc);
        img_pic = findViewById(R.id.img_pic);
        btnAddCart = findViewById(R.id.btnAddCart);
        btnBuyNow = findViewById(R.id.btnBuyNow);
        txt_rate = findViewById(R.id.txt_rate);
        txt_soldquantity = findViewById(R.id.txt_soldquantity);
    }
}