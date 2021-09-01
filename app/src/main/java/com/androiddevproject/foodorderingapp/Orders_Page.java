package com.androiddevproject.foodorderingapp;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class Orders_Page extends AppCompatActivity {
    DatabaseReference databaseReference;
    LinearLayoutManager layoutManager;
    LinearLayout no_internet_layout;
    LinearLayout no_orders_layout;
    FirebaseRecyclerAdapter<NewOrderInfo, MyViewHolder_orderAdapter> orders_adapter;
    FirebaseRecyclerOptions<NewOrderInfo> orders_options;
    DatabaseReference reference;
    RecyclerView rv_orders;
    Button try_again;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView((int) R.layout.orders__page);
        this.rv_orders = (RecyclerView) findViewById(R.id.rv_orders);
        this.no_orders_layout = (LinearLayout) findViewById(R.id.no_orders_layout);
        this.no_internet_layout = (LinearLayout) findViewById(R.id.no_internet_layout_myorders);
        this.try_again = (Button) findViewById(R.id.try_agian_btn_myorders);
        checkNetwork();
        this.try_again.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Orders_Page.this.checkNetwork();
            }
        });
        final String string = getSharedPreferences("USER", 0).getString("phone", "");
        DatabaseReference child = FirebaseDatabase.getInstance().getReference().child("Orders").child("OrdersChild");
        this.databaseReference = child;
        child.addValueEventListener(new ValueEventListener() {
            public void onCancelled(DatabaseError databaseError) {
            }

            public void onDataChange(DataSnapshot dataSnapshot) {
                if (!dataSnapshot.hasChild(string)) {
                    Orders_Page.this.rv_orders.setVisibility(View.GONE);
                    Orders_Page.this.no_orders_layout.setVisibility(View.VISIBLE);
                    no_internet_layout.setVisibility(View.GONE);
                    return;
                }
                Orders_Page.this.rv_orders.setVisibility(View.VISIBLE);
                Orders_Page.this.no_orders_layout.setVisibility(View.GONE);
                no_internet_layout.setVisibility(View.GONE);
            }
        });
        this.reference = FirebaseDatabase.getInstance().getReference().child("Orders").child("AllOrders");
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        this.layoutManager = linearLayoutManager;
        linearLayoutManager.setReverseLayout(true);
        this.layoutManager.setStackFromEnd(true);
        this.rv_orders.setLayoutManager(this.layoutManager);
        this.rv_orders.setHasFixedSize(true);
        load_ordersAdapter(string);
    }

    public void load_ordersAdapter(final String str) {
        this.databaseReference = FirebaseDatabase.getInstance().getReference().child("Orders").child("OrdersChild").child(str);
        this.orders_options = new FirebaseRecyclerOptions.Builder().setQuery((Query) this.databaseReference, NewOrderInfo.class).build();
        this.orders_adapter  = new FirebaseRecyclerAdapter<NewOrderInfo, MyViewHolder_orderAdapter>(this.orders_options) {
            /* access modifiers changed from: protected */
            public void onBindViewHolder(MyViewHolder_orderAdapter holder, int i, final NewOrderInfo newOrderInfo) {
                if (newOrderInfo.getOrderStatus().equals("In Queue") || newOrderInfo.getOrderStatus().equals("Received")) {
                    holder.cancelOrder_btn.setVisibility(View.VISIBLE);
                } else {
                    holder.cancelOrder_btn.setVisibility(View.GONE);
                }
                holder.orderTime_tv.setText(newOrderInfo.getOrderTime());
                holder.status.setText(newOrderInfo.getOrderStatus());
                TextView textView = holder.totalItem_tv;
                textView.setText(newOrderInfo.getTotalItems() + "");
                TextView textView2 = holder.grossTotal_tv;
                textView2.setText("$" + newOrderInfo.getGrossTotal());
                holder.cancelOrder_btn.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View view) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(Orders_Page.this);
                        builder.setTitle((CharSequence) "Confirm");
                        builder.setMessage((CharSequence) "Are you sure you want to cancel order?");
                        builder.setPositiveButton((CharSequence) "No", (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        });
                        builder.setNegativeButton((CharSequence) "Cancel Order", (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Orders_Page.this.databaseReference.child(newOrderInfo.getOrderId()).removeValue();
                                Orders_Page.this.reference.child(newOrderInfo.getOrderId()).child("orderStatus").setValue("Canceled");
                                Orders_Page.this.reference = FirebaseDatabase.getInstance().getReference().child("Orders").child("orderState").child("orderState");
                                Orders_Page.this.reference.setValue("old");
                                Orders_Page.this.reference = FirebaseDatabase.getInstance().getReference().child("Orders").child("OrdersPersonsInfo");
                                Orders_Page.this.reference.child(str).child("orderState").setValue("old");
                                Toast.makeText(Orders_Page.this, "Order Canceled", Toast.LENGTH_SHORT).show();
                                Orders_Page.this.finish();
                            }
                        });
                        builder.show();
                    }
                });
                holder.ordrLayout.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View view) {
                        Intent intent = new Intent(Orders_Page.this, Order_page.class);
                        intent.putExtra("phone", str);
                        intent.putExtra("id", newOrderInfo.getOrderId());
                        intent.putExtra("totalItems", newOrderInfo.getTotalItems() + "");
                        intent.putExtra("grossTotal", newOrderInfo.getGrossTotal());
                        Orders_Page.this.startActivity(intent);
                        Orders_Page.this.overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                    }
                });
            }

            public MyViewHolder_orderAdapter onCreateViewHolder(ViewGroup viewGroup, int i) {
                return new MyViewHolder_orderAdapter(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.order_layout_adapter, viewGroup, false));
            }
        };
//        this.orders_adapter = r0;
        orders_adapter.startListening();
        this.rv_orders.setAdapter(this.orders_adapter);
    }

    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    /* access modifiers changed from: private */
    public void checkNetwork() {
        if (((ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo() != null) {
            this.no_internet_layout.setVisibility(View.VISIBLE);
        } else {
            this.no_orders_layout.setVisibility(View.GONE);
            rv_orders.setVisibility(View.GONE);
        }
    }
}
