package org.example;

import java.io.File;
import java.util.ResourceBundle;

public class GlobalConfig {
    public static String rootDir = System.getProperty("user.dir") + File.separator;
    public static String serverUrl = "http://127.0.0.1:4723";
    public static String screenshots = rootDir + "screenshots/";
    public static String usersData = rootDir + "src/test/java/org/example/data/users.json";
    public static boolean isIos = Boolean.parseBoolean(System.getProperty("IOS"));
    public static String appType = isIos ? "ios" : "android";
    public static ResourceBundle appConfig = ResourceBundle.getBundle(appType);
}
