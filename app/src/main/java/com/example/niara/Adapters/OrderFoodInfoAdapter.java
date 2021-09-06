package com.example.niara.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.niara.Model.OrderInfo;
import com.example.niara.R;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class OrderFoodInfoAdapter extends RecyclerView.Adapter<OrderFoodInfoAdapter.OrderFoodInfoViewHolder> {
    private Context context;
    private List<JSONObject> orderInfos;
    private OrderClickListener orderClickListener;

    public OrderFoodInfoAdapter(Context context, List<JSONObject> orderInfos) {
        this.context = context;
        this.orderInfos = orderInfos;

    }

    public void setOrderClickListener(OrderClickListener orderClickListener) {
        this.orderClickListener = orderClickListener;
    }

    @NonNull
    @NotNull
    @Override
    public OrderFoodInfoViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        return new OrderFoodInfoViewHolder(LayoutInflater.from(context).inflate(R.layout.order_cell, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull OrderFoodInfoViewHolder holder, int position) {
        JSONObject data = orderInfos.get(position);

        try {
            holder.mTvFoodTitle.setText(data.getString("title"));
            holder.mTvstatus.setText(data.getString("status"));
            holder.mTvDate.setText(data.getString("ordered_date"));
            holder.mtvQuantity.setText(data.getString("quantity"));
            holder.orderid.setText(data.getString("orderid"));

//        Image Loading
            Glide.with(this.context).load(data.getString("prod_img")).into(holder.mIvFoodImg);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public int getItemCount() {
        return orderInfos.size();
    }

    public class OrderFoodInfoViewHolder extends RecyclerView.ViewHolder {

        private ImageView mIvFoodImg;
        private TextView mTvFoodTitle,orderid;
        private TextView mTvstatus;
        private TextView needhelp;
        private TextView mTvDate,mtvQuantity,mtvneedhelp;

        public OrderFoodInfoViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);

            mIvFoodImg = itemView.findViewById(R.id.iv_pdt_order_img);
            mTvFoodTitle = itemView.findViewById(R.id.tv_food_order_title);
            mTvstatus = itemView.findViewById(R.id.tv_food_order_status_req);
            orderid=itemView.findViewById(R.id.tv_food_order_id);
            mTvDate=itemView.findViewById(R.id.tv_food_order_date);
            mtvQuantity=itemView.findViewById(R.id.tv_product_quantity);
        }
    }

    public  interface  OrderClickListener{
        void onNeedClickListener(int position, JSONObject jsonObject);
//        void onAddQuantity(JSONObject cart);
//        void onMinusQuanity(JSONObject cart);
    }
}
