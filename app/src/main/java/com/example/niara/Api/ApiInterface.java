package com.example.niara.Api;

import com.example.niara.Model.Food;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiInterface {

    @GET ("/ProdInfo/")
    Call<ArrayList<Food>> getFood();

    
}
