package com.example.niara.ui.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;
import android.widget.Toast;

import com.example.niara.Adapters.CategoryAdapter;
import com.example.niara.Adapters.FoodAdapter;
import com.example.niara.Api.ApiClient;
import com.example.niara.Api.ApiInterface;
import com.example.niara.Model.Food;
import com.example.niara.R;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment implements CategoryAdapter.CategoryClickListener {

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

        loadFood();

        return view;
    }



    private void loadFood(){
        ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Getting your Food");
        progressDialog.show();

        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);

        Call<ArrayList<Food>> getFoodItems = apiInterface.getFood();

        getFoodItems.enqueue(new Callback<ArrayList<Food>>() {
            @Override
            public void onResponse(Call<ArrayList<Food>> call, Response<ArrayList<Food>> response) {

                if (response.isSuccessful()) {
//                    Toast.makeText(getContext(), "server returned data", Toast.LENGTH_LONG).show();
                    progressDialog.hide();
                    ArrayList<Food> value = response.body();
                    FoodAdapter foodAdapter = new FoodAdapter(getContext(),value);
                    rcFoodItems.setAdapter(foodAdapter);
                }
                else {
                    Toast.makeText(getContext(), "Server Error", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Food>> call, Throwable t) {
                progressDialog.hide();
//                Toast.makeText(getContext(), "network failure :( inform the user and possibly retry", Toast.LENGTH_SHORT).show();

                if (t instanceof IOException) {
//                    Toast.makeText(getContext(), "this is an actual network failure :( inform the user and possibly retry", Toast.LENGTH_SHORT).show();
                    Toast.makeText(getContext(), "Network Error. Please Retry :(", Toast.LENGTH_SHORT).show();
                    // logging probably not necessary
                }
                else {
//                    Toast.makeText(getContext(), "conversion issue! big problems :(", Toast.LENGTH_SHORT).show();
                    // todo log to some central bug tracking service
                    Log.e("Logs",t.toString());
                }
            }
        });
    }



    @Override
    public void onCategoryClicked(String categoryname) {
        categoryValue = categoryname;
        loadFood();
    }
}