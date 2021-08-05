package com.example.niara.ui.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.niara.R;

public class PaymentSuccessfulActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_successful);
    }

    @Override
    public void onBackPressed() {
        Intent moveback =
                new Intent(PaymentSuccessfulActivity.this, MainActivity.class);
        startActivity(moveback);
        finish();
    }

    public void gotohome(View view) {
        Intent moveback =
                new Intent(PaymentSuccessfulActivity.this, MainActivity.class);
        startActivity(moveback);
        finish();
    }
}