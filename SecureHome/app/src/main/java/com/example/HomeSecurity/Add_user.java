package com.example.HomeSecurity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Add_user extends AppCompatActivity {

    Button back,addFamilyUSer,reset;
    EditText name,phNumber,email;

    FirebaseAuth auth=FirebaseAuth.getInstance();
    FirebaseFirestore db=FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_user);

        back=(Button)findViewById(R.id.backbtn_adduser);
        addFamilyUSer=(Button)findViewById(R.id.addFamilyUser);
        reset=(Button)findViewById(R.id.resetFamilyUser);

        name=(EditText)findViewById(R.id.nameAdduser);
        phNumber=(EditText)findViewById(R.id.phoneNumberAdduser);
        email=(EditText)findViewById(R.id.emailIdAdduser);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Add_user.this, UserActivity.class);
                startActivity(intent);
                finish();
            }
        });

        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name.getText().clear();
                phNumber.getText().clear();
                email.getText().clear();
            }
        });


        addFamilyUSer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addUser();
            }

            private void addUser() {

                //Toast.makeText(Add_user.this, "Click add button", Toast.LENGTH_SHORT).show();
                FirebaseUser userDB = auth.getCurrentUser();
                String uid = userDB.getUid();

                String Uname=name.getText().toString();
                String Uemail=email.getText().toString();
                String Unumber=phNumber.getText().toString();

                Map<String, Object> familyuser = new HashMap<>();
                familyuser.put("name",Uname);
                familyuser.put("email",Uemail);
                familyuser.put("phone number",Unumber);

               // Toast.makeText(Add_user.this, uid, Toast.LENGTH_SHORT).show();
                if(TextUtils.isEmpty(Uname) || TextUtils.isEmpty(Uemail) || TextUtils.isEmpty(Unumber)){
                    Toast.makeText(Add_user.this, "Please Enter All the details", Toast.LENGTH_SHORT).show();
                    name.getText().clear();
                    phNumber.getText().clear();
                    email.getText().clear();
                }
                else{
                        db.collection("Users")
                            .document(uid+"/")
                            .collection("FamilyUser")
                            .document(Uname+"/")
                            .set(familyuser)
                            .addOnCompleteListener(Add_user.this, new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        Intent intent = new Intent(Add_user.this, UserActivity.class);
                                        startActivity(intent);
                                        finish();
                                    }
                                    else{
                                        Toast.makeText(Add_user.this,"Something Went Wrong. Try Again Later",Toast.LENGTH_SHORT).show();
                                    }
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(Add_user.this,"Something Went Wrong. Try Again Later",Toast.LENGTH_SHORT).show();
                                }
                            });
                }
            }
        });
    }
}