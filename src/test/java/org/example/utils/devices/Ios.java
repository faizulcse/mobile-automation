package org.example.utils.devices;

public class Ios extends DeviceType {
    public Ios(String id) {
        setId(id);
    }

    public String getDeviceName() {
        String[] words = getId().split(getPlatformVersion());
        return String.format("%s", words[0].trim());
    }

    public String getPlatformVersion() {
        String[] words = getId().split("\\s");
        return String.format("%s", getId().split("\\s")[words.length - 2].trim());
    }

    public String getUdid() {
        String[] words = getId().split("\\s");
        return String.format("%s", getId().split("\\s")[words.length - 1].trim());
    }
}
