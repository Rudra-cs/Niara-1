package com.example.niara.Api;


import com.example.niara.Model.Cart;
import com.example.niara.Model.CartInfo;
import com.example.niara.Model.ChangePassword;
import com.example.niara.Model.CreateCustomerInfo;
import com.example.niara.Model.CustomerFeedbackModel;
import com.example.niara.Model.Food;
import com.example.niara.Model.LoginToken;
import com.example.niara.Model.OrderInfo;
import com.example.niara.Model.UserInfo;
import com.example.niara.Model.UserRequest;
import com.example.niara.Model.UserResponse;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ApiInterface {

//    GET ALL THE PRODUCTS
    @GET("/ProdInfo/")
    Call<ArrayList<Food>> getFoodSearch();

    @GET("/ProdInfo/snacks/")
    Call<ArrayList<Food>> getSnacks();

    @GET("/ProdInfo/meals/")
    Call<ArrayList<Food>> getMeals();

    @GET("/ProdInfo/veg/")
    Call<ArrayList<Food>> getVeg();

    @GET("/ProdInfo/nonveg/")
    Call<ArrayList<Food>> getNonVeg();

    @GET("/ProdInfo/grocery/")
    Call<ArrayList<Food>> getGrocery();

    @GET("/ProdInfo/{id}")
    Call<Food> getProductList(@Path("id") int id);


//    CUSTOMER INFO AND AUTHENTICATION
    @POST("/CreateCustomerInfo/")
    Call<CreateCustomerInfo> sendCustomerinfo(@Body CreateCustomerInfo createCustomerInfo);

    @GET("/UserInfo/")
    Call<ArrayList<UserInfo>> getuserdetails();

    @POST("/apiregister/")
    Call<UserResponse> registerUser(@Body UserRequest userRequest);

    @FormUrlEncoded
    @POST("/apilogin/")
    Call<LoginToken> loginUser(@Field("username") String username, @Field("password") String password);

    @POST("/CreateContactInfo/")
    Call<CustomerFeedbackModel> sendFeedback(@Body CustomerFeedbackModel customerFeedbackModel);

    @POST("/apichangepassword/")
    Call<ChangePassword> sendPasswordChangeRequest(@Body ChangePassword changePassword);

    // Cart System
    @POST("/CreateCartInfo/")
    Call<Cart> sendCartFoodDetails(@Header("TOKEN") String token, @Body Cart cart);

    @GET("/CartInfo/")
    Call<ArrayList<CartInfo>> getCartDetails();

    @PATCH("/CartInfo/{id}")
    Call<CartInfo> updateCartItems(@Path("id") int id , @Body CartInfo cartInfo);

    @DELETE("/CartInfo/{id}")
    Call<Void> deleteCartItems(@Path("id") int id );

    @GET("/OrderInfo/")
    Call <ArrayList<OrderInfo>> getOrderinfo();

    @GET("/ProdInfo/{id}")
    Call<Food> getFoodinfo(@Path("id") int prodId);


}
