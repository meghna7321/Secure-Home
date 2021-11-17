package com.example.HomeSecurity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

public class ActivateActivity extends AppCompatActivity {

    ImageButton security_module,Fire_module;
    Button EnableButton;
    TextView subtxt;


    BottomNavigationView navbar;

    RecyclerView activateDeviceList;
    ArrayList<Device> AllDeviceList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activate);

//        activateDeviceList = findViewById(R.id.activateDeviceList);
//
//        initData();
//        initRecyclerView();

        security_module=(ImageButton)findViewById(R.id.imageView3);
        Fire_module=(ImageButton)findViewById(R.id.imageView4);

        subtxt=(TextView)findViewById(R.id.subtext);
        EnableButton=(Button)findViewById(R.id.statusButton);

        //system Enable or dissable
        EnableButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//               if(all module enable)
//                {
//                    diable All modules
//                    EnableButton.setText("Enable");
//                      subtxt.setText("Syastem is Deactivated");

//                }
//                else
//                {
//                    Enable All modules
//                    EnableButton.setText("Dissable");
  //              subtxt.setText("Syastem is fully Activated");
//                }
            }
        });


        security_module.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(ActivateActivity.this,Security_Module.class);
                startActivity(intent);
                finish();
            }
        });

        Fire_module.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(ActivateActivity.this,Fire_module.class);
                startActivity(intent);
                finish();
            }
        });


        
//        navbar = (BottomNavigationView)findViewById(R.id.navbar);
//        navbar.setSelectedItemId(R.id.menuActivate);
//
//        navbar.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
//            @Override
//            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//                Intent i;
//                switch (item.getItemId()){
//                    case R.id.menuHome:
//                        i = new Intent(ActivateActivity.this, HomeActivity.class);
//                        startActivity(i);
//                        finish();
//                        overridePendingTransition(0,0);
//                        return true;
//
//                    case R.id.menuUser:
//                        i = new Intent(ActivateActivity.this, UserActivity.class);
//                        startActivity(i);
//                        finish();
//                        overridePendingTransition(0,0);
//                        return true;
//
//                    case R.id.menuActivate:
//                        return true;
//
//                    case R.id.menuSettings:
//                        i = new Intent(ActivateActivity.this, SettingsActivity.class);
//                        startActivity(i);
//                        finish();
//                        overridePendingTransition(0,0);
//                        return true;
//
//                    default:
//                        return false;
//                }
//            }
//
//        });
    }

//    private void initRecyclerView() {
//        AllDeviceAdapter allDeviceAdapter = new AllDeviceAdapter(this,AllDeviceList);
//        activateDeviceList.setLayoutManager(new LinearLayoutManager(this));
//        activateDeviceList.setAdapter(allDeviceAdapter);
//    }
//
//    private void initData() {
//        AllDeviceList = new ArrayList<>();
////        AllDeviceList.add(new Device("","Front Door","Door Sensor"));
////        AllDeviceList.add(new Device("","Back Door","Door Sensor"));
//    }
}