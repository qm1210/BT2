package com.example.myapplication;

import android.view.LayoutInflater;
import android.widget.Toast;

import com.example.myapplication.databinding.ActivityRegisterBinding;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class RegisterActivity extends BaseActivity<ActivityRegisterBinding> {

    private UserDao userDao;
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();

    @Override
    protected ActivityRegisterBinding inflateBinding(LayoutInflater inflater) {
        return ActivityRegisterBinding.inflate(inflater);
    }

    @Override
    protected void initView() {
        userDao = AppDatabase.getInstance(this).userDao();

        binding.btnRegister.setOnClickListener(v -> {
            String fullName = binding.etFullName.getText().toString().trim();
            String username = binding.etUsername.getText().toString().trim();
            String password = binding.etPassword.getText().toString().trim();

            if (fullName.isEmpty() || username.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                return;
            }

            register(fullName, username, password);
        });

        binding.tvLogin.setOnClickListener(v -> finish());
    }

    private void register(String fullName, String username, String password) {
        executorService.execute(() -> {
            User existingUser = userDao.getUserByUsername(username);
            if (existingUser != null) {
                runOnUiThread(() -> Toast.makeText(this, "Tên đăng nhập đã tồn tại", Toast.LENGTH_SHORT).show());
                return;
            }

            User newUser = new User(username, password, fullName);
            userDao.insert(newUser);
            runOnUiThread(() -> {
                Toast.makeText(this, "Đăng ký thành công", Toast.LENGTH_SHORT).show();
                finish();
            });
        });
    }

    @Override
    protected void initData() {

    }
}
