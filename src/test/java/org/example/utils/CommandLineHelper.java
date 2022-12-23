package org.example.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class CommandLineHelper {
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
