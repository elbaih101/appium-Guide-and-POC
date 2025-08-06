package org.example.tests;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.android.AndroidDriver;
import org.example.drivers.DriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.net.MalformedURLException;

public class TestScricpts {

    public static final By ClickOnAccessibiltyBtn = AppiumBy.accessibilityId("Access'ibility");
    public static final By stopWatchButton=AppiumBy.id("com.google.android.deskclock:id/tab_menu_stopwatch");
    public static final By pageTitle=AppiumBy.id("com.google.android.deskclock:id/action_bar_title");
    public static final By swagLabUserNameField=AppiumBy.accessibilityId("test-Username");
     public static final By swagLabPasswordField=AppiumBy.accessibilityId("test-Password");
     public static final By swagLabLoginButton=AppiumBy.accessibilityId("test-LOGIN");
     public static final By swagLabProductPageTitle=AppiumBy.androidUIAutomator("new UiSelector().text(\"PRODUCTS\")");
     public static final By swagLabLoginErrorMessage=AppiumBy.androidUIAutomator("new UiSelector().text(\"Sorry, this user has been locked out.\")");

AndroidDriver driver;

    @DataProvider(name = "loginData")
    public Object[][] getLoginData() {
        return new Object[][]{
                {"locked_out_user", "secret_sauce","Sorry, this user has been locked out."},
                {"standard_user", "secret_sauce","success"}
        };
    }
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
    @Test(dataProvider = "loginData")
    public void loginToSwagLab(String user,String password,String message) throws InterruptedException {
Thread.sleep(5000);
driver.findElement(swagLabUserNameField).sendKeys(user);
        driver.findElement(swagLabPasswordField).sendKeys(password);
        driver.findElement(swagLabLoginButton).click();
        if(message.equals("success"))
            Assert.assertEquals(driver.findElement(swagLabProductPageTitle).getText(),"PRODUCTS");
        else
            Assert.assertEquals(driver.findElement(swagLabLoginErrorMessage).getText(),message);
    }

    @AfterClass
    public void tearDown() {
        DriverManager.quitDriver();
    }


}


