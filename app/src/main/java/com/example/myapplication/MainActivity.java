package com.example.myapplication;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import com.example.myapplication.databinding.ActivityMainBinding;

public class MainActivity extends BaseActivity<ActivityMainBinding> {

    private PreferenceManager preferenceManager;
    private AppDatabase db;

    @Override
    protected ActivityMainBinding inflateBinding(LayoutInflater inflater) {
        return ActivityMainBinding.inflate(inflater);
    }

    @Override
    protected void initView() {
        preferenceManager = new PreferenceManager(this);
        db = AppDatabase.getInstance(this);
        
        updateLoginButton();
        updateCartBadge();

        binding.btnLogin.setOnClickListener(v -> {
            if (preferenceManager.isLoggedIn()) {
                preferenceManager.setLogin(false, -1, "");
                updateLoginButton();
                updateCartBadge();
            } else {
                startActivity(new Intent(this, LoginActivity.class));
            }
        });

        binding.btnCart.setOnClickListener(v -> {
            if (preferenceManager.isLoggedIn()) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, CartFragment.newInstance())
                        .addToBackStack(null)
                        .commit();
            } else {
                startActivity(new Intent(this, LoginActivity.class));
            }
        });

        if (getSupportFragmentManager().findFragmentById(R.id.fragment_container) == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_container, ProductListFragment.newInstance(-1, "Tất cả sản phẩm"))
                    .commit();
        }
    }

    private void updateLoginButton() {
        if (preferenceManager.isLoggedIn()) {
            binding.btnLogin.setText("Đăng xuất (" + preferenceManager.getUsername() + ")");
        } else {
            binding.btnLogin.setText("Đăng nhập");
        }
    }

    public void updateCartBadge() {
        if (!preferenceManager.isLoggedIn()) {
            binding.tvCartBadge.setVisibility(View.GONE);
            return;
        }

        int userId = preferenceManager.getUserId();
        Order order = db.orderDao().getLastOrderByUserId(userId);
        if (order != null) {
            Integer totalQuantity = db.orderDetailDao().getTotalQuantityByOrderId(order.getId());
            if (totalQuantity != null && totalQuantity > 0) {
                binding.tvCartBadge.setText(String.valueOf(totalQuantity));
                binding.tvCartBadge.setVisibility(View.VISIBLE);
            } else {
                binding.tvCartBadge.setVisibility(View.GONE);
            }
        } else {
            binding.tvCartBadge.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateLoginButton();
        updateCartBadge();
    }

    @Override
    protected void initData() {
    }
}
