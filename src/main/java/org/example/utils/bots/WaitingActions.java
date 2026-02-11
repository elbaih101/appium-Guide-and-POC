package org.example.utils.bots;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.AppiumFluentWait;
import io.appium.java_client.android.AndroidDriver;
import org.example.utils.LogUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Sleeper;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Clock;
import java.time.Duration;

public class WaitingActions {
    private final WebDriver driver;
    private final WebDriverWait wait;
    private final ElementActions elementActions;
    private final BrowserActions browserActions;

    public WaitingActions(WebDriver driver) {
        this.driver = driver;
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        elementActions = new ElementActions(driver);
        browserActions = new BrowserActions(driver);
    }

    public void waitForElementToBeInvisible(By locator) {
        LogUtils.logDebug("Waiting For Element To Be Invisible With Locator", locator.toString());
        wait.ignoring(NoSuchElementException.class, StaleElementReferenceException.class)
                .until(dDriver -> elementActions.isRemovedFromDOM(locator));
    }

    public void waitForElementToBeVisible(By locator) {
        LogUtils.logDebug("Waiting For Element To Be visible With Locator", locator.toString());
        wait.until(dDriver -> elementActions.isElementVisible(locator));
    }

    public void waitForElementToNotBeDimmed(By locator) {
        LogUtils.logInfo("Waiting For Button To Be Clickable With Locator", locator.toString());
        wait.until(ExpectedConditions.not(
                ExpectedConditions.attributeContains(locator, "class", "dimmed")
        ));
    }

    public void waitForVisibleTextToChange(By locator){
        String currentText=elementActions.getVisibleText(locator);
        wait.until(dDriver->!currentText.equals(elementActions.getVisibleText(locator )));
    }

    public void waitForVisibleTextTobe(By locator,String text){
        wait.until(dDriver->elementActions.getVisibleText(locator ).equals(text));
    }
    public void waitForVisibleTextToNotBe(By locator,String text){
        wait.until(dDriver->!elementActions.getVisibleText(locator ).equals(text));
    }

    public void waitForCurrentUrlToContain(String urlSegment) {
        wait.until(ExpectedConditions.urlContains(urlSegment));
    }

    public void waitForElementToBeClickable(By locator) {
        wait.until(dDriver -> elementActions.isEnabled(locator));
    }

    public void waitForElementToContainText(By locator) {
        wait.until(dDriver -> !elementActions.isClear(locator));
    }

    public  void waitForElementInViewport(By locator) {
        wait.until(dDriver -> elementActions.isInViewport(locator));
    }
    public void waitForElementInViewport(WebElement element) {
        wait.until(dDriver ->elementActions.isInViewport(element));
    }


}
