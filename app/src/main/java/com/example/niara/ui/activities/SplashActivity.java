package com.example.niara.ui.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.example.niara.R;
import com.example.niara.ui.fragments.SettingsFragment;

public class SplashActivity extends AppCompatActivity {
    private static int splash_time=2000;
    Animation top_animation,bottom_animation;
    ImageView image;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);



        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                SharedPreferences sharedPreferences=getSharedPreferences(LoginActivity.PREFS_NAME,0);
                boolean hasLoggedin=sharedPreferences.getBoolean("hasLoggedIn",false);

                SharedPreferences sharedPreferences1=getSharedPreferences(SettingsFragment.PREFS_NAME,0);
                boolean hasLoggedins=sharedPreferences1.getBoolean("hasLoggedIn",true);

                if (hasLoggedin==true && hasLoggedins==false){
                    Intent i= new Intent(SplashActivity.this, MainActivity.class);
                    startActivity(i);
                    finish();
                }else{
                    Intent i= new Intent(SplashActivity.this, LoginActivity.class);
                    startActivity(i);
                    finish();
                }


            }
        },splash_time);
    }
}