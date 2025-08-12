package org.example.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class CommandExecuter {

    private CommandExecuter() {
        super();
    }

    public static Process executeCommand(String[] command) {
        try {
            ProcessBuilder processBuilder = new ProcessBuilder(command);
            // Remove duplicate redirects - inheritIO() already handles this
            Process process = processBuilder.start();

            // Only create threads if we're not using inheritIO()
            // Since we want to capture output through LogUtils, we'll handle streams manually
            new Thread(() -> {
                try (BufferedReader reader = new BufferedReader(
                        new InputStreamReader(process.getInputStream()))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        LogUtils.logInfo(line);
                    }
                } catch (IOException e) {
                    LogUtils.logError("Error reading process output: " + e.getMessage());
                }
            }).start();

            new Thread(() -> {
                try (BufferedReader reader = new BufferedReader(
                        new InputStreamReader(process.getErrorStream()))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        LogUtils.logError(line);
                    }
                } catch (IOException e) {
                    LogUtils.logError("Error reading process error: " + e.getMessage());
                }
            }).start();

            return process;

        } catch (IOException e) {
            LogUtils.logError("Failed to execute command: " + String.join(" ", command) +
                    " With Exception: " + e.getMessage());
            // Don't interrupt thread here - we're not in an InterruptedException handler
        }
        return null;
    }

    public static String executeCommandAndGetOutput(String[] command) {
        StringBuilder output = new StringBuilder();

        try {
            ProcessBuilder processBuilder = new ProcessBuilder(command);
            processBuilder.redirectErrorStream(true); // merge stderr into stdout
            Process process = processBuilder.start();

            try (BufferedReader reader = new BufferedReader(
                    new InputStreamReader(process.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    output.append(line).append(System.lineSeparator());
                }
            }

            int exitCode = process.waitFor();
            if (exitCode != 0) {
                LogUtils.logError("Command Execution Failed: " ,
                        String.join(" ", command) ,
                        " | Exit Code: " + exitCode);
            }

        } catch (IOException e) {
            LogUtils.logError("Failed to execute command: " +
                    String.join(" ", command) +
                    " With IOException: " + e.getMessage());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt(); // Restore interrupt status
            LogUtils.logError("Command execution interrupted: " +
                    String.join(" ", command) +
                    " With InterruptedException: " + e.getMessage());
        }
        return output.toString().trim();
    }

    public static boolean isWindows() {
        return System.getProperty("os.name").toLowerCase().contains("win");
    }
}