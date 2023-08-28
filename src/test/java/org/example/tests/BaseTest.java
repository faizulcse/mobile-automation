package org.example.tests;

import io.appium.java_client.service.local.AppiumDriverLocalService;
import org.example.setup.DriverSetup;
import org.example.utils.AppiumHelper;
import org.example.utils.FileHelper;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;

import java.lang.reflect.Method;

public class BaseTest extends DriverSetup {
    AppiumDriverLocalService service;
    String appiumUrl = serverUrl;

    @BeforeTest
    public void beforeTest() {
        if (!AppiumHelper.isActive(serverUrl)) {
            service = AppiumHelper.getAppiumService();
            service.start();
            appiumUrl = service.getUrl().toString();
        }
    }

    @AfterTest
    public void afterTest() {
        if (service != null && service.isRunning()) {
            service.stop();
        }
    }

    @BeforeMethod
    public void setUp(Method method) {
        DriverSetup.startDriver(appiumUrl);
    }

    @AfterMethod
    public void tearDown(ITestResult result) {
        FileHelper.takeScreenShot(driver, result.getName());
        DriverSetup.closeDriver();
    }
}
