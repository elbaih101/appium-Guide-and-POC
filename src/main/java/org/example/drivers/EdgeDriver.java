package org.example.drivers;

import org.example.utils.LogUtils;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;


public class EdgeDriver implements WebDriverInterface {
    @Override
    public RemoteWebDriver createDriver(WebDriverBuilder builder) {
        LogUtils.logDebug("creating edge Driver");
        EdgeOptions options = new EdgeOptions();

        if (builder.isHeadless()) {
            options.addArguments("--headless=new");
            LogUtils.logDebug("setting driver option to headless");
        }

        builder.getArguments().forEach(options::addArguments);
        builder.getPrefs().forEach(options::setExperimentalOption);
        return new org.openqa.selenium.edge.EdgeDriver(options);

    }

    @Override
    public WebDriverBuilder defaultBuilder() {
        LogUtils.logDebug("using default EdgeBuilder");
        return new WebDriverBuilder()
                .addArguments(
                        "--start-maximized",
                        "--disable-extensions",
                        "--disable-infobars",
                        "--disable-notifications",
                        "--remote-allow-origins=*",
                        "--host-resolver-rules=MAP admin.oms-tst.internal.eg.vodafone.com 10.230.86.252"
                );
    }
}
