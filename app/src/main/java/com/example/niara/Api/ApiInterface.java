package com.example.niara.Api;


import com.example.niara.Model.Cart;
import com.example.niara.Model.CartInfo;
import com.example.niara.Model.ChangePassword;
import com.example.niara.Model.CreateCustomerInfo;
import com.example.niara.Model.CreateOrderInfo;
import com.example.niara.Model.CustomerFeedbackModel;
import com.example.niara.Model.CustomerInfo;
import com.example.niara.Model.Food;
import com.example.niara.Model.ItemInfo;
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
    @GET("/ProdInfo_UYDuhgfb576MHF_FHY_jgygru657mf5dku7dn/")
    Call<ArrayList<Food>> getFoodSearch();

    @GET("/ProdInfo_UYDuhgfb576MHF_FHY_jgygru657mf5dku7dn/")
    Call<List<ItemInfo>> getItemSearch();

    @GET("/ProdInfo_UYDuhgfb576MHF_FHY_jgygru657mf5dku7dn/snacks/")
    Call<ArrayList<Food>> getSnacks();

    @GET("/ProdInfo_UYDuhgfb576MHF_FHY_jgygru657mf5dku7dn/meals/")
    Call<ArrayList<Food>> getMeals();

    @GET("/ProdInfo_UYDuhgfb576MHF_FHY_jgygru657mf5dku7dn/veg/")
    Call<ArrayList<Food>> getVeg();

    @GET("/ProdInfo_UYDuhgfb576MHF_FHY_jgygru657mf5dku7dn/nonveg/")
    Call<ArrayList<Food>> getNonVeg();

    @GET("/ProdInfo_UYDuhgfb576MHF_FHY_jgygru657mf5dku7dn/grocery/")
    Call<ArrayList<Food>> getGrocery();

    @GET("/ProdInfo_UYDuhgfb576MHF_FHY_jgygru657mf5dku7dn/{id}")
    Call<Food> getProductList(@Path("id") int id);


    //    CUSTOMER INFO AND AUTHENTICATION
    @POST("/CreateCustomerInfo_UYDuhgfb576MHF_FHY_jgygru657mf5dku7dn/")
    Call<CreateCustomerInfo> sendCustomerinfo(@Body CreateCustomerInfo createCustomerInfo);

    @GET("/UserInfo_UYDuhgfb576MHF_FHY_jgygru657mf5dku7dn/")
    Call<ArrayList<UserInfo>> getuserdetails();

    @GET("/CustomerInfo_UYDuhgfb576MHF_FHY_jgygru657mf5dku7dn/")
    Call<List<CustomerInfo>> getAllCustomers();

    @POST("/apiregister_UYDuhgfb576MHF_FHY_jgygru657mf5dku7dn/")
    Call<UserResponse> registerUser(@Body UserRequest userRequest);

    @FormUrlEncoded
    @POST("/apilogin_UYDuhgfb576MHF_FHY_jgygru657mf5dku7dn/")
    Call<LoginToken> loginUser(@Field("username") String username, @Field("password") String password);

    @POST("/CreateContactInfo_UYDuhgfb576MHF_FHY_jgygru657mf5dku7dn/")
    Call<CustomerFeedbackModel> sendFeedback(@Body CustomerFeedbackModel customerFeedbackModel);

    @POST("/apichangepassword_UYDuhgfb576MHF_FHY_jgygru657mf5dku7dn/")
    Call<ChangePassword> sendPasswordChangeRequest(@Body ChangePassword changePassword);

    @DELETE("/CustomerInfo_UYDuhgfb576MHF_FHY_jgygru657mf5dku7dn/{id}/")
    Call<Void> deleteaddressinfo(@Path("id") int id );



    // Cart System
    @POST("/CreateCartInfo_UYDuhgfb576MHF_FHY_jgygru657mf5dku7dn/")
    Call<Cart> sendCartFoodDetails(@Header("TOKEN") String token, @Body Cart cart);

    @GET("/CartInfo_UYDuhgfb576MHF_FHY_jgygru657mf5dku7dn/")
    Call<ArrayList<CartInfo>> getCartDetails();

    @PATCH("/CartInfo_UYDuhgfb576MHF_FHY_jgygru657mf5dku7dn/{id}/")
    Call<CartInfo> updateCartItems(@Path("id") int id , @Body CartInfo cartInfo);

    @DELETE("/CartInfo_UYDuhgfb576MHF_FHY_jgygru657mf5dku7dn/{id}/")
    Call<Void> deleteCartItems(@Path("id") int id );

    // Order Details Api
    @GET("/OrderInfo_UYDuhgfb576MHF_FHY_jgygru657mf5dku7dn/")
    Call <ArrayList<OrderInfo>> getOrderinfo();

    @POST("/CreateOrderInfo_UYDuhgfb576MHF_FHY_jgygru657mf5dku7dn/")

    Call<CreateOrderInfo> sendOrder(@Body CreateOrderInfo createOrderInfo);


}