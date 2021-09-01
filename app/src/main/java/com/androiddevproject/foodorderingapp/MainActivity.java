package com.androiddevproject.foodorderingapp;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private ImageView home_btn, menu_btn, search_btn;
    Button tryAgain_btn;
    private FrameLayout  cart_btn;
    LinearLayout home_fragment, search_fragment, offline_frgmnt,categ_frgmnt,menu, submain_layout;
    CardView btm_nvigation;
    private LinearLayout myAccount_btn, myOrder_btn, orderByphone_btn;
    private RecyclerView  rv_categ,rv_home;
    Boolean menuStatus = false;
    public static TextView  cartQty_tv;
    TextView signOut_tv;
    ConstraintLayout home_rv_layout;

    //expandable list view



    //Firebase recycler ADapetr inits
    DatabaseReference databaseReference;
    FirebaseRecyclerOptions<Products> categ_options;
    FirebaseRecyclerAdapter<Products, MyViewHolder_categAdapter> categ_Adapter;

    //Firebase inits home adapter
    FirebaseRecyclerOptions<Home> home_options;
    FirebaseRecyclerAdapter<Home, MyViewHolder_homeAdapter> home_adapter;


    public static int qtyyy = 1;


    //databse inits
     GroceryDBHelper groceryDBHelper =new GroceryDBHelper(MainActivity.this);
    SQLiteDatabase sqLiteDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.slide_out_down);
        //the intializers for all the components
        init();


        //to set the important fragment visible
        home_fragment.setVisibility(View.VISIBLE);
        sqLiteDatabase = groceryDBHelper.getWritableDatabase();
        long count = DatabaseUtils.queryNumEntries(sqLiteDatabase, "CART");
        cartQty_tv.setText(count+"");


        //To check network connection
        checkNetworkConnection();


        //recycelrView Setup for Product Listing
        rv_categ.setLayoutManager(new GridLayoutManager(this,2));
        rv_categ.setHasFixedSize(true);
        //recyclerView setup for home listing
        rv_home.setLayoutManager(new LinearLayoutManager(this));
        rv_home.setHasFixedSize(true);


        //ClickListeners for buttom buttons
        home_btn.setOnClickListener(this);
        menu_btn.setOnClickListener(this);
        cart_btn.setOnClickListener(this);
        search_btn.setOnClickListener(this);

        tryAgain_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkNetworkConnection();

            }
        });


        myAccount_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (getSharedPreferences("USER", 0).contains("phone")) {
                    startActivity(new Intent(MainActivity.this, MyAccount_page.class));
                    overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                    return;
                }
                Intent intent = new Intent(MainActivity.this, UserAccountPage.class);
                intent.putExtra("from", "myaccount");
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            }
        });

        myOrder_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (getSharedPreferences("USER", 0).contains("phone")) {
                    startActivity(new Intent(MainActivity.this, Orders_Page.class));
                    overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                    return;
                }
                Intent intent2 = new Intent(MainActivity.this, UserAccountPage.class);
                intent2.putExtra("from", "myorders");
                startActivity(intent2);
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            }
        });

        orderByphone_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, order_by_phone_page.class));
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            }
        });




