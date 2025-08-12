package org.example.utils.bots;

import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

public class ElementActions {
    AppiumDriver driver;
    TouchActions touchActions;
    public ElementActions(AppiumDriver driver){
        this.driver=driver;
        touchActions=new TouchActions(driver);
    }
    public WebElement findElement(By locator){
        return driver.findElement(locator);
    }

    public void click(By locator){
        findElement(locator).click();
    }

    public void tap(By locator){
        touchActions.tap(locator);
    }
    public void longTap(By locator){
        touchActions.longPress(locator);
    }
    public void sendKeys(By locator,String keys){
        findElement(locator).sendKeys(keys);
    }
    public void scrollTo(By locator,int maxScrolls){
        touchActions.scrollToElement(locator,maxScrolls);
    }
}
