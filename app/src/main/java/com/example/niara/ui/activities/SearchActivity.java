package com.example.niara.ui.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import com.example.niara.Adapters.SearchFoodAdapter;
import com.example.niara.Api.ApiClient;
import com.example.niara.Api.ApiInterface;
import com.example.niara.Model.Food;
import com.example.niara.R;
import com.example.niara.utils.NetworkChangeListener;

import java.io.IOException;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchActivity extends AppCompatActivity {
    NetworkChangeListener networkChangeListener=new NetworkChangeListener();

    private RecyclerView rcFoodItems;
    private SearchFoodAdapter foodAdapter;
    public ArrayList<Food> value;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        rcFoodItems = findViewById(R.id.rc_food);
        rcFoodItems.setLayoutManager(new LinearLayoutManager(this.getApplicationContext(),RecyclerView.VERTICAL,false));
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
    }

    private void filter(String text) {
        ArrayList<Food> filterList=new ArrayList<>();
        for(Food item:value){
            if(item.getTitle().toLowerCase().contains(text.toLowerCase())){
                filterList.add(item);

            }
        }
        foodAdapter.filteredList(filterList);
    }

    private void loadFood(){
//        ProgressDialog progressDialog = new ProgressDialog(getApplicationContext());
//        progressDialog.setMessage("Getting your Food");
//        progressDialog.show();

        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);

        Call<ArrayList<Food>> getFoodItems = apiInterface.getFoodSearch();

        getFoodItems.enqueue(new Callback<ArrayList<Food>>() {
            @Override
            public void onResponse(Call<ArrayList<Food>> call, Response<ArrayList<Food>> response) {

                if (response.isSuccessful()) {
//                    progressDialog.hide();
                    value = response.body();
                    foodAdapter = new SearchFoodAdapter(getApplicationContext(),value,SearchActivity.this::onItemClick);
                    rcFoodItems.setAdapter(foodAdapter);
                }
                else {
                    Toast.makeText(getApplicationContext(), "Server Error", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Food>> call, Throwable t) {
//                progressDialog.hide();
//                Toast.makeText(getContext(), "network failure :( inform the user and possibly retry", Toast.LENGTH_SHORT).show();

                if (t instanceof IOException) {
//                   Toast.makeText(getApplicationContext(), "Network Error. Please Retry :(", Toast.LENGTH_SHORT).show();

                }
                else {
//
                }
            }
        });
    }

    public void onItemClick(Food food) {

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