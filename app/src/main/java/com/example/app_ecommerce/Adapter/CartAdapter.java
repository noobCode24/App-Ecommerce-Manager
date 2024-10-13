package com.example.app_ecommerce.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.app_ecommerce.Model.ShoppingCart;
import com.example.app_ecommerce.R;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.MyViewHolder> {
    Context context;
    List<ShoppingCart> CartList;

    public CartAdapter(Context context, List<ShoppingCart> cartList) {
        CartList = cartList;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_cart, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        ShoppingCart shoppingCart = CartList.get(position);

        holder.txt_titleCart.setText(shoppingCart.getProduct_name());
        holder.numberItemTxt.setText(String.valueOf(shoppingCart.getQuantity()));
        DecimalFormat decimalFormatWithDecimal = new DecimalFormat("$#,##0.00");
        DecimalFormat decimalFormatWithoutDecimal = new DecimalFormat("$#,##0");
        if (shoppingCart.getOriginalPrice() % 1 == 0) {
            holder.txt_priceCart.setText(decimalFormatWithoutDecimal.format(shoppingCart.getOriginalPrice()));
            holder.txt_totalEachItem.setText(decimalFormatWithoutDecimal.format(shoppingCart.getPrice()));
        } else {
            holder.txt_priceCart.setText(decimalFormatWithDecimal.format(shoppingCart.getOriginalPrice()));
            holder.txt_totalEachItem.setText(decimalFormatWithDecimal.format(shoppingCart.getPrice()));
        }

        int imageResourceId = context.getResources().getIdentifier(shoppingCart.getImage(), "drawable", context.getPackageName());
        if (imageResourceId != 0) {
            holder.img_picCart.setImageResource(imageResourceId);
        } else {
            Picasso.get().load(shoppingCart.getImage()).into(holder.img_picCart);
        }
    }

    @Override
    public int getItemCount() {
        return CartList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        ImageView img_picCart;
        TextView txt_titleCart, txt_priceCart,txt_totalEachItem, numberItemTxt, minusCartBtn, plusCartBtn;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            img_picCart = itemView.findViewById(R.id.img_picCart);
            txt_titleCart = itemView.findViewById(R.id.txt_titleCart);
            txt_priceCart = itemView.findViewById(R.id.txt_priceCart);
            txt_totalEachItem = itemView.findViewById(R.id.txt_totalEachItem);
            numberItemTxt = itemView.findViewById(R.id.numberItemTxt);
            minusCartBtn = itemView.findViewById(R.id.minusCartBtn);
            plusCartBtn = itemView.findViewById(R.id.plusCartBtn);
        }
    }
}
