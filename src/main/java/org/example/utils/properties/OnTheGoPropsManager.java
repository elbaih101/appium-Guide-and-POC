package org.example.utils.properties;

import java.util.Properties;

public class OnTheGoPropsManager {

    private static final  String ON_THE_GO_PROP_FILEPATH =PropertiesUtil.getSystemProperty("user.dir")+ "/resources/oms-config/onTheGo.properties";

    private OnTheGoPropsManager() {
        super();
    }

    private static Properties getOnTheGoProperties() {
        return PropertiesUtil.readPropsFile(ON_THE_GO_PROP_FILEPATH);
    }


    public static String get(String key) {
        return getOnTheGoProperties().getProperty(key);
    }

    public static void setAndStore(String key, String value) {
        PropertiesUtil.setAndStoreProperty(key, value, ON_THE_GO_PROP_FILEPATH);
    }
}
