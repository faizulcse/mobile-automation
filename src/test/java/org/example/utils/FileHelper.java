package org.example.utils;

import io.appium.java_client.AppiumDriver;
import org.apache.commons.io.FileUtils;
import org.example.GlobalConfig;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;

import java.io.File;
import java.io.IOException;

public class FileHelper {
    public static void takeScreenShot(AppiumDriver driver, String screenshot) {
        if (driver != null) {
            String platformName = "_" + driver.getCapabilities().getCapability("platformName");
            String screenShotPath = GlobalConfig.screenshots + cleanString(screenshot) + platformName + ".png";
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
}
