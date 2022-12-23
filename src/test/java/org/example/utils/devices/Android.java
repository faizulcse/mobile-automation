package org.example.utils.devices;

import org.example.utils.CommandLineHelper;

public class Android extends DeviceType {
    public Android(String id) {
        setId(id);
    }

    public String getDeviceName() {
        return getId();
    }

    public String getPlatformVersion() {
        return CommandLineHelper.execute("adb -s " + getId() + " shell getprop ro.build.version.release").get(0);
    }

    public String getUdid() {
        return CommandLineHelper.execute("adb -s " + getId() + " shell 'settings get secure android_id'").get(0);
    }
}
