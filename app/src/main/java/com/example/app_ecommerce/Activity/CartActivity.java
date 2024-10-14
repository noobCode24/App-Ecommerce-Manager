package com.example.app_ecommerce.Activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.app_ecommerce.Adapter.CartAdapter;
import com.example.app_ecommerce.Model.ShoppingCart;
import com.example.app_ecommerce.R;
import com.example.app_ecommerce.utils.Utils;

import java.text.DecimalFormat;

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
        calculateCart();

        enableSwipeToDelete();
    }

    private void calculateCart() {
        double subTotal = 0;

        // Tính tổng giá trị của các sản phẩm trong giỏ hàng
        for (int i = 0; i < Utils.ShoppingCartList.size(); i++) {
            subTotal += Utils.ShoppingCartList.get(i).getPrice();
        }

        // Phí giao hàng cố định
        double delivery = 10.0;

        // Thuế 2% dựa trên tổng giá trị
        double percentTax = 0.02;
        double tax = Math.round(subTotal * percentTax * 100.0) / 100.0;

        // Tổng tiền (tổng sản phẩm + thuế + phí giao hàng)
        double total = Math.round((subTotal + tax + delivery) * 100.0) / 100.0;

        // Định dạng các giá trị
        DecimalFormat decimalFormatWithDecimal = new DecimalFormat("$#,##0.00");
        DecimalFormat decimalFormatWithoutDecimal = new DecimalFormat("$#,##0");

        // Kiểm tra nếu giá trị là số nguyên thì hiển thị không có phần thập phân
        txt_subtotal.setText(subTotal % 1 == 0 ? decimalFormatWithoutDecimal.format(subTotal) : decimalFormatWithDecimal.format(subTotal));
        txt_delivery.setText(delivery % 1 == 0 ? decimalFormatWithoutDecimal.format(delivery) : decimalFormatWithDecimal.format(delivery));
        txt_tax.setText(tax % 1 == 0 ? decimalFormatWithoutDecimal.format(tax) : decimalFormatWithDecimal.format(tax));
        txt_total.setText(total % 1 == 0 ? decimalFormatWithoutDecimal.format(total) : decimalFormatWithDecimal.format(total));
    }

    private void initControl() {
        // Xử lý sự kiện nhấn nút quay lại
        btnBack.setOnClickListener(v -> {
            Intent returnIntent = new Intent();
            returnIntent.putExtra("updated_cart_count", Utils.ShoppingCartList.size());
            setResult(RESULT_OK, returnIntent);
            finish();
        });

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
            cartAdapter = new CartAdapter(getApplicationContext(), Utils.ShoppingCartList, this::calculateCart); // Truyền listener
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

    private void enableSwipeToDelete() {
        ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false; // Không cần di chuyển item trong danh sách
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();

                // Hiển thị nút xoá và chờ người dùng click
                new Handler().postDelayed(() -> {
                    // Xử lý sự kiện click nút xoá
                    ShoppingCart removedItem = Utils.ShoppingCartList.get(position);

                    // Xóa sản phẩm khỏi danh sách giỏ hàng
                    Utils.ShoppingCartList.remove(position);
                    cartAdapter.notifyItemRemoved(position);

                    // Cập nhật lại tổng giá trị giỏ hàng
                    calculateCart();
                }, 100); // Đợi một thời gian ngắn trước khi xử lý click
            }

            @Override
            public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
                    View itemView = viewHolder.itemView;

                    // Giới hạn khoảng cách vuốt (dX) không vượt quá 50dp
                    float deleteButtonWidth = 350 * recyclerView.getContext().getResources().getDisplayMetrics().density; // 50dp in pixels
                    if (dX < -deleteButtonWidth) {
                        dX = -deleteButtonWidth;
                    }

                    // Vẽ nền đỏ cho phần hiển thị nút xoá
                    Paint p = new Paint();
                    p.setColor(Color.RED);
                    RectF background = new RectF(itemView.getRight() + dX, itemView.getTop(), itemView.getRight(), itemView.getBottom());
                    c.drawRect(background, p);

                    // Vẽ icon xóa
                    Bitmap icon = BitmapFactory.decodeResource(recyclerView.getContext().getResources(), R.drawable.ic_delete);
                    float iconMargin = (itemView.getHeight() - icon.getHeight()) / 2; // Căn giữa icon
                    float iconTop = itemView.getTop() + (itemView.getHeight() - icon.getHeight()) / 2;
                    float iconLeft = itemView.getRight() - iconMargin - icon.getWidth();
                    c.drawBitmap(icon, iconLeft, iconTop, p);

                    // Vuốt item theo cử chỉ vuốt của người dùng
                    super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
                }
            }
        };

        // Gắn ItemTouchHelper vào RecyclerView
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(recyclerViewCart);
    }
}