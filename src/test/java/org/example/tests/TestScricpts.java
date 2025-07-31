package org.example.tests;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.android.AndroidDriver;
import org.example.drivers.DriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.net.MalformedURLException;

public class TestScricpts {

    public static final By ClickOnAccessibiltyBtn = AppiumBy.accessibilityId("Access'ibility");
    public static final By stopWatchButton=AppiumBy.id("com.google.android.deskclock:id/tab_menu_stopwatch");
    public static final By pageTitle=AppiumBy.id("com.google.android.deskclock:id/action_bar_title");
AndroidDriver driver;
    @BeforeClass
    public void setup() throws MalformedURLException {
        DriverManager.initDriver();
        driver = DriverManager.getDriver();
    }

    @Test
    public void ClickAccBtn(){
        WebElement Button = driver.findElement(ClickOnAccessibiltyBtn);
        Button.click();
    }
    @Test
    public void navigateToStopWatchScreen(){
        driver.findElement(stopWatchButton).click();
        Assert.assertEquals(driver.findElement(pageTitle).getText(),"Stopwatch");
    }

    @AfterClass
    public void tearDown() {
        DriverManager.quitDriver();
    }


}


