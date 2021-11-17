package com.example.HomeSecurity;

public class Device {
    private String status;
    private String label;
    private String extra;
    private String deviceNo;
    private boolean enabled;

    public Device() {
    }

    public Device(String status, String label, boolean enabled,String extra,String deviceNo) {
        this.status = status;
        this.label = label;
        this.extra = extra;
        this.deviceNo = deviceNo;
        this.enabled = enabled;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getDeviceNo() {
        return deviceNo;
    }

    public void setDeviceNo(String deviceNo) {
        this.deviceNo = deviceNo;
    }

    public String getExtra() {
        return extra;
    }

    public void setExtra(String extra) {
        this.extra = extra;
    }

    public boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}
