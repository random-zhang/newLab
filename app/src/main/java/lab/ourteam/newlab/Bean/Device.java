package lab.ourteam.newlab.Bean;

import android.graphics.Bitmap;

import java.io.Serializable;

public class Device implements Serializable{//设备的基本信息
    private int deviceid;//设备编号
    private int suid; //设备唯一id
    private String devicename;
    private String dPiturePath;
    private Bitmap dPiture;
    private boolean isRunning;
    private String wssid;//设备ssid
    public int getDeviceid() {
        return deviceid;
    }

    public void setDeviceid(int deviceid) {
        this.deviceid = deviceid;
    }

    public String getDevicename() {
        return devicename;
    }

    public void setDevicename(String devicename) {
        this.devicename = devicename == null ? null : devicename.trim();
    }
    public String getDevicepiture() {
        return dPiturePath;
    }

    public void setDevicepiture(String devicepiture) {
        this.dPiturePath = devicepiture == null ? null : devicepiture.trim();
    }

    public Bitmap getdPiture() {
        return dPiture;
    }
    public void setdPiture(Bitmap dPiture) {
        this.dPiture = dPiture;
    }

    public boolean isRunning() {
        return isRunning;
    }

    public void setRunning(boolean running) {
        isRunning = running;
    }

    public String getWssid() {
        return wssid;
    }

    public void setWssid(String wssid) {
        this.wssid = wssid;
    }
    public int getSuid() {
        return suid;
    }

    public void setSuid(int suid) {
        this.suid = suid ;
    }
    public int getSubId(){
        return suid;
    }
}