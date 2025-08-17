package org.example.screens;

import io.appium.java_client.AppiumBy;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.support.locators.RelativeLocator;
import org.openqa.selenium.support.pagefactory.ByChained;

public class CartScreen extends BaseScreen {
    private static final By item = AppiumBy.accessibilityId("test-Item");
    private static final By itemDeleteButton= AppiumBy.xpath("//android.view.ViewGroup[@content-desc=\"test-Delete\"]");

    private By itemBy(String itemName) {
        return AppiumBy.xpath("//android.widget.TextView[@text=\"" + itemName + "\"]");
    }
    private By itemDeleteButton(String itemName) {
        return AppiumBy.xpath("//android.widget.TextView[@text=\"" + itemName + "\"]/../../../..//android.view.ViewGroup[@content-desc=\"test-Delete\"]");
    }


    public CartScreen() {
        super();
    }
    @Step("Swipe item '{itemName}' and delete it ")
    public CartScreen swipeAndDeleteItem(String itemName) {
        driver.elementActions().swipeLeft(itemBy(itemName));

        driver.elementActions().tap(itemDeleteButton(itemName));
        return this;
    }
}
