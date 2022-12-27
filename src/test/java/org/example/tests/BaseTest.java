package org.example.tests;

import org.example.Global;
import org.example.setup.DriverSetup;
import org.example.utils.DeviceHelper;
import org.example.utils.EnvHelper;
import org.example.utils.FileHelper;
import org.example.utils.LogMsg;
import org.example.utils.devices.DeviceType;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

import java.lang.reflect.Method;

public class BaseTest implements Global {
    public ThreadLocal<DeviceType> deviceThread = new ThreadLocal<>();
    public ThreadLocal<String> testThread = new ThreadLocal<>();
    public DeviceHelper deviceHelper = new DeviceHelper(appPlatform);
    public EnvHelper env;

    @BeforeMethod
    public void setUp(Method method) {
        String test = method.getDeclaringClass().getSimpleName() + "_" + method.getName();
        System.out.printf(LogMsg.TEST_CASE_NAME, appPlatform, test);
        DeviceType deviceType = deviceHelper.loadDevices().getDevice(isIos ? iosDevice : androidDevice);

        DriverSetup.startDriver(deviceType);
        env = FileHelper.getEnv(Global.isIos ? "com.example.android" : "com.example.ios");

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
