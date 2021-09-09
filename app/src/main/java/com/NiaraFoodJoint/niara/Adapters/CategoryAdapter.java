package com.NiaraFoodJoint.niara.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.NiaraFoodJoint.niara.R;

import org.jetbrains.annotations.NotNull;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder> {

    private Context context;
    private String[] titles;
    private CategoryClickListener listener;

    private int selectedPostion;

    public CategoryAdapter(Context context) {
        this.context = context;
        titles = context.getResources().getStringArray(R.array.food_list);
    }

    public void setListener(CategoryClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @NotNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        return new CategoryViewHolder(LayoutInflater.from(context).inflate(R.layout.cell_types, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull CategoryAdapter.CategoryViewHolder holder, int position) {

        String categoryName = titles[position];

        holder.mTvCategoryName.setText(categoryName);

        holder.mRlRoot.setOnClickListener(view -> {
            if (listener != null) {
                listener.onCategoryClicked(categoryName);
                selectedPostion = position;
                notifyDataSetChanged();
            }
        });

        if (position == selectedPostion) {
            holder.mRlRoot.setBackground(ResourcesCompat.getDrawable(context.getResources(), R.drawable.bg_category_selected, null));
            holder.mTvCategoryName.setTextColor(ResourcesCompat.getColor(context.getResources(), R.color.white, null));
        } else {
            holder.mRlRoot.setBackground(ResourcesCompat.getDrawable(context.getResources(), R.drawable.bg_category_unselected, null));
            holder.mTvCategoryName.setTextColor(ResourcesCompat.getColor(context.getResources(), R.color.white, null));
        }

    }

    @Override
    public int getItemCount() {
        return titles.length;
    }

    public class CategoryViewHolder extends RecyclerView.ViewHolder {

        private RelativeLayout mRlRoot;
        private TextView mTvCategoryName;

        public CategoryViewHolder(@NonNull View itemView) {
            super(itemView);

            mRlRoot = itemView.findViewById(R.id.rl_root_category);
            mTvCategoryName = itemView.findViewById(R.id.tv_category);
        }
    }
    public interface CategoryClickListener {
        void onCategoryClicked(String categoryname);
    }
}

