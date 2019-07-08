package lab.ourteam.newlab.Bean;

import android.graphics.Bitmap;

import java.io.Serializable;

public class Device implements Serializable{//设备的基本信息
    private int deviceid;//设备编号
    private int suid; //设备唯一id
    private String devicename;
    private String dPiturePath;
    private int Piture;
    private int status;//0 停止 1运行 2暂停
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

    public int getPiture() {
        return Piture;
    }
    public void setdPiture(int dPiture) {
        this.Piture = dPiture;
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

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}