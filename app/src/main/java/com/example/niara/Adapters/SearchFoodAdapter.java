package com.example.niara.Adapters;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.niara.Model.Food;
import com.example.niara.Model.ItemInfo;
import com.example.niara.R;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class SearchFoodAdapter extends RecyclerView.Adapter<SearchFoodAdapter.SearchFoodViewHolder> {
    private Context context;
    private List<ItemInfo> items;
    private ItemClickListener clickListener;


    public SearchFoodAdapter(Context context,List<ItemInfo> value, ItemClickListener clickListener) {
        this.context = context;
        this.items = value;
        this.clickListener = clickListener;
    }

    @NonNull
    @NotNull
    @Override
    public SearchFoodViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        return new SearchFoodViewHolder(LayoutInflater.from(context).inflate(R.layout.search_food_cell,parent,false));
    }


    @Override
    public void onBindViewHolder(@NonNull @NotNull SearchFoodViewHolder holder, int position) {
        ItemInfo data = items.get(position);

        holder.mTvFoodTitle.setText(data.getTitle());
        holder.mTvPrice.setText(String.valueOf(data.getDiscounted_price()));
        holder.mTvStrikeoutPrice.setText(String.valueOf(data.getSelling_price()));
        holder.mTvStrikeoutPrice.setPaintFlags(holder.mTvStrikeoutPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickListener.onItemClick(items.get(position));
            }
        });
    }
    public void getAllItemsInfo(List<ItemInfo> iteminfolist){
        this.items=iteminfolist;
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
    public void filteredList(List<ItemInfo> filterList) {
        items=filterList;
        notifyDataSetChanged();
    }

    public class SearchFoodViewHolder extends RecyclerView.ViewHolder {

        private TextView mTvFoodTitle;
        private TextView mTvPrice;
        private TextView mTvStrikeoutPrice;

        public SearchFoodViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            mTvFoodTitle = itemView.findViewById(R.id.tv_title);
            mTvPrice = itemView.findViewById(R.id.tv_price);
            mTvStrikeoutPrice = itemView.findViewById(R.id.tv_price_strikeout);
        }
    }
    public interface ItemClickListener {
        void onItemClick(ItemInfo food);
    }
}
