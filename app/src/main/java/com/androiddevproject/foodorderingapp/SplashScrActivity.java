package com.androiddevproject.foodorderingapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Timer;
import java.util.TimerTask;

public class SplashScrActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash_scr);



        Thread background = new Thread() {
            public void run() {
                try {
                    sleep(3000);

                    // After 5 seconds redirect to another intent
                    finish();
                    startActivity(new Intent(SplashScrActivity.this,MainActivity.class));
                    overridePendingTransition(R.anim.push_up_in, R.anim.push_up_out);

                } catch (Exception e) {
                }
            }
        };
        // start thread
        background.start();






    }




}
