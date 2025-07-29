package org.example.drivers;

import io.appium.java_client.service.local.AppiumDriverLocalService;
import io.appium.java_client.service.local.AppiumServiceBuilder;
import io.appium.java_client.service.local.flags.ServerArgument;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.io.File;

public class AppiumManager {

    static AppiumServiceBuilder builder = new AppiumServiceBuilder();

    public static void runAppium() {
        // Set the IP address (optional - default is localhost)
        builder.withIPAddress("127.0.0.1");

        // Set the port (default is 4723)
        builder.usingPort(4723);

        // Set the path to the Appium server binary (this step is crucial for Appium 2.x)
        builder.withAppiumJS(new File("/path/to/appium"));

        // Set desired capabilities (optional - you can add desired capabilities if needed)
        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability("platformName", "Android");  // Example capability
        capabilities.setCapability("deviceName", "Android Emulator");  // Example capability
        // Add more capabilities if necessary
        builder.withCapabilities(capabilities);

        // Start the Appium server
        AppiumDriverLocalService service = AppiumDriverLocalService.buildService(builder);
        service.start();

        // Log that the server is started
        System.out.println("Appium server started on port 4723");

        // Optionally, stop the service after some operations
        // service.stop();
    }

}
