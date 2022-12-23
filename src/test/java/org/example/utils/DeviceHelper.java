package org.example.utils;

import org.example.utils.devices.Android;
import org.example.utils.devices.DeviceType;
import org.example.utils.devices.Ios;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DeviceHelper {
    HashMap<String, Boolean> udid = new HashMap<>();
    String iosCommand = "xcrun xctrace list devices | grep 'Booted'";
    String androidCommand = "adb devices";
    String appType;
    boolean isIos;

    public DeviceHelper(String appType) {
        this.appType = appType.toLowerCase();
        this.isIos = appType.contains("ios");
    }

    public DeviceHelper loadDevices() {
        List<String> devices = isIos ? getIosDevices() : getAndroidDevices();
        if (devices.size() == 0)
            throw new RuntimeException(String.format("No %s devices connected", appType));
        for (String id : devices)
            udid.putIfAbsent(id, true);
        return this;
    }

    private synchronized boolean isAny() {
        return udid.containsValue(true);
    }

    private synchronized DeviceType deviceLock(String id) {
        DeviceType device = isIos ? new Ios(id) : new Android(id);
        device.setPlatformName(appType);
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
        throw new RuntimeException(String.format("No %s device available for running test.", appType));
    }

    private List<String> getAndroidDevices() {
        List<String> devices = CommandLine.execute(androidCommand);
        devices.remove("List of devices attached");
        List<String> list = new ArrayList<>();
        for (String device : devices) {
            if (device.contains("device"))
                list.add(device.split("\\s+")[0]);
        }
        return list;
    }

    private List<String> getIosDevices() {
        List<String> devices = CommandLine.execute(iosCommand);
        List<String> list = new ArrayList<>();
        for (String device : devices) {
            String info = (device.split("(Booted)")[0]).trim();
            String[] word = info.split("\\(");
            String[] model = word[0].split("\\s");
            String deviceName = model[0] + " ";
            String osVersion = model[1] + " ";
            String udid = word[1].replace(")", "").trim();
            list.add(deviceName + osVersion + udid);
        }
        return list;
    }
}