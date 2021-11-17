package com.example.HomeSecurity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

public class ForgotPasswordActivity extends AppCompatActivity {

    TextView back;
    Button sendEmail;
    EditText email;
    FirebaseFirestore db;
    FirebaseAuth auth;
    TextView text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        back = (TextView) findViewById(R.id.forgotPasswordBack);
        text = (TextView) findViewById(R.id.forgotPasswordText);
        sendEmail = (Button) findViewById(R.id.forgotPasswordSendEmail);
        email = (EditText) findViewById(R.id.forgotPasswordEmail);
        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ForgotPasswordActivity.this, StartActivity.class));
                finish();
            }
        });

        sendEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String txt_email = email.getText().toString();

                db.collection("Users")
                        .whereEqualTo("email",txt_email)
                        .get()
                        .addOnCompleteListener(ForgotPasswordActivity.this, new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if(task.isSuccessful()){
                                    auth.sendPasswordResetEmail(txt_email)
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()) {
                                                        text.setText("Check your email for the link");
                                                        Toast.makeText(ForgotPasswordActivity.this, "Email sent", Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            });
                                }
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(ForgotPasswordActivity.this, "Something Went Wrong.Try Again Later", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });
    }
}