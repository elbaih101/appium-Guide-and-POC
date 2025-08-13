package org.example.utils.bots;


import org.example.utils.LogUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.util.List;

public class ElementActions {
    WebDriver driver;
    TouchActions touchActions;
    Actions actions;

    public ElementActions(RemoteWebDriver driver) {
        this.driver = driver;
        touchActions = new TouchActions(driver);
        actions = new Actions(driver);
    }

    public WebElement findElement(By locator) {
        return driver.findElement(locator);
    }

    public void click(By locator) {
        findElement(locator).click();
    }

    public void tap(By locator) {
        touchActions.tap(locator);
    }

    public void longTap(By locator) {
        touchActions.longPress(locator);
    }

    public void sendKeys(By locator, String keys) {
        findElement(locator).sendKeys(keys);
    }

    public void scrollTo(By locator, int maxScrolls) {
        touchActions.scrollToElement(locator, maxScrolls);
    }

    public String getVisibleText(By locator) {
        LogUtils.logDebug("Checking Text Is Visible With Locator", locator.toString());
        return findElement(locator).getText();
    }

    public boolean isElementVisible(By locator) {
        LogUtils.logDebug("Checking Element Is Visible With Locator", locator.toString());
        return findElement(locator).isDisplayed();
    }

    public boolean isRemovedFromDOM(By locator) {
        LogUtils.logDebug("Checking Element Doesn't Exist in DOM", locator.toString());
        return findElements(locator).isEmpty();
    }

    public String getDOMProperty(By locator, String property) {
        LogUtils.logDebug("Getting Element Property from DOM", locator.toString());
        return findElement(locator).getDomProperty(property);
    }

    public String getDOMAttribute(By locator, String property) {
        LogUtils.logDebug("Getting Element Attribute from DOM", locator.toString());
        return findElement(locator).getDomAttribute(property);
    }

    public void moveTo(By locator) {
        LogUtils.logDebug("Moving To Element", locator.toString());
        actions.moveToElement(findElement(locator)).perform();
    }

    public List<WebElement> findElements(By locator) {
        return driver.findElements(locator);
    }

    public void selectFromElementsWithText(By elementsLocator, String text) {
        LogUtils.logInfo("Selecting option with text:", text);
        List<WebElement> options = findElements(elementsLocator);
        for (WebElement option : options) {
            if (option.getText().trim().equalsIgnoreCase(text)) {
                option.click();
                return;
            }
        }
        throw new NoSuchElementException("No element with text '" + text + "' found in: " + elementsLocator);
    }
}
