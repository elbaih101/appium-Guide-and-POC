# appium-Guide-and-POC
## Initiating the Base Ground
1. download the android sdk
2. download the platform tools
3. extract the files under the location android
  - create the Directory D:\tools\android\cmdline-tools\latest
  - Copy all the contents under the directory D:\tools\android\cmdline-tools to the D:\tools\android\cmdline-tools\latest Directory Except the latest folder
5. set the user variable ANDROID_HOME =D:\tools\android
5.add to path variable this variables
  %ANDROID_HOME%\cmdline-tools\latest\bin
  %ANDROID_HOME%\platform-tools
  %ANDROID_HOME%\emulator
  %ANDROID_HOME%\build-tools
  %ANDROID_HOME%\platforms

6. install the platforms and build and source and image for the desired android version in this case android 15 
sdkmanager --install "system-images;android-35;google_apis_playstore;x86_64" "sources;android-35" "platforms;android-35" "build-tools;35.0.1"

7 .create the AVD  via running the cli command
avdmanager create avd --name "AVD1" --package "system-images;android-35;google_apis_playstore;x86_64"

8. check the AVD is created via the two cli commands
 avdmanager list avd
emulator -list-avds

9. run the AVD
avdmanager -avd AVD1
To Check the device is readed  
adb devices 

 10. install appium via npm
npm install appium

11. set the env variable for the npm modules
C:\Users\V25MHanafy2\AppData\Roaming\npm

12.install the appium uiautomator2 driver via npm 
npm install -g appium-uiautomator2-driver
 
13. install the appium  uiautomator2 Drivers
appium driver install  uiautomator2
