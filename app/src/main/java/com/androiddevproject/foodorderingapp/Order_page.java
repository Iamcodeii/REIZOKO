package com.androiddevproject.foodorderingapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class Order_page extends AppCompatActivity {
    DatabaseReference databaseReference;
    TextView grossTotal_tv;
    FirebaseRecyclerAdapter<Order, MyViewHolder_Order> order_Adapter;
    FirebaseRecyclerOptions<Order> orders_options;
    RecyclerView rv_order;
    TextView totalItem_tv;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView((int) R.layout.order_page);
        this.rv_order = (RecyclerView) findViewById(R.id.rv_orderPRSN);
        this.totalItem_tv = (TextView) findViewById(R.id.totalItems_tvORDR);
        this.grossTotal_tv = (TextView) findViewById(R.id.grossTotal_tvORDR);
        this.rv_order.setLayoutManager(new LinearLayoutManager(this));
        this.rv_order.setHasFixedSize(true);
        Intent intent = getIntent();
        String stringExtra = intent.getStringExtra("phone");
        String stringExtra2 = intent.getStringExtra("id");
        String stringExtra3 = intent.getStringExtra("totalItems");
        float stringExtra4 = intent.getFloatExtra("grossTotal",0);
        this.totalItem_tv.setText(stringExtra3);
        this.grossTotal_tv.setText("$"+stringExtra4);
        load_orderAdapter(stringExtra, stringExtra2);
    }

    public void load_orderAdapter(String str, String str2) {
        this.databaseReference = FirebaseDatabase.getInstance().getReference().child("Orders").child("OrdersChild").child(str).child(str2).child("order");
        this.orders_options = new FirebaseRecyclerOptions.Builder().setQuery((Query) this.databaseReference, Order.class).build();
        order_Adapter  = new FirebaseRecyclerAdapter<Order, MyViewHolder_Order>(this.orders_options) {
            /* access modifiers changed from: protected */
            public void onBindViewHolder(MyViewHolder_Order myViewHolder_Order, int i, Order order) {
                myViewHolder_Order.name_tv.setText(order.getProductName());
                TextView textView = myViewHolder_Order.price_tv;
                textView.setText(order.getProductPrice() + "");
                TextView textView2 = myViewHolder_Order.qty_tv;
                textView2.setText(order.getProductQty() + "");
                TextView textView3 = myViewHolder_Order.priceTotal_tv;
                textView3.setText(order.getPriceTotal() + "");
            }

            public MyViewHolder_Order onCreateViewHolder(ViewGroup viewGroup, int i) {
                return new MyViewHolder_Order(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.order_layout_prsnadapter, viewGroup, false));
            }
        };
//        this.order_Adapter = order_Adapter;
        order_Adapter.startListening();
        this.rv_order.setAdapter(this.order_Adapter);
    }

    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }
}
