package com.NiaraFoodJoint.niara.ui.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.Toast;

import com.NiaraFoodJoint.niara.Api.ApiClient;
import com.NiaraFoodJoint.niara.Api.ApiInterface;
import com.NiaraFoodJoint.niara.ViewModel.ItemViewModel;
import com.NiaraFoodJoint.niara.utils.NetworkChangeListener;
import com.NiaraFoodJoint.niara.Adapters.SearchFoodAdapter;
import com.NiaraFoodJoint.niara.Model.ItemInfo;
import com.NiaraFoodJoint.niara.R;
import com.NiaraFoodJoint.niara.Repository.ItemInfoRepository;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchActivity extends AppCompatActivity {
    private NetworkChangeListener networkChangeListener=new NetworkChangeListener();
    private RecyclerView rcFoodItems;
    private ItemViewModel itemViewModel;
    private ItemInfoRepository itemInfoRepository;
    private RecyclerView recyclerView;
    private SearchFoodAdapter foodAdapter;
    private List<ItemInfo> itemInfolist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        recyclerView = findViewById(R.id.rc_food);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getApplicationContext(),RecyclerView.VERTICAL,false));
        EditText editText=findViewById(R.id.et_search);
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                filter(editable.toString());
            }
        });
        loadFood();

        itemInfolist=new ArrayList<>();
        itemInfoRepository=new ItemInfoRepository(getApplication());
        itemViewModel=new ViewModelProvider(this).get(ItemViewModel.class);
        itemViewModel.getAllItemInfo().observe(this, new Observer<List<ItemInfo>>() {
            @Override
            public void onChanged(List<ItemInfo> addressArrayList) {
                recyclerView.setAdapter(new SearchFoodAdapter(SearchActivity.this,itemInfolist,SearchActivity.this::onItemClick));
            }
        });
    }

    private void filter(String text) {
        List<ItemInfo> filterList=new ArrayList<>();
        for(ItemInfo item:itemInfolist){
            if(item.getTitle().toLowerCase().contains(text.toLowerCase())){
                filterList.add(item);

            }
        }
        recyclerView.setAdapter(new SearchFoodAdapter(SearchActivity.this,filterList,SearchActivity.this::onItemClick));
    }

    private void loadFood(){
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);

        Call<List<ItemInfo>> getFoodItems = apiInterface.getItemSearch();

        getFoodItems.enqueue(new Callback<List<ItemInfo>>() {
            @Override
            public void onResponse(Call<List<ItemInfo>> call, Response<List<ItemInfo>> response) {

                if (response.isSuccessful()) {
//                    progressDialog.hide();
                    itemInfolist = response.body();
                }

                else {
                    Toast.makeText(getApplicationContext(), "Server Error", Toast.LENGTH_SHORT).show();
                }
                itemInfoRepository.insert(itemInfolist);
            }

            @Override
            public void onFailure(Call<List<ItemInfo>> call, Throwable t) {

                if (t instanceof IOException) {
                }
                else {
//
                }
            }
        });
    }


    private void onItemClick(ItemInfo food) {

        Intent intent = new Intent(getApplicationContext(), ProductDesc.class);
        intent.putExtra("title",food.getTitle());
        intent.putExtra("desc",food.getDescription());
        intent.putExtra("price",String.valueOf(food.getSelling_price()));
        intent.putExtra("discountedprice",String.valueOf(food.getDiscounted_price()));
        intent.putExtra("image",food.getProduct_image());
        intent.putExtra("id",food.getId());
        startActivity(intent);

    }

}