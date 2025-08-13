package org.example.screens;

import com.fasterxml.jackson.databind.ser.Serializers;
import io.appium.java_client.AppiumDriver;
import org.example.drivers.DriverManager;
import org.example.drivers.MobileDriver;

public class BaseScreen {
    MobileDriver driver ;
    public BaseScreen (){
        this.driver=new MobileDriver(DriverManager.getDriver());
    }
}
