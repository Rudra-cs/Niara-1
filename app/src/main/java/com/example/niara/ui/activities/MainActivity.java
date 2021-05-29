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
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.example.niara.R;
import com.example.niara.ui.fragments.AboutUs;
import com.example.niara.ui.fragments.HomeFragment;
import com.example.niara.ui.fragments.MyCartFragment;
import com.example.niara.ui.fragments.ProductFragment;
import com.example.niara.ui.fragments.ProfileFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {
    private DrawerLayout drawerLayout;
    private SharedPreferences prefManager;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        TextView title=findViewById(R.id.AppTitle);

        toolbar.setTitle("");

        setSupportActionBar(toolbar);
        drawerLayout = findViewById(R.id.drawer_layout);

        prefManager = getApplicationContext().getSharedPreferences("LOGIN", MODE_PRIVATE);
        editor = prefManager.edit();

        BottomNavigationView bottom_nav = findViewById(R.id.bottomNavigationView);
        bottom_nav.setOnNavigationItemSelectedListener(nav_listener);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();


        ActionBarDrawerToggle drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open, R.string.close);

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.tool_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {

            case R.id.logOut:
                new AlertDialog.Builder(MainActivity.this).setTitle("Alert")
                        .setMessage("Are you sure you want to Logout")
                        .setPositiveButton("Done", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                editor.putBoolean("ISLOGGEDIN", false);
                                editor.apply();
                                startActivity(new Intent(MainActivity.this, LoginActivity.class));
                                finish();
                            }
                        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                }).show();
                break;


            case  R.id.about_us:
                String data1 = "swaugatkumarbeura5@gmail.com";
                Intent email=new Intent(Intent.ACTION_SEND);
                email.putExtra(Intent.EXTRA_EMAIL,new String[]{data1});
                email.putExtra(Intent.EXTRA_SUBJECT,"Help");
                email.putExtra(Intent.EXTRA_TEXT,"HELLO GUYS, I have a trouble shooting in the app");
                email.setType("message/rfc822");
                startActivity(Intent.createChooser(email,"Email Client"));
                break;
            case R.id.Settings:
                Toast.makeText(MainActivity.this, "settings changed", Toast.LENGTH_SHORT).show();
                break;
        }
        return true;
    }
    private BottomNavigationView.OnNavigationItemSelectedListener nav_listener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
            Fragment fragment = null;
            switch (menuItem.getItemId()){
                case R.id.home:
                    fragment = new HomeFragment();
                    break;

                case R.id.products:
                    fragment = new ProductFragment();
                    break;

                case R.id.profile:
                    fragment = new ProfileFragment();
                    break;

                case R.id.about_us:
                    fragment = new AboutUs();
                    break;
                case R.id.mycart:
                    fragment = new MyCartFragment();
                    break;
            }
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).commit();
            return true;
        }
    };
}