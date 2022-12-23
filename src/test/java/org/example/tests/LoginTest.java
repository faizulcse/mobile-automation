package org.example.tests;

import org.example.pages.PageFactory;
import org.example.pages.apps.LoginPage;
import org.testng.annotations.Test;

public class LoginTest extends BaseTest {
    @Test
    public void validLoginTest1() {
        LoginPage login = PageFactory.getLoginPage();
        login.printAppType();
    }

    @Test
    public void validLoginTest2() {

    }
}
