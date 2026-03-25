package com.example.myapplication;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import java.util.List;

@Dao
public interface OrderDao {
    @Insert
    long insert(Order order);

    @Query("SELECT * FROM orders WHERE userId = :userId LIMIT 1")
    Order getOrderByUserId(int userId);

    @Query("SELECT * FROM orders WHERE userId = :userId ORDER BY id DESC LIMIT 1")
    Order getLastOrderByUserId(int userId);
}
