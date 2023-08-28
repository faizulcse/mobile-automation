package org.example.pages.android;

import org.example.pages.BasePage;
import org.example.pages.LoginPage;

public class LoginAndroid extends BasePage implements LoginPage {
    public void printAppType() {
        System.out.println(appConfig.getString("DEVICE_NAME"));
    }
}
