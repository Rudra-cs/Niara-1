package com.NiaraFoodJoint.niara.ui.fragments;

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
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.NiaraFoodJoint.niara.Adapters.CartAdapter;
import com.NiaraFoodJoint.niara.Api.ApiClient;
import com.NiaraFoodJoint.niara.Api.ApiInterface;
import com.NiaraFoodJoint.niara.Model.CartInfo;
import com.NiaraFoodJoint.niara.Model.Food;
import com.NiaraFoodJoint.niara.ui.activities.PaymentActivity;
import com.NiaraFoodJoint.niara.utils.SessionManager;
import com.NiaraFoodJoint.niara.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyCartFragment extends Fragment {
    private RecyclerView rcFoodCart;
    private static ArrayList<JSONObject> userCartDetailsList;
    private static ArrayList<JSONObject> productList;
    private static ArrayList<JSONObject> cartProducts;
    private int userCart;
    private TextView tvSubtotal;
    private TextView tvTotal;
    private Button btnCheckout;
    private RelativeLayout nodisplay;
    private ScrollView cartscroll;


    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public MyCartFragment() {
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
        getUserFromCart(tvTotal,tvSubtotal,btnCheckout);
        nodisplay=view.findViewById(R.id.nothingaddedincart);
        cartscroll=view.findViewById(R.id.cartScroll);
        rcFoodCart = view.findViewById(R.id.rc_food_cart);
        rcFoodCart.setLayoutManager(new LinearLayoutManager(this.getContext(),RecyclerView.VERTICAL,false));
        return view;
    }


    private void getUserFromCart(TextView tvTotal, TextView tvSubtotal, Button btnCheckout) {
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
                    if (userCartDetailsList!=null){
                        getProduct(userCartDetailsList,tvTotal,tvSubtotal,btnCheckout,progressDialog);
                    }
                    else{

                    }
                } else {
                    Toast.makeText(getContext(),"Something Went Wrong",Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<ArrayList<CartInfo>> call, Throwable t) {
                Toast.makeText(getContext(),"Something Went Wrong",Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getProduct(ArrayList<JSONObject> userCartDetailsList, TextView tvTotal, TextView tvSubtotal, Button btnCheckout, ProgressDialog progressDialog)  {
        if (userCartDetailsList.size()!=0){
            ApiInterface apiInterfaceCart = ApiClient.getClient().create(ApiInterface.class);
            productList =  new ArrayList<>();
            cartProducts = new ArrayList<>();
            int subTotalPrice = 0,subTotal = 0;
            for (int i = 0; i < userCartDetailsList.size(); i++) {

                try {
                    int productId = (int) userCartDetailsList.get(i).get("product");
                    JSONObject object = new JSONObject();
                    object.put("id",userCartDetailsList.get(i).get("id"));
                    object.put("user", userCartDetailsList.get(i).get("user"));
                    object.put("quantity", userCartDetailsList.get(i).get("quantity"));
                    Log.d("CartList quantity",userCartDetailsList.get(i).get("quantity").toString() );
                    Call<Food> getUserProduct = apiInterfaceCart.getProductList(productId);
                    getUserProduct.enqueue(new Callback<Food>() {
                        @Override
                        public void onResponse(Call<Food> call, Response<Food> response) {
                            if(response.isSuccessful()){
                                progressDialog.hide();
                                assert response.body() != null;

                                try {
                                    object.put("productId", response.body().getId());
                                    object.put("title", response.body().getTitle());
                                    object.put("Product_quantity", response.body().getProduct_quantity());
                                    object.put("selling_price", response.body().getSelling_price());
                                    object.put("discounted_price", response.body().getDiscounted_price());
                                    object.put("description", response.body().getDescription());
                                    object.put("brand", response.body().getBrand());
                                    object.put("category", response.body().getCategory());
                                    object.put("product_image", response.body().getProduct_image());
                                    cartProducts.add(object);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                                Log.d("CartList", "Response:" + cartProducts.toString());
                                loadRecView(cartProducts,tvTotal,tvSubtotal,btnCheckout);
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
            progressDialog.hide();
            cartscroll.setVisibility(View.GONE);
            nodisplay.setVisibility(View.VISIBLE);
        }

    }

    private void loadRecView(ArrayList<JSONObject> cartProducts,  TextView tvTotal, TextView tvSubtotal, Button btnCheckout) {
        int subTotalPrice = 0,subTotal = 0;
        for (int i = 0;i<cartProducts.size();i++){
            try {
                subTotalPrice = Integer.parseInt(cartProducts.get(i).getString("quantity")) * Integer.parseInt(cartProducts.get(i).getString("discounted_price")) ;
            } catch (JSONException e) {
                e.printStackTrace();
            }
            subTotal = subTotal + subTotalPrice;
        }
        Log.d("callingthissubtotal",String.valueOf(subTotal));
        tvSubtotal.setText(String.valueOf(subTotal));
        tvTotal.setText(String.valueOf(subTotal+70));

        CartAdapter cartAdapter = new CartAdapter(getContext(),cartProducts);
        rcFoodCart.setAdapter(cartAdapter);

        cartAdapter.setCartListener(position -> {
            ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
            try {
               Call<Void> removeCartItems = apiInterface.deleteCartItems((Integer) cartProducts.get(position).get("id"));
                removeCartItems.enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        if (response.isSuccessful() && response!=null){
                            Toast.makeText(getContext(), "Removed Successfully!!",Toast.LENGTH_SHORT).show();
                            cartProducts.remove(position);
                            int subTotalPrice1 = 0,subTotal1 = 0;
                            Log.d("callingthiscartsize",String.valueOf(cartProducts.size()));
                            for(int j=0;j<cartProducts.size();j++){
                                try {
                                    subTotalPrice1 = subTotalPrice1+Integer.parseInt(cartProducts.get(j).getString("quantity")) * Integer.parseInt(cartProducts.get(j).getString("discounted_price")) ;
                                    Log.d("callingthissubtotal",String.valueOf(subTotalPrice1));
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                            subTotal1 = subTotal1 + subTotalPrice1;
                            Log.d("callingthissubtotal1",String.valueOf(subTotal1));
                            tvSubtotal.setText(String.valueOf(subTotal1));
                            tvTotal.setText(String.valueOf(subTotal1+70));
                            cartAdapter.notifyDataSetChanged();

                        }
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        Toast.makeText(getContext(), "Something Went Wrong",Toast.LENGTH_SHORT).show();
                    }
                });
            }catch (JSONException e) {
                e.printStackTrace();
            }
        });

        if (cartProducts.size()<1){
            Toast.makeText(getContext(),"Cart is Empty",Toast.LENGTH_LONG).show();
            cartscroll.setVisibility(View.GONE);
            nodisplay.setVisibility(View.VISIBLE);
        }

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
