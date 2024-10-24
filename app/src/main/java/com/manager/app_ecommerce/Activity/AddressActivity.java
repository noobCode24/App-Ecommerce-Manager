package com.manager.app_ecommerce.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.manager.app_ecommerce.R;

public class AddressActivity extends AppCompatActivity {
    private Button btnSaveAddress;
    private ImageView btnBack;
    private EditText edtEnterAddress;

    private static final float ALPHA_DISABLED = 0.5f;
    private static final float ALPHA_ENABLED = 1.0f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_address);

        // Thiết lập insets cho view chính
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initView();
        setupControlEvents();
    }

    private void initView() {
        btnSaveAddress = findViewById(R.id.btnSaveAddress);
        btnBack = findViewById(R.id.btnBack);
        edtEnterAddress = findViewById(R.id.edtenteraddress);

        // Vô hiệu hóa nút Save ban đầu
        setSaveButtonState(false);
    }

    private void setupControlEvents() {
        setupBackButton();
        setupSaveButton();
        handleAddressInput();
    }

    // Xử lý sự kiện nút Back
    private void setupBackButton() {
        btnBack.setOnClickListener(v -> finish());
    }

    // Xử lý sự kiện nút Save
    private void setupSaveButton() {
        btnSaveAddress.setOnClickListener(v -> {
            String addressText = edtEnterAddress.getText().toString();

            // Trả lại địa chỉ nhập cho CartActivity
            Intent returnIntent = new Intent();
            returnIntent.putExtra("address", addressText);
            setResult(RESULT_OK, returnIntent);
            finish(); // Quay lại trang trước đó
        });
    }

    // Lắng nghe thay đổi văn bản trong ô nhập địa chỉ
    private void handleAddressInput() {
        edtEnterAddress.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                boolean isAddressEntered = !s.toString().trim().isEmpty();
                setSaveButtonState(isAddressEntered);
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    // Thiết lập trạng thái của nút Save
    private void setSaveButtonState(boolean isEnabled) {
        btnSaveAddress.setEnabled(isEnabled);
        btnSaveAddress.setAlpha(isEnabled ? ALPHA_ENABLED : ALPHA_DISABLED);
    }
}
