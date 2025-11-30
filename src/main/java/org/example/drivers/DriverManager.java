package org.example.drivers;


import com.google.gson.JsonObject;
import io.appium.java_client.android.AndroidDriver;

import io.appium.java_client.android.options.UiAutomator2Options;
import org.example.utils.JsonUtils;
import org.example.utils.LogUtils;
import org.example.utils.PropertiesManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.RemoteWebDriver;


import java.io.File;
import java.net.MalformedURLException;

import java.net.URL;

import java.time.Duration;
import java.util.Optional;

import static org.testng.Assert.fail;


public class DriverManager {

    private static final ThreadLocal<RemoteWebDriver> DRIVER_THREAD_LOCAL = new ThreadLocal<>();

    private DriverManager() {
        super();
    }


    public static void initDriver(String browserName) {

        RemoteWebDriver driver = DriverFactory.getDriver(browserName).
                createDriver(DriverFactory.getDriver(browserName).defaultBuilder());
        setDriver(driver);
    }
    public static void setDriver(RemoteWebDriver driver) {
        LogUtils.logDebug("Seting Thread Driver as :",driver.toString());
        DRIVER_THREAD_LOCAL.set(driver);
    }


    public static RemoteWebDriver getDriver() {
        if (DRIVER_THREAD_LOCAL.get() == null) {
            fail("Driver is Null");
        }
        return DRIVER_THREAD_LOCAL.get();
    }
    public static void unloadDriver(){
        DRIVER_THREAD_LOCAL.remove();
    }


    public static void quitDriver() {
        getDriver().quit();

    }

}