package com.example.HomeSecurity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class Security_Module extends AppCompatActivity {

    Button back;
    TextView moduleList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_security__module);

        back=(Button)findViewById(R.id.backbtn_user);

        //set text view with all th list of security module
        moduleList=(TextView)findViewById(R.id.security_module_lsit);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Security_Module.this,ActivateActivity.class);
                startActivity(intent);
                finish();
            }
        });


    }
}