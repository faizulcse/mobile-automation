package org.example.utils.devices;

import org.example.utils.CommandLine;

public class Android extends DeviceType {
    public Android(String id) {
        setId(id);
    }

    public String getDeviceName() {
        return getId();
    }

    public String getPlatformVersion() {
        return CommandLine.execute(String.format("adb -s %s shell getprop ro.build.version.release", getId())).get(0);
    }

    public String getUdid() {
        return CommandLine.execute(String.format("adb -s %s shell 'settings get secure android_id'", getId())).get(0);
    }
}
