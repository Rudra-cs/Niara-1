package com.example.niara.ui.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.niara.R;

import static com.example.niara.utils.Config.BASE_URL;

public class ProductDesc extends AppCompatActivity {

    private TextView tvTitle;
    private TextView tvPrice;
    private TextView tvDesc;
    private TextView tvQuantity;
    private TextView tvHeadTitle;

    private Button btnPlus;
    private Button btnAddToCart;
    private Button btnMinus;
    private Button btnBuy;

    private ImageView ivImage;
    private ImageView btnBack;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_desc);

        tvTitle = findViewById(R.id.tv_pdt_title);
        tvDesc = findViewById(R.id.tv_pdt_desc);
        tvPrice = findViewById(R.id.tv_pdt_price);
        tvQuantity = findViewById(R.id.tv_pdt_quantity);
        tvHeadTitle = findViewById(R.id.tv_head_title);

        btnPlus = findViewById(R.id.btn_add);
        btnMinus = findViewById(R.id.btn_minus);
        btnAddToCart = findViewById(R.id.btn_add_to_cart);
        btnBuy = findViewById(R.id.btn_buy_now);

        btnBack = findViewById(R.id.back);
        ivImage = findViewById(R.id.iv_pdt_img);

        Intent intent = getIntent();
        tvTitle.setText(intent.getStringExtra("title"));
        tvPrice.setText(intent.getStringExtra("price"));
        tvDesc.setText(intent.getStringExtra("desc"));
        tvHeadTitle.setText(intent.getStringExtra("title"));
        tvQuantity.setText(String.valueOf(1));

        String imageUrl = intent.getStringExtra("image");
        Glide.with(this).load(BASE_URL + imageUrl).into(ivImage);

        btnPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    String p = intent.getStringExtra("price");
                    int price = Integer.valueOf(p);
                    String q = tvQuantity.getText().toString();
                    int quantity = Integer.valueOf(q);
                    if( quantity>=1){
                        quantity++;
                        tvQuantity.setText(String.valueOf(quantity));
                        price = price*quantity;
                        tvPrice.setText(String.valueOf(price));
                    }else{

                    }
                }catch (Exception e){
                    Log.e("Quantity","Quantity error");
                }
            }
        });

        btnMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    String p = intent.getStringExtra("price");
                    int price = Integer.valueOf(p);
                    String q = tvQuantity.getText().toString();
                    int quantity = Integer.valueOf(q);
                    if( quantity>1){
                        quantity--;
                        tvQuantity.setText(String.valueOf(quantity));
                        price = price*quantity;
                        tvPrice.setText(String.valueOf(price));
                    }else{

                    }
                }catch (Exception e){
                    Log.e("Quantity","Quantity error");
                }
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        btnAddToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ProductDesc.this, "Added to Cart", Toast.LENGTH_SHORT).show();
            }
        });

        btnBuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ProductDesc.this, "Added to Checkout", Toast.LENGTH_SHORT).show();
            }
        });

    }
}