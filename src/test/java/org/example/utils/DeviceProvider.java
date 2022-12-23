package org.example.utils;

import org.example.utils.devices.Android;
import org.example.utils.devices.DeviceType;
import org.example.utils.devices.Ios;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DeviceProvider {
    private final boolean isIos;
    private static final HashMap<String, Boolean> udid = new HashMap<>();

    public DeviceProvider(String appType) {
        this.isIos = appType.equalsIgnoreCase("ios");
        List<String> devices = isIos ? CommandLineHelper.getIosDevices() : CommandLineHelper.getAndroidDevices();
        if (devices.size() == 0)
            throw new RuntimeException("No " + appType + " devices connected");
        for (String id : devices)
            udid.putIfAbsent(id, true);
    }

    private synchronized boolean isAny() {
        return udid.containsValue(true);
    }

    private synchronized DeviceType deviceLock(String id) {
        DeviceType device = isIos ? new Ios(id) : new Android(id);
        device.setPlatformName(isIos ? "iOS" : "android");
        udid.replace(id, false);
        return device;
    }

    public synchronized void deviceUnlock(String id) {
        udid.replace(id, true);
    }

    public synchronized DeviceType getDevice(String id) {
        return (udid.containsKey(id) && udid.get(id)) ? deviceLock(id) : getDevice();
    }

    public synchronized DeviceType getDevice() {
        if (isAny()) {
            for (Map.Entry<String, Boolean> device : udid.entrySet()) {
                if (udid.get(device.getKey()))
                    return deviceLock(device.getKey());
            }
        }
        throw new RuntimeException("No available device for running test");
    }
}