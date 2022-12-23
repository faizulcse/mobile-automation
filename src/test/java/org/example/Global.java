package org.example;

import org.example.utils.ResourceHelper;

public interface Global {
    ResourceHelper app = new ResourceHelper("app-config").getBundle();
    String automationName = app.getString("AUTOMATION_NAME");
    int implicitWait = app.getInt("IMPLICIT_WAIT");
    int explicitWait = app.getInt("EXPLICIT_WAIT");
    String consoleLog = app.getString("CONSOLE");

    String langName = app.getString("LANGUAGE").toLowerCase();
    String appType = app.getString("APP_TYPE").toLowerCase();
    boolean isIos = appType.equals("ios");
    String deviceName = app.getString(isIos ? "IOS_DEVICE_NAME" : "ANDROID_DEVICE_NAME");
}
