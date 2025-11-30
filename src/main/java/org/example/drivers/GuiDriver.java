package org.example.drivers;


import org.example.utils.bots.*;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.RemoteWebDriver;

public class GuiDriver {
    RemoteWebDriver driver;

    public GuiDriver(RemoteWebDriver driver) {
        this.driver = driver;
    }

    public ElementActions elementActions() {
        return new ElementActions(driver);
    }

    public WaitingActions waitingActions() {
        return new WaitingActions(driver);
    }

    public ValidationActions validationActions() {
        return new ValidationActions(driver);
    }

    public ScreenShotsActions screenShotsActions() {
        return new ScreenShotsActions(driver);
    }
    public BrowserActions browserActions(){return new BrowserActions(driver);}
    public ScrollActions scrollActions() {return new ScrollActions(driver);}
}
