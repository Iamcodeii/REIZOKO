package com.androiddevproject.foodorderingapp;

import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;

public class MyViewHolder_orderAdapter extends RecyclerView.ViewHolder {
    Button cancelOrder_btn;
    TextView grossTotal_tv;
    TextView orderTime_tv;
    LinearLayout ordrLayout;
    TextView status;
    TextView totalItem_tv;

    public MyViewHolder_orderAdapter(View view) {
        super(view);
        this.ordrLayout = (LinearLayout) view.findViewById(R.id.ordrsLayout);
        this.totalItem_tv = (TextView) view.findViewById(R.id.totalItemsORDR_tv);
        this.grossTotal_tv = (TextView) view.findViewById(R.id.grossTotalORDR_tv);
        this.status = (TextView) view.findViewById(R.id.ordrStatusPrsn_tv);
        this.orderTime_tv = (TextView) view.findViewById(R.id.ordrTimePrsn_tv);
        this.cancelOrder_btn = (Button) view.findViewById(R.id.cancelOrder_btn);
    }
}
