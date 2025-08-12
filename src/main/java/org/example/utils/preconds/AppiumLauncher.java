package org.example.utils.preconds;

import org.example.utils.CommandExecuter;
import org.example.utils.LogUtils;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static org.example.utils.CommandExecuter.isWindows;

public class AppiumLauncher {
    private static Process appiumProcess;
    private static String currentPort = "4723"; // Default port

    public static void startAppium(String port, String logLevel) {
        if (appiumProcess != null && appiumProcess.isAlive()) {
            LogUtils.logInfo("Appium is already running on port " + currentPort);
            return;
        }

        String[] appiumCommand = buildAppiumCommand(port, logLevel);
        LogUtils.logInfo("Starting Appium on port " + (port != null ? port : "4723") +
                " with log level " + (logLevel != null ? logLevel : "info"));

        try {
            appiumProcess = CommandExecuter.executeCommand(appiumCommand);
            if (appiumProcess == null) {
                throw new IllegalStateException("Failed to start Appium process");
            }
            currentPort = port != null ? port : "4723";
        } catch (Exception e) {
            LogUtils.logError("Failed to start Appium: " + e.getMessage());
            throw new RuntimeException("Appium startup failed", e);
        }
    }

    public static void startAppium() {
        startAppium(null, null);
    }

    public static void startAppiumWithDriver(String port, String logLevel, String driverName) {
        if (appiumProcess != null && appiumProcess.isAlive()) {
            LogUtils.logInfo("Appium is already running on port " + currentPort);
            return;
        }

        if (driverName == null || driverName.trim().isEmpty()) {
            throw new IllegalArgumentException("Driver name cannot be null or empty");
        }

        String[] appiumCommand = buildAppiumCommandWithDriver(port, logLevel, driverName);
        LogUtils.logInfo("Starting Appium on port " + (port != null ? port : "4723") +
                " with driver " + driverName);

        try {
            appiumProcess = CommandExecuter.executeCommand(appiumCommand);
            if (appiumProcess == null) {
                throw new IllegalStateException("Failed to start Appium process with driver");
            }
            currentPort = port != null ? port : "4723";
        } catch (Exception e) {
            LogUtils.logError("Failed to start Appium with driver: " + e.getMessage());
            throw new RuntimeException("Appium with driver startup failed", e);
        }
    }

    private static String[] buildAppiumCommand(String port, String logLevel) {
        List<String> commandParts = new ArrayList<>();

        // Appium executable
        if (isWindows()) {
            commandParts.add("cmd.exe");
            commandParts.add("/c");
            commandParts.add("appium");
        } else {
            commandParts.add("appium"); // Unix/Mac
        }

        // Optional args
        if (port != null && !port.isBlank()) {
            commandParts.add("--port");
            commandParts.add(port);
        }
        if (logLevel != null && !logLevel.isBlank()) {
            commandParts.add("--log-level");
            commandParts.add(logLevel);
        }

        // Return as array
        return commandParts.toArray(new String[0]);
    }

    public static void stopAppium() {
        if (appiumProcess != null && appiumProcess.isAlive()) {
            try {
                appiumProcess.destroy();
                // Give it some time to terminate gracefully
                if (!appiumProcess.waitFor(10, java.util.concurrent.TimeUnit.SECONDS)) {
                    appiumProcess.destroyForcibly();
                }
                LogUtils.logInfo("Appium server stopped successfully");
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                LogUtils.logError("Interrupted while stopping Appium: " + e.getMessage());
            }
        } else {
            LogUtils.logInfo("No Appium process to stop");
        }
        appiumProcess = null;
    }

    private static String[] buildAppiumCommandWithDriver(String port, String logLevel, String driverName) {
        List<String> commandParts = new ArrayList<>();

        if (isWindows()) {
            commandParts.add("cmd.exe");
            commandParts.add("/c");
            commandParts.add("appium");
        } else {
            commandParts.add("appium");
        }

        if (port != null && !port.isBlank()) {
            commandParts.add("--port");
            commandParts.add(port);
        }

        if (logLevel != null && !logLevel.isBlank()) {
            commandParts.add("--log-level");
            commandParts.add(logLevel);
        }

        commandParts.add("--use-driver");
        commandParts.add(driverName.trim());

        return commandParts.toArray(new String[0]);
    }

    public static void waitForAppium() {
        waitForAppium(60000); // 1 minute default timeout
    }

    public static void waitForAppium(long timeoutMs) {
        String appiumUrl = "http://localhost:" + currentPort + "/status";
        LogUtils.logInfo("Waiting for Appium server to be ready at " + appiumUrl + "...");

        long start = System.currentTimeMillis();
        while (System.currentTimeMillis() - start < timeoutMs) {
            try {
                HttpURLConnection conn = (HttpURLConnection) new URL(appiumUrl).openConnection();
                conn.setConnectTimeout(3000);
                conn.setReadTimeout(3000);
                conn.setRequestMethod("GET");
                conn.connect();

                int responseCode = conn.getResponseCode();
                if (responseCode == 200) {
                    LogUtils.logInfo("Appium server is ready");
                    return;
                }

                LogUtils.logInfo("Appium server not ready yet (HTTP " + responseCode +
                        "), retrying... (" + ((System.currentTimeMillis() - start) / 1000) + "s elapsed)");

            } catch (IOException e) {
                // Expected during startup, continue trying
                LogUtils.logInfo("Still waiting for Appium server... (" +
                        ((System.currentTimeMillis() - start) / 1000) + "s elapsed)");
            }

            try {
                Thread.sleep(2000); // Check every 2 seconds
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new RuntimeException("Interrupted while waiting for Appium", e);
            }
        }
        throw new IllegalStateException("Appium server did not start within " + (timeoutMs / 1000) + " seconds");
    }

    public static boolean isAppiumRunning() {
        return appiumProcess != null && appiumProcess.isAlive();
    }

    public static String getCurrentPort() {
        return currentPort;
    }
}