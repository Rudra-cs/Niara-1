package com.example.niara.ui.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.niara.Adapters.CustomerInfoAdapter;
import com.example.niara.Api.ApiClient;
import com.example.niara.Api.ApiInterface;
import com.example.niara.Model.CartInfo;
import com.example.niara.Model.CreateCustomerInfo;
import com.example.niara.Model.CreateOrderInfo;
import com.example.niara.Model.CustomerInfo;
import com.example.niara.Model.Food;
import com.example.niara.R;
import com.example.niara.utils.NetworkChangeListener;
import com.example.niara.utils.SessionManager;
import com.google.gson.JsonObject;
import com.razorpay.Checkout;
import com.razorpay.Order;
import com.razorpay.PaymentData;
import com.razorpay.PaymentResultWithDataListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PaymentActivity extends AppCompatActivity implements PaymentResultWithDataListener,CustomerInfoAdapter.ItemClickListener, AdapterView.OnItemSelectedListener {
    private NetworkChangeListener networkChangeListener=new NetworkChangeListener();
    private int userCart;
    private TextView tvAmount,tv_subtotal,nameconfirmed,localityconfirmed,cityconfirmed,zipcodeconfirmed,mobileconfirmed,stateconfirmed,addressnone;
    private Button addAddress,cancelform;
    private Boolean addressSelected=false;
    private RecyclerView recyclerView;
    private CustomerInfoAdapter customerInfoAdapter;
    private List<CustomerInfo> customerInfolist;
    private ArrayList<CartInfo> cartInfoArrayList;
    private int i,j;
    private int CustomerID;
    private RelativeLayout noaddress;
    private LinearLayout selectaddressLL,addresscategory;
    private Order order = null;
    private LinearLayout paymentpage,confirmingpage;
    private String orderid,paymentOrderid,paymentSignature;
    private Spinner spinner;
    private String city;
    private EditText fullname,mobile,zipcode,locality;
    private Button paybutton,orderbutton;
    private TextView tvamount,tvselectaddres;
    private String reciept;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        nameconfirmed=findViewById(R.id.nameconfirmed);
        localityconfirmed=findViewById(R.id.localityconfirmed);
        cityconfirmed=findViewById(R.id.cityconfirmed);
        zipcodeconfirmed=findViewById(R.id.zipcodeconfirmed);
        mobileconfirmed=findViewById(R.id.mobileconfirmed);
        stateconfirmed=findViewById(R.id.stateconfirmed);
        addresscategory=findViewById(R.id.addressCategory);
        addressnone=findViewById(R.id.addressnone);

        orderbutton=findViewById(R.id.confirmOrderButton);

        paymentpage=findViewById(R.id.paymentpage);
        confirmingpage=findViewById(R.id.confirmingpage);

        //for address form
        selectaddressLL = findViewById(R.id.addressformLL);
        fullname = findViewById(R.id.fullname_et_address);
        mobile = findViewById(R.id.mobile);
        zipcode = findViewById(R.id.zipcode);
        locality = findViewById(R.id.et_locality_address);
        EditText state = findViewById(R.id.state);
        cancelform=findViewById(R.id.cancelform);

        state.setEnabled(false);
        state.setFocusable(false);
        state.setFocusableInTouchMode(false);
        String[] city = {"Bhubaneswar", "Cuttack"};
        spinner = findViewById(R.id.planets_spinner_city);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, city);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);

        //for bill
        Intent intent = getIntent();
        tvAmount = findViewById(R.id.tvamount);
        tv_subtotal = findViewById(R.id.tv_subTotal);
        tvAmount.setText(intent.getStringExtra("amount"));
        tvselectaddres = findViewById(R.id.selectAddress_tv);
        tv_subtotal.setText(intent.getStringExtra("subtotalamount"));
        noaddress = findViewById(R.id.nothingaddedinaddress);


        int amount = Math.round(Float.parseFloat(String.valueOf(tvAmount.getText().toString())) * 1);

        customerInfolist = new ArrayList<>();
        cartInfoArrayList = new ArrayList<>();

        //generating an order id

        //for address
        recyclerView = findViewById(R.id.rc_address);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), RecyclerView.HORIZONTAL, false));
        loadAddress();
        Checkout.preload(getApplicationContext());
        paybutton = findViewById(R.id.paybutton);
        tvamount = findViewById(R.id.tvamount);
