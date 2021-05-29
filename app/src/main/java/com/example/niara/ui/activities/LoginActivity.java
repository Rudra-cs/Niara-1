package com.example.niara.ui.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import com.example.niara.R;

public class LoginActivity extends AppCompatActivity {
    private SharedPreferences prefManager;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        prefManager = getApplicationContext().getSharedPreferences("LOGIN", MODE_PRIVATE);
        editor = prefManager.edit();

        boolean isUserLoggedIn = prefManager.getBoolean("ISLOGGEDIN", false);
        if (isUserLoggedIn) {
            moveToHomeScreen();
        }
    }

    public void gotohome(View view) {
        startActivity(new Intent(LoginActivity.this, MainActivity.class));
        finish();


    }

    private void moveToHomeScreen() {
        startActivity(new Intent(LoginActivity.this, MainActivity.class));
        finish();

    }

    public void movetosignup(View view) {
        Intent intent=new Intent(LoginActivity.this, RegistrationActivity.class);
        startActivity(intent);

    }


}