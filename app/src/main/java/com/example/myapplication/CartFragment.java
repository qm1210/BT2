package com.example.myapplication;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.myapplication.databinding.FragmentCartBinding;

import java.util.ArrayList;
import java.util.List;

public class CartFragment extends BaseFragment<FragmentCartBinding> {

    private AppDatabase db;
    private PreferenceManager preferenceManager;
    private CartAdapter adapter;
    private List<OrderDetail> orderDetails = new ArrayList<>();

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
            if (getActivity() instanceof MainActivity) {
                ((MainActivity) getActivity()).updateCartBadge();
            }
        });
        binding.rvCart.setAdapter(adapter);
    }

    @Override
    protected void initData() {
        loadCartData();
    }

    private void loadCartData() {
        if (!preferenceManager.isLoggedIn()) return;

        int userId = preferenceManager.getUserId();
        Order order = db.orderDao().getLastOrderByUserId(userId);
        
        if (order != null) {
            orderDetails = db.orderDetailDao().getOrderDetailsByOrderId(order.getId());
        } else {
            orderDetails = new ArrayList<>();
        }

        adapter.updateData(orderDetails);
        updateEmptyState();
    }

    private void updateEmptyState() {
        if (orderDetails == null || orderDetails.isEmpty()) {
            binding.rvCart.setVisibility(View.GONE);
            binding.layoutEmpty.setVisibility(View.VISIBLE);
        } else {
            binding.rvCart.setVisibility(View.VISIBLE);
            binding.layoutEmpty.setVisibility(View.GONE);
        }
    }
}
