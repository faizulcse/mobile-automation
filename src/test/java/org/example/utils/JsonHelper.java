package org.example.utils;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.IOException;

public class JsonHelper {
    public static String readJsonFile(String jsonFile) {
        try (FileReader fr = new FileReader(jsonFile)) {
            return new JSONParser().parse(fr).toString();
        } catch (IOException | ParseException e) {
            throw new RuntimeException(e);
        }
    }

    public static JsonObject getJsonObject(String jsonFile) {
        return new Gson().fromJson(jsonFile, JsonObject.class);
    }

    public static Object getJsonObject(String jsonFile, Class<?> clazz) {
        return new Gson().fromJson(jsonFile, clazz);
    }

    public static JsonArray getJsonArray(String jsonFile) {
        return new Gson().fromJson(jsonFile, JsonArray.class);
    }

    public static JsonArray getJsonArray(String jsonFile, Class<?> clazz) {
        return new Gson().fromJson(readJsonFile(jsonFile), JsonArray.class);
    }
}
