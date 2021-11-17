package com.example.HomeSecurity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;


import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.ismaeldivita.chipnavigation.ChipNavigationBar;


public class HomeActivity extends AppCompatActivity {

    TextView user, logout;
    CardView door, window, motion, fire, smoke, gasLeak, alarm;
    FirebaseAuth auth;
    BottomNavigationView navbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        user = (TextView) findViewById(R.id.homeUsername);
        logout = (TextView) findViewById(R.id.homeLogout);
        door = (CardView) findViewById(R.id.homeDoor);
        window = (CardView) findViewById(R.id.homeWindow);
        motion = (CardView) findViewById(R.id.homeMotion);
        fire = (CardView) findViewById(R.id.homeFire);
        smoke = (CardView) findViewById(R.id.homeSmoke);
        gasLeak = (CardView) findViewById(R.id.homeGasLeak);
        alarm = (CardView) findViewById(R.id.homeAlarms);
        navbar = (BottomNavigationView) findViewById(R.id.navbar);

        auth = FirebaseAuth.getInstance();
        FirebaseUser userDB = auth.getCurrentUser();
        String username = userDB.getDisplayName();

        user.setText(username);

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                auth.signOut();
                startActivity(new Intent(HomeActivity.this,StartActivity.class));
                finish();
            }
        });

        door.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(HomeActivity.this, SensorActivity.class);
                i.putExtra("extra","Door");
                startActivity(i);
            }
        });

        window.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(HomeActivity.this, SensorActivity.class);
                i.putExtra("extra","Window");
                startActivity(i);
            }
        });
        motion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(HomeActivity.this, SensorActivity.class);
                i.putExtra("extra","Motion");
                startActivity(i);
            }
        });

        fire.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(HomeActivity.this, SensorActivity.class);
                i.putExtra("extra","Fire");
                startActivity(i);
            }
        });
        smoke.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(HomeActivity.this, SensorActivity.class);
                i.putExtra("extra","Smoke");
                startActivity(i);
            }
        });
        gasLeak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(HomeActivity.this, SensorActivity.class);
                i.putExtra("extra","Gas Leak");
                startActivity(i);
            }
        });

        alarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(HomeActivity.this, SensorActivity.class);
                i.putExtra("extra","Alarm");
                startActivity(i);
            }
        });

        navbar.setSelectedItemId(R.id.menuHome);

        navbar.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Intent i;
                switch (item.getItemId()){
                    case R.id.menuHome:
                            return true;

                    case R.id.menuUser:
                        i = new Intent(HomeActivity.this, UserActivity.class);
                        startActivity(i);
                        finish();
                        overridePendingTransition(0,0);
                        return true;

//                    case R.id.menuActivate:
//                        i = new Intent(HomeActivity.this, ActivateActivity.class);
//                        startActivity(i);
//                        finish();
//                        overridePendingTransition(0,0);
//                        return true;

                    case R.id.menuSettings:
                        i = new Intent(HomeActivity.this, SettingsActivity.class);
                        startActivity(i);
                        finish();
                        overridePendingTransition(0,0);
                        return true;

                    default:
                        return false;
                }
            }

        });

//        navbar.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
//            @Override
//            public void onNavigationItemReselected(@NonNull MenuItem menuItem) {
//                switch (menuItem.getItemId()){
//                    case R.id.menuHome:
//
//                    case R.id.menuUser:
//                        Intent i = new Intent(HomeActivity.this, UserActivity.class);
//                        startActivity(i);
//                        finish();
//                        overridePendingTransition(0,0);
//                        return;
//
////                    case R.id.about:
////                        startActivity(new Intent(getApplicationContext(),About.class));
////                        finish();
////                        overridePendingTransition(0,0);
////                        return;
//                }
//            }
//        });

    }
}