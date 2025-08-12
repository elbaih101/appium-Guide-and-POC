package org.example.utils.properties;

import java.util.Properties;


import static org.example.utils.properties.PropertiesUtil.loadProperties;

public class EnvManager {

    private static final String BASE_PATH = PropertiesUtil.getSystemProperty("user.dir") + "/resources/oms-config/env/";
    private static String env;

    private EnvManager() {
        super();
    }

    private static Properties getEnvProperties() {
        env = PropertiesUtil.getSystemProperty("ENVIRONMENT", "testing"); // default to 'dev'
        String filePath = BASE_PATH + env.toLowerCase();

        return loadProperties(filePath);
    }

    @SuppressWarnings("")
    public static String getEnv() {
        return env;
    }

    public static String get(String key, String def) {
        return getEnvProperties().getProperty(key, def);
    }

    public static String get(String key) {
        return getEnvProperties().getProperty(key);
    }
}

