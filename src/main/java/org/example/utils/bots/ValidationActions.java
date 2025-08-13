package org.example.utils.bots;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.Assert;

public class ValidationActions {
    WebDriver driver;
    public ValidationActions(RemoteWebDriver driver) {
        this.driver=driver;
    }

    public ValidationActions(WebDriver driver) {
        this.driver = driver;
    }

    public void assertEquals(String actual, String expected) {
        Assert.assertEquals(actual, expected, "The actual text: " + "'" + actual + "'" + " and expected text: " + "'" + expected + "'" + " are not matching");
    }

    public void assertContains(String actual, String expected) {
        Assert.assertTrue(actual.contains(expected),  "Actual: '" + actual + "' does not contain Expected: '" + expected + "'");
    }
}
