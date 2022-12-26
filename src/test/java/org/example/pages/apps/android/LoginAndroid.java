package org.example.pages.apps.android;

import org.example.Global;
import org.example.pages.apps.LoginPage;

public class LoginAndroid extends LoginPage {
    @Override
    public void printAppType() {
        System.out.println(Global.appPlatform);
    }
}