//        paybutton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (addressSelected) {
//                    startPayment(String.valueOf(amount));
//                } else {
//                    Toast.makeText(PaymentActivity.this, "Please Select an address to continue", Toast.LENGTH_SHORT).show();
//                }
//
//            }
//        });

        orderbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (addressSelected) {
                    confirmOrder(String.valueOf(amount));
                } else {
                    Toast.makeText(PaymentActivity.this, "Please Select an address to continue", Toast.LENGTH_SHORT).show();
                }

            }
        });

        addAddress = findViewById(R.id.addAddressbutton);
        addAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectaddressLL.setVisibility(View.VISIBLE);
                addAddress.setVisibility(View.GONE);
            }

        });

        if (addressSelected){
            addAddress.setVisibility(View.GONE);
        }

//        Runnable objRunnable=new Runnable() {
//            @Override
//            public void run() {
//                //
//                RazorpayClient razorpay = null;
//                JSONObject orderRequest = new JSONObject();
//                try {
//                    razorpay = new RazorpayClient("rzp_test_Zdmf4HFzNEDhMD", "zkwDT8tUbFtBxWdUsnF0v11t");
//                } catch (RazorpayException e) {
//                    e.printStackTrace();
//                }
//                try {
//                    orderRequest.put("amount", amount); // amount in the smallest currency unit
//                    orderRequest.put("currency", "INR");
//                    orderRequest.put("receipt", reciept);
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//
//                try {
//                    Log.d("orderreq", String.valueOf(orderRequest));
//                    order = razorpay.Orders.create(orderRequest);
//                } catch (RazorpayException e) {
//                    e.printStackTrace();
//                }
//                orderid = order.get("id");
//            }
//        };
//
//        Thread objBgThread=new Thread(objRunnable);
//        objBgThread.start();

    }

    private void confirmOrder(String amount) {
        String receipt = generateRandomString(7);
        Log.d("receipt",receipt);
        SessionManager sessionManager = new SessionManager(getApplicationContext());
        userCart = sessionManager.getUserid();
        Toast.makeText(PaymentActivity.this,"Order Confirmed",Toast.LENGTH_SHORT).show();

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
                    for (int j=0;j<cartInfoArrayList.size();j++){

                        CreateOrderInfo createOrderInfo=new CreateOrderInfo();
                        createOrderInfo.setUser(userCart);
                        createOrderInfo.setCustomer(CustomerID);
                        createOrderInfo.setProduct(cartInfoArrayList.get(j).getProduct());
                        createOrderInfo.setQuantity(cartInfoArrayList.get(j).getQuantity());
                        createOrderInfo.setRozorpay_paymentId(amount+"Rs");
                        createOrderInfo.setRozorpay_orderId(receipt);
                        createOrderInfo.setRozorpay_signature("00000000000");
                        CreateOrder(createOrderInfo);
                        int k=cartInfoArrayList.get(j).getCartId();
                        DeleteCart(cartInfoArrayList.get(j).getCartId());


                    }
                    startActivity(new Intent(PaymentActivity.this, PaymentSuccessfulActivity.class));
                    finish();


                } else {
                    Toast.makeText(PaymentActivity.this,"Something went wrong",Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<ArrayList<CartInfo>> call, Throwable t) {
            }
        });
    }

    public static String generateRandomString(int length) {
        // You can customize the characters that you want to add into
        // the random strings
        String CHAR_LOWER = "abcdefghijklmnopqrstuvwxyz";
        String CHAR_UPPER = CHAR_LOWER.toUpperCase();
        String NUMBER = "0123456789";

        String DATA_FOR_RANDOM_STRING = CHAR_LOWER + CHAR_UPPER + NUMBER;
        SecureRandom random = new SecureRandom();

        if (length < 1) throw new IllegalArgumentException();

        StringBuilder sb = new StringBuilder(length);

        for (int i = 0; i < length; i++) {
            // 0-62 (exclusive), random returns 0-61
            int rndCharAt = random.nextInt(DATA_FOR_RANDOM_STRING.length());
            char rndChar = DATA_FOR_RANDOM_STRING.charAt(rndCharAt);

            sb.append(rndChar);
        }

        return sb.toString();
    }




    private void loadAddress() {
        if(customerInfolist!=null){
            customerInfolist.clear();
        }
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<List<CustomerInfo>> address = apiInterface.getAllCustomers();
        address.enqueue(new Callback<List<CustomerInfo>>() {
            @Override
            public void onResponse(Call<List<CustomerInfo>> call, Response<List<CustomerInfo>> response) {
                if (response.isSuccessful() && response.body()!=null){
                    SessionManager sessionManager=new SessionManager(PaymentActivity.this);
                    int userid=sessionManager.getUserid();
                    for (i=0;i<response.body().size();i++){
                        if (String.valueOf(userid)==String.valueOf((response.body()).get(i).getUser())){
                            customerInfolist.add(response.body().get(i));
                        }
                    }
                    displayAddresListInRecyclerView(customerInfolist);


                }
                else{
                    Toast.makeText(PaymentActivity.this,"address not found",Toast.LENGTH_SHORT).show();
                    noaddress.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<List<CustomerInfo>> call, Throwable t) {
                Toast.makeText(PaymentActivity.this,"address fetching failed",Toast.LENGTH_SHORT).show();

            }
        });

    }

    private void displayAddresListInRecyclerView(List<CustomerInfo> customerInfolist) {
        List list = new ArrayList(customerInfolist);
        if (customerInfolist!=null){
            Collections.reverse(list);
            customerInfoAdapter= new CustomerInfoAdapter(PaymentActivity.this,list,PaymentActivity.this);
            recyclerView.setAdapter(customerInfoAdapter);
        }
    }

    private  String generateReceipt(int len) {
        SessionManager sessionManager=new SessionManager(PaymentActivity.this);
        String userid=sessionManager.getUsername();
        String chars = userid ;
        Random rnd = new Random();
        StringBuilder sb = new StringBuilder(len);
        for (int i = 0; i < len; i++)
            sb.append(chars.charAt(rnd.nextInt(chars.length())));
        return sb.toString();
    }

    private void startPayment(String amount) {

        reciept = generateReceipt(7);
        Checkout checkout = new Checkout();
        checkout.setKeyID("rzp_test_Zdmf4HFzNEDhMD");
        checkout.setImage(R.drawable.lgn);
        final Activity activity = this;

        try {
            JSONObject options = new JSONObject();
            options.put("name", "Niara");
            options.put("description", "hotel");
            options.put("image", "https://s3.amazonaws.com/rzp-mobile/images/rzp.png");
            options.put("order_id", orderid);//from response of step 3.
            options.put("theme.color", "#0066FF");
            options.put("currency", "INR");
            options.put("amount", amount);//pass amount in currency subunits
            options.put("prefill.email", "example@example.com");
            options.put("prefill.contact","9988776655");
            JSONObject retryObj = new JSONObject();
            retryObj.put("enabled", true);
            retryObj.put("max_count", 4);
            options.put("retry", retryObj);
            checkout.open(activity, options);

        } catch(Exception e) {
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
        SessionManager sessionManager = new SessionManager(getApplicationContext());
        userCart = sessionManager.getUserid();
        paymentOrderid=paymentData.getOrderId();
        paymentSignature=paymentData.getSignature();
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
                    for (int j=0;j<cartInfoArrayList.size();j++){
                        CreateOrderInfo createOrderInfo=new CreateOrderInfo();
                        createOrderInfo.setUser(userCart);
                        createOrderInfo.setCustomer(CustomerID);
                        createOrderInfo.setProduct(cartInfoArrayList.get(j).getProduct());
                        createOrderInfo.setQuantity(cartInfoArrayList.get(j).getQuantity());
                        createOrderInfo.setRozorpay_paymentId(paymentData.getPaymentId());
                        createOrderInfo.setRozorpay_orderId("00000000000");
                        createOrderInfo.setRozorpay_signature("00000000000");
                        CreateOrder(createOrderInfo);
                        int k=cartInfoArrayList.get(j).getCartId();
                        DeleteCart(cartInfoArrayList.get(j).getCartId());

                        startActivity(new Intent(PaymentActivity.this, PaymentSuccessfulActivity.class));
                        finish();
                    }

                } else {
                    Toast.makeText(PaymentActivity.this,"Something went wrong",Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<ArrayList<CartInfo>> call, Throwable t) {
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
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<CreateOrderInfo> createOrderInfoCall=apiInterface.sendOrder(createOrderInfo);
        createOrderInfoCall.enqueue(new Callback<CreateOrderInfo>() {

            @Override
            public void onResponse(Call<CreateOrderInfo> call, Response<CreateOrderInfo> response) {
                if (response.isSuccessful()){
                }else{
                }
            }
            @Override
            public void onFailure(Call<CreateOrderInfo> call, Throwable t) {

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    public void onPaymentError(int i, String s, PaymentData paymentData) {
        Toast.makeText(PaymentActivity.this,"Your Payment Couldn't be Processed, please Retry",Toast.LENGTH_SHORT).show();
    }


    @Override
    public void onItemClick(CustomerInfo address) {
        CustomerID=address.getId();
        addressSelected=true;
        addressnone.setVisibility(View.GONE);
        addresscategory.setVisibility(View.VISIBLE);
        nameconfirmed.setText(address.getName());
        localityconfirmed.setText(address.getLocality());
        mobileconfirmed.setText(address.getMobile());
        cityconfirmed.setText(address.getCity());
        stateconfirmed.setText(address.getState());
        zipcodeconfirmed.setText(address.getZipcode());
        tvselectaddres.setVisibility(View.GONE);

    }

    @Override
    public void onRemoveClicked(CustomerInfo address,int position) {
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<Void> removeCartItems = apiInterface.deleteaddressinfo((Integer) address.getId());
        removeCartItems.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()){
                    Toast.makeText(PaymentActivity.this, "Removed Successfully!!"+position,Toast.LENGTH_SHORT).show();
                    loadAddress();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(PaymentActivity.this, "Something Went Wrong",Toast.LENGTH_SHORT).show();
            }
        });
    }

    private boolean validateName(){
        String name = fullname.getText().toString().trim();
        if (name.isEmpty()) {
            fullname.setError("Field can not be empty");
            return false;
        }else {
            fullname.setError(null);
            return true;
        }
    }

    private boolean validatelocality(){
        String locality1 = locality.getText().toString().trim();
        if (locality1.isEmpty()) {
            locality.setError("Field can not be empty");
            return false;
        }else {
            locality.setError(null);
            return true;
        }
    }

    private boolean validatezipcode(){
        String pincode = zipcode.getText().toString().trim();
        if (pincode.isEmpty()) {
            zipcode.setError("Field can not be empty");
            return false;
        }else {
            zipcode.setError(null);
            return true;
        }
    }

    private boolean validatephone(){
        String phone = mobile.getText().toString().trim();
        if (phone.isEmpty()) {
            mobile.setError("Field can not be empty");
            return false;
        }else {
            mobile.setError(null);
            return true;
        }
    }

    public void submitForm(View view) {

        if (validatelocality() && validateName() && validatephone() && validatezipcode()){
            CreateCustomerInfo createCustomerInfo=new CreateCustomerInfo();
            createCustomerInfo.setName(fullname.getText().toString().trim());
            createCustomerInfo.setCity(city);
            createCustomerInfo.setLocality(locality.getText().toString().trim());
            createCustomerInfo.setMobile(mobile.getText().toString().trim());
            createCustomerInfo.setState("Odisha");
            createCustomerInfo.setZipcode(zipcode.getText().toString().trim());
            SessionManager sessionManager=new SessionManager(getApplicationContext());
            int user=sessionManager.getUserid();
            createCustomerInfo.setUser(user);

            sendAddress(createCustomerInfo);
            cancelform.setVisibility(View.GONE);

            fullname.setText("");
            locality.setText("");
            mobile.setText("");
            zipcode.setText("");
        }else{
            Toast.makeText(PaymentActivity.this,"Please fill in the credentials correctly",Toast.LENGTH_SHORT).show();
        }


    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        city = adapterView.getItemAtPosition(i).toString();
        Toast.makeText(adapterView.getContext(), city, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    private void sendAddress(CreateCustomerInfo createCustomerInfo) {
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<CreateCustomerInfo> createCustomerInfoCall=apiInterface.sendCustomerinfo(createCustomerInfo);
        Log.d("Createinfo", String.valueOf(createCustomerInfo));
        createCustomerInfoCall.enqueue(new Callback<CreateCustomerInfo>() {
            @Override
            public void onResponse(Call<CreateCustomerInfo> call, Response<CreateCustomerInfo> response) {
                if (response.isSuccessful()){
                    Toast.makeText(PaymentActivity.this,"Your Address has been added",Toast.LENGTH_SHORT).show();
                    loadAddress();
                    selectaddressLL.setVisibility(View.GONE);
                    addAddress.setVisibility(View.VISIBLE);

                }else {
                    Toast.makeText(PaymentActivity.this,"Your Address couldn't be added",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<CreateCustomerInfo> call, Throwable t) {
                Toast.makeText(PaymentActivity.this,"Something Went Wrong",Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void cancelform(View view) {
        selectaddressLL.setVisibility(View.GONE);
        addAddress.setVisibility(View.VISIBLE);
    }

    public void cancelAddressClicked(View view) {
        addressSelected=false;
        addressnone.setVisibility(View.VISIBLE);
        addresscategory.setVisibility(View.GONE);
        tvselectaddres.setVisibility(View.VISIBLE);
    }
}
