package com.example.niara.Api;
import com.example.niara.Model.Food;
import com.example.niara.Model.LoginRequest;
import com.example.niara.Model.LoginToken;
import com.example.niara.Model.UserRequest;
import com.example.niara.Model.UserResponse;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface ApiInterface {

    @GET ("/ProdInfo/")
    Call<ArrayList<Food>> getFood();

    @GET ("/CartInfo/")
    Call<ArrayList<Food>> getCartDetails();

    @POST("/apiregister/")
    Call<UserResponse> registerUser(@Body UserRequest userRequest);


    @FormUrlEncoded
    @POST("/apilogin/")
    Call<LoginToken> loginUser(@Field("username")String username,@Field("password") String password);


}
