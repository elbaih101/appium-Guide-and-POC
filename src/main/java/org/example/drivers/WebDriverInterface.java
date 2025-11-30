package org.example.drivers;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.RemoteWebDriver;

public interface WebDriverInterface {
    // Factory methods
    RemoteWebDriver createDriver(WebDriverBuilder builder);

    WebDriverBuilder defaultBuilder();
}