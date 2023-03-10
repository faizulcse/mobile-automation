package org.example.utils;

import com.google.gson.JsonObject;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import org.apache.commons.io.FileUtils;
import org.example.Global;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;

import java.io.File;
import java.io.IOException;
import java.net.Socket;
import java.net.URL;

public class FileHelper {
    public static String findFile(String name) {
        try {
            File[] list = new File(Global.rootDir).listFiles((dir, file) -> file.endsWith(name));
            return list != null ? list[0].getAbsolutePath() : null;
        } catch (Exception e) {
            throw new RuntimeException(String.format(LogMsg.APP_FILE_NOT_FOUND, name));
        }
    }

    public static EnvHelper getEnv(String appPack) {
        String file = JsonHelper.readJsonFile(Global.env);
        JsonObject envList = new JsonObject();
        try {
            envList = JsonHelper.getJsonObject(file);
            String env = envList.getAsJsonObject(appPack).toString();
            return (EnvHelper) JsonHelper.getJsonObject(env, EnvHelper.class);
        } catch (Exception e) {
            throw new RuntimeException(String.format(LogMsg.ENV_DATA_NOT_FOUND, appPack, envList.keySet()));
        }
    }

    public static void takeScreenShot(AppiumDriver<MobileElement> driver, String screenshot) {
        if (driver != null) {
            String platformName = "_" + driver.getCapabilities().getCapability("platformName");
            String screenShotPath = Global.screenshots + cleanString(screenshot) + platformName + ".png";
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
