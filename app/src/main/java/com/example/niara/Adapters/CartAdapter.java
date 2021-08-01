package com.example.niara.Adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.niara.Api.ApiClient;
import com.example.niara.Api.ApiInterface;
import com.example.niara.Model.CartInfo;
import com.example.niara.Model.Food;
import com.example.niara.R;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {

    private Context context;
    private ArrayList<JSONObject> cartInfo;
    private CartClickListener cartClickListener;

    public void refreshlist(JSONObject jsonObject){
        if (cartInfo!=null && cartInfo.size()>0){
            for (JSONObject ob:cartInfo){
                if (ob.optInt("id")==jsonObject.optInt("id")){
                    try{
//                        ob.getInt("quantity")=jsonObject.getInt("quantity");
                        cartInfo.remove(ob);
                        cartInfo.add(jsonObject);
                        notifyDataSetChanged();
                    }catch (Exception e){
                        e.printStackTrace();
                    }

                }
            }
        }
    }

    public void setCartListener(CartAdapter.CartClickListener cartListener) {
        this.cartClickListener = cartListener;
    }

    public CartAdapter(Context context, ArrayList<JSONObject> cartInfo) {
        this.context = context;
        this.cartInfo = cartInfo;
    }

    @NonNull
    @NotNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        return new CartViewHolder(LayoutInflater.from(context).inflate(R.layout.cart_cell,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull CartAdapter.CartViewHolder holder, int position) {
        JSONObject data = cartInfo.get(position);

        try {
            Log.i("orderid "+ data.get("id"),"position "+position );
            holder.mTvCartFoodQuantity.setText(String.valueOf(data.get("quantity")));
            holder.mTvCartFoodTitle.setText(String.valueOf(data.get("title")));
            holder.mTvCartFoodPrice.setText(String.valueOf((Integer) data.get("discounted_price")* (Integer) data.get("quantity")));
//            Image Loading
            Glide.with(this.context).load(data.get("product_image")).into(holder.mIvCartImage);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        holder.mBtnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if((Integer) data.get("quantity") <= 20) {
                        CartInfo cartInfo = new CartInfo();
                        cartInfo.setQuantity((Integer) data.get("quantity") + 1);
                        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
                        Call<CartInfo> addItems = apiInterface.updateCartItems((Integer) data.get("id"),cartInfo);

                        addItems.enqueue(new Callback<CartInfo>() {
                            @Override
                            public void onResponse(Call<CartInfo> call, Response<CartInfo> response) {
                                if (response.isSuccessful()){
                                    Toast.makeText(v.getContext(), "Hello Plus",Toast.LENGTH_SHORT).show();
                                    notifyDataSetChanged();
                                }else {
                                    Toast.makeText(v.getContext(), "Hello Error",Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<CartInfo> call, Throwable t) {
                                Toast.makeText(v.getContext(), "Something Went Wrong!!",Toast.LENGTH_SHORT).show();
                            }
                        });

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        holder.mBtnMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                try {
                    if((Integer) data.get("quantity") > 0) {
                        CartInfo cartInfo = new CartInfo();
                        int q= (int) data.get("quantity");
                        cartInfo.setQuantity(q- 1);
                        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
                        Call<CartInfo> addItems = apiInterface.updateCartItems((Integer) data.get("id"),cartInfo);

                        addItems.enqueue(new Callback<CartInfo>() {
                            @Override
                            public void onResponse(Call<CartInfo> call, Response<CartInfo> response) {
                                if (response.isSuccessful()){
                                    Toast.makeText(v.getContext(), "Hello Minus",Toast.LENGTH_SHORT).show();
                                    notifyDataSetChanged();
                                }else {
                                    Toast.makeText(v.getContext(), "Hello Error",Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<CartInfo> call, Throwable t) {
                                Toast.makeText(v.getContext(), "Something Went Wrong!!",Toast.LENGTH_SHORT).show();
                            }
                        });

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });


//        holder.mBtnRemove.setOnClickListener(v -> {
//            ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
//            try {
//                Log.d("orderid", String.valueOf(data.get("id")));
//                Call<Void> removeCartItems = apiInterface.deleteCartItems((Integer) data.get("id"));
//                Toast.makeText(v.getContext(), String.valueOf(data.get("id")),Toast.LENGTH_SHORT).show();
//                removeCartItems.enqueue(new Callback<Void>() {
//                    @Override
//                    public void onResponse(Call<Void> call, Response<Void> response) {
//                        if (response.isSuccessful()){
//                            Toast.makeText(v.getContext(), "Removed Successfully!!" + response.body(),Toast.LENGTH_SHORT).show();
//                            notifyDataSetChanged();
//                        }
//                    }
//
//                    @Override
//                    public void onFailure(Call<Void> call, Throwable t) {
//                        Toast.makeText(v.getContext(), "Something Went Wrong",Toast.LENGTH_SHORT).show();
//                    }
//                });
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//
//
//        });

        holder.mBtnRemove.setOnClickListener(view -> {
            if (cartClickListener != null) {
                cartClickListener.onRemoveListener(position);
                notifyDataSetChanged();
            }
        });

    }

    @Override
    public int getItemCount() {
        return cartInfo.size();
    }

    public class CartViewHolder extends RecyclerView.ViewHolder {

        private ImageView mIvCartImage;
        private TextView mTvCartFoodTitle;
        private TextView mTvCartFoodPrice;
        private TextView mTvCartFoodQuantity;
        private Button mBtnAdd;
        private Button mBtnMinus;
        private Button mBtnRemove;


        public CartViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);

            mIvCartImage = itemView.findViewById(R.id.iv_pdt_cart_img);
            mTvCartFoodTitle = itemView.findViewById(R.id.tv_food_cart_title);
            mTvCartFoodPrice = itemView.findViewById(R.id.tv_food_cart_price);
            mTvCartFoodQuantity = itemView.findViewById(R.id.tv_pdt_quantity);
            mBtnAdd = itemView.findViewById(R.id.btn_add);
            mBtnMinus = itemView.findViewById(R.id.btn_minus);
            mBtnRemove = itemView.findViewById(R.id.btn_remove);
        }
    }

    public  interface  CartClickListener{
        void onRemoveListener(int position);
    }

}