package org.example.screens;

import io.appium.java_client.AppiumBy;
import io.qameta.allure.Step;
import org.openqa.selenium.By;

public class ProductsScreen extends BaseScreen {
    public static final By swagLabProductPageTitle = AppiumBy.androidUIAutomator("new UiSelector().text(\"PRODUCTS\")");
    public static final By productsList = AppiumBy.accessibilityId("test-PRODUCTS");
    public static final By cartButton = AppiumBy.accessibilityId("test-Cart");
    public static final By cartCount = AppiumBy.androidUIAutomator("new UiSelector().description(\"test-Cart\").childSelector(new UiSelector().className(\"android.widget.TextView\"))");

    private By productNameWidget(String productName) {
        return AppiumBy.androidUIAutomator("new UiSelector().text(\"" + productName + "\")");
    }

    private By productAddToCartButton(String productName) {
        return AppiumBy.androidUIAutomator("new UiSelector().text(\"" + productName + "\").fromParent(new UiSelector().description(\"test-ADD TO CART\"))");
    }

    public ProductsScreen() {
        super();
    }

    @Step("scroll to '{productName}'")
    public ProductsScreen scrollToProduct(String productName) {
        driver.waitingActions().waitForElementTobeVisible(productsList);
        driver.elementActions().scrollTo(productAddToCartButton(productName), 4);
        return this;
    }

    @Step("tapping on product '{productName}'")
    public ProductDetailsScreen tapOnProduct(String productName) {
        driver.elementActions().tap(productNameWidget(productName));
        return new ProductDetailsScreen();
    }

    @Step("adding '{productName}' to Cart")
    public ProductsScreen addProductToCart(String productName) {
        driver.elementActions().tap(productAddToCartButton(productName));
        return this;
    }

    @Step("open cart page")
    public CartScreen tapOnCartIcon() {
        driver.elementActions().tap(cartButton);
        return new CartScreen();
    }

    @Step("Assert cart Contains '{itemsCount}'items ")
    public ProductsScreen validateCartItemsCountIs(String itemsCount){
        driver.validationActions().assertEquals(driver.elementActions().getVisibleText(cartCount),itemsCount);
        return this;
    }
}
