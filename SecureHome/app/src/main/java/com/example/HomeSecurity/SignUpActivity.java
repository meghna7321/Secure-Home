
package com.example.HomeSecurity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
//import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.FirebaseFirestore;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;

public class SignUpActivity extends AppCompatActivity {

    TextView back;
    EditText name, username, email, password, confirmPassword;
    Button signUp;
    FirebaseAuth auth;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        back = (TextView) findViewById(R.id.signUpBack);
        username = (EditText) findViewById(R.id.signUpUsername);
        password = (EditText) findViewById(R.id.signUpPassword);
        name = (EditText) findViewById(R.id.signUpName);
        email = (EditText) findViewById(R.id.signUpEmail);
        confirmPassword = (EditText) findViewById(R.id.signUpConfirmPassword);
        signUp = (Button) findViewById(R.id.signUpSignUp);
        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignUpActivity.this, StartActivity.class));
                finish();
            }
        });

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String txt_name = name.getText().toString();
                String txt_email = email.getText().toString();
                String txt_username = username.getText().toString();
                String txt_password = password.getText().toString();
                String txt_confirmPassword = confirmPassword.getText().toString();

                if(TextUtils.isEmpty(txt_name) || TextUtils.isEmpty(txt_email) || TextUtils.isEmpty(txt_password) || TextUtils.isEmpty(txt_confirmPassword) || TextUtils.isEmpty(txt_username)){
                    Toast.makeText(SignUpActivity.this,"Please Enter All The Details",Toast.LENGTH_LONG).show();
                }
                else if(!txt_password.equals(txt_confirmPassword)){
                    Toast.makeText(SignUpActivity.this,"Passwords do not match",Toast.LENGTH_SHORT).show();
                    password.setText("");
                    confirmPassword.setText("");
                }
                else if(txt_password.length() <= 8){
                    Toast.makeText(SignUpActivity.this,"Password should have more than 8 characters",Toast.LENGTH_SHORT).show();
                    password.setText("");
                    confirmPassword.setText("");
                }
                else{
                    registerUser(txt_email,txt_password,txt_name,txt_username);
                }
            }
        });
    }

    private void registerUser(String txt_email, String txt_password, String txt_name, String txt_username) {
        auth.createUserWithEmailAndPassword(txt_email,txt_password).addOnCompleteListener(SignUpActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    FirebaseUser userDB = auth.getCurrentUser();
                    String uid = userDB.getUid();

                    UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                            .setDisplayName(txt_name).build();

                    userDB.updateProfile(profileUpdates);

                    String hashedPassword = getEncryptedPassword(txt_password);
                    //Toast.makeText(SignUpActivity.this, hashedPassword, Toast.LENGTH_SHORT).show();
                    Map<String, Object> user = new HashMap<>();
                    user.put("userID",uid);
                    user.put("name",txt_name);
                    user.put("username",txt_username);
                    user.put("email",txt_email);
                    user.put("password",hashedPassword);

                    db.collection("Users")
                            .document(uid+"/")
                            .set(user)
                            .addOnCompleteListener(SignUpActivity.this, new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        startActivity(new Intent(SignUpActivity.this,HomeActivity.class));
                                        finish();
                                    }
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(SignUpActivity.this,"Something Went Wrong. Try Again Later",Toast.LENGTH_SHORT).show();
                                }
                            });
                }else{
                    Toast.makeText(SignUpActivity.this,"Something Went Wrong. Try Again Later",Toast.LENGTH_SHORT).show();
                }
            }
        });
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
}