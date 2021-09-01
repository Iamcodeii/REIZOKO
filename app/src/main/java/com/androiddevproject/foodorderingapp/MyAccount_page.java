package com.androiddevproject.foodorderingapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.FrameLayout;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MyAccount_page extends AppCompatActivity {
    Button Updt_btn;
    AutoCompleteTextView addressUpdt_box;
    DatabaseReference databaseReference;
    FrameLayout logout_btn;
    DatabaseReference mDatabase;
    AutoCompleteTextView nameUpdt_box;
    AutoCompleteTextView phoneUpdt_box;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView((int) R.layout.activity_my_account_page);
        this.nameUpdt_box = (AutoCompleteTextView) findViewById(R.id.nameUpdt_box);
        this.phoneUpdt_box = (AutoCompleteTextView) findViewById(R.id.phoneUpdt_box);
        this.addressUpdt_box = (AutoCompleteTextView) findViewById(R.id.addressUpdt_box);
        this.Updt_btn = (Button) findViewById(R.id.Updt_btn);
        this.logout_btn = (FrameLayout) findViewById(R.id.logout_btn);
        checkNetwork();
        final SharedPreferences sharedPreferences = getSharedPreferences("USER", 0);
        final String string = sharedPreferences.getString("phone", "");
        DatabaseReference child = FirebaseDatabase.getInstance().getReference().child("Users").child(string).child("name");
        this.databaseReference = child;
        child.addValueEventListener(new ValueEventListener() {
            public void onCancelled(DatabaseError databaseError) {
            }

            public void onDataChange(DataSnapshot dataSnapshot) {
                MyAccount_page.this.nameUpdt_box.setText((String) dataSnapshot.getValue(String.class));
            }
        });
        DatabaseReference child2 = FirebaseDatabase.getInstance().getReference().child("Users").child(string).child("phone");
        this.databaseReference = child2;
        child2.addValueEventListener(new ValueEventListener() {
            public void onCancelled(DatabaseError databaseError) {
            }

            public void onDataChange(DataSnapshot dataSnapshot) {
                MyAccount_page.this.phoneUpdt_box.setText((String) dataSnapshot.getValue(String.class));
            }
        });
        DatabaseReference child3 = FirebaseDatabase.getInstance().getReference().child("Users").child(string).child("address");
        this.databaseReference = child3;
        child3.addValueEventListener(new ValueEventListener() {
            public void onCancelled(DatabaseError databaseError) {
            }

            public void onDataChange(DataSnapshot dataSnapshot) {
                MyAccount_page.this.addressUpdt_box.setText((String) dataSnapshot.getValue(String.class));
            }
        });
        this.phoneUpdt_box.setEnabled(false);
        this.mDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(string);
        this.Updt_btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                String obj = MyAccount_page.this.nameUpdt_box.getText().toString();
                String obj2 = MyAccount_page.this.addressUpdt_box.getText().toString();
                if (obj.equals("")) {
                    MyAccount_page.this.nameUpdt_box.setError("Name field is empty");
                } else if (obj2.equals("")) {
                    MyAccount_page.this.addressUpdt_box.setError("Name field is empty");
                } else {
                    MyAccount_page.this.mDatabase.setValue(new User(obj, string, obj2));
                    MyAccount_page.this.finish();
                }
            }
        });
        this.logout_btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                sharedPreferences.edit().clear().commit();
                MyAccount_page.this.nameUpdt_box.setText("");
                MyAccount_page.this.phoneUpdt_box.setText("");
                MyAccount_page.this.addressUpdt_box.setText("");
                MyAccount_page.this.finish();
            }
        });
    }

    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    private boolean checkNetwork() {
        return ((ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo() != null;
    }
}
