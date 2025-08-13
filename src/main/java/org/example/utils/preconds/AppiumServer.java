package org.example.utils.preconds;
import io.appium.java_client.service.local.AppiumDriverLocalService;
import io.appium.java_client.service.local.AppiumServiceBuilder;
import io.appium.java_client.service.local.flags.GeneralServerFlag;
import org.example.utils.LogUtils;

import java.time.Duration;

public class AppiumServer {

    private static AppiumDriverLocalService service;

    public static void startServer() {
        if (service == null || !service.isRunning()) {
            service = new AppiumServiceBuilder()
                    .withIPAddress("127.0.0.1")
                    .usingPort(4723)
                    .withTimeout(Duration.ofSeconds(120)) // 2 minutes timeout
                    .withArgument(GeneralServerFlag.RELAXED_SECURITY)
                    .withArgument(GeneralServerFlag.LOG_LEVEL, "info")
                    .build();

            try {
              LogUtils.logInfo("Starting Appium Server...");
                service.start();
                LogUtils.logInfo("Appium Server started at: " + service.getUrl());
            } catch (Exception e) {
                LogUtils.logError("Failed to start server: " + e.getMessage());
                throw new RuntimeException("Could not start Appium server", e);
            }
        }
    }

    public static void startServerWithCustomConfig() {
        // Custom configuration
        service = new AppiumServiceBuilder()
                .withIPAddress("127.0.0.1")
                .usingPort(4723)
                .withArgument(GeneralServerFlag.SESSION_OVERRIDE)
                .withArgument(GeneralServerFlag.LOG_LEVEL, "error")
                .build();

        service.start();

        if (service.isRunning()) {
            System.out.println("Appium server started successfully");
        }
    }

    public static void stopServer() {
        if (service != null && service.isRunning()) {
            service.stop();
            System.out.println("Appium server stopped");
        }
    }

    public static String getServerUrl() {
        return service != null ? service.getUrl().toString() : null;
    }
}