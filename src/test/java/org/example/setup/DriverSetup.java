package org.example.setup;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import org.example.Global;
import org.example.utils.AppiumHelper;
import org.example.utils.LogMsg;
import org.example.utils.devices.DeviceType;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.util.logging.Level;
import java.util.logging.Logger;

public class DriverSetup {
    public final static ThreadLocal<AppiumHelper> appiumThread = new ThreadLocal<>();
    public final static ThreadLocal<AppiumDriver<MobileElement>> driverThread = new ThreadLocal<>();

    public static void startDriver(DeviceType type) {
        Logger.getLogger(LogMsg.ORG_OPEN_QA_SELENIUM).setLevel(Level.OFF);
        try {
            DesiredCapabilities caps = new CapsGenerator().getCaps(type);
            AppiumHelper appium = new AppiumHelper();
            AppiumDriver<MobileElement> driver = new AppiumDriver<>(appium.getUrl(), caps);
            driverThread.set(driver);
            appiumThread.set(appium);
            System.out.printf(LogMsg.APP_INSTALLED_MSG, type.getPlatformName(), getAppPack());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String getAppPack() {
        return driverThread.get().getCapabilities().getCapability(Global.isIos ? "bundleId" : "appPackage").toString();
    }

    public static AppiumDriver<MobileElement> getAppiumDriver() {
        return driverThread.get();
    }

    public static void closeDriver() {
        try {
            if (driverThread.get() != null) driverThread.get().quit();
            if (appiumThread.get() != null) appiumThread.get().stopServer();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}