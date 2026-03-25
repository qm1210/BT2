package com.example.myapplication;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.myapplication.databinding.FragmentCartBinding;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class CartFragment extends BaseFragment<FragmentCartBinding> {

    private AppDatabase db;
    private PreferenceManager preferenceManager;
    private CartAdapter adapter;
    private List<OrderDetail> orderDetails = new ArrayList<>();
    private Order currentOrder;

    public static CartFragment newInstance() {
        return new CartFragment();
    }

    @Override
    protected FragmentCartBinding inflateBinding(@NonNull LayoutInflater inflater, @Nullable ViewGroup container) {
        return FragmentCartBinding.inflate(inflater, container, false);
    }

    @Override
    protected void initView() {
        db = AppDatabase.getInstance(requireContext());
        preferenceManager = new PreferenceManager(requireContext());

        binding.btnBack.setOnClickListener(v -> getParentFragmentManager().popBackStack());

        binding.rvCart.setLayoutManager(new LinearLayoutManager(requireContext()));
        adapter = new CartAdapter(orderDetails, db, () -> {
            updateEmptyState();
            updateTotalPrice();
            if (getActivity() instanceof MainActivity) {
                ((MainActivity) getActivity()).updateCartBadge();
            }
        });
        binding.rvCart.setAdapter(adapter);

        binding.btnCheckout.setOnClickListener(v -> handleCheckout());
    }

    private void handleCheckout() {
        if (currentOrder != null && !orderDetails.isEmpty()) {
            // 1. Cập nhật trạng thái Order thành PAID
            currentOrder.setStatus("PAID");
            db.orderDao().update(currentOrder);

            Toast.makeText(requireContext(), "Thanh toán thành công!", Toast.LENGTH_SHORT).show();

            // 2. Chuyển sang màn hình hóa đơn
            getParentFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, InvoiceFragment.newInstance(currentOrder.getId()))
                    .addToBackStack(null)
                    .commit();

            if (getActivity() instanceof MainActivity) {
                ((MainActivity) getActivity()).updateCartBadge();
            }
        }
    }

    @Override
    protected void initData() {
        loadCartData();
    }

    private void loadCartData() {
        if (!preferenceManager.isLoggedIn()) return;

        int userId = preferenceManager.getUserId();
        currentOrder = db.orderDao().getPendingOrderByUserId(userId);
        
        if (currentOrder != null) {
            orderDetails = db.orderDetailDao().getOrderDetailsByOrderId(currentOrder.getId());
        } else {
            orderDetails = new ArrayList<>();
        }

        adapter.updateData(orderDetails);
        updateEmptyState();
        updateTotalPrice();
    }

    private void updateTotalPrice() {
        double total = 0;
        for (OrderDetail detail : orderDetails) {
            Product product = db.productDao().getProductById(detail.getProductId());
            if (product != null) {
                total += product.getPrice() * detail.getQuantity();
            }
        }
        binding.tvTotalPrice.setText(String.format(Locale.getDefault(), "$%.2f", total));
    }

    private void updateEmptyState() {
        if (orderDetails == null || orderDetails.isEmpty()) {
            binding.rvCart.setVisibility(View.GONE);
            binding.layoutEmpty.setVisibility(View.VISIBLE);
            binding.layoutCheckout.setVisibility(View.GONE);
        } else {
            binding.rvCart.setVisibility(View.VISIBLE);
            binding.layoutEmpty.setVisibility(View.GONE);
            binding.layoutCheckout.setVisibility(View.VISIBLE);
        }
    }
}
