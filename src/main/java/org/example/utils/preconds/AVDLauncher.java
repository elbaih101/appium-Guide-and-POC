package org.example.utils.preconds;

import com.google.gson.JsonObject;
import org.example.utils.CommandExecuter;
import org.example.utils.JsonUtils;
import org.example.utils.LogUtils;
import org.example.utils.PropertiesManager;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static org.example.utils.CommandExecuter.isWindows;

public class AVDLauncher {
    private static Process avdProcess;
    private static PropertiesManager probs;
    private static File capabilities;
    private static JsonObject caps;

    // Static initialization block to handle potential errors
    static {
        try {
            probs = PropertiesManager.fromFile("src/main/resources/capability.properties");
            String capabilitiesPath = probs.getOrDefault("capabilities", "");
            if (capabilitiesPath.isEmpty()) {
                throw new IllegalStateException("Capabilities path is not configured in properties file");
            }
            capabilities = new File(capabilitiesPath);
            if (!capabilities.exists()) {
                throw new IllegalStateException("Capabilities file does not exist: " + capabilitiesPath);
            }
            caps = JsonUtils.readJson(capabilities.getPath()).getAsJsonObject();
        } catch (Exception e) {
            LogUtils.logError("Failed to initialize AVDLauncher: " + e.getMessage());
            throw new RuntimeException("AVDLauncher initialization failed", e);
        }
    }

    public static void launchEmulator() {
        launchEmulator(false); // Default to interactive mode
    }

    public static void launchEmulator(boolean headless) {
        if (avdProcess != null && avdProcess.isAlive()) {
            LogUtils.logInfo("Emulator is already running");
            return;
        }

        try {
            if (!caps.get("appium:uuid").getAsString().contains("emulator"))
                return;
            String[] command = buildCommand(headless);
            LogUtils.logInfo("Launching emulator with command: " + String.join(" ", command));
            avdProcess = CommandExecuter.executeCommand(command);

            if (avdProcess == null) {
                throw new IllegalStateException("Failed to start emulator process");
            }

        } catch (Exception e) {
            LogUtils.logError("Failed to launch emulator: " + e.getMessage());
            throw new RuntimeException("Emulator launch failed", e);
        }
    }

    public static void stopAVD() {
        if (avdProcess != null && avdProcess.isAlive()) {
            try {
                avdProcess.destroy();
                // Give it some time to terminate gracefully
                if (!avdProcess.waitFor(10, java.util.concurrent.TimeUnit.SECONDS)) {
                    avdProcess.destroyForcibly();
                }
                LogUtils.logInfo("Emulator stopped successfully");
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                LogUtils.logError("Interrupted while stopping emulator: " + e.getMessage());
            }
        } else {
            LogUtils.logInfo("No emulator process to stop");
        }
        avdProcess = null;
    }

    private static String[] buildCommand() {
        return buildCommand(false); // Default to interactive mode
    }

    private static String[] buildCommand(boolean headless) {
        String emulatorExecutable = isWindows() ? "emulator.exe" : "emulator";
        String deviceName = null;

        if (caps != null && caps.has("appium:deviceName")) {
            deviceName = caps.get("appium:deviceName").getAsString();
        }

        if (deviceName == null || deviceName.trim().isEmpty()) {
            throw new IllegalArgumentException("Device name is missing in capabilities file");
        }

        List<String> commandParts = new ArrayList<>();
        commandParts.add(emulatorExecutable);
        commandParts.add("-avd");
        commandParts.add(deviceName.trim());

        if (headless) {
            commandParts.add("-no-window");     // Run without GUI window
            commandParts.add("-no-audio");      // Disable audio
            commandParts.add("-gpu");
            commandParts.add("off");           // Disable GPU for headless
        } else {
            // Interactive mode settings
//            commandParts.add("-gpu");
//            commandParts.add("swiftshader_indirect");  // Better GPU compatibility
            commandParts.add("-no-audio");      // Audio often causes issues
        }

        // Common settings
//        commandParts.add("-memory");
//        commandParts.add("2048");              // 2GB RAM
//        commandParts.add("-cores");
//        commandParts.add("2");                 // 2 CPU cores
//        commandParts.add("-no-snapshot-save"); // Don't save snapshots
//        commandParts.add("-no-snapshot-load"); // Don't load snapshots on boot

        return commandParts.toArray(new String[0]);
    }

    public static void waitForDevice() {
        waitForDevice(120000); // 2 minutes default timeout
        waitForDeviceBootComplete();
    }

