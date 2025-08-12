package org.example.screens;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.By;

public class LoginScreen extends BaseScreen {
    public static final By swagLabUserNameField = AppiumBy.accessibilityId("test-Username");
    public static final By swagLabPasswordField = AppiumBy.accessibilityId("test-Password");
    public static final By swagLabLoginButton = AppiumBy.accessibilityId("test-LOGIN");
    public static final By swagLabProductPageTitle = AppiumBy.androidUIAutomator("new UiSelector().text(\"PRODUCTS\")");
    public static final By swagLabLoginErrorMessage = AppiumBy.androidUIAutomator("new UiSelector().text(\"Sorry, this user has been locked out.\")");

    public LoginScreen(AppiumDriver driver) {
        super(driver);
    }

}
