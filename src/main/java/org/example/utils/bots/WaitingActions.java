package org.example.utils.bots;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.AppiumFluentWait;
import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Sleeper;

import java.time.Clock;
import java.time.Duration;


public class WaitingActions {
    RemoteWebDriver driver;
    FluentWait<RemoteWebDriver> wait ;

    public WaitingActions(RemoteWebDriver driver) {
        this.driver=driver;
        wait = new FluentWait<>(driver, Clock.systemUTC(), Sleeper.SYSTEM_SLEEPER);
        wait.withTimeout(Duration.ofSeconds(10));
    }
    public  void waitForActivityToBe(String activity){
        wait.until(d -> ((AndroidDriver) d).currentActivity().equals(activity)
        );
    }
    public void waitForElementTobeVisible(By locator){
        wait.ignoring(NoSuchElementException.class).until(d->d.findElement(locator).isDisplayed());

    }
}
