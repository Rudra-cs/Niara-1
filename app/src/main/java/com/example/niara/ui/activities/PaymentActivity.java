package com.example.niara.ui.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.niara.Adapters.CustomerInfoAdapter;
import com.example.niara.Api.ApiClient;
import com.example.niara.Api.ApiInterface;
import com.example.niara.Model.Address;
import com.example.niara.Model.CartInfo;
import com.example.niara.Model.CreateOrderInfo;
import com.example.niara.Model.CustomerFeedbackModel;
import com.example.niara.Model.Food;
import com.example.niara.R;
import com.example.niara.Repository.CustomerInfoRepository;
import com.example.niara.ViewModel.CustomerInfoViewModel;
import com.example.niara.utils.NetworkChangeListener;
import com.example.niara.utils.SessionManager;
import com.razorpay.Checkout;
import com.razorpay.PaymentData;
import com.razorpay.PaymentResultListener;
import com.razorpay.PaymentResultWithDataListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PaymentActivity extends AppCompatActivity implements PaymentResultWithDataListener,CustomerInfoAdapter.ItemClickListener {
    NetworkChangeListener networkChangeListener=new NetworkChangeListener();
    private int userCart;
    private TextView tvAmount;

    private CustomerInfoViewModel customerInfoViewModel;
    private CustomerInfoRepository customerInfoRepository;
    private RecyclerView recyclerView;
    private CustomerInfoAdapter customerInfoAdapter;
    private List<Address> customerInfolist;
    private ArrayList<CartInfo> cartInfoArrayList;
    private int i,j;
    private int CustomerID;

    private static final String TAG = MainActivity.class.getSimpleName();
    public Button paybutton;
    TextView tvamount;
    public String amount;
    Checkout checkout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        Intent intent = getIntent();
        tvAmount=findViewById(R.id.tvamount);
        tvAmount.setText(intent.getStringExtra("amount"));



        int amount = Math.round(Float.parseFloat(String.valueOf(tvAmount.getText().toString())) * 100);

        recyclerView=findViewById(R.id.rc_address);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(),RecyclerView.HORIZONTAL, false));

        customerInfolist=new ArrayList<>();
        cartInfoArrayList=new ArrayList<>();

        loadAddress();

        customerInfoRepository=new CustomerInfoRepository(getApplication());
        customerInfoViewModel=new ViewModelProvider(this).get(CustomerInfoViewModel.class);
        customerInfoViewModel.getAllCustomerInfo().observe(this, new Observer<List<Address>>() {
            @Override
            public void onChanged(List<Address> addressArrayList) {
                Log.d("customer123", String.valueOf(customerInfolist));
                recyclerView.setAdapter(new CustomerInfoAdapter(PaymentActivity.this,customerInfolist,PaymentActivity.this::onItemClick));
            }
        });


        Checkout.preload(getApplicationContext());
        paybutton=findViewById(R.id.paybutton);
        tvamount=findViewById(R.id.tvamount);
        paybutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startPayment(String.valueOf(amount));
            }
        });
    }

    private void loadAddress() {
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<List<Address>> address = apiInterface.getAllCustomers();
        address.enqueue(new Callback<List<Address>>() {
            @Override
            public void onResponse(Call<List<Address>> call, Response<List<Address>> response) {
                if (response.isSuccessful()){
                    Toast.makeText(PaymentActivity.this,"address success",Toast.LENGTH_SHORT).show();
                    SessionManager sessionManager=new SessionManager(PaymentActivity.this);
                    int userid=sessionManager.getUserid();
                    Log.d("userid",String.valueOf(userid));

//                    customerInfolist=new ArrayList<>();
                    for (i=0;i<response.body().size();i++){
//                        Log.d("responsesingle", String.valueOf((response.body()).get(i).getUser()));
                        if (String.valueOf(userid)==String.valueOf((response.body()).get(i).getUser())){
                            customerInfolist.add(response.body().get(i));
//                            Log.d("booleanresponse", "true");
//
                            Log.d("addresslistsingle", String.valueOf(customerInfolist));
                        }else{
                            Log.d("booleanresponse", "false");
                        }
                    }
                    Log.d("addresslist", String.valueOf(customerInfolist));
                    customerInfoRepository.insert(customerInfolist);

                }else{
                    Toast.makeText(PaymentActivity.this,"address error",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Address>> call, Throwable t) {
                Toast.makeText(PaymentActivity.this,"address fail",Toast.LENGTH_SHORT).show();

            }
        });

    }

    public void startPayment(String amount) {
        /**
         * Instantiate Checkout
         */
        Checkout checkout = new Checkout();
        checkout.setKeyID("rzp_test_OAWqK2LwBw2eQg");

        /**
         * Set your logo here
         */
        checkout.setImage(R.drawable.ic_launcher_background);

        /**
         * Reference to current activity
         */
        final Activity activity = this;

        /**
         * Pass your payment options to the Razorpay Checkout as a JSONObject
         */
        try {
            JSONObject options = new JSONObject();

            options.put("name", "Niara");
            options.put("description", "Reference No. #123456");
            options.put("image", "https://s3.amazonaws.com/rzp-mobile/images/rzp.png");
//            options.put("order_id", "order_DBJOWzybf0sJbb");//from response of step 3.
            options.put("theme.color", "#3399cc");
            options.put("currency", "INR");
            options.put("amount", amount);//pass amount in currency subunits
            options.put("prefill.email", "gaurav.kumar@example.com");
            options.put("prefill.contact","9988776655");
            JSONObject retryObj = new JSONObject();
            retryObj.put("enabled", true);
            retryObj.put("max_count", 4);
            options.put("retry", retryObj);

            checkout.open(activity, options);

        } catch(Exception e) {
            Log.e(TAG, "Error in starting Razorpay Checkout", e);
        }
    }


    @Override
    protected void onStart() {
        IntentFilter filter1=new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(networkChangeListener,filter1);
        super.onStart();
    }


    @Override
    protected void onStop() {
        unregisterReceiver(networkChangeListener);
        super.onStop();
    }

    @Override
    public void onPaymentSuccess(String s, PaymentData paymentData) {
        Log.d("stringresponse", String.valueOf(paymentData.getOrderId()));
        Log.d("stringresponse1", String.valueOf(paymentData.getPaymentId()));
        Log.d("stringresponse3", String.valueOf(paymentData.getSignature()));
        SessionManager sessionManager = new SessionManager(getApplicationContext());
        userCart = sessionManager.getUserid();
        Toast.makeText(PaymentActivity.this,"Payment Successful",Toast.LENGTH_SHORT).show();

        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<ArrayList<CartInfo>> getUserCart = apiInterface.getCartDetails();
        getUserCart.enqueue(new Callback<ArrayList<CartInfo>>() {
            @Override
            public void onResponse(Call<ArrayList<CartInfo>> call, Response<ArrayList<CartInfo>> response) {
                if (response.isSuccessful() && response.body()!=null) {
                    for (int i = 0; i < response.body().size(); i++) {
                        if (response.body().get(i).getUser() == userCart) {
                            cartInfoArrayList.add(response.body().get(i));

                        }
                    }
                    Log.d("cartinfoarraylist1", String.valueOf(cartInfoArrayList));
                    Log.d("swaugat123", String.valueOf(CustomerID));
                    for (int j=0;j<cartInfoArrayList.size();j++){
                        Log.d("cartinfoarraylist2", String.valueOf(cartInfoArrayList));
                        CreateOrderInfo createOrderInfo=new CreateOrderInfo();
                        createOrderInfo.setUser(userCart);
                        createOrderInfo.setCustomer(CustomerID);
                        createOrderInfo.setProduct(cartInfoArrayList.get(j).getProduct());
                        createOrderInfo.setQuantity(cartInfoArrayList.get(j).getQuantity());
                        createOrderInfo.setRozorpay_paymentId(paymentData.getPaymentId());
                        createOrderInfo.setRozorpay_orderId("0000000000000");
                        createOrderInfo.setRozorpay_signature("11111111111");

                        CreateOrder(createOrderInfo);
                        int k=cartInfoArrayList.get(j).getCartId();
                        Log.d("valueofk",String.valueOf(k));
                        DeleteCart(cartInfoArrayList.get(j).getCartId());


                    }

                } else {
                    Log.e("CartDetails", "Network Error or Callback Error");
                }
                Log.d("cartinfoarraylist3", String.valueOf(cartInfoArrayList));
            }
            @Override
            public void onFailure(Call<ArrayList<CartInfo>> call, Throwable t) {
                Log.e("CartDetails", "Something Went Wrong: " + t.toString());
            }
        });

    }

    private void DeleteCart(int cartId) {
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<Void> deleteCartItems=apiInterface.deleteCartItems(cartId);
        deleteCartItems.enqueue(new Callback<Void>() {


            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {

            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {

            }
        });

    }

    private void CreateOrder(CreateOrderInfo createOrderInfo) {
        Log.d("createorder123", String.valueOf(createOrderInfo.getUser()));
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<CreateOrderInfo> createOrderInfoCall=apiInterface.sendOrder(createOrderInfo);
        createOrderInfoCall.enqueue(new Callback<CreateOrderInfo>() {

            @Override
            public void onResponse(Call<CreateOrderInfo> call, Response<CreateOrderInfo> response) {
                if (response.isSuccessful()){
//                    Log.d("createordersuccess","Successfully");
                }else{
//                    Log.d("createordersuccess","failed");
                }

            }

            @Override
            public void onFailure(Call<CreateOrderInfo> call, Throwable t) {

            }
        });
    }

    @Override
    public void onPaymentError(int i, String s, PaymentData paymentData) {

    }


    @Override
    public void onItemClick(Address address) {
        CustomerID=address.getId();
    }
}