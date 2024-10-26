package com.manager.app_ecommerce.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.manager.app_ecommerce.Interface.ItemClickListener;
import com.manager.app_ecommerce.Model.EventBus.InviceEvent;
import com.manager.app_ecommerce.Model.Invoice;
import com.manager.app_ecommerce.R;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

public class InvoiceAdapter extends RecyclerView.Adapter<InvoiceAdapter.MyViewHolder> {
    private List<Invoice> listInvoice;
    private Context context;
    private RecyclerView.RecycledViewPool viewPool = new RecyclerView.RecycledViewPool();

    public InvoiceAdapter(Context context, List<Invoice> listInvoice) {
        this.context = context;
        this.listInvoice = listInvoice;
    }



    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_purchase_history_list, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Invoice invoice = listInvoice.get(position);
        holder.idInvoice.setText("Đơn hàng: " + invoice.getId());
        holder.idStatusInvoice.setText(statusInvoice(invoice.getStatus()));
        LinearLayoutManager layoutManager = new LinearLayoutManager(
                holder.recyclerview_Detail.getContext(),
                LinearLayoutManager.VERTICAL,
                false
        );
        layoutManager.setInitialPrefetchItemCount(invoice.getItem().size());
//        can phai co adapter cho chi tiet
        InvoiceDetailAdapter invoiceDetailAdapter = new InvoiceDetailAdapter(context, invoice.getItem());
        holder.recyclerview_Detail.setLayoutManager(layoutManager);
        holder.recyclerview_Detail.setAdapter(invoiceDetailAdapter);
        holder.recyclerview_Detail.setRecycledViewPool(viewPool);
        holder.setListener(new ItemClickListener() {
            @Override
            public void onClick(View view, int pos, boolean isLongclick) {
                if (isLongclick) {
                    EventBus.getDefault().postSticky(new InviceEvent(invoice));
                }
            }
        });
    }

    private String statusInvoice(int status){
        String result = "";
        switch (status){
            case 0:
                result = "Chờ xác nhận";
                break;
            case 1:
                result = "Đã xác nhận";
                break;
            case 2:
                result = "Đang giao hàng";
                break;
            case 3:
                result = "Đã giao hàng";
                break;
            case 4:
                result = "Đã hủy";
                break;
        }
        return result;
    }
    @Override
    public int getItemCount() {
        return listInvoice.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener {
        TextView idInvoice, idStatusInvoice;
        RecyclerView recyclerview_Detail;
        ItemClickListener listener;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            idInvoice = itemView.findViewById(R.id.idInvoice);
            recyclerview_Detail = itemView.findViewById(R.id.recyclerview_Detail);
            idStatusInvoice = itemView.findViewById(R.id.idStatus);
            itemView.setOnLongClickListener(this);
        }

        public void setListener(ItemClickListener listener) {
            this.listener = listener;
        }

        @Override
        public boolean onLongClick(View v) {
            listener.onClick(v, getAdapterPosition(), true);
            return false;
        }
    }

}
