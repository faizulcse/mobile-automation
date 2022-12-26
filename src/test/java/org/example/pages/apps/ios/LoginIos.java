package org.example.pages.apps.ios;

import org.example.Global;
import org.example.pages.apps.LoginPage;

public class LoginIos extends LoginPage {
    @Override
    public void printAppType() {
        System.out.println(Global.appPlatform);
    }
}
