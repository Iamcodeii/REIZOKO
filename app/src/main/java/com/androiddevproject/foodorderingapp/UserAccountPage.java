package com.androiddevproject.foodorderingapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class UserAccountPage extends AppCompatActivity {
    AutoCompleteTextView address_box;
    TextView alreadyMember_tv;
    Button crt_btn;
    Button login_btn;
    DatabaseReference mDatabse;
    AutoCompleteTextView name_box;
    AutoCompleteTextView phoneLgn_box;
    AutoCompleteTextView phone_box;
    LinearLayout signIn_layout;
    TextView signIn_tv;
    LinearLayout signUp_layout;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView((int) R.layout.user_account_page);
        this.phoneLgn_box = (AutoCompleteTextView) findViewById(R.id.phonLogin_box);
        this.name_box = (AutoCompleteTextView) findViewById(R.id.fNameCRT_box);
        this.phone_box = (AutoCompleteTextView) findViewById(R.id.phoneCRT_box);
        this.address_box = (AutoCompleteTextView) findViewById(R.id.addressCRT_box);
        this.login_btn = (Button) findViewById(R.id.btn_login);
        this.crt_btn = (Button) findViewById(R.id.CRTbtn);
        this.alreadyMember_tv = (TextView) findViewById(R.id.alreadyMember_tv);
        this.signIn_tv = (TextView) findViewById(R.id.signIn_tv);
        this.signIn_layout = (LinearLayout) findViewById(R.id.signIn_layout);
        this.signUp_layout = (LinearLayout) findViewById(R.id.signUp_layout);
        this.mDatabse = FirebaseDatabase.getInstance().getReference().child("Users");
        final String stringExtra = getIntent().getStringExtra("from");
        if (stringExtra.equals("cartpage") && getSharedPreferences("USER", 0).contains("phone")) {
            startActivity(new Intent(this, Location_Page.class));
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            finish();
        }
        this.alreadyMember_tv.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                UserAccountPage.this.signIn_layout.setVisibility(View.VISIBLE);
                UserAccountPage.this.signUp_layout.setVisibility(View.GONE);
            }
        });
        this.signIn_tv.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                UserAccountPage.this.signIn_layout.setVisibility(View.GONE);
                UserAccountPage.this.signUp_layout.setVisibility(View.VISIBLE);
            }
        });
        this.crt_btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                final String trim = UserAccountPage.this.name_box.getText().toString().trim();
                final String obj = UserAccountPage.this.phone_box.getText().toString();
                final String obj2 = UserAccountPage.this.address_box.getText().toString();
                if (trim.equals("")) {
                    UserAccountPage.this.name_box.setError("Name field is empty");
                } else if (obj.equals("")) {
                    UserAccountPage.this.phone_box.setError("Name field is empty");
                } else if (obj2.equals("")) {
                    UserAccountPage.this.address_box.setError("Name field is empty");
                } else {
                    UserAccountPage.this.mDatabse.addListenerForSingleValueEvent(new ValueEventListener() {
                        public void onCancelled(DatabaseError databaseError) {
                        }

                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot.hasChild(obj)) {
                                Toast.makeText(UserAccountPage.this, "User Already Exists", Toast.LENGTH_LONG).show();
                                return;
                            }
                            UserAccountPage.this.mDatabse.child(obj).setValue(new User(trim, obj, obj2));
                            SharedPreferences.Editor edit = UserAccountPage.this.getSharedPreferences("USER", 0).edit();
                            edit.putString("phone", obj);
                            edit.putString("name", trim);
                            edit.apply();
                            UserAccountPage.this.name_box.setText("");
                            UserAccountPage.this.phone_box.setText("");
                            UserAccountPage.this.address_box.setText("");
                            if (stringExtra.equals("myorders")) {
                                UserAccountPage.this.startActivity(new Intent(UserAccountPage.this, Orders_Page.class));
                                UserAccountPage.this.overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                                UserAccountPage.this.finish();
                            }
                            if (stringExtra.equals("cartpage")) {
                                UserAccountPage.this.startActivity(new Intent(UserAccountPage.this, CheckOut_Page.class));
                                UserAccountPage.this.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                                UserAccountPage.this.finish();
                            }
                            if (stringExtra.equals("myaccount")) {
                                UserAccountPage.this.startActivity(new Intent(UserAccountPage.this, MyAccount_page.class));
                                UserAccountPage.this.overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                                UserAccountPage.this.finish();
                            }
                        }
                    });
                }
            }
        });
        this.login_btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                final String obj = UserAccountPage.this.phoneLgn_box.getText().toString();
                if (obj.isEmpty()) {
                    UserAccountPage.this.phoneLgn_box.setError("Phone box is empty");
                } else {
                    UserAccountPage.this.mDatabse.addValueEventListener(new ValueEventListener() {
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot.hasChild(obj)) {
                                SharedPreferences.Editor edit = UserAccountPage.this.getSharedPreferences("USER", 0).edit();
                                edit.putString("name", (String) dataSnapshot.child(obj).child("name").getValue(String.class));
                                edit.putString("phone", obj);
                                edit.apply();
                                if (stringExtra.equals("myorders")) {
                                    UserAccountPage.this.startActivity(new Intent(UserAccountPage.this, Orders_Page.class));
                                    UserAccountPage.this.overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                                    UserAccountPage.this.finish();
                                }
                                if (stringExtra.equals("cartpage")) {
                                    UserAccountPage.this.startActivity(new Intent(UserAccountPage.this, CheckOut_Page.class));
                                    UserAccountPage.this.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                                    UserAccountPage.this.finish();
                                }
                                if (stringExtra.equals("myaccount")) {
                                    UserAccountPage.this.startActivity(new Intent(UserAccountPage.this, MyAccount_page.class));
                                    UserAccountPage.this.overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                                    UserAccountPage.this.finish();
                                    return;
                                }
                                return;
                            }
                            Toast.makeText(UserAccountPage.this, "This User is not Registered", Toast.LENGTH_LONG).show();
                        }

                        public void onCancelled(DatabaseError databaseError) {
                            throw databaseError.toException();
                        }
                    });
                }
            }
        });
    }

    public void finish() {
        super.finish();
    }
}
