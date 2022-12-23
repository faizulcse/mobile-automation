package org.example.tests;

import org.example.Global;
import org.example.setup.DriverSetup;
import org.example.utils.DeviceHelper;
import org.example.utils.EnvHelper;
import org.example.utils.FileHelper;
import org.example.utils.devices.DeviceType;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

import java.lang.reflect.Method;
import java.util.logging.Level;
import java.util.logging.Logger;

public class BaseTest implements Global {
    public ThreadLocal<DeviceType> deviceThread = new ThreadLocal<>();
    public ThreadLocal<String> testThread = new ThreadLocal<>();
    public DeviceHelper deviceHelper = new DeviceHelper(appType);
    public EnvHelper env;

    @BeforeMethod
    public void setUp(Method method) {
        Logger.getLogger("org.openqa.selenium").setLevel(Level.OFF);
        String test = method.getDeclaringClass().getSimpleName() + "_" + method.getName();
        DeviceType deviceType = deviceHelper.loadDevices().getDevice(deviceName);

        DriverSetup.startDriver(deviceType);
        String appPack = DriverSetup.getAppPack();
        env = FileHelper.getEnv("com.example.org");

        testThread.set(test);
        deviceThread.set(deviceType);
    }

    @AfterMethod
    public void tearDown(ITestResult result) {
        FileHelper.takeScreenShot(DriverSetup.getAppiumDriver(), testThread.get());
        DriverSetup.closeDriver();
        deviceHelper.deviceUnlock(deviceThread.get().getId());
    }
}
