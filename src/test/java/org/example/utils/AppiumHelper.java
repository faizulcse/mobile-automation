package org.example.utils;

import io.appium.java_client.service.local.AppiumDriverLocalService;
import io.appium.java_client.service.local.AppiumServiceBuilder;
import io.appium.java_client.service.local.flags.GeneralServerFlag;

import java.net.URL;

public class AppiumHelper {
    AppiumDriverLocalService service;

    public AppiumHelper() {
        startServer();
    }

    private void startServer() {
        try {
            AppiumServiceBuilder builder = new AppiumServiceBuilder();
            builder.withArgument(GeneralServerFlag.SESSION_OVERRIDE);
            builder.withArgument(GeneralServerFlag.LOG_LEVEL, "error");
            builder.usingAnyFreePort();
            service = AppiumDriverLocalService.buildService(builder);
            service.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public URL getUrl() {
        return service.getUrl();
    }

    public void stopServer() {
        try {
            if (service != null && service.isRunning())
                service.stop();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
