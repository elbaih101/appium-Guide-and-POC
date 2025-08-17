package org.example.tests;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.AppiumFluentWait;
import io.appium.java_client.android.AndroidDriver;
import org.example.drivers.DriverManager;
import org.example.screens.LoginScreen;
import org.example.screens.ProductsScreen;
import org.example.utils.listeners.TestNgListener;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.support.ui.Sleeper;
import org.testng.Assert;
import org.testng.annotations.*;

import java.net.MalformedURLException;
import java.time.Clock;
import java.time.Duration;

@Listeners(TestNgListener.class)
public class TestScricpts {

    public static final By ClickOnAccessibiltyBtn = AppiumBy.accessibilityId("Access'ibility");
    public static final By stopWatchButton = AppiumBy.id("com.google.android.deskclock:id/tab_menu_stopwatch");
    public static final By pageTitle = AppiumBy.id("com.google.android.deskclock:id/action_bar_title");
    public static final By swagLabUserNameField = AppiumBy.accessibilityId("test-Username");
    public static final By swagLabPasswordField = AppiumBy.accessibilityId("test-Password");
    public static final By swagLabLoginButton = AppiumBy.accessibilityId("test-LOGIN");
    public static final By swagLabProductPageTitle = AppiumBy.androidUIAutomator("new UiSelector().text(\"PRODUCTS\")");
    public static final By swagLabLoginErrorMessage = AppiumBy.androidUIAutomator("new UiSelector().text(\"Sorry, this user has been locked out.\")");

    AndroidDriver driver;

    @DataProvider(name = "loginData")
    public Object[][] getLoginData() {
        return new Object[][]{
                {"locked_out_user", "secret_sauce", "Sorry, this user has been locked out."},
                {"standard_user", "secret_sauce", "success"}
        };
    }

    @BeforeClass
    public void setup() throws MalformedURLException {
        DriverManager.initDriver();
        driver = DriverManager.getDriver();
    }

    //
//    @Test
//    public void ClickAccBtn() {
//        WebElement Button = driver.findElement(ClickOnAccessibiltyBtn);
//        Button.click();
//    }
//
//    @Test
//    public void navigateToStopWatchScreen() {
//        driver.findElement(stopWatchButton).click();
//        Assert.assertEquals(driver.findElement(pageTitle).getText(), "Stopwatch");
//    }
//@Listeners(TestNgListener.class)
//    @Test(dataProvider = "loginData")
//    public void loginToSwagLab(String user, String password, String message) {
//
//        AppiumFluentWait<AndroidDriver> wait = new AppiumFluentWait<>(driver, Clock.systemUTC(), Sleeper.SYSTEM_SLEEPER);
//        wait.withTimeout(Duration.ofSeconds(5));
//        wait.until(d -> d.currentActivity().equals(".MainActivity"));
//        wait.ignoring(NoSuchElementException.class).until(d -> d.findElement(swagLabUserNameField).isDisplayed());
//        driver.findElement(swagLabUserNameField).sendKeys(user);
//        driver.findElement(swagLabPasswordField).sendKeys(password);
//        driver.findElement(swagLabLoginButton).click();
//        if (message.equals("success")) {
//            wait.ignoring(NoSuchElementException.class).until(d -> d.findElement(swagLabProductPageTitle).isDisplayed());
//
//            Assert.assertEquals(driver.findElement(swagLabProductPageTitle).getText(), "PRODUCTS");
//        } else
//            Assert.assertEquals(driver.findElement(swagLabLoginErrorMessage).getText(), message);
//    }

    @Test()
    public void loginAndScrollAndOpenAProductDetailsPage(){
        new LoginScreen().login("standard_user","secret_sauce");
        new ProductsScreen().scrollToProduct("Sauce Labs Bolt T-Shirt")
                .tapOnProduct("Sauce Labs Bolt T-Shirt")
                .assertItemPageContainsItemName("Sauce Labs Bolt T-Shirt");


    }
@Test()
public void addItemToCartAndDeleteItUsingSwipe(){
    new LoginScreen().login("standard_user","secret_sauce");
    new ProductsScreen().scrollToProduct("Sauce Labs Bolt T-Shirt")
            .addProductToCart("Sauce Labs Bolt T-Shirt")
            .validateCartItemsCountIs("1")
            .tapOnCartIcon().swipeAndDeleteItem("Sauce Labs Bolt T-Shirt");
}

    @AfterClass
    public void tearDown() {
        DriverManager.quitDriver();
    }


}


