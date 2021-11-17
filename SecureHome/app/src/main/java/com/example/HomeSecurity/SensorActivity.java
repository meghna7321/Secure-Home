package com.example.HomeSecurity;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.app.NotificationCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SensorActivity extends AppCompatActivity {

    ImageView back;
    TextView add, labelText,addDeviceExtra;
    AlertDialog.Builder dialogBuilder;
    AlertDialog dialog;
    EditText label, deviceNo;
    Button cancel, addDevice;
    FirebaseDatabase database;
    FirebaseAuth auth;
    DatabaseReference ref;
    FirebaseFirestore db;
    RecyclerView sensorList;
    DeviceAdapter adapter;
    DatabaseReference status, enabled;
    ArrayList<Device> deviceList;
    SwitchCompat addDeviceSwitch;
    Bundle b;
    String extra;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensors);

        b = getIntent().getExtras();
        extra = b.getString("extra");

        back = findViewById(R.id.sensorBack);
        add = findViewById(R.id.sensorAdd);
        labelText = findViewById(R.id.sensorText);
        addDeviceExtra = findViewById(R.id.addDeviceExtra);
        addDeviceSwitch = (SwitchCompat) findViewById(R.id.addDeviceSwitch);
        database = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        sensorList = findViewById(R.id.sensorList);
        sensorList.setHasFixedSize(true);
        sensorList.setLayoutManager(new LinearLayoutManager(this));
        deviceList = new ArrayList<Device>();
        adapter = new DeviceAdapter(this, deviceList);
        sensorList.setAdapter(adapter);

        labelText.setText(extra+" Sensors");

        //getting sensors list
        String uid = auth.getCurrentUser().getUid();
        deviceList.clear();
        db.collection("Users/" + uid + "/" + extra + " Sensors/")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        for (QueryDocumentSnapshot doc : task.getResult()) {
                            String deviceNumber = doc.get("deviceNo").toString();
                            addDeviceToList(deviceNumber);
                        }
                    }
                });

        //go back to home page
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SensorActivity.this, HomeActivity.class));
                finish();
            }
        });

        //to open add device popup
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createDialog();
            }
        });

    }
    private void createDialog() {
        dialogBuilder = new AlertDialog.Builder(SensorActivity.this);
        final View doorPopup = getLayoutInflater().inflate(R.layout.pop_up, null);

        label = doorPopup.findViewById(R.id.popUplabel);
        deviceNo = doorPopup.findViewById(R.id.popUpDeviceNo);
        cancel = doorPopup.findViewById(R.id.popUpCancel);
        addDevice = doorPopup.findViewById(R.id.popUpAdd);

        dialogBuilder.setView(doorPopup);
        dialog = dialogBuilder.create();
        dialog.show();

        addDevice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String txt_label = label.getText().toString();
                String txt_deviceNo = deviceNo.getText().toString();

                if (TextUtils.isEmpty(txt_label) || TextUtils.isEmpty(txt_deviceNo)) {
                    Toast.makeText(SensorActivity.this, "Please Enter All Details", Toast.LENGTH_LONG).show();
                }
                else
                    {

                    ref = database.getReference().child(extra);
                    ref.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.hasChild(txt_deviceNo)) {
                                if (!snapshot.hasChild(txt_deviceNo + "/User_id")) {
                                    //Realtime Database Update
                                    String uid = auth.getCurrentUser().getUid();
                                    Map<String, Object> device = new HashMap<>();
                                    device.put("label", txt_label);
                                    device.put("deviceNo", txt_deviceNo);
                                    device.put("User_id", uid);
                                    device.put("enabled",true);
                                    ref.child(txt_deviceNo).updateChildren(device);

                                    //Firestore Update
                                    db.collection("Users").document(uid + "/"+ extra + " Sensors/" + txt_label)
                                            .set(device)
                                            .addOnCompleteListener(SensorActivity.this, new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()) {
                                                        dialog.dismiss();
                                                        deviceList.clear();
                                                        db.collection("Users/" + uid + "/"+extra+" Sensors/")
                                                                .get()
                                                                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                                    @Override
                                                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                                        for (QueryDocumentSnapshot doc : task.getResult()) {
                                                                            String deviceNumber = doc.get("deviceNo").toString();
                                                                            addDeviceToList(deviceNumber);
                                                                        }
                                                                    }
                                                                });
                                                    }
                                                }
                                            });
                                } else {
                                    Toast.makeText(SensorActivity.this, "Invalid Device Number...", Toast.LENGTH_SHORT).show();
                                    deviceNo.setText("");
                                }

                            } else {
                                Toast.makeText(SensorActivity.this, "Invalid Device Number", Toast.LENGTH_SHORT).show();
                                deviceNo.setText("");
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Toast.makeText(SensorActivity.this, "Something Went Wrong.Try Again Later.", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    private void addDeviceToList(String deviceNo) {
        final int[] pos = new int[1];
        status = database.getReference().child(extra).child(deviceNo);
        status.keepSynced(true);

        //Called when new device is added to list
        status.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Device d = snapshot.getValue(Device.class);
                String status = d.getStatus();
                String label = d.getLabel();
                boolean enabled = d.getEnabled();
                String deviceNo = d.getDeviceNo();
                d.setLabel(label);
                d.setStatus(status);
                d.setEnabled(enabled);
                d.setDeviceNo(deviceNo);
                d.setExtra(extra);
                deviceList.add(d);
                pos[0] = deviceList.indexOf(d);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
        //Called when status of device changes
        status.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Device d = snapshot.getValue(Device.class);
                String status = d.getStatus();
                String label = d.getLabel();
                boolean enabled = d.getEnabled();
                deviceList.get(pos[0]).setStatus(status);
                deviceList.get(pos[0]).setEnabled(enabled);
                adapter.notifyItemChanged(pos[0]);
                if(status.equals("Open") && enabled == true)
                {
                    String message = "Someone has opened the "+label;
                    notifyUser(message);
                }
                else if(status.equals("Detecting") && enabled == true)
                {
                    String message = extra+" is being detected in "+label;
                    notifyUser(message);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void notifyUser(String message) {
        Intent notificationIntent = new Intent(SensorActivity.this,SensorActivity.class);
        notificationIntent.putExtra("extra",extra);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        PendingIntent contentIntent = PendingIntent.getActivity(this,0,notificationIntent,PendingIntent.FLAG_UPDATE_CURRENT);


        NotificationCompat.Builder notification = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle("Home Security")
                .setStyle(new NotificationCompat.BigTextStyle()
                .bigText(message))
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true)
                .setContentIntent(contentIntent);

        // Add as notification
        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        manager.notify(0, notification.build());
    }

}
