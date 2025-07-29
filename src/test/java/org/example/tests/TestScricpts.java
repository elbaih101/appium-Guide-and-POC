package org.example.tests;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.android.AndroidDriver;
import org.example.drivers.DriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.net.MalformedURLException;

public class TestScricpts {

    public static final By ClickOnAccessibiltyBtn = AppiumBy.accessibilityId("Access'ibility");
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

    @AfterClass
    public void tearDown() {
        DriverManager.quitDriver();
    }
}


