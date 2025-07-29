## while running the code

### intelij virtual java machiene

the intellij virtyal machiene may not have access to the appium drivers installed globally
1. after finishing the environment setup and the emulator running and the appium is installed
2. Running the code may not work
3. this may be due to driver not found exception throwed by appium (Pay attention to the console logs insdie the runner)
4. if so please reinstall the drivers from within the intellij console
  For Example
   ```sh
    appium driver install uiautomator2
   ```
