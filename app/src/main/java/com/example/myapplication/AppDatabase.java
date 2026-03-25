package com.example.myapplication;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {User.class, Category.class, Product.class, Order.class, OrderDetail.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    private static volatile AppDatabase instance;

    public abstract UserDao userDao();
    public abstract CategoryDao categoryDao();
    public abstract ProductDao productDao();

    public static AppDatabase getInstance(Context context) {
        if (instance == null) {
            synchronized (AppDatabase.class) {
                if (instance == null) {
                    instance = Room.databaseBuilder(context.getApplicationContext(),
                                    AppDatabase.class, "shop_database")
                            .fallbackToDestructiveMigration()
                            .allowMainThreadQueries() // Only for simplicity in this exercise
                            .build();
                }
            }
        }
        return instance;
    }
}
