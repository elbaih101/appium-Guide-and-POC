package org.example.utils;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.util.JSONPObject;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.openqa.selenium.json.Json;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class JsonUtils {
    private static final ObjectMapper objectMapper = new ObjectMapper()
            .enable(SerializationFeature.INDENT_OUTPUT);

    // Convert Java object to JSON string
    public static String toJson(Object obj) {
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to convert object to JSON", e);
        }
    }

    // Convert JSON string to Java object
    public static <T> T fromJson(String json, Class<T> clazz) {
        try {
            return objectMapper.readValue(json, clazz);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to convert JSON to object", e);
        }
    }

    // Read JSON from file and convert to Java object
    public static <T> T fromJsonFile(File file, Class<T> clazz) {
        try {
            return objectMapper.readValue(file, clazz);
        } catch (IOException e) {
            throw new RuntimeException("Failed to read JSON from file", e);
        }
    }

    // Write Java object to JSON file
    public static void toJsonFile(Object obj, File file) {
        try {
            objectMapper.writeValue(file, obj);
        } catch (IOException e) {
            throw new RuntimeException("Failed to write JSON to file", e);
        }
    }

    public static JsonElement readJson(String filePath) {
        try {
           FileReader reader = new FileReader((filePath));
                return  JsonParser.parseReader(reader);
        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
        }
        return null;
    }

    public static <T> T readJsonFile(String filePath, Class<T> clazz) throws IOException {
        File file = new File(filePath);
        return objectMapper.readValue(file, clazz);
    }

    public static <T> T fromJsonString(String jsonString, Class<T> clazz) throws IOException {
        return objectMapper.readValue(jsonString, clazz);
    }

    public static String toJsonString(Object object) throws IOException {
        return objectMapper.writeValueAsString(object);
    }

    public static void writeJsonFile(Object object, String filePath) throws IOException {
        objectMapper.writeValue(new File(filePath), object);
    }

    public static ObjectMapper getObjectMapper() {
        return objectMapper;
    }
}
