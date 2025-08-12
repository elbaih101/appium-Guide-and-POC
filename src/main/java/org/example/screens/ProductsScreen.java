package org.example.screens;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.By;

public class ProductsScreen extends BaseScreen {
   public static final By swagLabProductPageTitle = AppiumBy.androidUIAutomator("new UiSelector().text(\"PRODUCTS\")");
    public ProductsScreen(AppiumDriver driver) {
        super(driver);
    }

}
