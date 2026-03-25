package com.example.myapplication;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.databinding.ItemCartBinding;

import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {

    private List<OrderDetail> orderDetails;
    private AppDatabase db;
    private OnItemDeletedListener listener;

    public interface OnItemDeletedListener {
        void onItemDeleted();
    }

    public CartAdapter(List<OrderDetail> orderDetails, AppDatabase db, OnItemDeletedListener listener) {
        this.orderDetails = orderDetails;
        this.db = db;
        this.listener = listener;
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemCartBinding binding = ItemCartBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new CartViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        OrderDetail detail = orderDetails.get(position);
        Product product = db.productDao().getProductById(detail.getProductId());

        if (product != null) {
            holder.binding.tvProductName.setText(product.getName());
            holder.binding.tvProductPrice.setText(String.format("$%.2f", product.getPrice()));
            holder.binding.tvQuantity.setText("Số lượng: " + detail.getQuantity());

            holder.binding.btnDelete.setOnClickListener(v -> {
                db.orderDetailDao().delete(detail);
                orderDetails.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, orderDetails.size());
                if (listener != null) {
                    listener.onItemDeleted();
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return orderDetails == null ? 0 : orderDetails.size();
    }

    public void updateData(List<OrderDetail> newDetails) {
        this.orderDetails = newDetails;
        notifyDataSetChanged();
    }

    static class CartViewHolder extends RecyclerView.ViewHolder {
        ItemCartBinding binding;

        public CartViewHolder(ItemCartBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
