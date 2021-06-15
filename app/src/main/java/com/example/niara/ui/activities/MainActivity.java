package com.example.niara.ui.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.etebarian.meowbottomnavigation.MeowBottomNavigation;
import com.example.niara.Api.ApiClient;
import com.example.niara.Api.ApiInterface;
import com.example.niara.Model.ChangePassword;
import com.example.niara.Model.CustomerFeedbackModel;
import com.example.niara.R;
import com.example.niara.ui.fragments.AboutUs;
import com.example.niara.ui.fragments.HomeFragment;
import com.example.niara.ui.fragments.MyCartFragment;
import com.example.niara.ui.fragments.ProductFragment;
import com.example.niara.ui.fragments.ProfileFragment;
import com.example.niara.ui.fragments.SettingsFragment;
import com.example.niara.utils.NetworkChangeListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private SharedPreferences prefManager;
    private SharedPreferences.Editor editor;
    private DrawerLayout drawerLayout;
    MeowBottomNavigation meowBottomNavigation;

    NetworkChangeListener networkChangeListener=new NetworkChangeListener();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        TextView title = findViewById(R.id.AppTitle);

        toolbar.setTitle("");

        //setSupportActionBar(toolbar);
        drawerLayout = findViewById(R.id.drawer_layout);

        meowBottomNavigation = findViewById(R.id.bottomNavigationView);
        meowBottomNavigation.add(new MeowBottomNavigation.Model(1, R.drawable.ic_home));
        meowBottomNavigation.add(new MeowBottomNavigation.Model(2, R.drawable.ic_cart));
        meowBottomNavigation.add(new MeowBottomNavigation.Model(3, R.drawable.ic_baseline_account_circle_24));
        meowBottomNavigation.add(new MeowBottomNavigation.Model(4, R.drawable.ic_baseline_settings_24));

        meowBottomNavigation.setOnClickMenuListener(new MeowBottomNavigation.ClickListener() {
            @Override
            public void onClickItem(MeowBottomNavigation.Model item) {

            }
        });
        meowBottomNavigation.setOnShowListener(new MeowBottomNavigation.ShowListener() {
            @Override
            public void onShowItem(MeowBottomNavigation.Model item) {
                Fragment fragment = null;
                switch (item.getId()) {
                    case 1:
                        fragment = new HomeFragment();
                        break;

                    case 3:
                        fragment = new ProfileFragment();
                        break;

                    case 4:
                        fragment = new SettingsFragment();
                        break;
                    case 2:
                        fragment = new MyCartFragment();
                        break;
                }
                loadfragment(fragment);
            }
        });
        meowBottomNavigation.show(1, true);

    }

    private void loadfragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit();
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


//    public void changepassword(View view) {
//        ChangePassword changePassword=new ChangePassword();
//        changePassword.setOld_password(fullname.getText().toString());
//        changePassword.setNew_password(email.getText().toString());
//
//        sendFeedback(customerFeedbackModel);
//
//    }
//
//    public void submitForm(View view) {
//        CustomerFeedbackModel customerFeedbackModel=new CustomerFeedbackModel();
//        customerFeedbackModel.setName(fullname.getText().toString());
//        customerFeedbackModel.setEmail(email.getText().toString());
//        customerFeedbackModel.setPhone(phone.getText().toString());
//        customerFeedbackModel.setDesc(textArea.getText().toString());
//
//        sendFeedback(customerFeedbackModel);
//
//    }
//
//    private void sendFeedback(CustomerFeedbackModel customerFeedbackModel) {
//        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
//        Call<CustomerFeedbackModel> customerFeedbackCall=apiInterface.sendFeedback(customerFeedbackModel);
//        customerFeedbackCall.enqueue(new Callback<CustomerFeedbackModel>() {
//            @Override
//            public void onResponse(Call<CustomerFeedbackModel> call, Response<CustomerFeedbackModel> response) {
//                if (response.isSuccessful()){
//                    Toast.makeText(CustomerFeedback.this,"Your Feedback has been sent",Toast.LENGTH_SHORT).show();
//                }else {
//                    Toast.makeText(CustomerFeedback.this,"Your Feedback couldn't be sent",Toast.LENGTH_SHORT).show();
//                }
//
//            }
//
//            @Override
//            public void onFailure(Call<CustomerFeedbackModel> call, Throwable t) {
//                Toast.makeText(CustomerFeedback.this,"Something Went Wrong",Toast.LENGTH_SHORT).show();
//
//            }
//        });
//    }
}