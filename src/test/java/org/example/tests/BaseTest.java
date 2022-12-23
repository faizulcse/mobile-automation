package org.example.tests;

import org.example.Global;
import org.example.setup.DriverSetup;
import org.example.utils.DeviceProvider;
import org.example.utils.EnvHelper;
import org.example.utils.FileHelper;
import org.example.utils.devices.DeviceType;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;

import java.lang.reflect.Method;
import java.util.logging.Level;
import java.util.logging.Logger;

public class BaseTest implements Global {
    public static EnvHelper env;
    ThreadLocal<DeviceType> device = new ThreadLocal<>();
    ThreadLocal<String> testName = new ThreadLocal<>();
    DeviceProvider deviceProvider;
    String appPack;

    @BeforeTest
    public void beforeTest() {
        deviceProvider = new DeviceProvider(appType);
    }

    @AfterTest
    public void afterTest() {
    }

    @BeforeMethod
    public void setUp(Method method) {
        Logger.getLogger("org.openqa.selenium").setLevel(Level.OFF);
        testName.set(method.getDeclaringClass().getSimpleName() + "_" + method.getName());
        device.set(deviceProvider.getDevice(deviceName));
        DriverSetup.startDriver(device.get());
        appPack = DriverSetup.getAppPack();
        env = FileHelper.getEnv("com.example.org"); // replace "com.example.org" with appPack
    }

    @AfterMethod
    public void tearDown(ITestResult result) {
        FileHelper.takeScreenShot(DriverSetup.getAppiumDriver(), testName.get());
        DriverSetup.closeDriver();
        deviceProvider.deviceUnlock(device.get().getId());
    }
}
