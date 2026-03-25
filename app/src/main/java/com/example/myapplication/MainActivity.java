package com.example.myapplication;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import com.example.myapplication.databinding.ActivityMainBinding;

public class MainActivity extends BaseActivity<ActivityMainBinding> {

    private PreferenceManager preferenceManager;

    @Override
    protected ActivityMainBinding inflateBinding(LayoutInflater inflater) {
        return ActivityMainBinding.inflate(inflater);
    }

    @Override
    protected void initView() {
        preferenceManager = new PreferenceManager(this);
        
        updateLoginButton();

        binding.btnLogin.setOnClickListener(v -> {
            if (preferenceManager.isLoggedIn()) {
                preferenceManager.setLogin(false, -1, "");
                updateLoginButton();
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

    @Override
    protected void onResume() {
        super.onResume();
        updateLoginButton();
    }

    @Override
    protected void initData() {
    }
}
