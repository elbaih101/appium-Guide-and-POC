package org.example.drivers;


import org.example.utils.LogUtils;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;


public class ChromeDriver implements WebDriverInterface {


    @Override
    public RemoteWebDriver createDriver(WebDriverBuilder builder) {
        LogUtils.logDebug("Creating Chrome Driver");
        ChromeOptions options = new ChromeOptions();

        if (builder.isHeadless()) {
            options.addArguments("--headless=new");
            LogUtils.logDebug("setting driver option to headless");

        }

        builder.getArguments().forEach(options::addArguments);
        builder.getPrefs().forEach(options::setExperimentalOption);
        return new org.openqa.selenium.chrome.ChromeDriver(options);
    }

    @Override
    public WebDriverBuilder defaultBuilder() {
        LogUtils.logDebug("using default Chrome Builder");
        return new WebDriverBuilder().addArguments("--start-maximized",
                "--disable-extensions",
                "--disable-infobars",
                "--disable-notifications",
                "--remote-allow-origins=*",
                "--host-resolver-rules=MAP admin.oms-tst.internal.eg.vodafone.com 10.230.86.252");
    }

}
