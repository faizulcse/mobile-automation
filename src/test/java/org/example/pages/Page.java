package org.example.pages;

import org.openqa.selenium.By;

public interface Page {
    void back();

    void hideKeyboard();

    void clear(By by);

    void click(By by);

    void setValue(By by, String value);

    boolean isVisible(By by);

    boolean isPresent(By by, int timeout);

    void waitForDisappear(By by, int timeout);

    void waitAndClick(By by, int timeout);

    void scrollAndClick(By by, int timeout);

    void swipeUp();

    void swipeDown();

    void scrollToTop();

    void sleep(int seconds);

    void sleep(long millis);
}
