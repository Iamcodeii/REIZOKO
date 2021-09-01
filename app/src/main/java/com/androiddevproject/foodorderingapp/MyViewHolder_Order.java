package com.androiddevproject.foodorderingapp;

import android.view.View;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;

public class MyViewHolder_Order extends RecyclerView.ViewHolder {
    TextView name_tv;
    TextView priceTotal_tv;
    TextView price_tv;
    TextView qty_tv;

    public MyViewHolder_Order(View view) {
        super(view);
        this.name_tv = (TextView) view.findViewById(R.id.orderProductName_tv);
        this.price_tv = (TextView) view.findViewById(R.id.orderProductPrice_tv);
        this.qty_tv = (TextView) view.findViewById(R.id.orderProductQty_tv);
        this.priceTotal_tv = (TextView) view.findViewById(R.id.orderProductPriceTotal_tv);
    }
}
