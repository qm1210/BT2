package com.example.myapplication;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.databinding.ItemCategoryFilterBinding;

import java.util.List;

public class CategoryFilterAdapter extends RecyclerView.Adapter<CategoryFilterAdapter.ViewHolder> {

    private final List<Category> categories;
    private final OnCategoryClickListener listener;
    private int selectedPosition = 0;

    public interface OnCategoryClickListener {
        void onCategoryClick(Category category);
    }

    public CategoryFilterAdapter(List<Category> categories, OnCategoryClickListener listener) {
        this.categories = categories;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemCategoryFilterBinding binding = ItemCategoryFilterBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Category category = categories.get(position);
        holder.binding.tvCategoryName.setText(category.getName());

        if (selectedPosition == position) {
            holder.binding.cardCategory.setCardBackgroundColor(Color.BLUE);
            holder.binding.tvCategoryName.setTextColor(Color.WHITE);
        } else {
            holder.binding.cardCategory.setCardBackgroundColor(Color.LTGRAY);
            holder.binding.tvCategoryName.setTextColor(Color.BLACK);
        }

        holder.itemView.setOnClickListener(v -> {
            int previousSelected = selectedPosition;
            selectedPosition = holder.getAdapterPosition();
            notifyItemChanged(previousSelected);
            notifyItemChanged(selectedPosition);
            listener.onCategoryClick(category);
        });
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ItemCategoryFilterBinding binding;

        public ViewHolder(ItemCategoryFilterBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
