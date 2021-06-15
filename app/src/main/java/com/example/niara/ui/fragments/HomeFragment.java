package com.example.niara.ui.fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.niara.Adapters.CategoryAdapter;
import com.example.niara.Adapters.FoodAdapter;
import com.example.niara.Api.ApiClient;
import com.example.niara.Api.ApiInterface;
import com.example.niara.Model.Food;
import com.example.niara.ui.activities.LoginActivity;
import com.example.niara.ui.activities.MainActivity;
import com.example.niara.ui.activities.ProductDesc;
import com.example.niara.R;
import com.example.niara.ui.activities.SearchActivity;
import com.example.niara.utils.NetworkChangeListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment implements CategoryAdapter.CategoryClickListener, FoodAdapter.ItemClickListener{

    private String categoryValue = "Delicious";
    private RecyclerView rcFoodItems;
    private RecyclerView foodRecyclerView;


    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public HomeFragment() {
        // Required empty public constructor
    }

    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
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
        View view =  inflater.inflate(R.layout.fragment_home, container, false);

        foodRecyclerView = view.findViewById(R.id.rc_categories_food);
        foodRecyclerView.setLayoutManager(new LinearLayoutManager(this.getContext(),RecyclerView.HORIZONTAL, false));

        rcFoodItems = view.findViewById(R.id.rc_food);
        rcFoodItems.setLayoutManager(new LinearLayoutManager(this.getContext(),RecyclerView.VERTICAL,false));

        CategoryAdapter categoryAdapter = new CategoryAdapter(this.getActivity());
        categoryAdapter.setListener(this);
        foodRecyclerView.setAdapter(categoryAdapter);
        TextView searchbar=view.findViewById(R.id.tv_search);
        searchbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), SearchActivity.class));

            }
        });

        loadFood();

        return view;
    }



    private void loadFood() {
        ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Getting your Food");
        progressDialog.show();

        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);

        HashMap<String, Object> queries = new HashMap<>();

        Call<ArrayList<Food>> getFoodItems = apiInterface.getFoodSearch();

        getFoodItems.enqueue(new Callback<ArrayList<Food>>() {
            @Override
            public void onResponse(Call<ArrayList<Food>> call, Response<ArrayList<Food>> response) {

                if (response.isSuccessful()) {
                    progressDialog.hide();
                    ArrayList<Food> value = response.body();
                    FoodAdapter foodAdapter = new FoodAdapter(getContext(),value,HomeFragment.this);
                    rcFoodItems.setAdapter(foodAdapter);
                }
                else {
                    Toast.makeText(getContext(), "Server Error", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Food>> call, Throwable t) {
                progressDialog.hide();

                if (t instanceof IOException) {

                }
                else {
                    Log.e("Logs",t.toString());
                }
            }
        });
    }



    @Override
    public void onCategoryClicked(String categoryname) {
        categoryValue = categoryname;
        switch (categoryValue){
            case "Delicious":
                loadFood();
                break;
            case "Snacks":
                loadSnacks();
                break;
            case "Meals":
                loadMeals();
                break;
            case "Veg":
                loadVeg();
                break;
            case "Non-Veg":
                loadNonVeg();
                break;
            case "Grocery":
                grocery();
                break;
            default:
                loadFood();
                break;
        }
    }

    private void grocery() {
        ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Getting your Food");
        progressDialog.show();

        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);

        HashMap<String, Object> queries = new HashMap<>();

        Call<ArrayList<Food>> getFoodItems = apiInterface.getGrocery();

        getFoodItems.enqueue(new Callback<ArrayList<Food>>() {
            @Override
            public void onResponse(Call<ArrayList<Food>> call, Response<ArrayList<Food>> response) {

                if (response.isSuccessful()) {
                    progressDialog.hide();
                    ArrayList<Food> value = response.body();
                    FoodAdapter foodAdapter = new FoodAdapter(getContext(),value,HomeFragment.this);
                    rcFoodItems.setAdapter(foodAdapter);
                }
                else {
                    Toast.makeText(getContext(), "Server Error", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Food>> call, Throwable t) {
                progressDialog.hide();

                if (t instanceof IOException) {

                }
                else {
                    Log.e("Logs",t.toString());
                }
            }
        });
    }

    private void loadNonVeg() {
        ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Getting your Food");
        progressDialog.show();

        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);


        Call<ArrayList<Food>> getFoodItems = apiInterface.getNonVeg();

        getFoodItems.enqueue(new Callback<ArrayList<Food>>() {
            @Override
            public void onResponse(Call<ArrayList<Food>> call, Response<ArrayList<Food>> response) {

                if (response.isSuccessful()) {
                    progressDialog.hide();
                    ArrayList<Food> value = response.body();
                    FoodAdapter foodAdapter = new FoodAdapter(getContext(),value,HomeFragment.this);
                    rcFoodItems.setAdapter(foodAdapter);
                }
                else {
                    Toast.makeText(getContext(), "Server Error", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Food>> call, Throwable t) {
                progressDialog.hide();

                if (t instanceof IOException) {

                }
                else {
                    Log.e("Logs",t.toString());
                }
            }
        });
    }

    private void loadVeg() {
        ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Getting your Food");
        progressDialog.show();

        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);



        Call<ArrayList<Food>> getFoodItems = apiInterface.getVeg();

        getFoodItems.enqueue(new Callback<ArrayList<Food>>() {
            @Override
            public void onResponse(Call<ArrayList<Food>> call, Response<ArrayList<Food>> response) {

                if (response.isSuccessful()) {
                    progressDialog.hide();
                    ArrayList<Food> value = response.body();
                    FoodAdapter foodAdapter = new FoodAdapter(getContext(),value,HomeFragment.this);
                    rcFoodItems.setAdapter(foodAdapter);
                }
                else {
                    Toast.makeText(getContext(), "Server Error", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Food>> call, Throwable t) {
                progressDialog.hide();

                if (t instanceof IOException) {

                }
                else {
                    Log.e("Logs",t.toString());
                }
            }
        });
    }

    private void loadMeals() {
        ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Getting your Food");
        progressDialog.show();

        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);



        Call<ArrayList<Food>> getFoodItems = apiInterface.getMeals();

        getFoodItems.enqueue(new Callback<ArrayList<Food>>() {
            @Override
            public void onResponse(Call<ArrayList<Food>> call, Response<ArrayList<Food>> response) {

                if (response.isSuccessful()) {
                    progressDialog.hide();
                    ArrayList<Food> value = response.body();
                    FoodAdapter foodAdapter = new FoodAdapter(getContext(),value,HomeFragment.this);
                    rcFoodItems.setAdapter(foodAdapter);
                }
                else {
                    Toast.makeText(getContext(), "Server Error", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Food>> call, Throwable t) {
                progressDialog.hide();

                if (t instanceof IOException) {

                }
                else {
                    Log.e("Logs",t.toString());
                }
            }
        });
    }

    private void loadSnacks() {
        ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Getting your Food");
        progressDialog.show();

        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);

        Call<ArrayList<Food>> getFoodItems = apiInterface.getSnacks();

        getFoodItems.enqueue(new Callback<ArrayList<Food>>() {
            @Override
            public void onResponse(Call<ArrayList<Food>> call, Response<ArrayList<Food>> response) {

                if (response.isSuccessful()) {
                    progressDialog.hide();
                    ArrayList<Food> value = response.body();
                    FoodAdapter foodAdapter = new FoodAdapter(getContext(),value,HomeFragment.this);
                    rcFoodItems.setAdapter(foodAdapter);
                }
                else {
                    Toast.makeText(getContext(), "Server Error", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Food>> call, Throwable t) {
                progressDialog.hide();

                if (t instanceof IOException) {

                }
                else {
                    Log.e("Logs",t.toString());
                }
            }
        });
    }

    @Override
    public void onItemClick(Food food) {

        Intent intent = new Intent(getActivity(), ProductDesc.class);
        intent.putExtra("title",food.getTitle());
        intent.putExtra("desc",food.getDescription());
        intent.putExtra("price",String.valueOf(food.getSelling_price()));
        intent.putExtra("image",food.getProduct_image());
        startActivity(intent);

    }

}