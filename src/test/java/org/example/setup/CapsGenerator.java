package org.example.setup;

import org.example.utils.FileHelper;
import org.example.utils.devices.DeviceType;
import org.openqa.selenium.remote.DesiredCapabilities;

public class CapsGenerator {

    public DesiredCapabilities getCaps(DeviceType type) {
        String lang = "en";
        String locale = "us";
        boolean isIos = type.getPlatformName().equalsIgnoreCase("ios");

        DesiredCapabilities caps = new DesiredCapabilities();
        caps.setCapability("deviceName", type.getDeviceName());
        caps.setCapability("platformName", type.getPlatformName());
        caps.setCapability("platformVersion", type.getPlatformVersion());
        caps.setCapability("udid", isIos ? type.getUdid() : type.getDeviceName());
        caps.setCapability("automationName", isIos ? "XCUITest" : "appium");
        caps.setCapability("app", FileHelper.findFile(isIos ? ".ipa" : ".apk"));
        caps.setCapability("language", isIos ? lang + "_" + locale : lang);
        caps.setCapability("locale", locale);
        caps.setCapability("appWaitActivity", "*");
        caps.setCapability("autoGrantPermissions", true);
        caps.setCapability("autoAcceptAlerts", true);
        caps.setCapability("fullReset", true);
        caps.setCapability("noReset", false);
        return caps;
    }
}
