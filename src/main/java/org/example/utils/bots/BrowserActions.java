package org.example.utils.bots;

import org.openqa.selenium.WebDriver;

public class BrowserActions {
    WebDriver driver;

    public BrowserActions(WebDriver driver) {
        this.driver = driver;
    }

    public void navigateTo(String url) {
        driver.navigate().to(url);
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

    public String getWindowTitle(){
        return driver.getTitle();
    }
}
