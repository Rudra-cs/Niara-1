package com.example.niara.ui.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.niara.Api.ApiClient;
import com.example.niara.Api.ApiInterface;
import com.example.niara.Model.Cart;
import com.example.niara.R;
import com.example.niara.utils.SessionManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.niara.utils.Config.BASE_URL;
import static com.example.niara.utils.SessionManager.KEY_TOKEN;
import static com.example.niara.utils.SessionManager.USERNAME;

public class ProductDesc extends AppCompatActivity {

    private TextView tvTitle;
    private TextView tvPrice;
    private TextView tvDesc;
    private TextView tvQuantity;
    private TextView tvHeadTitle;
    private int tvProdId;

    private Button btnPlus;
    private Button btnAddToCart;
    private Button btnMinus;
    private Button btnBuy;

    private ImageView ivImage;
    private ImageView btnBack;

    public  String token;
    String user;


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
        tvProdId = (intent.getIntExtra("prodId",1));

        String imageUrl = intent.getStringExtra("image");
        Glide.with(this).load(imageUrl).into(ivImage);

//        Shared Preference
        SharedPreferences preferences = getSharedPreferences("userLoginSessions", Context.MODE_PRIVATE);
        token = preferences.getString("TOKEN",KEY_TOKEN);
         user = preferences.getString("USERNAME",USERNAME);

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

//        BAck Button
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

//        Add to Cart Function
        btnAddToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
//                    Cart Model
                    Cart cartDetails = new Cart();
                    cartDetails.setUser(user);
                    cartDetails.setProduct(Integer.valueOf(tvQuantity.getText().toString()));
                    cartDetails.setProduct(Integer.valueOf(tvProdId));

                    ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
                    Call<Cart> pushToCart = apiInterface.sendCartFoodDetails(token,cartDetails);
                    pushToCart.enqueue(new Callback<Cart>() {
                        @Override
                        public void onResponse(Call<Cart> call, Response<Cart> response) {
                            if (response.isSuccessful()){
                                Toast.makeText(ProductDesc.this, "Added to Cart", Toast.LENGTH_SHORT).show();
                            }else{
                                Toast.makeText(ProductDesc.this, "Some Problem Occurred.", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<Cart> call, Throwable t) {
                            Toast.makeText(ProductDesc.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                        }
                    });


                }catch (Exception err){
                    Toast.makeText(ProductDesc.this, "Error: "+err, Toast.LENGTH_SHORT).show();
                }

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