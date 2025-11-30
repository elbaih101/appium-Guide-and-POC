package org.example.drivers;

import java.util.*;

public class WebDriverBuilder {
    private boolean headless = false;
    private final List<String> arguments = new ArrayList<>();
    private final Map<String, Object> prefs = new HashMap<>();
    private final Map<String, Map<String, Object>> cdpCommands = new HashMap<>(); // Add this line

    public WebDriverBuilder setHeadless(boolean headless) {
        this.headless = headless;
        return this;
    }

    public WebDriverBuilder addArguments(String... arg) {
        arguments.addAll(Arrays.stream(arg).toList());
        return this;
    }

    public WebDriverBuilder addPreferences(Map<String, Object> prefs) {
        this.prefs.putAll(prefs);
        return this;
    }

    public WebDriverBuilder addPreference(String pref, Object value) {
        this.prefs.put(pref, value);
        return this;
    }

    // Add these new methods for CDP commands
    public WebDriverBuilder addCdpCommand(String command, Map<String, Object> parameters) {
        this.cdpCommands.put(command, parameters);
        return this;
    }

    public WebDriverBuilder addNetworkThrottling(int downloadKbps, int uploadKbps, int latencyMs) {
        Map<String, Object> networkConditions = Map.of(
                "offline", false,
                "downloadThroughput", downloadKbps * 1024,
                "uploadThroughput", uploadKbps * 1024,
                "latency", latencyMs
        );
        return addCdpCommand("Network.emulateNetworkConditions", networkConditions);
    }

    // Convenience methods for common network speeds
    public WebDriverBuilder addSlowNetwork() {
        return addNetworkThrottling(25, 25, 300); // Slow 2G
    }

    public WebDriverBuilder addFast3G() {
        return addNetworkThrottling(1600, 750, 150); // Fast 3G
    }

    public WebDriverBuilder addSlow4G() {
        return addNetworkThrottling(4000, 3000, 20); // Slow 4G
    }

    // Getters
    public boolean isHeadless() {
        return headless;
    }

    public List<String> getArguments() {
        return arguments;
    }

    public Map<String, Object> getPrefs() {
        return prefs;
    }

    public Map<String, Map<String, Object>> getCdpCommands() { // Add this getter
        return cdpCommands;
    }
}