package org.example.setup;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.ios.IOSDriver;
import org.example.GlobalConfig;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.net.URL;
import java.time.Duration;

public class DriverSetup extends GlobalConfig {
    public static AppiumDriver driver;

    public static void startDriver(String appiumUrl) {
        try {
            DesiredCapabilities caps = new CapsGenerator().getAppCapabilities(appType);
            driver = isIos ? new IOSDriver(new URL(appiumUrl), caps) : new AppiumDriver(new URL(appiumUrl), caps);
            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(Integer.parseInt(appConfig.getString("IMPLICIT_WAIT"))));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void closeDriver() {
        try {
            driver.quit();
        } catch (Exception ignored) {
        }
    }
}