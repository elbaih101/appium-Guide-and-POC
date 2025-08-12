package org.example.screens;

import com.fasterxml.jackson.databind.ser.Serializers;
import io.appium.java_client.AppiumDriver;
import org.example.drivers.MobileDriver;

public class BaseScreen {
    MobileDriver driver ;
    public BaseScreen (AppiumDriver driver){
        this.driver=new MobileDriver(driver);
    }
}
