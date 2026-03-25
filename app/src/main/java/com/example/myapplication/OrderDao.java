package com.example.myapplication;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import java.util.List;

@Dao
public interface OrderDao {
    @Insert
    long insert(Order order);

    @Update
    void update(Order order);

    @Query("SELECT * FROM orders WHERE userId = :userId AND status = 'PENDING' LIMIT 1")
    Order getPendingOrderByUserId(int userId);

    @Query("SELECT * FROM orders WHERE id = :orderId")
    Order getOrderById(int orderId);

    @Query("SELECT * FROM orders WHERE userId = :userId ORDER BY id DESC")
    List<Order> getAllOrdersByUserId(int userId);

    @Query("SELECT * FROM orders WHERE userId = :userId ORDER BY id DESC LIMIT 1")
    Order getLastOrderByUserId(int userId);
}
