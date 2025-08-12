package org.example.utils.properties;




import org.apache.commons.io.FileUtils;
import org.example.utils.LogUtils;

import java.io.*;
import java.util.Collection;
import java.util.Properties;

public class PropertiesUtil {

    private final Properties properties;

    public PropertiesUtil(String filePath) {
       this.properties= readPropsFile(filePath);
    }

    public PropertiesUtil loadPropsToSystem() {
        LogUtils.logInfo("Loading Properties To System");
        for (String key : properties.stringPropertyNames()) {
            System.setProperty(key, properties.getProperty(key));
        }
        return this;
    }

    public static Properties readPropsFile(String filePath) {
        LogUtils.logInfo("Setting the Properties Object");
        Properties properties = new Properties();

        try (FileInputStream fis = new FileInputStream(filePath)) {
            properties.load(fis);
        } catch (IOException e) {
            throw new IllegalArgumentException("Failed to load properties from: " + filePath, e);
        }

        return properties;
    }

    public String getProperty(String key) {
        String value = properties.getProperty(key);
        LogUtils.logInfo("getting Property [", key, ":", value, "]");
        return value;
    }

    public static String getSystemProperty(String key, String def) {
        String value = System.getProperty(key, def);
        LogUtils.logInfo("getting System Property", key, ":", value);
        return value;
    }

    public static String getSystemProperty(String key) {
        String value = System.getProperty(key);
        LogUtils.logInfo("getting System Property", key, ":", value);
        return value;
    }

    public static Properties loadProperties(String filePath) {
        LogUtils.logInfo("Loading Properties Recursively From File Path", filePath);
        Properties props = new Properties();
        Collection<File> propertiesFilesList;
        propertiesFilesList = FileUtils.listFiles(new File(filePath), new String[]{"properties"}, true);
        propertiesFilesList.forEach(file -> {
            try (FileInputStream fis = new FileInputStream(file)) {
                props.load(fis);

            } catch (IOException e) {
                throw new IllegalArgumentException("Could not load properties for ", e);
            }
        });
        LogUtils.logInfo("Successfully Loaded Properties Recursively From File Path", filePath);
        return props;
    }

    public static void setAndStoreProperty(String key, String value, String path) {

        Properties prop = loadProperties(path);
        prop.setProperty(key, value);
        savePropertiesToFile(path, prop);
    }

    private static void savePropertiesToFile(String path, Properties prop) {
        try (OutputStream outputStream = new FileOutputStream(path)) {
            prop.store(outputStream, "on the go props");
            LogUtils.logInfo("Properties saved to {}", path);
        } catch (IOException e) {
            throw new IllegalArgumentException("Failed to save properties to: " + path, e);
        }
    }

}
