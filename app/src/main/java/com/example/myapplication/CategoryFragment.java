package com.example.myapplication;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.myapplication.databinding.FragmentCategoryBinding;

import java.util.List;

public class CategoryFragment extends BaseFragment<FragmentCategoryBinding> {

    @Override
    protected FragmentCategoryBinding inflateBinding(@NonNull LayoutInflater inflater, @Nullable ViewGroup container) {
        return FragmentCategoryBinding.inflate(inflater, container, false);
    }

    @Override
    protected void initView() {
        binding.rvCategories.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    @Override
    protected void initData() {
        AppDatabase db = AppDatabase.getInstance(requireContext());
        List<Category> categories = db.categoryDao().getAllCategories();

        if (categories.isEmpty()) {
            // Seed some data for testing
            db.categoryDao().insert(new Category("Electronics"));
            db.categoryDao().insert(new Category("Clothing"));
            db.categoryDao().insert(new Category("Books"));
            categories = db.categoryDao().getAllCategories();
        }

        CategoryAdapter adapter = new CategoryAdapter(categories, category -> {
            ProductListFragment fragment = ProductListFragment.newInstance(category.getId(), category.getName());
            getParentFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .addToBackStack(null)
                    .commit();
        });
        binding.rvCategories.setAdapter(adapter);
    }
}
