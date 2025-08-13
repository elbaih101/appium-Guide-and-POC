package org.example.screens;

import io.appium.java_client.AppiumBy;
import io.qameta.allure.Step;
import org.openqa.selenium.By;

public class ProductsScreen extends BaseScreen {
    public static final By swagLabProductPageTitle = AppiumBy.androidUIAutomator("new UiSelector().text(\"PRODUCTS\")");
    public static final By productsList = AppiumBy.accessibilityId("test-PRODUCTS");
    private String productName;

    private By productNameWidget() {
        return AppiumBy.androidUIAutomator("new UiSelector().text(\"" + productName + "\")");
    }

    public ProductsScreen() {
        super();
    }

    @Step("scroll to '{productName}'")
    public ProductsScreen scrollToProduct(String productName) {
        driver.waitingActions().waitForElementTobeVisible(productsList);
        this.productName = productName;
        driver.elementActions().scrollTo(productNameWidget(), 4);
        return this;
    }

    @Step("tapping on product '{productName}'")
    public ProductDetailsScreen tapOnProduct(String productName) {
        this.productName = productName;
        driver.elementActions().tap(productNameWidget());
        return new ProductDetailsScreen();
    }

}
