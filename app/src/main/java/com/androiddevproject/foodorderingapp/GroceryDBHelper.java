package com.androiddevproject.foodorderingapp;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class GroceryDBHelper extends SQLiteOpenHelper {
    public GroceryDBHelper(@Nullable Context context) {
        super(context, "DukanAsan.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE CART (productId TEXT, productName TEXT, productImage TEXT, productPrice REAL, productQty INTEGER,priceTotal REAL)";
        db.execSQL(sql);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
