package com.example.HomeSecurity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;

public class SettingsActivity extends AppCompatActivity {

    BottomNavigationView navbar;
    TextView changeUserName,changePassword;
    Button EnableButton;
    TextView name,username,email;

    FirebaseAuth auth=FirebaseAuth.getInstance();
    FirebaseFirestore db= FirebaseFirestore.getInstance();
    FirebaseUser userDB = auth.getCurrentUser();
    String uid = userDB.getUid();
    Map<String, Object> Updateuser = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        navbar = (BottomNavigationView)findViewById(R.id.navbar);
        navbar.setSelectedItemId(R.id.menuSettings);

        changeUserName=(TextView) findViewById(R.id.changeUserName);
        changePassword=(TextView) findViewById(R.id.changePassword);
        name=(TextView) findViewById(R.id.getSettingName);
        username=(TextView) findViewById(R.id.getSettingUsername);
        email=(TextView) findViewById(R.id.getSettingEmail);
        EnableButton=(Button)findViewById(R.id.statusButton);

        //Display user personal info
        showData(name,username,email);

        changeUserName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //change User name in fire base
                AlertDialog.Builder changeUser=new AlertDialog.Builder(SettingsActivity.this);
                changeUser.setTitle("Enter user name");

                final EditText Dialogusername=new EditText(SettingsActivity.this);
                changeUser.setView(Dialogusername);

                changeUser.setPositiveButton("Change", new DialogInterface.OnClickListener(){

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                       // Toast.makeText(SettingsActivity.this, "Enter your new user name", Toast.LENGTH_SHORT).show();
                        String user=Dialogusername.getText().toString();

                        Updateuser.put("username",user);

                        db.collection("Users")
                                .document(uid+"/")
                                .update(Updateuser)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Toast.makeText(SettingsActivity.this, "Username Updated", Toast.LENGTH_SHORT).show();
//                                        Intent i=new Intent(SettingsActivity.this,SettingsActivity.class);
//                                        startActivity(i);
//                                        finish();
                                        showData(name,username,email);
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(SettingsActivity.this, "Something went wrong please try again", Toast.LENGTH_SHORT).show();
                                    }
                                });

                    }
                });

                changeUser.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                changeUser.show();
            }
        });

        changePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //change password in firebase
//                Intent intent=new Intent(SettingsActivity.this,ForgotPasswordActivity.class);
//                startActivity(intent);
//                finish();
                AlertDialog.Builder changePassword=new AlertDialog.Builder(SettingsActivity.this);
                changePassword.setTitle("Enter current password");

                final EditText password=new EditText(SettingsActivity.this);
                changePassword.setView(password);

                changePassword.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        db.collection("Users")
                                .document(uid+"/")
                                .get()
                                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                        String pw= getEncryptedPassword(password.getText().toString());
                                        DocumentSnapshot doc=task.getResult();
                                        Toast.makeText(SettingsActivity.this,doc.getString("password"), Toast.LENGTH_SHORT).show();
                                        Toast.makeText(SettingsActivity.this,pw, Toast.LENGTH_SHORT).show();
                                        if (pw.equals(doc.getString("password"))){
                                            confirmPassword();
                                        }
                                        else{
                                            Toast.makeText(SettingsActivity.this, "Incorrect Password", Toast.LENGTH_SHORT).show();
                                            password.getText().clear();
                                        }
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(SettingsActivity.this, "Somthing went wrong try again.", Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }
                });
                changePassword.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                changePassword.show();
            }
        });

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



        navbar.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Intent i;
                switch (item.getItemId()){
                    case R.id.menuHome:
                        i = new Intent(SettingsActivity.this, HomeActivity.class);
                        startActivity(i);
                        finish();
                        overridePendingTransition(0,0);
                        return true;

                    case R.id.menuUser:
                        i = new Intent(SettingsActivity.this, UserActivity.class);
                        startActivity(i);
                        finish();
                        overridePendingTransition(0,0);
                        return true;

//                    case R.id.menuActivate:
//                        i = new Intent(SettingsActivity.this, ActivateActivity.class);
//                        startActivity(i);
//                        finish();
//                        overridePendingTransition(0,0);
//                        return true;

                    case R.id.menuSettings:
                        return true;

                    default:
                        return false;
                }
            }

        });
    }

    private void confirmPassword() {

        AlertDialog.Builder NewPassword=new AlertDialog.Builder(SettingsActivity.this);
        NewPassword.setTitle("New Password");

        final EditText password=new EditText(SettingsActivity.this);
        password.setHint("Enter new Password");
        NewPassword.setView(password);

        final EditText Confirmpassword=new EditText(SettingsActivity.this);
        password.setHint("Confirm Password");
        NewPassword.setView(Confirmpassword);

        NewPassword.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (password.getText().toString().equals(Confirmpassword.getText().toString())){

                    String temp=getEncryptedPassword(password.getText().toString());
                    Updateuser.put("password",temp);
                    db.collection("Users")
                            .document(uid+"/")
                            .update(Updateuser)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(SettingsActivity.this, "Password changed successfully", Toast.LENGTH_SHORT).show();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(SettingsActivity.this, "Something went wrong try again", Toast.LENGTH_SHORT).show();
                                }
                            });
                }
                else
                {
                    Toast.makeText(SettingsActivity.this, "Password not match", Toast.LENGTH_SHORT).show();
                    password.getText().clear();
                    Confirmpassword.getText().clear();
                }
            }
        });
        NewPassword.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        NewPassword.show();

    }

    private String getEncryptedPassword(String password)  {
        String generatedPassword = null;
        String input = password;
        try {
            byte[] salt = getSalt();
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            md.update(salt);
            byte[] bytes = md.digest(input.getBytes());
            StringBuilder sb = new StringBuilder();
            for(int i=0; i< bytes.length ;i++)
            {
                sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
            }
            generatedPassword = sb.toString();
        }
        catch (NoSuchAlgorithmException e)
        {
            e.printStackTrace();
        }
        return generatedPassword;
    }
    private static byte[] getSalt() throws NoSuchAlgorithmException
    {
        SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
        byte[] salt = new byte[16];
        sr.nextBytes(salt);
        return salt;
    }


    public void showData(TextView name, TextView username, TextView email){

        db.collection("Users")
                .document(uid+"/")
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        DocumentSnapshot documentSnapshot=task.getResult();
                        if(task.isSuccessful()){
                            name.setText(documentSnapshot.getString("name"));
                            username.setText(documentSnapshot.getString("username"));
                            email.setText(documentSnapshot.getString("email"));
                        }
                    }
                });
    }
}