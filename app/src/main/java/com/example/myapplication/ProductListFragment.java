package com.example.myapplication;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.myapplication.databinding.FragmentProductListBinding;

import java.util.ArrayList;
import java.util.List;

public class ProductListFragment extends BaseFragment<FragmentProductListBinding> {

    private static final String ARG_CATEGORY_ID = "category_id";
    private static final String ARG_CATEGORY_NAME = "category_name";

    private int categoryId;
    private String categoryName;
    private AppDatabase db;

    public static ProductListFragment newInstance(int categoryId, String categoryName) {
        ProductListFragment fragment = new ProductListFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_CATEGORY_ID, categoryId);
        args.putString(ARG_CATEGORY_NAME, categoryName);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            categoryId = getArguments().getInt(ARG_CATEGORY_ID);
            categoryName = getArguments().getString(ARG_CATEGORY_NAME);
        }
    }

    @Override
    protected FragmentProductListBinding inflateBinding(@NonNull LayoutInflater inflater, @Nullable ViewGroup container) {
        return FragmentProductListBinding.inflate(inflater, container, false);
    }

    @Override
    protected void initView() {
        binding.rvProducts.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.rvCategoriesFilter.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
    }

    @Override
    protected void initData() {
        db = AppDatabase.getInstance(requireContext());
        
        setupCategoryFilter();
        loadProducts(categoryId, categoryName);
    }

    private void setupCategoryFilter() {
        List<Category> categories = db.categoryDao().getAllCategories();
        
        // Ensure data exists for demo
        if (categories.isEmpty()) {
            db.categoryDao().insert(new Category("Electronics"));
            db.categoryDao().insert(new Category("Clothing"));
            db.categoryDao().insert(new Category("Books"));
            categories = db.categoryDao().getAllCategories();
            
            // Seed products too
            for (Category cat : categories) {
                db.productDao().insert(new Product(cat.getName() + " Product 1", 10.0, cat.getId()));
                db.productDao().insert(new Product(cat.getName() + " Product 2", 20.0, cat.getId()));
            }
        }

        List<Category> filterList = new ArrayList<>();
        Category allCategory = new Category("Tất cả");
        allCategory.setId(-1);
        filterList.add(allCategory);
        filterList.addAll(categories);

        CategoryFilterAdapter adapter = new CategoryFilterAdapter(filterList, category -> {
            loadProducts(category.getId(), category.getName());
        });
        binding.rvCategoriesFilter.setAdapter(adapter);
    }

    private void loadProducts(int id, String name) {
        binding.tvCategoryTitle.setText(name);
        List<Product> products;
        if (id == -1) {
            products = db.productDao().getAllProducts();
        } else {
            products = db.productDao().getProductsByCategory(id);
        }

        ProductAdapter adapter = new ProductAdapter(products, product -> {
            ProductDetailFragment fragment = ProductDetailFragment.newInstance(product.getId());
            getParentFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .addToBackStack(null)
                    .commit();
        });
        binding.rvProducts.setAdapter(adapter);
    }
}
