package org.example.utils.devices;

import org.example.utils.Command;
import org.example.utils.CommandLine;

public class Android extends DeviceType {
    public Android(String id) {
        setId(id);
    }

    public String getDeviceName() {
        return getId();
    }

    public String getPlatformVersion() {
        return CommandLine.execute(String.format(Command.ANDROID_PLATFORM_VERSION, getId())).get(0);
    }

    public String getUdid() {
        return CommandLine.execute(String.format(Command.ANDROID_DEVICE_UDID, getId())).get(0);
    }
}
