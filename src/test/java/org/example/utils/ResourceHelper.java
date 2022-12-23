package org.example.utils;

import java.util.ResourceBundle;

public class ResourceHelper {
    private final ResourceBundle bundle;

    public ResourceHelper(String file) {
        this.bundle = ResourceBundle.getBundle(file);
    }

    public ResourceHelper getBundle() {
        return this;
    }

    public String getString(String key) {
        return System.getProperty(key) == null ? bundle.getString(key) : System.getProperty(key);
    }

    public boolean getBool(String key) {
        return Boolean.parseBoolean(getString(key));
    }

    public int getInt(String key) {
        return Integer.parseInt(getString(key));
    }
}
