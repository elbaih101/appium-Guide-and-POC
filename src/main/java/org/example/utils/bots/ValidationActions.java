package org.example.utils.bots;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.Assert;

public class ValidationActions {
    WebDriver driver;

    public ValidationActions(WebDriver driver) {
        this.driver = driver;
    }

    public void assertEquals(String actual, String expected) {
        Assert.assertEquals(actual, expected, "The actual text: " + "'" + actual + "'" + " and expected text: " + "'" + expected + "'" + " are not matching");
    }

    public void assertContains(String actual, String expected) {
        Assert.assertTrue(actual.contains(expected), "The actual text: " + "'" + actual + "'" + " does not contain expected text: " + "'" + expected + "'\n");
    }

    public void assertTrue(boolean bol) {
        Assert.assertTrue(bol);
    }
    public void assertTrue(boolean bol, String message) {
        Assert.assertTrue(bol, message);
    }

    public void assertFalse(boolean bol) {
        Assert.assertFalse(bol);
    }

    public void assertFalse(boolean bol, String message) {
        Assert.assertFalse(bol, message);
    }

}
