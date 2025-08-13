package org.example.screens;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.AppiumDriver;
import io.qameta.allure.Step;
import org.openqa.selenium.By;

public class ProductDetailsScreen extends BaseScreen {
    private static final By pageDescription = AppiumBy.androidUIAutomator("new UiSelector().description(\"test-Inventory item page\")");
    private String itemName;
    private By itemNameWidget(){return   AppiumBy.androidUIAutomator("new UiSelector().text(\""+itemName+"\")");}
    public ProductDetailsScreen(){
        super();
    }

    @Step("Assert that Product Name in the Product Details Screen is '{itemName}'")
    public void assertItemPageContainsItemName(String itemName){
        this.itemName =itemName;
        driver.waitingActions().waitForElementTobeVisible(pageDescription);
        driver.validationActions().assertEquals(driver.elementActions().getVisibleText(itemNameWidget()),itemName);
    }
}
