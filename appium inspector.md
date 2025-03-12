## Step-by-Step Guide

### 1. Launch the Android Emulator
Before running any tests, you need to launch the Android emulator.

```bash
# List available devices
$ emulator -list-avds

# Launch the emulator (replace "Pixel_3_API_30" with your AVD name)
$ emulator -avd AVD1 -memory 4096 -skin 480x720
```

You should see the emulator boot up on your machine.

### 2. Start the Appium Server
Next, you need to start the Appium server. Use the following command to run the server with CORS enabled:

```bash
# Start Appium server with CORS enabled
$ appium --allow-cors
```

This will start the Appium server, and you should see logs in the terminal showing that it’s running.

### 3. Connect ADB to the Emulator
Ensure that ADB is connected to the running emulator by executing the following:

```bash
# List connected devices
$ adb devices
```

This should display the emulator device, e.g., `emulator-5554`, confirming that ADB has detected your emulator.

### 4. Check the Emulator’s Android Version
To verify the Android version running on the emulator, use this command:

```bash
# Check the Android version
$ adb shell getprop ro.build.version.release
```

It will return the Android version, such as `11`, `12`, etc.

### 5. Configure Appium Inspector

1. **Launch Appium Desktop**  
   Open the Appium Desktop application to start configuring the Appium Inspector.

2. **Create a New Session**  
   - In Appium Desktop, click on **Start New Session**.
   - Enter the desired capabilities for your session. Here's an example configuration for an Android emulator:

```json
{
  "platformName": "Android",
  "appium:platformVersion": "15",  // Update this with your emulator version
  "appium:automationName": "uiautomator2",
  "appium:deviceName": "emulator-5554"
}
```

3. **Start the Session**  
   Once the configuration is set, click **Start Session** in Appium Inspector. It will connect to the emulator, and you should see the UI of the emulator inside the Appium Inspector, ready for interaction.