    public static void waitForDevice(long timeoutMs) {
        LogUtils.logInfo("Waiting for Android device to be ready...");
        long start = System.currentTimeMillis();

        while (System.currentTimeMillis() - start < timeoutMs) {
            try {
                String[] command = {"adb", "devices"};
                String output = CommandExecuter.executeCommandAndGetOutput(command);

                if (output != null && output.contains("\tdevice")) {
                    LogUtils.logInfo("Android device is ready");
                    return;
                }

                Thread.sleep(3000); // Check every 3 seconds
                LogUtils.logInfo("Still waiting for device... (" +
                        ((System.currentTimeMillis() - start) / 1000) + "s elapsed)");

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new RuntimeException("Interrupted while waiting for device", e);
            } catch (Exception e) {
                LogUtils.logError("Error while waiting for device: " + e.getMessage());
                // Continue trying instead of failing immediately
            }
        }
        throw new IllegalStateException("Device did not become ready within " + (timeoutMs / 1000) + " seconds");
    }

    public static boolean isEmulatorRunning() {
        return avdProcess != null && avdProcess.isAlive();
    }

    /**
     * Launch emulator with custom parameters for better interaction
     */
    public static void launchInteractiveEmulator() {
        if (avdProcess != null && avdProcess.isAlive()) {
            LogUtils.logInfo("Emulator is already running");
            return;
        }

        try {
            String[] command = buildInteractiveCommand();
            LogUtils.logInfo("Launching interactive emulator with command: " + String.join(" ", command));
            avdProcess = CommandExecuter.executeCommand(command);

            if (avdProcess == null) {
                throw new IllegalStateException("Failed to start emulator process");
            }

        } catch (Exception e) {
            LogUtils.logError("Failed to launch interactive emulator: " + e.getMessage());
            throw new RuntimeException("Interactive emulator launch failed", e);
        }
    }

    private static String[] buildInteractiveCommand() {
        String emulatorExecutable = isWindows() ? "emulator.exe" : "emulator";
        String deviceName = null;

        if (caps != null && caps.has("appium:deviceName")) {
            deviceName = caps.get("appium:deviceName").getAsString();
        }

        if (deviceName == null || deviceName.trim().isEmpty()) {
            throw new IllegalArgumentException("Device name is missing in capabilities file");
        }

        List<String> commandParts = new ArrayList<>();
        commandParts.add(emulatorExecutable);
        commandParts.add("-avd");
        commandParts.add(deviceName.trim());

        // Settings optimized for manual interaction
        commandParts.add("-gpu");
        commandParts.add("host");              // Use host GPU for better performance
        commandParts.add("-skin");
        commandParts.add("1080x1920");
        commandParts.add("-memory");
        commandParts.add("4096");              // More RAM for better performance
        commandParts.add("-cores");
        commandParts.add("4");                 // More CPU cores
        commandParts.add("-camera-back");
        commandParts.add("webcam0");           // Enable back camera
        commandParts.add("-camera-front");
        commandParts.add("webcam0");           // Enable front camera
        commandParts.add("-no-snapshot-save");
        commandParts.add("-no-snapshot-load");
        commandParts.add("-feature");
        commandParts.add("WindowsHypervisorPlatform"); // For Windows users

        return commandParts.toArray(new String[0]);
    }

    /**
     * Check if emulator is responsive to ADB commands
     */
    public static boolean isEmulatorResponsive() {
        try {
            String[] command = {"adb", "shell", "getprop", "sys.boot_completed"};
            String output = CommandExecuter.executeCommandAndGetOutput(command);
            return "1".equals(output.trim());
        } catch (Exception e) {
            LogUtils.logError("Error checking emulator responsiveness: " + e.getMessage());
            return false;
        }
    }

    public static void waitForDeviceBootComplete() {
        LogUtils.logInfo("Waiting for device to complete boot process...");
        long start = System.currentTimeMillis();
        long timeout = 180000; // 3 minutes

        while (System.currentTimeMillis() - start < timeout) {
            try {
                String[] bootCommand = {"adb", "shell", "getprop", "sys.boot_completed"};
                String bootResult = CommandExecuter.executeCommandAndGetOutput(bootCommand);

                String[] readyCommand = {"adb", "shell", "getprop", "dev.bootcomplete"};
                String readyResult = CommandExecuter.executeCommandAndGetOutput(readyCommand);

                if ("1".equals(bootResult.trim()) && "1".equals(readyResult.trim())) {
                    LogUtils.logInfo("Device is fully booted and ready");
                    return;
                }

                LogUtils.logInfo("Device still booting... (boot: " + bootResult.trim() +
                        ", ready: " + readyResult.trim() + ")");
                Thread.sleep(5000);

            } catch (Exception e) {
                LogUtils.logInfo("Still waiting for device boot...");
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                    throw new RuntimeException("Interrupted while waiting for boot", ie);
                }
            }
        }

        LogUtils.logError("Device did not complete boot within timeout");
        // Don't throw exception - let Appium handle it
    }
}
