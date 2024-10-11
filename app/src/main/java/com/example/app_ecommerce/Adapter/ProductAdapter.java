package com.example.app_ecommerce.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.app_ecommerce.Model.ProductModel;
import com.example.app_ecommerce.R;
import com.squareup.picasso.Picasso;

import java.util.List;
public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.MyViewHolder> {
    private List<ProductModel> productList;
    private Context context;

    public ProductAdapter(Context context, List<ProductModel> productList) {
        this.productList = productList;
        this.context = context;
    }

    @NonNull
    @Override
    public ProductAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View item = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_pop_list, parent, false);

        return new MyViewHolder(item);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductAdapter.MyViewHolder holder, int position) {
        ProductModel productModel = productList.get(position);
        holder.txt_title.setText(productModel.getProduct_name());
        holder.txt_stock.setText(String.valueOf(productModel.getStock_quantity()));
        holder.txt_price.setText(String.format("$%.2f", productModel.getPrice()));
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

//    public class viewHolder {
//        TextView productName, productPrice, productStock;
//        ImageView productImage;
//    }

//    @Override
//    public View getView(int position, View convertView, ViewGroup parent) {
//        viewHolder viewHolder = null;
//        if(convertView == null){
//            viewHolder = new viewHolder();
//            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//            convertView = layoutInflater.inflate(R.layout.viewholder_pop_list, null);
//            viewHolder.productName = convertView.findViewById(R.id.item_title);
//            viewHolder.productPrice = convertView.findViewById(R.id.txt_price);
//            viewHolder.productStock = convertView.findViewById(R.id.txtStock);
//            viewHolder.productImage = convertView.findViewById(R.id.item_image);
//            convertView.setTag(viewHolder);
//        } else{
//            viewHolder = (ProductAdapter.viewHolder) convertView.getTag();
//            viewHolder.productName.setText(productList.get(position).getProduct_name());
//            viewHolder.productPrice.setText(String.format("$%.2f", productList.get(position).getPrice()));
//            viewHolder.productStock.setText(String.valueOf(productList.get(position).getStock_quantity()));
//            Picasso.get().load(productList.get(position).getImage()).into(viewHolder.productImage);
//        }
//
//        return convertView;
//    }

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
