package com.androiddevproject.foodorderingapp;

import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MyViewHolder_categAdapter extends RecyclerView.ViewHolder {
    ImageView iv_productImage, addqty_btn, minusqty_btn;
    TextView tv_productName, tv_productPrice, tv_productqty;
    FrameLayout addcart_btn, removecart_btn, removeCart_layout;
    LinearLayout qtyDepart_layout;

    public MyViewHolder_categAdapter(@NonNull View itemView) {
        super(itemView);
        iv_productImage = itemView.findViewById(R.id.iv_productImage);
        addqty_btn = itemView.findViewById(R.id.addqty_btn);
        minusqty_btn = itemView.findViewById(R.id.minusqty_btn);
        tv_productName = itemView.findViewById(R.id.tv_productName);
        tv_productPrice = itemView.findViewById(R.id.tv_productPrice);
        tv_productqty = itemView.findViewById(R.id.productqty_tv);
        addcart_btn = itemView.findViewById(R.id.addcart_btn);
        removecart_btn = itemView.findViewById(R.id.removecart_btn);
        removeCart_layout = itemView.findViewById(R.id.removeCart_layout);
        qtyDepart_layout = itemView.findViewById(R.id.qtyDepart_layout);

    }
}
