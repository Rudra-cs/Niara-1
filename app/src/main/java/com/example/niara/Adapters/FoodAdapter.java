package com.example.niara.Adapters;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.niara.Model.Food;
import com.example.niara.R;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import static com.example.niara.utils.Config.BASE_URL;

public class FoodAdapter extends RecyclerView.Adapter<FoodAdapter.FoodViewHolder> {

    private Context context;
    private ArrayList<Food> foods;
    private ItemClickListener clickListener;


    public FoodAdapter(Context context, ArrayList<Food> foods, ItemClickListener clickListener) {
        this.context = context;
        this.foods = foods;
        this.clickListener = clickListener;
    }

    @NonNull
    @NotNull
    @Override
    public FoodViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        return new FoodViewHolder(LayoutInflater.from(context).inflate(R.layout.food_cell, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull FoodAdapter.FoodViewHolder holder, int position) {
        Food data = foods.get(position);

        holder.mTvFoodTitle.setText(data.getTitle());
        holder.mTvPrice.setText(String.valueOf(data.getDiscounted_price()));
        holder.mTvStrikeoutPrice.setText(String.valueOf(data.getSelling_price()));
        holder.mTvStrikeoutPrice.setPaintFlags(holder.mTvStrikeoutPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

//        Image Loading
        Glide.with(this.context).load(BASE_URL+data.getProduct_image()).into(holder.mIvFoodImg);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickListener.onItemClick(foods.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return foods.size();
    }

    public class FoodViewHolder extends RecyclerView.ViewHolder {

        private ImageView mIvFoodImg;
        private TextView mTvFoodTitle;
        private TextView mTvPrice;
        private TextView mTvStrikeoutPrice;

        public FoodViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);

            mIvFoodImg = itemView.findViewById(R.id.iv_product);
            mTvFoodTitle = itemView.findViewById(R.id.tv_title);
            mTvPrice = itemView.findViewById(R.id.tv_price);
            mTvStrikeoutPrice = itemView.findViewById(R.id.tv_price_strikeout);
        }
    }
    public interface ItemClickListener {
        void onItemClick(Food food);
    }
}
