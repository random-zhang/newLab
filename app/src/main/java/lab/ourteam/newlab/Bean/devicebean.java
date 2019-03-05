package lab.ourteam.newlab.Bean;

import java.io.Serializable;

public class devicebean implements Serializable {
    private int deviceId;
    private String deviceName;
    public devicebean(int deviceId,String deviceName){
        this.deviceId=deviceId;
        this.deviceName=deviceName;
    }
    public void setDeviceId(int deviceId) {
        this.deviceId = deviceId;
    }

    public int getDeviceId() {
        return deviceId;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public String getDeviceName() {
        return deviceName;
    }
}
