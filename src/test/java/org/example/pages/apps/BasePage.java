package org.example.pages.apps;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import io.appium.java_client.TouchAction;
import io.appium.java_client.touch.WaitOptions;
import io.appium.java_client.touch.offset.PointOption;
import org.example.Global;
import org.example.pages.Page;
import org.example.setup.DriverSetup;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.support.ui.FluentWait;

import java.time.Duration;
import java.util.List;

public class BasePage implements Page, Global {
    public static AppiumDriver<MobileElement> driver = DriverSetup.getAppiumDriver();
    public static String appPack = DriverSetup.getAppPack();

    Dimension dim = driver.manage().window().getSize();

    @Override
    public void back() {
        throw new RuntimeException("Method not implemented yet.");
    }

    @Override
    public void hideKeyboard() {
        driver.hideKeyboard();
    }

    @Override
    public void clear(By by) {
        driver.findElement(by).clear();
    }

    @Override
    public void click(By by) {
        driver.findElement(by).click();
    }

    @Override
    public void setValue(By by, String value) {
        driver.findElement(by).clear();
        driver.findElement(by).sendKeys(value);
    }

    @Override
    public boolean isVisible(By by) {
        try {
            return driver.findElement(by).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean isPresent(By by, int timeout) {
        try {
            return getWait(timeout).until((driver) -> isVisible(by));
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public void waitForDisappear(By by, int timeout) {
        getWait(timeout).withMessage(by.toString()).until((driver) -> !isVisible(by));
    }

    @Override
    public void waitAndClick(By by, int timeout) {
        waitForPresent(by, timeout).click();
    }

    @Override
    public void scrollAndClick(By by, int timeout) {
        scrollToElement(by, timeout).click();
    }

    @Override
    public void swipeUp() {
        swipe(dim.getWidth() / 2.0, dim.getHeight() * 0.8, dim.getWidth() / 2.0, dim.getHeight() * 0.2);
    }

    @Override
    public void swipeDown() {
        swipe(dim.getWidth() / 2.0, dim.getHeight() * 0.5, dim.getWidth() / 2.0, dim.getHeight() * 0.8);
    }

    @Override
    public void scrollToTop() {
        throw new RuntimeException("Method not implemented yet.");
    }

    @Override
    public void sleep(int seconds) {
        sleep(seconds * 1000L);
    }

    @Override
    public void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public MobileElement findElement(By by) {
        return driver.findElement(by);
    }

    public List<MobileElement> findElements(By by) {
        return driver.findElements(by);
    }

    public FluentWait<AppiumDriver<MobileElement>> getWait(int timeout) {
        return new FluentWait<>(driver).withTimeout(Duration.ofSeconds(timeout)).pollingEvery(Duration.ofMillis(500));
    }

    public MobileElement waitForPresent(By by) {
        return waitForPresent(by, Global.explicitWait);
    }

    public List<MobileElement> waitForList(By by) {
        return waitForList(by, Global.explicitWait);
    }

    public MobileElement scrollToElement(By by) {
        return scrollToElement(by, Global.explicitWait);
    }

    public MobileElement waitForPresent(By by, int timeout) {
        try {
            getWait(timeout).withMessage(by.toString()).until((driver) -> isVisible(by));
            return driver.findElement(by);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public List<MobileElement> waitForList(By by, int timeout) {
        try {
            getWait(timeout).withMessage(by.toString()).until((driver) -> driver.findElements(by).size() > 0 && isVisible(by));
            return driver.findElements(by);
        } catch (Exception e) {
            throw new RuntimeException(e.getLocalizedMessage());
        }
    }

    public MobileElement scrollToElement(By by, int timeout) {
        try {
            getWait(timeout).withMessage(by.toString()).until((driver) -> {
                if (isVisible(by))
                    return true;
                swipeUp();
                return false;
            });
            return driver.findElement(by);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void swipe(double x1, double y1, double x2, double y2) {
        new TouchAction<>(driver).press(PointOption.point((int) x1, (int) y1))
                .waitAction(new WaitOptions().withDuration(Duration.ofMillis(800)))
                .moveTo(PointOption.point((int) x2, (int) y2))
                .release().perform();
        sleep(1);
    }
}
