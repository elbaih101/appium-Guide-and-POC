# Appium Guide and POC

## Initiating the Base Setup

1. **Download the Android Command-line tools**
   [command line tools](https://developer.android.com/studio#command-line-tools-only)
   - Ensure you have the Android SDK downloaded.

3. **Download the Platform-Tools**
   [Platform-tools](https://developer.android.com/tools/releases/platform-tools#downloads)
   - Download the necessary platform tools.

5. **Extract the Files**
   - Extract the files to the location `D:\tools\android`.
   - Create the directory `D:\tools\android\cmdline-tools\latest`.
   - Copy all the contents from `D:\tools\android\cmdline-tools` to `D:\tools\android\cmdline-tools\latest` except the `latest` folder.

6. **Set Environment Variables**
   - Set the user variable `ANDROID_HOME` to `D:\tools\android`.
   - Add the following paths to the system `PATH` variable:
     ```
     %ANDROID_HOME%\cmdline-tools\latest\bin
     %ANDROID_HOME%\platform-tools
     %ANDROID_HOME%\emulator
     %ANDROID_HOME%\build-tools
     %ANDROID_HOME%\platforms
     ```

7. **Install Android Platforms and Tools**
   - Install the platforms, build tools, and system image for the desired Android version (e.g., Android 15):
     ```sh
     sdkmanager --install "system-images;android-35;google_apis_playstore;x86_64" "sources;android-35" "platforms;android-35" "build-tools;35.0.1"
     ```

8. **Create the AVD**
   - Create the Android Virtual Device (AVD) via the CLI command:
     ```sh
     avdmanager create avd --name "AVD1" --package "system-images;android-35;google_apis_playstore;x86_64"
     ```

9. **Check the AVD**
   - Verify that the AVD has been created using the following commands:
     ```sh
     avdmanager list avd
     emulator -list-avds
     ```

10. **Run the AVD**
   - Start the AVD using:
     ```sh
     emulator -avd AVD1 -memory 4096 -skin 1080x1920
     ```
   - Check if the device is detected:
     ```sh
     adb devices
     ```
   if while running faced with issue like
   ```
      PANIC: Cannot find AVD system path. Please define ANDROID_SDK_ROOT
      PANIC: Broken AVD system path. Check your ANDROID_SDK_ROOT value
   ```
  - make sure all the files are downloaded (system-images ,platforms,build-tppls) in the android path
  - make sure the platform-tools downloaded in the same path 
11. **Install Appium**
   - Install Appium via npm:
     ```sh
     npm install -g appium
     ```

11. **Set NPM Environment Variable**
    - check the root location of your npm global packages
      ```sh
         npm root -g
      ```
    - Set the environment variable for npm modules:
      ```
      C:\Users\<YourUsername>\AppData\Roaming\npm
      ```

12. **Install Appium UIAutomator2 Driver**

    - Install the Appium UIAutomator2 driver globally via npm:
      ```sh
      npm install -g appium-uiautomator2-driver
      ```
    - Install the driver using Appium:
      ```sh
      appium driver install uiautomator2
      ```

By following these steps, you will set up your environment for Appium and Android development.

Note: Replace `<YourUsername>` with your actual Windows username in step 10.

[Follow up with appium inspector](appium-inspector.md)

![image](https://github.com/user-attachments/assets/c91320bb-b4eb-44ef-bbb5-bce232f9a440)
