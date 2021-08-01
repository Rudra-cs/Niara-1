package com.example.niara.ui.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.niara.Api.ApiClient;
import com.example.niara.Api.ApiInterface;
import com.example.niara.Model.CreateCustomerInfo;
import com.example.niara.R;
import com.example.niara.utils.NetworkChangeListener;
import com.example.niara.utils.SessionManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreateCusstomerInfoActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    NetworkChangeListener networkChangeListener=new NetworkChangeListener();


    Spinner spinner;
    public String city;
    private EditText fullname,mobile,zipcode,locality;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address);

        fullname=findViewById(R.id.fullname_et_address);
        mobile=findViewById(R.id.mobile);
        zipcode=findViewById(R.id.zipcode);
        locality=findViewById(R.id.et_locality_address);

        EditText state=findViewById(R.id.state);
        state.setEnabled(false);
        state.setFocusable(false);
        state.setFocusableInTouchMode(false);

        String[] city = { "Bhubaneswar", "Cuttack"};
        spinner= findViewById(R.id.planets_spinner_city);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, city);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        city = parent.getItemAtPosition(position).toString();
        Toast.makeText(parent.getContext(), city, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    public void submitForm(View view) {
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
        Log.d("addressuserdetails", String.valueOf(createCustomerInfo.getCity()));

    }

    private void sendAddress(CreateCustomerInfo createCustomerInfo) {
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<CreateCustomerInfo> createCustomerInfoCall=apiInterface.sendCustomerinfo(createCustomerInfo);
        Log.d("Createinfo", String.valueOf(createCustomerInfo));
        createCustomerInfoCall.enqueue(new Callback<CreateCustomerInfo>() {
            @Override
            public void onResponse(Call<CreateCustomerInfo> call, Response<CreateCustomerInfo> response) {
                if (response.isSuccessful()){
                    Toast.makeText(CreateCusstomerInfoActivity.this,"Your Address has been added",Toast.LENGTH_SHORT).show();

                }else {
                    Toast.makeText(CreateCusstomerInfoActivity.this,"Your Address couldn't be added",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<CreateCustomerInfo> call, Throwable t) {
                Toast.makeText(CreateCusstomerInfoActivity.this,"Something Went Wrong",Toast.LENGTH_SHORT).show();
            }
        });
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


}