package org.example.drivers;


import com.google.gson.JsonObject;
import io.appium.java_client.android.AndroidDriver;

import io.appium.java_client.android.options.UiAutomator2Options;
import org.example.utils.JsonUtils;
import org.example.utils.PropertiesManager;


import java.io.File;
import java.net.MalformedURLException;

import java.net.URL;
import java.nio.file.Path;
import java.util.Optional;


public class DriverManager {


    public static AndroidDriver driver;


    public static AndroidDriver getDriver() {

        return driver;

    }


    public static void initDriver() throws MalformedURLException {

        if (driver == null) {
            PropertiesManager probs =PropertiesManager.fromFile("src/main/resources/capability.properties");
            File capbilities = new File(probs.getOrDefault("capabilities",""));
            JsonObject caps = JsonUtils.readJson(capbilities.getPath()).getAsJsonObject();
            UiAutomator2Options options = new UiAutomator2Options()


                    .setDeviceName(caps.has("appium:deviceName") ? caps.get("appium:deviceName").getAsString():null)

                    .setPlatformName(caps.get("platformName").getAsString())

                    .setAutomationName(caps.has("appium:automationName") ? caps.get("appium:automationName").getAsString():null)

                    .setUdid(caps.has("appium:uuid") ? caps.get("appium:uuid").getAsString():null)

                    .setApp(
                            Optional.ofNullable(caps.has("appium:app") && !caps.get("appium:app").isJsonNull()
                                            ? caps.get("appium:app").getAsString()
                                            : null)
                                    .map(path -> new File(path).getAbsolutePath())
                                    .orElse(null))
                    .setAppPackage(caps.has("appium:appPackage") ? caps.get("appium:appPackage").getAsString() : null)
                    .setAppActivity(caps.has("appium:appActivity") ? caps.get("appium:appActivity").getAsString() : null);


            URL url = new URL("http://0.0.0.0:4723");

            driver = new AndroidDriver(url, options);

        }

    }


    public static void quitDriver() {


        if (driver != null) {

            driver.quit();

            driver = null;


        }


    }

}