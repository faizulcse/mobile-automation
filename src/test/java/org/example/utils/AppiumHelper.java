package org.example.utils;

import io.appium.java_client.service.local.AppiumDriverLocalService;
import io.appium.java_client.service.local.AppiumServiceBuilder;
import io.appium.java_client.service.local.flags.GeneralServerFlag;

import java.io.IOException;
import java.net.Socket;
import java.net.URL;

public class AppiumHelper {

    public static AppiumDriverLocalService getAppiumService() {
        AppiumServiceBuilder builder = new AppiumServiceBuilder();
        builder.withArgument(GeneralServerFlag.SESSION_OVERRIDE);
        builder.withArgument(GeneralServerFlag.LOG_LEVEL, "error");
        builder.usingAnyFreePort();
        return AppiumDriverLocalService.buildService(builder);
    }

    public static boolean isActive(String appiumUrl) {
        try {
            URL url = new URL(appiumUrl);
            Socket socket = new Socket(url.getHost(), url.getPort());
            socket.close();
            return true;
        } catch (IOException ignored) {
            return false;
        }
    }
}
