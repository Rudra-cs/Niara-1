package com.example.niara.ui.fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.niara.Adapters.CartAdapter;
import com.example.niara.Api.ApiClient;
import com.example.niara.Api.ApiInterface;
import com.example.niara.Model.CartInfo;
import com.example.niara.Model.Food;
import com.example.niara.R;
import com.example.niara.ui.activities.LoginActivity;
import com.example.niara.ui.activities.MainActivity;
import com.example.niara.ui.activities.PaymentActivity;
import com.example.niara.ui.activities.ProductDesc;
import com.example.niara.utils.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyCartFragment extends Fragment {

    public RecyclerView rcFoodCart;
    public static ArrayList<JSONObject> userCartDetailsList;
    public static ArrayList<JSONObject> productList;
    public static ArrayList<JSONObject> cartProducts;
    public int userCart;
    public TextView tvSubtotal;
    public TextView tvTotal;
    public Button btnCheckout;


    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public MyCartFragment() {
        // Required empty public constructor
    }


    public static MyCartFragment newInstance(String param1, String param2) {
        MyCartFragment fragment = new MyCartFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my_cart, container, false);

        SessionManager sessionManager = new SessionManager(getContext());
        userCart = sessionManager.getUserid();

        tvSubtotal = view.findViewById(R.id.tv_subTotal);
        tvTotal = view.findViewById(R.id.tv_total_price);
        btnCheckout = view.findViewById(R.id.btn_checkout);

        rcFoodCart = view.findViewById(R.id.rc_food_cart);
        rcFoodCart.setLayoutManager(new LinearLayoutManager(this.getContext(),RecyclerView.VERTICAL,false));

        getUserFromCart(tvTotal,tvSubtotal,btnCheckout);


        return view;
    }


    public void getUserFromCart(TextView tvTotal, TextView tvSubtotal, Button btnCheckout) {
        ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Loading Cart");
        progressDialog.show();

        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<ArrayList<CartInfo>> getUserCart = apiInterface.getCartDetails();
        userCartDetailsList = new ArrayList<>();

        getUserCart.enqueue(new Callback<ArrayList<CartInfo>>() {
            @Override
            public void onResponse(Call<ArrayList<CartInfo>> call, Response<ArrayList<CartInfo>> response) {
                if (response.isSuccessful() && response.body()!=null) {
                    for (int i = 0; i < response.body().size(); i++) {
                        if (response.body().get(i).getUser() == userCart) {
                            JSONObject ob = new JSONObject();
                            try {
                                ob.put("id", response.body().get(i).getCartId());
                                ob.put("user", response.body().get(i).getUser());
                                ob.put("product", response.body().get(i).getProduct());
                                ob.put("quantity", response.body().get(i).getQuantity());
                                userCartDetailsList.add(ob);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                    getProduct(userCartDetailsList,tvTotal,tvSubtotal,btnCheckout,progressDialog);
//                    progressDialog.hide();
                } else {
                    Log.e("CartDetails", "Network Error or Callback Error");
                }
            }
            @Override
            public void onFailure(Call<ArrayList<CartInfo>> call, Throwable t) {
                Log.e("CartDetails", "Something Went Wrong: " + t.toString());
            }
        });



    }

    private void getProduct(ArrayList<JSONObject> userCartDetailsList, TextView tvTotal, TextView tvSubtotal, Button btnCheckout, ProgressDialog progressDialog)  {
        if (userCartDetailsList.size()!=0){
            ApiInterface apiInterfaceCart = ApiClient.getClient().create(ApiInterface.class);
            productList =  new ArrayList<>();
            for (int i = 0; i < userCartDetailsList.size(); i++) {
                try {
                      int productId = (int) userCartDetailsList.get(i).get("product");
                    Call<Food> getUserProduct = apiInterfaceCart.getProductList(productId);

                    getUserProduct.enqueue(new Callback<Food>() {
                        @Override
                        public void onResponse(Call<Food> call, Response<Food> response) {
                            if(response.isSuccessful()){
                                progressDialog.hide();
                                assert response.body() != null;
                                JSONObject object = new JSONObject();
                                try {
                                    object.put("id", response.body().getId());
                                    object.put("title", response.body().getTitle());
                                    object.put("Product_quantity", response.body().getProduct_quantity());
                                    object.put("selling_price", response.body().getSelling_price());
                                    object.put("discounted_price", response.body().getDiscounted_price());
                                    object.put("description", response.body().getDescription());
                                    object.put("brand", response.body().getBrand());
                                    object.put("category", response.body().getCategory());
                                    object.put("product_image", response.body().getProduct_image());
                                    productList.add(object);
//                                    Log.d("CartList", "Response:" + productList.size()+response.body().getId());
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                if (productList.size() == userCartDetailsList.size()){
//                                    Log.d("CartList", "Response:" + productList.toString());
//                                    Log.d("CartList", "Response:" + userCartDetailsList.size() + " and " + productList.size());
                                    loadRecView(userCartDetailsList,productList,tvTotal,tvSubtotal,btnCheckout);
                                }
                            }else{
                            }
                        }

                        @Override
                        public void onFailure(Call<Food> call, Throwable t) {
                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }else {
            Toast.makeText(getContext(),"Cart is Empty",Toast.LENGTH_LONG).show();
        }

    }

    private void loadRecView(ArrayList<JSONObject> userCartDetailsList, ArrayList<JSONObject> productList, TextView tvTotal, TextView tvSubtotal, Button btnCheckout) {
        cartProducts = new ArrayList<>();
        int subTotalPrice = 0,subTotal = 0;

        for (int i = 0;i<userCartDetailsList.size();i++){
            JSONObject object2 = new JSONObject();
            try {
                object2.put("id", userCartDetailsList.get(i).get("id"));
                object2.put("user", userCartDetailsList.get(i).get("user"));
                object2.put("quantity", userCartDetailsList.get(i).get("quantity"));

                object2.put("productId", productList.get(i).get("id"));
                object2.put("title", productList.get(i).get("title"));
                object2.put("Product_quantity", productList.get(i).get("Product_quantity"));
                object2.put("selling_price", productList.get(i).get("selling_price"));
                object2.put("discounted_price", productList.get(i).get("discounted_price"));
                object2.put("description", productList.get(i).get("description"));
                object2.put("brand", productList.get(i).get("brand"));
                object2.put("category", productList.get(i).get("category"));
                object2.put("product_image", productList.get(i).get("product_image"));

                cartProducts.add(object2);
                Log.d("CartList", "Response:" + cartProducts.toString());
//                                    Log.d("CartList", "Response:" + productList.size()+response.body().getId());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                subTotalPrice = Integer.parseInt(cartProducts.get(i).getString("quantity")) * Integer.parseInt(cartProducts.get(i).getString("discounted_price")) ;
            } catch (JSONException e) {
                e.printStackTrace();
            }
            subTotal = subTotal + subTotalPrice;
        }
        tvSubtotal.setText(String.valueOf(subTotal));
        tvTotal.setText(String.valueOf(subTotal+70));

        CartAdapter cartAdapter = new CartAdapter(getContext(),cartProducts);
        rcFoodCart.setAdapter(cartAdapter);


        btnCheckout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Integer.valueOf(tvTotal.getText().toString())>70){
                    Intent intent = new Intent(getContext(), PaymentActivity.class);
                    intent.putExtra("amount",tvTotal.getText().toString());
                    intent.putExtra("subtotalamount",tvSubtotal.getText().toString());
                    startActivity(intent);
                }
            }
        });

    }
}