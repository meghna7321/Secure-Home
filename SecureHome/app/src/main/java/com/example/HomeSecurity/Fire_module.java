package com.example.HomeSecurity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class Fire_module extends AppCompatActivity {

    TextView firemodules;
    Button back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fire_module);

        firemodules=(TextView)findViewById(R.id.fire_module_text);
        back=(Button)findViewById(R.id.backbtn_adduser);

        //set textview and list all the list of fire modules

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Fire_module.this,ActivateActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}