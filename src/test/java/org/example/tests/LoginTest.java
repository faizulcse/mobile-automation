package org.example.tests;

import org.example.pages.android.LoginAndroid;
import org.example.pages.ios.LoginIos;
import org.testng.annotations.Test;

public class LoginTest extends BaseTest {
    @Test
    public void validLoginTest1() {
        var loginPage = isIos ? new LoginIos() : new LoginAndroid();
        loginPage.printAppType();
    }
}
