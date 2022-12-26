package org.example;

import org.example.utils.ResourceHelper;

import java.io.File;

public interface Global {
    ResourceHelper app = new ResourceHelper("app-config").getBundle();
    String automationName = app.getString("AUTOMATION_NAME");
    int implicitWait = app.getInt("IMPLICIT_WAIT");
    int explicitWait = app.getInt("EXPLICIT_WAIT");
    String consoleLog = app.getString("CONSOLE");

    String langName = app.getString("LANGUAGE").toLowerCase();
    String appPlatform = app.getString("APP_PLATFORM").toLowerCase();
    boolean isIos = appPlatform.equals("ios");
    String iosApp = app.getString("IOS_APP");
    String iosDevice = app.getString("IOS_DEVICE");
    String androidApp = app.getString("ANDROID_APP");
    String androidDevice = app.getString("ANDROID_DEVICE");

    String rootDir = System.getProperty("user.dir") + File.separator;
    String env = rootDir + "src/test/java/org/example/utils/data/env.json";
    String screenshots = rootDir + "screenshots/";
}
