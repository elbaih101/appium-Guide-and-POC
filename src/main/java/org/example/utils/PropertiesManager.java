package org.example.utils;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;
import java.util.Properties;

public class PropertiesManager {

    private final Properties properties = new Properties();

    /**
     * Load properties from a file path.
     */
    public static PropertiesManager fromFile(String filePath)  {
        try (InputStream in = new FileInputStream(filePath)) {
            return fromInputStream(in);
        }catch (IOException e){
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Load properties from a classpath resource (e.g., "config/app.properties").
     */
    public static PropertiesManager fromClasspath(String resourcePath) throws IOException {
        InputStream in = Thread.currentThread()
                .getContextClassLoader()
                .getResourceAsStream(resourcePath);
        if (in == null) {
            throw new FileNotFoundException("Resource not found: " + resourcePath);
        }
        return fromInputStream(in);
    }

    /**
     * Load properties from an InputStream.
     */
    public static PropertiesManager fromInputStream(InputStream in) throws IOException {
        PropertiesManager manager = new PropertiesManager();
        manager.properties.load(in);
        return manager;
    }

    /**
     * Get raw string property.
     */
    public Optional<String> get(String key) {
        return Optional.ofNullable(properties.getProperty(key));
    }

    /**
     * Get a string property with default.
     */
    public String getOrDefault(String key, String defaultValue) {
        return properties.getProperty(key, defaultValue);
    }

    /**
     * Get int property.
     */
    public Optional<Integer> getInt(String key) {
        return get(key).map(Integer::parseInt);
    }

    /**
     * Get boolean property.
     */
    public Optional<Boolean> getBoolean(String key) {
        return get(key).map(Boolean::parseBoolean);
    }

    /**
     * Save properties to a file.
     */
    public void saveToFile(Path filePath, String comments) throws IOException {
        try (Writer writer = Files.newBufferedWriter(filePath)) {
            properties.store(writer, comments);
        }
    }

    /**
     * Set a property in memory (does not persist unless saved).
     */
    public void set(String key, String value) {
        properties.setProperty(key, value);
    }

    /**
     * Check if a property exists.
     */
    public boolean contains(String key) {
        return properties.containsKey(key);
    }

    /**
     * Remove a property.
     */
    public void remove(String key) {
        properties.remove(key);
    }

    /**
     * Get all properties.
     */
    public Properties getAll() {
        return properties;
    }
}