package com.androiddevproject.foodorderingapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;

public class order_by_phone_page extends AppCompatActivity {
    FrameLayout phone_btn;
    FrameLayout whatsapp_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.order_by_phone_page);
        this.phone_btn = (FrameLayout) findViewById(R.id.orderbyPhone_btn);
        this.whatsapp_btn = (FrameLayout) findViewById(R.id.orderbyWhatsapp_btn);

        phone_btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                startActivity(new Intent("android.intent.action.DIAL", Uri.parse("tel:4169782011")));
            }
        });
        whatsapp_btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent("android.intent.action.VIEW");
                intent.setData(Uri.parse("https://api.whatsapp.com/send?phone=4169782011"));
                startActivity(intent);
            }
        });
    }
}