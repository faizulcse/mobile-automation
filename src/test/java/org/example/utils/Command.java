package org.example.utils;

public interface Command {
    String ADB_DEVICES_COMMAND = "adb devices";
    String ATTACHED_DEVICES_MSG = "List of devices attached";
    String ANDROID_PLATFORM_VERSION = "adb -s %s shell getprop ro.build.version.release";
    String ANDROID_DEVICE_UDID = "adb -s %s shell 'settings get secure android_id'";
    String IOS_DEVICE_COMMAND = "xcrun xctrace list devices";
    String IOS_DEVICES_MSG = "== Devices ==";
}
