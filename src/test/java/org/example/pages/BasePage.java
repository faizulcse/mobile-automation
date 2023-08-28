package org.example.pages;

import org.example.setup.DriverSetup;
import org.openqa.selenium.Dimension;

public class BasePage extends DriverSetup {
    Dimension dim = driver.manage().window().getSize();

    public void sleep(int seconds) {
        sleep(seconds * 1000L);
    }

    public void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException ignored) {
        }
    }
}