package com.example.niara.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.niara.Model.CartInfo;
import com.example.niara.Model.Food;
import com.example.niara.R;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {

    private Context context;
    private ArrayList<JSONObject> cartInfo;

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

//        holder.mTvCartFoodTitle.setText(data.get());
        try {
            holder.mTvCartFoodQuantity.setText(String.valueOf(data.get("quantity")));
//            holder.mTvCartFoodTitle.setText(String.valueOf(data.get("product")));
        } catch (JSONException e) {
            e.printStackTrace();
        }

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
}
