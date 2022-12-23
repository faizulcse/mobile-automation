package org.example.pages;

import org.example.Global;
import org.example.pages.apps.LoginPage;
import org.example.pages.apps.android.LoginAndroid;
import org.example.pages.apps.ios.LoginIos;

public class PageFactory {
    public static LoginPage getLoginPage() {
        return Global.isIos ? new LoginIos() : new LoginAndroid();
    }
}
