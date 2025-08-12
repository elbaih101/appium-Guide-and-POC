package org.example.utils.bots;


import io.appium.java_client.AppiumDriver;
import org.example.utils.LogUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.RemoteWebDriver;

public class ScreenShotsActions {
    WebDriver driver;
    public static final String SCREEN_SHOTS_PATH = "target/screenshots/";

    public ScreenShotsActions(RemoteWebDriver driver) {
        this.driver = driver;
    }


    public byte[] takeScreenShotByte() {
        LogUtils.logInfo("Taking screenshot");
        //
        return ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);

    }
    public String takeScreenShotBase64() {
        LogUtils.logInfo("Taking screenshot");
        return ((TakesScreenshot) driver).getScreenshotAs(OutputType.BASE64);
    }



}
