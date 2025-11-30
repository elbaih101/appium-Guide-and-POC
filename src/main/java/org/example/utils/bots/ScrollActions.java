package org.example.utils.bots;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;

public class ScrollActions {

    private final JavascriptExecutor js;
    private final ElementActions elementActions;

    public ScrollActions(RemoteWebDriver driver) {
        this.js =driver;
        elementActions = new ElementActions(driver);
    }

    // Scroll to a specific element
    public void scrollToElement(By locator) {
        WebElement element = elementActions.findElement(locator);
        js.executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});", element);
    }

    // Scroll vertically by pixel amount
    public void scrollVertically(int pixels) {
        js.executeScript("window.scrollBy(0, arguments[0]);", pixels);
    }

    // Scroll horizontally by pixel amount
    public void scrollHorizontally(int pixels) {
        js.executeScript("window.scrollBy(arguments[0], 0);", pixels);
    }

    // Scroll to the bottom of the page
    public void scrollToBottom() {
        js.executeScript("window.scrollTo(0, document.body.scrollHeight);");
    }

    // Scroll to the top of the page
    public void scrollToTop() {
        js.executeScript("window.scrollTo(0, 0);");
    }

    // Scroll to a specific position
    public void scrollToPosition(int x, int y) {
        js.executeScript("window.scrollTo(arguments[0], arguments[1]);", x, y);
    }
}

