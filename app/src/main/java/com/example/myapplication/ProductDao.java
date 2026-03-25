package com.example.myapplication;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface ProductDao {
    @Query("SELECT * FROM products")
    List<Product> getAllProducts();

    @Query("SELECT * FROM products WHERE categoryId = :categoryId")
    List<Product> getProductsByCategory(int categoryId);

    @Query("SELECT * FROM products WHERE id = :productId LIMIT 1")
    Product getProductById(int productId);

    @Insert
    void insert(Product product);

    @Update
    void update(Product product);

    @Delete
    void delete(Product product);
}
