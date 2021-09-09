package com.NiaraFoodJoint.niara.ui.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.NiaraFoodJoint.niara.Api.ApiClient;
import com.NiaraFoodJoint.niara.Api.ApiInterface;
import com.NiaraFoodJoint.niara.utils.NetworkChangeListener;
import com.NiaraFoodJoint.niara.utils.SessionManager;
import com.bumptech.glide.Glide;
import com.NiaraFoodJoint.niara.Model.Cart;
import com.NiaraFoodJoint.niara.R;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductDesc extends AppCompatActivity {
    private NetworkChangeListener networkChangeListener=new NetworkChangeListener();
    private TextView tvTitle;
    private TextView tvPrice;
    private TextView tvDesc;
    private TextView tvQuantity;
    private TextView tvHeadTitle;
    private int tvProdId;
    private Button btnPlus;
    private Button btnAddToCart;
    private Button btnMinus;
    private Button btnBuy,btnGotoHome;
    private ImageView ivImage,addedsuccessfullyLogo;
    private ImageView btnBack;
    private   String token;
    private TextView tvTot;
    private TextView addedsuccesfully;
    private LinearLayout navigationProddesc;
    private CardView addtocartlayout;
    private int user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_desc);

        tvTitle = findViewById(R.id.tv_pdt_title);
        tvDesc = findViewById(R.id.tv_pdt_desc);
        tvPrice = findViewById(R.id.tv_pdt_price);
        tvQuantity = findViewById(R.id.tv_pdt_quantity);
        tvHeadTitle = findViewById(R.id.tv_head_title);
        tvTot=findViewById(R.id.tv_tot_price);
        addedsuccesfully=findViewById(R.id.addedSuccessfullytvProdDesc);
        navigationProddesc=findViewById(R.id.navigation_prod_desc);
        addtocartlayout=findViewById(R.id.addtocardlayout);
        addedsuccessfullyLogo=findViewById(R.id.addedlogo);

        btnPlus = findViewById(R.id.btn_add1);
        btnMinus = findViewById(R.id.btn_minus1);
        btnAddToCart = findViewById(R.id.btn_add_to_cart);
        btnBuy = findViewById(R.id.btn_buy_now);
        btnGotoHome=findViewById(R.id.btn_go_to_home);

        btnBack = findViewById(R.id.back);
        ivImage = findViewById(R.id.iv_pdt_img);

        Intent intent = getIntent();
        tvTitle.setText(intent.getStringExtra("title"));
        tvPrice.setText(intent.getStringExtra("discountedprice"));
        tvDesc.setText(intent.getStringExtra("desc"));
        tvTot.setText(intent.getStringExtra("discountedprice"));
        tvHeadTitle.setText(intent.getStringExtra("title"));
        tvQuantity.setText(String.valueOf(1));
        tvProdId = (intent.getIntExtra("id",1));


        String imageUrl = intent.getStringExtra("image");
        Glide.with(this).load(imageUrl).into(ivImage);

        SharedPreferences preferences = getSharedPreferences("userLoginSessions", Context.MODE_PRIVATE);
        token = preferences.getString("TOKEN", SessionManager.KEY_TOKEN);
        SessionManager sessionManager = new SessionManager(getApplicationContext());
         user =  sessionManager.getUserid();

        btnPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    String p = intent.getStringExtra("discountedprice");
                    int price = Integer.valueOf(p);
                    String q = tvQuantity.getText().toString();
                    int quantity = Integer.valueOf(q);
                    if( quantity>=1){
                        quantity++;
                        tvQuantity.setText(String.valueOf(quantity));
                        price = price*quantity;
                        tvTot.setText(String.valueOf(price));
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
                    String p = intent.getStringExtra("discountedprice");
                    int price = Integer.valueOf(p);
                    String q = tvQuantity.getText().toString();
                    int quantity = Integer.valueOf(q);
                    if( quantity>1){
                        quantity--;
                        tvQuantity.setText(String.valueOf(quantity));
                        price = price*quantity;
                        tvTot.setText(String.valueOf(price));
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
                btnAddToCart.setEnabled(false);
                try {
                    Cart cartDetails = new Cart();
                    cartDetails.setUser(user);
                    cartDetails.setQuantity(Integer.valueOf(tvQuantity.getText().toString()));
                    cartDetails.setProduct(Integer.valueOf(tvProdId));
                    ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
                    Call<Cart> pushToCart = apiInterface.sendCartFoodDetails(token,cartDetails);
                    pushToCart.enqueue(new Callback<Cart>() {
                        @Override
                        public void onResponse(Call<Cart> call, Response<Cart> response) {
                            if (response.isSuccessful()){
                                Toast.makeText(ProductDesc.this, "Added to Cart", Toast.LENGTH_SHORT).show();
                                addedsuccesfully.setVisibility(View.VISIBLE);
                                addedsuccessfullyLogo.setVisibility(View.VISIBLE);
                                addtocartlayout.setVisibility(View.GONE);
                                navigationProddesc.setVisibility(View.VISIBLE);


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

        btnGotoHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProductDesc.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });


        btnBuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProductDesc.this, MainActivity.class);
                intent.putExtra("value", "something");
                startActivity(intent);
                finish();
            }
        });

    }
    @Override
    protected void onStart() {
        IntentFilter filter1=new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(networkChangeListener,filter1);
        super.onStart();
    }


    @Override
    protected void onStop() {
        unregisterReceiver(networkChangeListener);
        super.onStop();
    }

}