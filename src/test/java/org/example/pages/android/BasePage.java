package org.example.pages.android;

import io.appium.java_client.android.AndroidDriver;
import org.example.setup.DriverSetup;
import org.openqa.selenium.interactions.PointerInput;
import org.openqa.selenium.interactions.Sequence;
import org.openqa.selenium.support.PageFactory;

import java.time.Duration;
import java.util.Collections;

public class BasePage extends DriverSetup {
    AndroidDriver driver;

    public BasePage() {
        this.driver = (AndroidDriver) getDriver();
        PageFactory.initElements(this.driver, this);
    }

    public void hideKeyboard() {
        driver.hideKeyboard();
    }

    public void swipe(double x1, double y1, double x2, double y2) {
        PointerInput finger = new PointerInput(PointerInput.Kind.TOUCH, "finger");
        Sequence sequence = new Sequence(new PointerInput(PointerInput.Kind.TOUCH, "finger"), 1);
        sequence.addAction(finger.createPointerMove(Duration.ofSeconds(0), PointerInput.Origin.viewport(), (int) x1, (int) y1));
        sequence.addAction(finger.createPointerDown(0));
        sequence.addAction(finger.createPointerMove(Duration.ofMillis(800), PointerInput.Origin.viewport(), (int) x2, (int) y2));
        sequence.addAction(finger.createPointerUp(0));
        driver.perform(Collections.singletonList(sequence));
        sleep(1);
    }

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