//        Exapandable list View starttup



    }



    private void init(){
        //the intializers for all the components
        home_btn = (ImageView) findViewById(R.id.home_btn);
        menu_btn = findViewById(R.id.grid_btn);
        search_btn = findViewById(R.id.search_btn);
        home_fragment = findViewById(R.id.home_frgmnt);
        cart_btn = findViewById(R.id.cart_btn);
        categ_frgmnt = findViewById(R.id.categ_fragment);
        offline_frgmnt = findViewById(R.id.offline_frgmnt);
        tryAgain_btn = findViewById(R.id.try_again_btn);
        menu = findViewById(R.id.menu);
        btm_nvigation = findViewById(R.id.btm_nvigation);
        search_fragment = findViewById(R.id.search_fragment);
        
        rv_categ = findViewById(R.id.categ_rv);
        cartQty_tv = findViewById(R.id.cartQty_tv);
        rv_home = findViewById(R.id.rv_home);
        home_rv_layout = findViewById(R.id.home_rv_layout);
        submain_layout = findViewById(R.id.subMain_layout);
        myAccount_btn = findViewById(R.id.myAccount_btn);
        myOrder_btn = findViewById(R.id.myOrders_btn);
        orderByphone_btn = findViewById(R.id.orderByPhone_btn);


        DrawableCompat.setTint(home_btn.getDrawable(), ContextCompat.getColor(this, R.color.afterColor));
        DrawableCompat.setTint(search_btn.getDrawable(), ContextCompat.getColor(this, R.color.beforeColor));
        categ_frgmnt.setVisibility(View.GONE);
        home_fragment.setVisibility(View.VISIBLE);
        search_fragment.setVisibility(View.GONE);
        load_homeAdapter();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.home_btn:
                categ_frgmnt.setVisibility(View.GONE);
                home_fragment.setVisibility(View.VISIBLE);
                search_fragment.setVisibility(View.GONE);

                DrawableCompat.setTint(home_btn.getDrawable(), ContextCompat.getColor(this, R.color.afterColor));
                DrawableCompat.setTint(search_btn.getDrawable(), ContextCompat.getColor(this, R.color.beforeColor));

                if (menuStatus){
//                    menu.setVisibility(View.GONE);
//                    menuStatus= false;

                    this.menu.animate().translationX((float) (-this.menu.getWidth())).alpha(1.0f).setDuration(500).setListener(new AnimatorListenerAdapter() {
                        public void onAnimationEnd(Animator animator) {
                            super.onAnimationEnd(animator);
                        }
                    });
                    this.menuStatus = false;
                    return;
                }
                checkNetworkConnection();
                load_homeAdapter();
                break;
            case R.id.grid_btn:


                if (menuStatus){
//                    menu.setVisibility(View.GONE);
//                    menuStatus= false;

                    this.menu.animate().translationX((float) (-this.menu.getWidth())).alpha(1.0f).setDuration(500).setListener(new AnimatorListenerAdapter() {
                        public void onAnimationEnd(Animator animator) {
                            super.onAnimationEnd(animator);
                        }
                    });
                    this.menuStatus = false;
                    return;
                }


                else {
//                    menu.setVisibility(View.VISIBLE);
//                    menuStatus= true;

                    this.menu.animate().translationX(0.0f).alpha(1.0f).setDuration(500).setListener((Animator.AnimatorListener) null);
                    this.menuStatus = true;
                }






                break;
            case R.id.cart_btn:
               startActivity(new Intent(this, CartActivity.class));
                overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);

                break;
            case R.id.search_btn:
                categ_frgmnt.setVisibility(View.GONE);
                home_fragment.setVisibility(View.GONE);
                search_fragment.setVisibility(View.VISIBLE);

                checkNetworkConnection();

                DrawableCompat.setTint(home_btn.getDrawable(), ContextCompat.getColor(this, R.color.beforeColor));
                DrawableCompat.setTint(search_btn.getDrawable(), ContextCompat.getColor(this, R.color.afterColor));
                if (menuStatus){
//                    menu.setVisibility(View.GONE);
//                    menuStatus= false;

                    this.menu.animate().translationX((float) (-this.menu.getWidth())).alpha(1.0f).setDuration(500).setListener(new AnimatorListenerAdapter() {
                        public void onAnimationEnd(Animator animator) {
                            super.onAnimationEnd(animator);
                        }
                    });
                    this.menuStatus = false;
                    return;
                }
                break;


        }

    }


    private void checkNetworkConnection(){
        ConnectivityManager manager=(ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork=manager.getActiveNetworkInfo();
        if (null!= activeNetwork){
            offline_frgmnt.setVisibility(View.GONE);
            submain_layout.setVisibility(View.VISIBLE);

        }else {
            offline_frgmnt.setVisibility(View.VISIBLE);
            submain_layout.setVisibility(View.GONE);
        }

    }










    @Override
    public void onBackPressed() {

        if (menuStatus){
            menu.setVisibility(View.GONE);
            menuStatus= false;
        }

        if (categ_frgmnt.getVisibility() != View.VISIBLE){
            final AlertDialog.Builder builder = new AlertDialog.Builder(this);

            builder.setTitle("Confirm");
            builder.setMessage("Are you sure you want to exit?");
            builder.setPositiveButton("Cancel", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    dialog.dismiss();
                }
            });
            builder.setNegativeButton("Yes", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    MainActivity.super.onBackPressed();
                }
            });
            builder.show();
        }else {
            categ_frgmnt.setVisibility(View.GONE);
            home_fragment.setVisibility(View.VISIBLE);
            search_fragment.setVisibility(View.GONE);

            DrawableCompat.setTint(home_btn.getDrawable(), ContextCompat.getColor(this, R.color.afterColor));
            DrawableCompat.setTint(search_btn.getDrawable(), ContextCompat.getColor(this, R.color.beforeColor));
            load_homeAdapter();
        }



    }

   public  void load_categAdapter(String rootText){

       categ_frgmnt.setVisibility(View.VISIBLE);
       home_fragment.setVisibility(View.GONE);
       search_fragment.setVisibility(View.GONE);

       DrawableCompat.setTint(home_btn.getDrawable(), ContextCompat.getColor(MainActivity.this, R.color.afterColor));
       DrawableCompat.setTint(search_btn.getDrawable(), ContextCompat.getColor(MainActivity.this, R.color.beforeColor));
       DrawableCompat.setTint(menu_btn.getDrawable(), ContextCompat.getColor(MainActivity.this, R.color.beforeColor));


        databaseReference = FirebaseDatabase.getInstance().getReference().child("Products").child(rootText);
        categ_options = new FirebaseRecyclerOptions.Builder<Products>().setQuery(databaseReference, Products.class).build();
        categ_Adapter = new FirebaseRecyclerAdapter<Products, MyViewHolder_categAdapter>(categ_options) {
            @Override
            protected void onBindViewHolder(@NonNull final MyViewHolder_categAdapter Holder, int i, @NonNull final Products products) {
                Picasso.with(MainActivity.this).load(products.getProductImage()).placeholder(R.drawable.loading_icon).into(Holder.iv_productImage);
                Holder.tv_productName.setText(products.getProductName());
                Holder.tv_productPrice.setText(String.valueOf(products.getProductPrice()));

                Holder.addcart_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        sqLiteDatabase =groceryDBHelper.getWritableDatabase();



                        
                        if (CheckIsProductAlreadyInCartorNot("CART","productId",products.getProductId())){
                            ContentValues values =new ContentValues();
                            values.put("productQty",Integer.parseInt(Holder.tv_productqty.getText().toString().trim()));
                            sqLiteDatabase.update("CART",values,"productId = ?",new String[]{products.getProductId()});
                            Toast.makeText(MainActivity.this, "Item Updated", Toast.LENGTH_SHORT).show();
                        }else {
                            ContentValues values =new ContentValues();
                            values.put("productName",products.getProductName());
                            values.put("productPrice", products.productPrice);
                            values.put("productQty",Integer.parseInt(Holder.tv_productqty.getText().toString().trim()));
                            values.put("productImage",products.getProductImage());
                            values.put("productId",products.getProductId());
                            sqLiteDatabase.insert("CART",null,values);
                            Toast.makeText(MainActivity.this, "Item added", Toast.LENGTH_SHORT).show();
                        }





                        long count = DatabaseUtils.queryNumEntries(sqLiteDatabase, "CART");
                        cartQty_tv.setText(count+"");

                        Holder.removeCart_layout.setVisibility(View.VISIBLE);
                        Holder.addcart_btn.setVisibility(View.GONE);
                        Holder.qtyDepart_layout.setVisibility(View.GONE);

                    }
                });

                Holder.removecart_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        sqLiteDatabase = groceryDBHelper.getWritableDatabase();
                        deleteProduct(products.getProductName(),"CART",sqLiteDatabase);

                        long count = DatabaseUtils.queryNumEntries(sqLiteDatabase, "CART");
                        cartQty_tv.setText(count+"");


                        Holder.removeCart_layout.setVisibility(View.GONE);
                        Holder.addcart_btn.setVisibility(View.VISIBLE);
                        Holder.qtyDepart_layout.setVisibility(View.VISIBLE);
                    }
                });

                Holder.addqty_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        qtyyy = Integer.parseInt(Holder.tv_productqty.getText().toString().trim());
                        qtyyy++;
                        Holder.tv_productqty.setText(qtyyy+"");
                        Holder.tv_productPrice.setText(Integer.parseInt(Holder.tv_productqty.getText().toString().trim()) * products.getProductPrice()+"");

                    }
                });
                Holder.minusqty_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        qtyyy = Integer.parseInt(Holder.tv_productqty.getText().toString().trim());
                        if (qtyyy == 1){
                            return;
                        }
                        qtyyy--;
                        Holder.tv_productqty.setText(qtyyy+"");
                        Holder.tv_productPrice.setText(Integer.parseInt(Holder.tv_productqty.getText().toString().trim()) * products.getProductPrice()+"");
                    }
                });

            }

            @NonNull
            @Override
            public MyViewHolder_categAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.product_layout,parent,false);
                return new MyViewHolder_categAdapter(view);
            }
        };
        categ_Adapter.startListening();
        rv_categ.setAdapter(categ_Adapter);

   }






    public void load_homeAdapter(){
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Products List");
        home_options = new FirebaseRecyclerOptions.Builder<Home>().setQuery(databaseReference, Home.class).build();
        home_adapter = new FirebaseRecyclerAdapter<Home, MyViewHolder_homeAdapter>(home_options) {
            @Override
            protected void onBindViewHolder(@NonNull MyViewHolder_homeAdapter holder, int i, @NonNull final Home home) {
                holder.cardTitle_tv.setText(home.getCardTitle());
                Picasso.with(MainActivity.this).load(home.cardImage).placeholder(R.drawable.loading_icon).into(holder.cardImage_iv);
                holder.card_bg.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        load_categAdapter(home.cardTitle);
                    }
                });

            }

            @NonNull
            @Override
            public MyViewHolder_homeAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.home_item_layout,parent,false);
                return new MyViewHolder_homeAdapter(view);
            }
        };
        home_adapter.startListening();
        rv_home.setAdapter(home_adapter);

    }







    public  void deleteProduct(String productName,String tableName, SQLiteDatabase sqLiteDatabase){
        sqLiteDatabase.delete(tableName,"productName = ?",new String[]{productName});



    }

    public boolean CheckIsProductAlreadyInCartorNot(String TableName,
                                               String dbfield, String fieldValue) {

        GroceryDBHelper groceryDBHelper =new GroceryDBHelper(this);
        sqLiteDatabase = groceryDBHelper.getWritableDatabase();
        String Query = "Select * from " + TableName + " where " + dbfield + " = "  + "'"+fieldValue+"'" ;
        Cursor cursor = sqLiteDatabase.rawQuery(Query,null);
        if(cursor.getCount() <= 0){
            cursor.close();
            return false;
        }
        cursor.close();
        return true;
    }




}
