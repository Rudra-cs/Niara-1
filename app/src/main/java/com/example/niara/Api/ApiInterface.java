package com.example.niara.Api;
import com.example.niara.Model.ChangePassword;
import com.example.niara.Model.CustomerFeedbackModel;
import com.example.niara.Model.Food;
import com.example.niara.Model.LoginToken;
import com.example.niara.Model.UserRequest;
import com.example.niara.Model.UserResponse;

import java.util.ArrayList;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.QueryMap;

public interface ApiInterface {

    @GET ("/ProdInfo/")
    Call<ArrayList<Food>> getFoodSearch();

    @GET ("/ProdInfo/snacks/")
    Call<ArrayList<Food>> getSnacks();

    @GET ("/ProdInfo/meals/")
    Call<ArrayList<Food>> getMeals();
    @GET ("/ProdInfo/veg/")
    Call<ArrayList<Food>> getVeg();
    @GET ("/ProdInfo/nonveg/")
    Call<ArrayList<Food>> getNonVeg();
    @GET ("/ProdInfo/grocery/")
    Call<ArrayList<Food>> getGrocery();

    @GET ("/CartInfo/")
    Call<ArrayList<Food>> getCartDetails();

    @POST("/apiregister/")
    Call<UserResponse> registerUser(@Body UserRequest userRequest);


    @FormUrlEncoded
    @POST("/apilogin/")
    Call<LoginToken> loginUser(@Field("username")String username,@Field("password") String password);

    @POST("/CreateContactInfo/")
    Call<CustomerFeedbackModel> sendFeedback(@Body CustomerFeedbackModel customerFeedbackModel);

    @POST("/apichangepassword/")
    Call<ChangePassword> sendPasswordChangeRequest(@Body ChangePassword changePassword);
}
