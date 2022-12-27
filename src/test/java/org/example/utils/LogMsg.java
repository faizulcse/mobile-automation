package org.example.utils;

public interface LogMsg {
    String ORG_OPEN_QA_SELENIUM = "org.openqa.selenium";
    String TEST_CASE_NAME = "Test(%s): =====> %s%n";
    String METHOD_NOT_IMPLEMENTED = "Method not implemented yet";
    String APP_FILE_NOT_FOUND = "No such file [%s] found.";
    String ENV_DATA_NOT_FOUND = "No EnvData found with [%s] ===> Available: %s";
    String NO_DEVICES_CONNECTED = "No %s devices connected";
    String NO_AVAILABLE_DEVICES = "No %s device available for running test.";
}
