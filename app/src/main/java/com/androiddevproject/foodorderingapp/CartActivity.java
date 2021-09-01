package com.androiddevproject.foodorderingapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class CartActivity extends AppCompatActivity {
    public static LinearLayout empty_cart_layout;
    public static TextView grossTotal_tv;
    public static RecyclerView rv_cart;
    public static TextView totalItems_tv;
    FrameLayout checkOutbtn;
    SQLiteDatabase sqLiteDatabase;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView((int) R.layout.cart_activity);
        rv_cart = (RecyclerView) findViewById(R.id.rv_cart);
        this.checkOutbtn = (FrameLayout) findViewById(R.id.checkout_btn);
        empty_cart_layout = (LinearLayout) findViewById(R.id.empty_cart_layout);
        totalItems_tv = (TextView) findViewById(R.id.totalItems_tv);
        grossTotal_tv = (TextView) findViewById(R.id.grossTotal_tv);
        empty_cart_layout.setVisibility(View.GONE);
        rv_cart.setVisibility(View.VISIBLE);
        SQLiteDatabase writableDatabase = new GroceryDBHelper(this).getWritableDatabase();
        this.sqLiteDatabase = writableDatabase;
        final long queryNumEntries = DatabaseUtils.queryNumEntries(writableDatabase, "CART");
        loadCart_Adapter(this.sqLiteDatabase, this);
        if (queryNumEntries < 1) {
            empty_cart_layout.setVisibility(View.VISIBLE);
            rv_cart.setVisibility(View.GONE);
        }
        this.checkOutbtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (queryNumEntries >= 1) {
                    if (!CartActivity.this.checkNetworkConnection()) {
                        Toast.makeText(CartActivity.this, "No internet available", Toast.LENGTH_LONG).show();
                        return;
                    }
                    Intent intent = new Intent(CartActivity.this, UserAccountPage.class);
                    intent.putExtra("from", "cartpage");
                    CartActivity.this.startActivity(intent);
                    CartActivity.this.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                }
            }
        });
    }

    public static void loadCart_Adapter(SQLiteDatabase sQLiteDatabase, Context context) {
        rv_cart.setLayoutManager(new GridLayoutManager(context, 2));
        rv_cart.setHasFixedSize(true);
        CartAdapter cartAdapter = new CartAdapter(context, sQLiteDatabase.query("CART", (String[]) null, (String) null, (String[]) null, (String) null, (String) null, (String) null));
        rv_cart.setAdapter(cartAdapter);
        cartAdapter.notifyDataSetChanged();
    }

    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    /* access modifiers changed from: private */
    public boolean checkNetworkConnection() {
        return ((ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo() != null;
    }

    private boolean checkNetwork() {
        return ((ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo() != null;
    }
}
