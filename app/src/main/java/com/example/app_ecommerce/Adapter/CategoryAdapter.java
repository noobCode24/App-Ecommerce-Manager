package com.example.app_ecommerce.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.app_ecommerce.Model.ProductModel;
import com.example.app_ecommerce.R;

import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.MyViewHolder> {
    private List<ProductModel> productList;
    private Context context;

    public CategoryAdapter(Context context, List<ProductModel> productList) {
        this.productList = productList;
        this.context = context;
    }

    @NonNull
    @Override
    public CategoryAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View item = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_product_by_category_list, parent, false);

        return new MyViewHolder(item);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryAdapter.MyViewHolder holder, int position) {
        ProductModel productModel = productList.get(position);
        holder.txt_title.setText(productModel.getProduct_name());
        holder.txt_stock.setText(String.valueOf(productModel.getStock_quantity()));
        if (productModel.getPrice() % 1 == 0) {
            holder.txt_price.setText(String.format("$%.0f", productModel.getPrice()));
        } else {
            holder.txt_price.setText(String.format("$%.2f", productModel.getPrice()));
        }

        // Lấy ảnh từ drawable bằng resource ID
        int imageResourceId = context.getResources().getIdentifier(productModel.getImage(), "drawable", context.getPackageName());
        holder.item_image.setImageResource(imageResourceId);

        // lấy ảnh từ internet
        //Picasso.get().load(productList.get(position).getImage()).into(holder.item_image);
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        TextView txt_title, txt_price, txt_stock;
        ImageView item_image;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            txt_title = itemView.findViewById(R.id.item_title);
            txt_price = itemView.findViewById(R.id.txt_price);
            txt_stock = itemView.findViewById(R.id.txtStock);
            item_image = itemView.findViewById(R.id.item_image);
        }
    }
}
