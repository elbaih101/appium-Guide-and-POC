package org.example.screens;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.AppiumDriver;
import io.qameta.allure.Step;
import org.openqa.selenium.By;

public class LoginScreen extends BaseScreen {
    public static final By swagLabUserNameField = AppiumBy.accessibilityId("test-Username");
    public static final By swagLabPasswordField = AppiumBy.accessibilityId("test-Password");
    public static final By swagLabLoginButton = AppiumBy.accessibilityId("test-LOGIN");
    public static final By swagLabProductPageTitle = AppiumBy.androidUIAutomator("new UiSelector().text(\"PRODUCTS\")");
    public static final By swagLabLoginErrorMessage = AppiumBy.androidUIAutomator("new UiSelector().text(\"Sorry, this user has been locked out.\")");

    public LoginScreen() {
        super();
    }

    @Step("logging in to swag labs '{user}' and '{pass}'")
    public ProductsScreen login(String user, String pass) {
        driver.waitingActions().waitForElementTobeVisible(swagLabUserNameField);
        driver.elementActions().sendKeys(swagLabUserNameField, user);
        driver.elementActions().sendKeys(swagLabPasswordField, pass);
        driver.elementActions().click(swagLabLoginButton);
        return new ProductsScreen();
    }
}
