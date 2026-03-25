package com.example.myapplication;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.myapplication.databinding.FragmentProductDetailBinding;

public class ProductDetailFragment extends BaseFragment<FragmentProductDetailBinding> {

    private static final String ARG_PRODUCT_ID = "product_id";
    private int productId;

    public static ProductDetailFragment newInstance(int productId) {
        ProductDetailFragment fragment = new ProductDetailFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PRODUCT_ID, productId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            productId = getArguments().getInt(ARG_PRODUCT_ID);
        }
    }

    @Override
    protected FragmentProductDetailBinding inflateBinding(@NonNull LayoutInflater inflater, @Nullable ViewGroup container) {
        return FragmentProductDetailBinding.inflate(inflater, container, false);
    }

    @Override
    protected void initView() {
        binding.btnBack.setOnClickListener(v -> getParentFragmentManager().popBackStack());
    }

    @Override
    protected void initData() {
        AppDatabase db = AppDatabase.getInstance(requireContext());
        Product product = db.productDao().getProductById(productId);

        if (product != null) {
            binding.tvDetailName.setText("Name: " + product.getName());
            binding.tvDetailPrice.setText(String.format("Price: $%.2f", product.getPrice()));
            
            Category category = db.categoryDao().getCategoryById(product.getCategoryId());
            if (category != null) {
                binding.tvDetailCategory.setText("Category: " + category.getName());
            }
        }
    }
}
