package com.example.myapplication;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import java.util.List;

@Dao
public interface OrderDetailDao {
    @Insert
    void insert(OrderDetail orderDetail);

    @Update
    void update(OrderDetail orderDetail);

    @Delete
    void delete(OrderDetail orderDetail);

    @Query("SELECT * FROM order_details WHERE orderId = :orderId AND productId = :productId")
    OrderDetail getOrderDetail(int orderId, int productId);

    @Query("SELECT * FROM order_details WHERE orderId = :orderId")
    List<OrderDetail> getOrderDetailsByOrderId(int orderId);

    @Query("SELECT SUM(quantity) FROM order_details WHERE orderId = :orderId")
    Integer getTotalQuantityByOrderId(int orderId);
}
