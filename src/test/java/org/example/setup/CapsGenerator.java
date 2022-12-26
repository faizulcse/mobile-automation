package org.example.setup;

import io.appium.java_client.remote.AndroidMobileCapabilityType;
import io.appium.java_client.remote.AutomationName;
import io.appium.java_client.remote.IOSMobileCapabilityType;
import io.appium.java_client.remote.MobileCapabilityType;
import org.example.Global;
import org.example.utils.FileHelper;
import org.example.utils.devices.DeviceType;
import org.openqa.selenium.Platform;
import org.openqa.selenium.remote.DesiredCapabilities;

public class CapsGenerator {

    public DesiredCapabilities getCaps(DeviceType device) {
        String lang = "en";
        String locale = "us";
        DesiredCapabilities caps = new DesiredCapabilities();
        caps.setCapability(MobileCapabilityType.DEVICE_NAME, device.getDeviceName());
        caps.setCapability(MobileCapabilityType.PLATFORM_VERSION, device.getPlatformVersion());
        if (Global.isIos) {
            caps.setCapability(MobileCapabilityType.PLATFORM_NAME, Platform.IOS);
            caps.setCapability(MobileCapabilityType.AUTOMATION_NAME, AutomationName.IOS_XCUI_TEST);
            caps.setCapability(MobileCapabilityType.UDID, device.getUdid());
            caps.setCapability(MobileCapabilityType.APP, FileHelper.findFile(Global.iosApp));
            caps.setCapability(IOSMobileCapabilityType.AUTO_ACCEPT_ALERTS, true);
            caps.setCapability(MobileCapabilityType.LANGUAGE, lang + "_" + locale);
        } else {
            caps.setCapability(MobileCapabilityType.PLATFORM_NAME, Platform.ANDROID);
            caps.setCapability(MobileCapabilityType.AUTOMATION_NAME, AutomationName.APPIUM);
            caps.setCapability(MobileCapabilityType.UDID, device.getDeviceName());
            caps.setCapability(MobileCapabilityType.APP, FileHelper.findFile(Global.androidApp));
            caps.setCapability(AndroidMobileCapabilityType.APP_WAIT_ACTIVITY, "*");
            caps.setCapability(AndroidMobileCapabilityType.AUTO_GRANT_PERMISSIONS, true);
            caps.setCapability(MobileCapabilityType.LANGUAGE, lang);
        }
        caps.setCapability(MobileCapabilityType.LOCALE, locale);
        caps.setCapability(MobileCapabilityType.FULL_RESET, true);
        caps.setCapability(MobileCapabilityType.NO_RESET, false);
        return caps;
    }
}
