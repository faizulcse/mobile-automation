package org.example.setup;

import io.appium.java_client.remote.AndroidMobileCapabilityType;
import io.appium.java_client.remote.IOSMobileCapabilityType;
import io.appium.java_client.remote.MobileCapabilityType;
import org.example.GlobalConfig;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.io.File;
import java.util.Objects;

public class CapsGenerator extends GlobalConfig {
    public DesiredCapabilities getAppCapabilities(String appType) {
        File[] appPath = Objects.requireNonNull(new File(rootDir).listFiles((dir, file) -> file.endsWith(appConfig.getString("APP"))));
        if (appPath.length == 0)
            throw new RuntimeException(String.format("ERROR! Invalid %S app path '%s'", appType, rootDir));

        DesiredCapabilities caps = new DesiredCapabilities();
        switch (appType) {
            case "ios":
                caps.setCapability(MobileCapabilityType.DEVICE_NAME, appConfig.getString("DEVICE_NAME"));
                caps.setCapability(MobileCapabilityType.PLATFORM_NAME, appConfig.getString("PLATFORM_NAME"));
                caps.setCapability(MobileCapabilityType.PLATFORM_VERSION, appConfig.getString("PLATFORM_VERSION"));
                caps.setCapability(MobileCapabilityType.AUTOMATION_NAME, appConfig.getString("AUTOMATION_NAME"));
                caps.setCapability(MobileCapabilityType.UDID, appConfig.getString("UDID"));
                caps.setCapability(MobileCapabilityType.APP, appPath[0].getAbsolutePath());
                caps.setCapability(IOSMobileCapabilityType.AUTO_ACCEPT_ALERTS, appConfig.getString("AUTO_ACCEPT_ALERTS"));
                caps.setCapability(MobileCapabilityType.FULL_RESET, appConfig.getString("FULL_RESET"));
                caps.setCapability(MobileCapabilityType.NO_RESET, appConfig.getString("NO_RESET"));
                caps.setCapability(MobileCapabilityType.LANGUAGE, "en");
                caps.setCapability(MobileCapabilityType.LOCALE, "en_us");
                return caps;
            case "android":
                caps.setCapability(MobileCapabilityType.DEVICE_NAME, appConfig.getString("DEVICE_NAME"));
                caps.setCapability(MobileCapabilityType.PLATFORM_NAME, appConfig.getString("PLATFORM_NAME"));
                caps.setCapability(MobileCapabilityType.PLATFORM_VERSION, appConfig.getString("PLATFORM_VERSION"));
                caps.setCapability(MobileCapabilityType.AUTOMATION_NAME, appConfig.getString("AUTOMATION_NAME"));
                caps.setCapability(MobileCapabilityType.UDID, appConfig.getString("UDID"));
                caps.setCapability(MobileCapabilityType.APP, appPath[0].getAbsolutePath());
                caps.setCapability(AndroidMobileCapabilityType.APP_WAIT_ACTIVITY, "*");
                caps.setCapability(AndroidMobileCapabilityType.AUTO_GRANT_PERMISSIONS, appConfig.getString("AUTO_GRANT_PERMISSIONS"));
                caps.setCapability(MobileCapabilityType.FULL_RESET, appConfig.getString("FULL_RESET"));
                caps.setCapability(MobileCapabilityType.NO_RESET, appConfig.getString("NO_RESET"));
                caps.setCapability(MobileCapabilityType.LANGUAGE, "en");
                caps.setCapability(MobileCapabilityType.LOCALE, "us");
                return caps;
            default:
                throw new RuntimeException(String.format("Unknown App type: %s", appType));
        }
    }
}
