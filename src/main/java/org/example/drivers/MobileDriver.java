package org.example.drivers;

import io.appium.java_client.AppiumDriver;
import org.example.utils.bots.ElementActions;
import org.example.utils.bots.ScrollingActions;
import org.example.utils.bots.ValidationActions;
import org.example.utils.bots.WaitingActions;

public class MobileDriver {

    AppiumDriver driver ;


    public MobileDriver(AppiumDriver driver) {
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

    public ScrollingActions screenShotsActions() {
        return new ScrollingActions(driver);
    }


}
