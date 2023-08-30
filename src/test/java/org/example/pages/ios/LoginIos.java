package org.example.pages.ios;

import org.example.pages.LoginPage;
import org.example.pages.android.BasePage;

public class LoginIos extends BasePage implements LoginPage {
    public void printAppType() {
        System.out.println(appConfig.getString("DEVICE_NAME"));
    }
}
