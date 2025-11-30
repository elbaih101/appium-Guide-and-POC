package org.example.drivers;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.RemoteWebDriver;

public class FirefoxDriver implements WebDriverInterface {
    @Override
    public RemoteWebDriver createDriver(WebDriverBuilder builder) {
        FirefoxOptions options = new FirefoxOptions();

        if (builder.isHeadless()) {
            options.addArguments("-headless");
        }

        builder.getArguments().forEach(options::addArguments);
        builder.getPrefs().forEach(options::addPreference);
        return new org.openqa.selenium.firefox.FirefoxDriver(options);
    }

    @Override
    public WebDriverBuilder defaultBuilder() {
        return new WebDriverBuilder().addArguments(
                "--start-maximized",
                "--disable-extensions",
                "--disable-infobars",
                "--disable-notifications",
                "--remote-allow-origins=*");

    }

}