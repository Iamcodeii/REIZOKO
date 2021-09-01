package com.androiddevproject.foodorderingapp;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MyViewHolder_homeAdapter extends RecyclerView.ViewHolder {

    ImageView  cardImage_iv;
    TextView cardTitle_tv;
    LinearLayout card_bg;
    public MyViewHolder_homeAdapter(@NonNull View itemView) {
        super(itemView);
        card_bg = itemView.findViewById(R.id.hmCard);
        cardImage_iv = itemView.findViewById(R.id.hmCard_iv);
        cardTitle_tv  = itemView.findViewById(R.id.hmTitle_tv);


    }
}
