package org.example.utils;

public interface ErrorMsg {
    String APP_INSTALLED_MSG = "App(%s): ===> %s%n";
    String ORG_OPEN_QA_SELENIUM = "org.openqa.selenium";
    String APP_FILE_NOT_FOUND = "No such file [*%s] found.";
    String ENV_DATA_NOT_FOUND = "No EnvData found with [%s] ===> Available: %s";
    String NO_DEVICES_CONNECTED = "No %s devices connected";
    String NO_AVAILABLE_DEVICES = "No %s device available for running test.";
}
