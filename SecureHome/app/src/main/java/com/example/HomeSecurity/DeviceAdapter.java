package com.example.HomeSecurity;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.database.FirebaseDatabase;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SwitchCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class DeviceAdapter extends RecyclerView.Adapter<DeviceAdapter.DeviceViewHolder> {

    ArrayList<Device> device;
    Context context;
    static FirebaseDatabase database = FirebaseDatabase.getInstance();


    public DeviceAdapter(Context context, ArrayList<Device> device) {
        this.context = context;
        this.device = device;
    }

    @Override
    public void onBindViewHolder(@NonNull DeviceViewHolder holder, int position) {
        Device d = device.get(position);
        holder.status.setText(d.getStatus());
        holder.label.setText(d.getLabel());
        holder.addDeviceSwitch.setChecked(d.getEnabled());
        holder.deviceNo.setText(d.getDeviceNo());
        holder.extra.setText(d.getExtra());
    }

    @NonNull
    @Override
    public DeviceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.add_device, parent, false);
        return new DeviceViewHolder(view);
    }

    @Override
    public int getItemCount() {
        if(device.size() == 0){
            return 0;
        }else{
            return device.size();
        }
    }

    public static class DeviceViewHolder extends RecyclerView.ViewHolder{

        TextView status, label, extra, deviceNo;
        SwitchCompat addDeviceSwitch;
        public DeviceViewHolder(@NonNull View itemView) {
            super(itemView);
            status = itemView.findViewById(R.id.addDeviceStatus);
            label = itemView.findViewById(R.id.addDeviceLabel);
            extra = itemView.findViewById(R.id.addDeviceExtra);
            deviceNo = itemView.findViewById(R.id.addDeviceDeviceNo);
            addDeviceSwitch = itemView.findViewById(R.id.addDeviceSwitch);
            addDeviceSwitch.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    toggleState(deviceNo.getText().toString(), extra.getText().toString());
                }
            });
        }

        private void toggleState(String deviceNumber, String extra) {

            if(addDeviceSwitch.isChecked()){
                database.getReference().child(extra).child(deviceNumber).child("enabled").setValue(true);
                addDeviceSwitch.setChecked(true);
            }
            else{
                database.getReference().child(extra).child(deviceNumber).child("enabled").setValue(false);
                addDeviceSwitch.setChecked(false);
            }
        }

    }
}
