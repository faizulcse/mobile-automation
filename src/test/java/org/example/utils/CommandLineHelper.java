package org.example.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class CommandLineHelper {
    public static List<String> getAndroidDevices() {
        List<String> devices = execute("adb devices");
        devices.remove("List of devices attached");
        List<String> list = new ArrayList<>();
        for (String device : devices) {
            if (device.contains("device"))
                list.add(device.split("\\s+")[0]);
        }
        return list;
    }

    public static List<String> getIosDevices() {
        List<String> devices = execute("xcrun simctl list devices | grep 'Booted'");
        List<String> list = new ArrayList<>();

        for (String device : devices) {
            String info = (device.split("(Booted)")[0]).trim();
            String[] word = info.split("\\(");
            String[] model = word[0].split("\\s");
            String deviceName = model[0] + " ";
            String osVersion = model[1] + " ";
            String udid = word[1].replace(")", "").trim();
            list.add(deviceName + osVersion + udid);
        }
        return list;
    }

    public static List<String> execute(String command) {
        boolean windows = System.getProperty("os.name").toLowerCase().contains("windows");
        List<String> builderList = new ArrayList<>();
        builderList.add(windows ? "cmd.exe" : "sh");
        builderList.add(windows ? "/c" : "-c");
        builderList.add(command);
        try {
            List<String> output = new ArrayList<>();
            Process process = new ProcessBuilder(builderList).start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));

            String line;
            while ((line = reader.readLine()) != null && !line.isEmpty()) {
                output.add(line);
            }
            return output;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
