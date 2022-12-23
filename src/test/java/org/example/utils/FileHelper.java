package org.example.utils;

import com.google.gson.JsonObject;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;

import java.io.File;
import java.io.IOException;
import java.net.Socket;
import java.net.URL;

public class FileHelper {
    public static String rootDir = System.getProperty("user.dir") + File.separator;
    public static String screenshotDir = rootDir + "screenshots/";


    public static String findFile(String name) {
        try {
            File[] list = new File(rootDir).listFiles((dir, file) -> file.endsWith(name));
            return list != null ? list[0].getAbsolutePath() : null;
        } catch (Exception e) {
            throw new RuntimeException(String.format("No such file [*%s] found.", name));
        }
    }

    public static EnvHelper getEnv(String appPack) {
        String file = JsonHelper.readJsonFile(rootDir + "src/test/java/org/example/utils/data/env.json");
        JsonObject envList = new JsonObject();
        try {
            envList = JsonHelper.getJsonObject(file);
            String env = envList.getAsJsonObject(appPack).toString();
            return (EnvHelper) JsonHelper.getJsonObject(env, EnvHelper.class);
        } catch (Exception e) {
            throw new RuntimeException(String.format("No EnvData found with [%s] ===> Available: %s", appPack, envList.keySet()));
        }
    }

    public static void takeScreenShot(AppiumDriver<MobileElement> driver, String screenshot) {
        if (driver != null) {
            String platformName = "_" + driver.getCapabilities().getCapability("platformName");
            String screenShotPath = screenshotDir + cleanString(screenshot) + platformName + ".png";
            try {
                File screenshotFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
                FileUtils.copyFile(screenshotFile, new File(screenShotPath));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static String cleanString(String str) {
        return str.replaceAll("['()#.,\"]", "").replaceAll("[/ :-]", "_");
    }

    public static boolean isOpen(String address) {
        try {
            URL url = new URL(address);
            Socket socket = new Socket(url.getHost(), url.getPort());
            socket.close();
            return true;
        } catch (IOException ignored) {
            return false;
        }
    }
}
