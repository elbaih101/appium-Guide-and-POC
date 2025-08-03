package org.example.drivers;


import com.google.gson.JsonObject;
import io.appium.java_client.android.AndroidDriver;

import io.appium.java_client.android.options.UiAutomator2Options;
import org.example.utils.JsonUtils;


import java.io.File;
import java.net.MalformedURLException;

import java.net.URL;


public class DriverManager {


    public static AndroidDriver driver;


    public static AndroidDriver getDriver() {

        return driver;

    }


    public static void initDriver() throws MalformedURLException {

        if (driver == null) {
            File capbilities=new File("src/main/resources/inspector/capabilities.json");
            JsonObject caps= JsonUtils.readJson(capbilities.getPath()).getAsJsonObject();
            UiAutomator2Options options = new UiAutomator2Options()    .setDeviceName("Test")

//                    .setPlatformName("Android")

//                    .setAutomationName("uiautomator2")

//                    .setUdid("emulator-5554")
//                    .setAppPackage("com.google.android.deskclock")
//                    .setAppActivity("com.android.deskclock.DeskClock");

                   // .setApp("D:\\Projects\\appium-Guide-and-POC\\src\\main\\resources\\apps\\ApiDemos-debug.apk");



                    .setDeviceName(caps.get("appium:deviceName").getAsString())
//
                    .setPlatformName(caps.get("platformName").getAsString())
//
                    .setAutomationName(caps.get("appium:automationName").getAsString())
//
                    .setUdid(caps.get("appium:deviceName").getAsString())
//
                    .setApp(caps.get("appium:app").getAsString());


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