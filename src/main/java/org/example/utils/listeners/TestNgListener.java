package org.example.utils.listeners;

import io.qameta.allure.Allure;
import org.example.drivers.DriverManager;
import org.example.utils.LogUtils;
import org.example.utils.allure.AllureUtils;
import org.example.utils.bots.ScreenShotsActions;
import org.example.utils.preconds.AVDLauncher;
import org.example.utils.preconds.AppiumLauncher;
import org.testng.*;

import java.io.ByteArrayInputStream;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;

public class TestNgListener implements
        IExecutionListener,
        ISuiteListener,
        ITestListener,
        IConfigurationListener {

    // Thread-safe flags to prevent multiple starts/stops
    private static final AtomicBoolean emulatorRunning = new AtomicBoolean(false);
    private static final AtomicBoolean appiumRunning = new AtomicBoolean(false);

    // Track thread -> test name mapping for clarity in logs
    private static final ConcurrentHashMap<Long, String> threadTestMap = new ConcurrentHashMap<>();

    @Override
    public void onExecutionStart() {
        LogUtils.logInfo("===== Test Execution Started =====");

        if (emulatorRunning.compareAndSet(false, true)) {
            LogUtils.logInfo("Starting emulator...");
            AVDLauncher.launchEmulator();
            AVDLauncher.waitForDevice();
        } else {
            LogUtils.logDebug("Emulator already running, skipping start.");
        }

        if (appiumRunning.compareAndSet(false, true)) {
            LogUtils.logInfo("Starting Appium server...");
            AppiumLauncher.startAppium();
            AppiumLauncher.waitForAppium();
        } else {
            LogUtils.logDebug("Appium already running, skipping start.");
        }
    }

    @Override
    public void onExecutionFinish() {
        LogUtils.logInfo("===== Test Execution Finished =====");

        if (appiumRunning.compareAndSet(true, false)) {
            LogUtils.logInfo("Stopping Appium server...");
            AppiumLauncher.stopAppium();
        } else {
            LogUtils.logDebug("Appium was not running, skipping stop.");
        }

        if (emulatorRunning.compareAndSet(true, false)) {
            LogUtils.logInfo("Stopping emulator...");
            AVDLauncher.stopAVD();
        } else {
            LogUtils.logDebug("Emulator was not running, skipping stop.");
        }

        AllureUtils.generateReport();
    }

    @Override
    public void onStart(ISuite suite) {
        LogUtils.logInfo("Suite Started: " + suite.getName());
    }

    @Override
    public void onFinish(ISuite suite) {
        LogUtils.logInfo("Suite Finished: " + suite.getName());
    }

    @Override
    public void onTestStart(ITestResult result) {
        long threadId = Thread.currentThread().getId();
        threadTestMap.put(threadId, result.getMethod().getMethodName());
        LogUtils.logInfo("Test Started (Thread " + threadId + "): " + result.getMethod().getMethodName());
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        LogUtils.logInfo("Test Passed: " + result.getMethod().getMethodName());
    }

    @Override
    public void onTestFailure(ITestResult result) {
        LogUtils.logError("Test Failed: " + result.getMethod().getMethodName(), result.getThrowable().getMessage());

        try {
            byte[] screenshot = new ScreenShotsActions(DriverManager.getDriver()).takeScreenShotByte();
            synchronized (Allure.class) { // Ensure thread-safe Allure attachment
                Allure.addAttachment("Failure Screenshot - " + result.getMethod().getMethodName(),
                        "image/png",
                        new ByteArrayInputStream(screenshot),
                        "png");
            }
        } catch (Exception e) {
            LogUtils.logError("Failed to capture screenshot: ", e.getMessage());
        }
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        LogUtils.logInfo("Test Skipped: " + result.getMethod().getMethodName());
    }

    @Override
    public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
        LogUtils.logInfo("Test Failed Within Success Percentage: " + result.getMethod().getMethodName());
    }

    @Override
    public void beforeConfiguration(ITestResult tr) {
        LogUtils.logInfo("Before Configuration: " + tr.getMethod().getMethodName());
    }

    @Override
    public void onConfigurationSuccess(ITestResult itr) {
        LogUtils.logInfo("Configuration Succeeded: " + itr.getMethod().getMethodName());
    }

    @Override
    public void onConfigurationFailure(ITestResult itr) {
        LogUtils.logError("Configuration Failed: " + itr.getMethod().getMethodName(), itr.getThrowable().getMessage());
    }

    @Override
    public void onConfigurationSkip(ITestResult itr) {
        LogUtils.logInfo("Configuration Skipped: " + itr.getMethod().getMethodName());
    }
}
