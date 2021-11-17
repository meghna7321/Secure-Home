package com.example.HomeSecurity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserActivity extends AppCompatActivity {

    Button addUser,removeUser;
    BottomNavigationView navbar;
    TextView adminUser,users;

    FirebaseAuth auth=FirebaseAuth.getInstance();
    FirebaseFirestore db= FirebaseFirestore.getInstance();
    FirebaseUser userDB = auth.getCurrentUser();
    String uid = userDB.getUid();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);


        addUser=(Button)findViewById(R.id.add_usertext);
        removeUser=(Button)findViewById(R.id.remove_user);
        adminUser=(TextView)findViewById(R.id.AdminUser);
        users=(TextView)findViewById(R.id.user2);

        //Set current User name
        String username = userDB.getDisplayName();
        adminUser.setText(username);


        //set users
        FirebaseFirestore.getInstance()
                .collection("Users")
                .document(uid+"/")
                .collection("FamilyUser")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {

                        if(task.isSuccessful()){
                            if(!task.getResult().isEmpty()){
                                String listUser;
                                users.setText("");
                                for(QueryDocumentSnapshot document: task.getResult()){
                                    //listUser.add(document.getId()+"\n");
                                    listUser=document.getId();
                                    users.append(listUser+"\n");
                                    //users.setText(listUser.toString());
                                }
                            }
                            else
                            {
                              users.setText("No user added");
                            }
                        }
                        else
                        {
                            users.setText("No user added");
                        }
                    }
                });


        addUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(UserActivity.this,Add_user.class);
                startActivity(intent);
                finish();
                
                //add user from firebase and add user list into textview user2

            }
        });

        removeUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //remove user from firebase and update textview with new user list
                AlertDialog.Builder removeUser=new AlertDialog.Builder(UserActivity.this);
                removeUser.setTitle("Enter user name");

                final EditText username=new EditText(UserActivity.this);
                removeUser.setView(username);

                removeUser.setPositiveButton("remove", new DialogInterface.OnClickListener(){

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(UserActivity.this, "user removed", Toast.LENGTH_SHORT).show();
                        String user=username.getText().toString();
                        db.collection("Users")
                                .document(uid+"/")
                                .collection("FamilyUser")
                                .document(user+"/")
                                .delete()
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        Intent i=new Intent(UserActivity.this,UserActivity.class);
                                        startActivity(i);
                                        finish();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(UserActivity.this, "please enter valid user", Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }
                });

                removeUser.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                removeUser.show();
            }
        });


        navbar = (BottomNavigationView)findViewById(R.id.navbar);
        navbar.setSelectedItemId(R.id.menuUser);

        navbar.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Intent i;
                switch (item.getItemId()){
                    case R.id.menuHome:
                        i = new Intent(UserActivity.this, HomeActivity.class);
                        startActivity(i);
                        finish();
                        overridePendingTransition(0,0);
                        return true;

                    case R.id.menuUser:
                        return true;

//                    case R.id.menuActivate:
//                        i = new Intent(UserActivity.this, ActivateActivity.class);
//                        startActivity(i);
//                        finish();
//                        overridePendingTransition(0,0);
//                        return true;

                    case R.id.menuSettings:
                        i = new Intent(UserActivity.this, SettingsActivity.class);
                        startActivity(i);
                        finish();
                        overridePendingTransition(0,0);
                        return true;

                    default:
                        return false;
                }
            }

        });

    }

    @Override
    protected void onStart() {



        super.onStart();

    }
}
