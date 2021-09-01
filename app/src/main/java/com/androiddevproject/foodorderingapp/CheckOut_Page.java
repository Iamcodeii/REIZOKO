package com.androiddevproject.foodorderingapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class CheckOut_Page extends AppCompatActivity {
    ConstraintLayout cod_layout, jazz_layout;

    TextView totalItemsORDR_tv, grossTotalORDR_tv;
    FrameLayout delivery_charge_layout;
    TextView shopMore_tv, deliveryCharges_tv, finalPrice_tv;
    ImageView jazzc_btn, cod_btn;
    TextView customerName_tv;
    FrameLayout placeOrder_btn;

    SQLiteDatabase sqLiteDatabase;
    GroceryDBHelper groceryDBHelper=new GroceryDBHelper(this);

    DatabaseReference databaseReferenceperson;
    DatabaseReference databaseReferenceordr;
    DatabaseReference reference;


    float finalPrice = (float) 0.0f;

    //notification inits
    RequestQueue mRequestQueue;
    String url= "https://fcm.googleapis.com/fcm/send";

    String latStr ="";
    String longtStr ="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_out__page);

        totalItemsORDR_tv = findViewById(R.id.totalItemsORDR_tv);
        grossTotalORDR_tv = findViewById(R.id.grossTotalORDR_tv);
        finalPrice_tv = findViewById(R.id.finalPrice_tv);
        placeOrder_btn = findViewById(R.id.placeOrder_btn);
        customerName_tv = findViewById(R.id.customerName_tv);

        mRequestQueue = Volley.newRequestQueue(this);

        Intent intent = getIntent();
        latStr = intent.getStringExtra("lat");
        longtStr = intent.getStringExtra("longt");


        sqLiteDatabase = groceryDBHelper.getWritableDatabase();

        SharedPreferences preferences=getSharedPreferences("USER",MODE_PRIVATE);
        final String name= preferences.getString("name","");
        final String phone=preferences.getString("phone","");
        customerName_tv.setText(name);



        final long count = DatabaseUtils.queryNumEntries(sqLiteDatabase, "CART");
        totalItemsORDR_tv.setText(count+"");
        final float tPrice= getSumTotalPrice(sqLiteDatabase);
        grossTotalORDR_tv.setText("$"+String.valueOf(tPrice));
        finalPrice= (float) (tPrice+5.0);
        finalPrice_tv.setText("$"+finalPrice);

        placeOrder_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                placeOrder_btn.setVisibility(View.GONE);
                databaseReferenceperson = FirebaseDatabase.getInstance().getReference().child("Orders").child("orderState");
                databaseReferenceperson.setValue(new OrderPerson("new"));
                SimpleDateFormat df= new SimpleDateFormat("dd-MM-yyyy HH:mm");
                String currentTime = df.format(Calendar.getInstance().getTime());
                OrderPerson orderPerson=new OrderPerson(phone,name,"new");
                databaseReferenceperson =FirebaseDatabase.getInstance().getReference().child("Orders").child("OrdersPersonsInfo").child(phone);
                databaseReferenceperson.setValue(orderPerson);
                long random = System.currentTimeMillis();
                final String orderId = "order"+random;
                databaseReferenceordr =FirebaseDatabase.getInstance().getReference().child("Orders").child("OrdersChild").child(phone).child(orderId);
                databaseReferenceordr.setValue(new NewOrderInfo("In Queue","new",orderId,currentTime,count,finalPrice));
                databaseReferenceordr =FirebaseDatabase.getInstance().getReference().child("Orders").child("OrdersChild").child(phone).child(orderId).child("order");
                reference =FirebaseDatabase.getInstance().getReference().child("Orders").child("AllOrders").child(orderId);
                reference.setValue(new OrderName("In Queue","new",orderId,phone,currentTime,count,finalPrice));
                reference= FirebaseDatabase.getInstance().getReference().child("Orders").child("AllOrders").child(orderId).child("order");

                //code to read data from sqlite and put it to firebase
                String selectQuery = "SELECT  * FROM " + "CART";
                sqLiteDatabase = groceryDBHelper.getWritableDatabase();
                final Cursor cursor      = sqLiteDatabase.rawQuery(selectQuery, null);
                if (cursor.moveToFirst()) {
                    do {
                        // get the data into array, or class variable
                        String id= cursor.getString(cursor.getColumnIndex("productId"));
                        String name = cursor.getString(cursor.getColumnIndex("productName"));
                        String image = cursor.getString(cursor.getColumnIndex("productImage"));
                        float price= cursor.getFloat(cursor.getColumnIndex("productPrice"));
                        long qty= cursor.getInt(cursor.getColumnIndex("productQty"));
                        float priceTotal= cursor.getFloat(cursor.getColumnIndex("priceTotal"));
                        Order order=new Order(name,image,id,price,qty,priceTotal);
                        databaseReferenceordr.child(id).setValue(order);
                        reference.child(id).setValue(order);



                    } while (cursor.moveToNext());
                }




                //code to send notification to admin panel
                //lets see what we can do !Good Luck
                SimpleDateFormat dfOrder= new SimpleDateFormat("HH:mm");
                String time = dfOrder.format(Calendar.getInstance().getTime());
                sendNotificationToAdminPanel(name,time,count,finalPrice);
                FirebaseMessaging.getInstance().subscribeToTopic(orderId);





                databaseReferenceordr =FirebaseDatabase.getInstance().getReference().child("Orders").child("OrdersChild").child(phone);
                databaseReferenceordr.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.hasChild(orderId)){




                            sqLiteDatabase.delete("CART",null,null);
                            sqLiteDatabase.execSQL("delete from CART");
                            cursor.close();


                            startActivity(new Intent(CheckOut_Page.this,MainActivity.class));
                            overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
                            Toast.makeText(CheckOut_Page.this, "Order Placed", Toast.LENGTH_SHORT).show();
                            finish();
                        }else {
                            Toast.makeText(CheckOut_Page.this, "error...Check Your Internet", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }


        });




    }





    public float getSumTotalPrice(SQLiteDatabase sqLiteDatabase){
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT SUM(" + "priceTotal" + ") as Total FROM " + "CART", null);

        if (cursor.moveToFirst()) {

            float total = cursor.getFloat(cursor.getColumnIndex("Total"));// get final total
            return total;
        }

        return 0;
    }

    @Override
    public void finish() {
        super.finish();
    }
    public void onBackPressed(){
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
    }

    private void sendNotificationToAdminPanel(String name,String time,long totalItems, float grossTotal){

        JSONObject mainObject=new JSONObject();
        try {
            mainObject.put("to","/topics/"+"order");
            JSONObject notificationObject= new JSONObject();
            notificationObject.put("title","New Order by "+name);
            notificationObject.put("body","@ "+time+" of "+totalItems+" Items "+" $"+grossTotal);
            mainObject.put("notification",notificationObject);


            JsonObjectRequest request=new JsonObjectRequest(Request.Method.POST, url, mainObject,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            }
            )
            {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String,String> header=new HashMap<>();
                    header.put("content-type","application/json");
                    header.put("authorization","key=AAAAxnnY2Eo:APA91bEkmyCeZzO_hGxOSuWw3hTyoZ6sDL_3yCi5y_oWkLcSIfCZ9JwjrBn_OPxlWkPZn9eS5Fy6kwU4FFI-rwrb_dasxGqZfoSYFmcT1idqVpWdgeTJHQ3mLQW42GVFQQEfpuuHSdz_");
                    return header;
                }
            };
            mRequestQueue.add(request);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
