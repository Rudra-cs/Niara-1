package com.example.niara.ui.activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.etebarian.meowbottomnavigation.MeowBottomNavigation;
import com.example.niara.R;
import com.example.niara.ui.fragments.HomeFragment;
import com.example.niara.ui.fragments.MyCartFragment;
import com.example.niara.ui.fragments.OrderFragment;
import com.example.niara.ui.fragments.SettingsFragment;
import com.example.niara.utils.NetworkChangeListener;

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

        drawerLayout = findViewById(R.id.drawer_layout);

        meowBottomNavigation = findViewById(R.id.bottomNavigationView);
        meowBottomNavigation.add(new MeowBottomNavigation.Model(1, R.drawable.ic_home));
        meowBottomNavigation.add(new MeowBottomNavigation.Model(2, R.drawable.ic_cart));
        meowBottomNavigation.add(new MeowBottomNavigation.Model(3, R.drawable.ic_order));
        meowBottomNavigation.add(new MeowBottomNavigation.Model(4, R.drawable.ic_baseline_settings_24));

        meowBottomNavigation.setOnClickMenuListener(new MeowBottomNavigation.ClickListener() {
            @Override
            public void onClickItem(MeowBottomNavigation.Model item) {

            }
        });

        meowBottomNavigation.setOnReselectListener(new MeowBottomNavigation.ReselectListener() {
            @Override
            public void onReselectItem(MeowBottomNavigation.Model item) {
                Fragment fragment = null;
                switch (item.getId()) {
                    case 1:
                        fragment = new HomeFragment();
                        break;

                    case 3:
                        fragment = new OrderFragment();
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

        meowBottomNavigation.setOnShowListener(new MeowBottomNavigation.ShowListener() {
            @Override
            public void onShowItem(MeowBottomNavigation.Model item) {
                Fragment fragment = null;
                switch (item.getId()) {
                    case 1:
                        fragment = new HomeFragment();
                        break;

                    case 3:
                        fragment = new OrderFragment();
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 101) {
            Log.d("successful","success");
            if (resultCode == Activity.RESULT_OK) {
                Fragment fragment = null;
                fragment = new MyCartFragment();
                loadfragment(fragment);
            }
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



}