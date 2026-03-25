package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.myapplication.databinding.FragmentProductDetailBinding;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ProductDetailFragment extends BaseFragment<FragmentProductDetailBinding> {

    private static final String ARG_PRODUCT_ID = "product_id";
    private int productId;
    private PreferenceManager preferenceManager;
    private AppDatabase db;

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
        preferenceManager = new PreferenceManager(requireContext());
        db = AppDatabase.getInstance(requireContext());

        binding.btnBack.setOnClickListener(v -> getParentFragmentManager().popBackStack());

        binding.btnAddToCart.setOnClickListener(v -> handleAddToCart());
    }

    private void handleAddToCart() {
        if (!preferenceManager.isLoggedIn()) {
            Toast.makeText(requireContext(), "Vui lòng đăng nhập để mua hàng", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(requireContext(), LoginActivity.class);
            startActivity(intent);
            return;
        }

        int userId = preferenceManager.getUserId();
        
        // 1. Tìm hoặc tạo Order cho User
        Order order = db.orderDao().getLastOrderByUserId(userId);
        int orderId;
        if (order == null) {
            String currentDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());
            Order newOrder = new Order(currentDate, userId);
            orderId = (int) db.orderDao().insert(newOrder);
        } else {
            orderId = order.getId();
        }

        // 2. Thêm sản phẩm vào OrderDetail
        OrderDetail existingDetail = db.orderDetailDao().getOrderDetail(orderId, productId);
        if (existingDetail != null) {
            existingDetail.setQuantity(existingDetail.getQuantity() + 1);
            db.orderDetailDao().update(existingDetail);
        } else {
            OrderDetail newDetail = new OrderDetail(orderId, productId, 1);
            db.orderDetailDao().insert(newDetail);
        }

        Toast.makeText(requireContext(), "Đã thêm vào giỏ hàng", Toast.LENGTH_SHORT).show();

        // Cập nhật badge ở MainActivity
        if (getActivity() instanceof MainActivity) {
            ((MainActivity) getActivity()).updateCartBadge();
        }
    }

    @Override
    protected void initData() {
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
