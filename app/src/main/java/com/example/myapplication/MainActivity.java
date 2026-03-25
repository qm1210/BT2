package com.example.myapplication;

import android.view.LayoutInflater;
import com.example.myapplication.databinding.ActivityMainBinding;

public class MainActivity extends BaseActivity<ActivityMainBinding> {

    @Override
    protected ActivityMainBinding inflateBinding(LayoutInflater inflater) {
        return ActivityMainBinding.inflate(inflater);
    }

    @Override
    protected void initView() {
        // Initialize your views here
    }

    @Override
    protected void initData() {
        // Initialize your data here
    }
}
