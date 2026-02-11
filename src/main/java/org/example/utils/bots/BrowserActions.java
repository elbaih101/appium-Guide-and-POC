package org.example.utils.bots;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WindowType;

public class BrowserActions {
    WebDriver driver;

    public BrowserActions(WebDriver driver) {
        this.driver = driver;
    }

    public void navigateTo(String url) {
        driver.navigate().to(url);
    }

    public void openInANewTab(String url) {
        driver.switchTo().newWindow(WindowType.TAB);
        driver.navigate().to(url);
    }

    public void switchToTabByTitle(String windowTitle) {
        for (String handle : driver.getWindowHandles()) {
            driver.switchTo().window(handle);
            if (driver.getTitle().equals(windowTitle)) {
                break;
            }
        }
    }

    public void refresh() {
        driver.navigate().refresh();
    }

    public void goBack() {
        driver.navigate().back();
    }

    public void goForward() {
        driver.navigate().forward();
    }

    public String getCurrentUrl() {
        return driver.getCurrentUrl();
    }

    public String getWindowTitle() {
        return driver.getTitle();
    }
}
