package org.example.utils.devices;

public class Ios extends DeviceType {
    public Ios(String id) {
        setId(id);
    }

    public String getDeviceName() {
        return getId().split("\\s")[0] + " " + getId().split("\\s")[1];
    }

    public String getPlatformVersion() {
        return getId().split("\\s")[1];
    }

    public String getUdid() {
        return getId().split("\\s")[2];
    }
}
