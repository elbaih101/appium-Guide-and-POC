# Appium Guide and POC

## Initiating the Base Setup

1. **Download the Android SDK**
   - Ensure you have the Android SDK downloaded.

2. **Download the Platform Tools**
   - Download the necessary platform tools.

3. **Extract the Files**
   - Extract the files to the location `D:\tools\android`.
   - Create the directory `D:\tools\android\cmdline-tools\latest`.
   - Copy all the contents from `D:\tools\android\cmdline-tools` to `D:\tools\android\cmdline-tools\latest` except the `latest` folder.

4. **Set Environment Variables**
   - Set the user variable `ANDROID_HOME` to `D:\tools\android`.
   - Add the following paths to the system `PATH` variable:
     ```
     %ANDROID_HOME%\cmdline-tools\latest\bin
     %ANDROID_HOME%\platform-tools
     %ANDROID_HOME%\emulator
     %ANDROID_HOME%\build-tools
     %ANDROID_HOME%\platforms
     ```

5. **Install Android Platforms and Tools**
   - Install the platforms, build tools, and system image for the desired Android version (e.g., Android 15):
     ```
     sdkmanager --install "system-images;android-35;google_apis_playstore;x86_64" "sources;android-35" "platforms;android-35" "build-tools;35.0.1"
     ```

6. **Create the AVD**
   - Create the Android Virtual Device (AVD) via the CLI command:
     ```
     avdmanager create avd --name "AVD1" --package "system-images;android-35;google_apis_playstore;x86_64"
     ```

7. **Check the AVD**
   - Verify that the AVD has been created using the following commands:
     ```
     avdmanager list avd
     emulator -list-avds
     ```

8. **Run the AVD**
   - Start the AVD using:
     ```
     emulator -avd AVD1 -memory 4096 -skin 1080x1920
     ```
   - Check if the device is detected:
     ```
     adb devices
     ```

9. **Install Appium**
   - Install Appium via npm:
     ```
     npm install -g appium
     ```

10. **Set NPM Environment Variable**
    - Set the environment variable for npm modules:
      ```
      C:\Users\<YourUsername>\AppData\Roaming\npm
      ```

11. **Install Appium UIAutomator2 Driver**
    - Install the Appium UIAutomator2 driver globally via npm:
      ```
      npm install -g appium-uiautomator2-driver
      ```
    - Install the driver using Appium:
      ```
      appium driver install uiautomator2
      ```

By following these steps, you will set up your environment for Appium and Android development.

Note: Replace `<YourUsername>` with your actual Windows username in step 10.
