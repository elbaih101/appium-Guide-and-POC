# Complete Android Development Setup Guide for Bazzite

This guide walks you through setting up a complete Android development environment with Appium testing capabilities on Bazzite using distrobox.

## Overview
- **Host OS**: Bazzite (immutable Fedora-based)
- **Development Environment**: Ubuntu distrobox container
- **IDE**: IntelliJ IDEA (installed via JetBrains Toolbox on host)
- **Android SDK**: Installed in distrobox, exported to host
- **Appium**: Installed in distrobox for mobile automation testing

## Part 1: Install and Setup Distrobox

### 1.1 Verify Distrobox Installation
```bash
# Check if distrobox is already installed (should be on Bazzite)
distrobox --version

# If not installed, install it
rpm-ostree install distrobox
systemctl reboot
```
Or use the latest version:
```bash
# Install latest distrobox directly
curl -s https://raw.githubusercontent.com/89luca89/distrobox/main/install | sh -s -- --prefix ~/.local
```

### 1.2 Create Ubuntu Development Container
```bash
# Create Ubuntu container for development
distrobox create --name android-dev --image ubuntu:24.04

# Enter the container (this will set it up on first run)
distrobox enter android-dev
```

### 1.3 Basic Ubuntu Setup (Inside Container)
```bash
# Update package lists
sudo apt update && sudo apt upgrade -y

# Install essential development tools
sudo apt install -y wget curl unzip git build-essential openjdk-17-jdk nodejs npm
```

## Part 2: Install Android SDK in Distrobox

### 2.1 Create Android SDK Directory Structure
```bash
# Create the directory structure (inside distrobox)
mkdir -p ~/tools/android/cmdline-tools
cd ~/tools/android/cmdline-tools
```

### 2.2 Download and Install Android Command Line Tools
```bash
# Download Android command line tools (Linux version)
wget https://dl.google.com/android/repository/commandlinetools-linux-11076708_latest.zip

# Extract and organize
unzip commandlinetools-linux-*_latest.zip
mv cmdline-tools latest
rm commandlinetools-linux-*_latest.zip
```

### 2.3 Set Environment Variables
```bash
# Add Android environment variables to ~/.bashrc
echo 'export ANDROID_HOME=~/tools/android' >> ~/.bashrc
echo 'export ANDROID_SDK_ROOT=~/tools/android' >> ~/.bashrc
echo 'export PATH=$PATH:$ANDROID_HOME/cmdline-tools/latest/bin:$ANDROID_HOME/platform-tools:$ANDROID_HOME/emulator' >> ~/.bashrc

# Apply changes
source ~/.bashrc
```

### 2.4 Install Android SDK Components
```bash
# Accept licenses
sdkmanager --licenses

# Install essential components
sdkmanager "platform-tools"
sdkmanager "emulator"
sdkmanager "platforms;android-34"
sdkmanager "build-tools;34.0.0"

# Verify installation
sdkmanager --list
adb version
```

## Part 3: Install Appium in Distrobox

### 3.1 Install Appium
```bash
# Install Appium globally
npm install -g appium

# Install Appium Doctor for diagnostics
npm install -g @appium/doctor

# Install UiAutomator2 driver for Android
appium driver install uiautomator2

# Verify installation
appium --version
appium driver list --installed
```

### 3.2 Install Additional Dependencies
```bash
# Install Python packages for Appium (optional)
sudo apt install -y python3 python3-pip
pip3 install Appium-Python-Client selenium
```

### 3.3 Run Appium Doctor
```bash
# Check Android setup
appium-doctor --android
```

## Part 4: Export Tools to Host System

### 4.1 Export Android Tools
```bash
# Export Android tools so they're available on Bazzite host
distrobox-export --bin /home/$USER/tools/android/platform-tools/adb
distrobox-export --bin /home/$USER/tools/android/cmdline-tools/latest/bin/avdmanager
distrobox-export --bin /home/$USER/tools/android/cmdline-tools/latest/bin/sdkmanager
distrobox-export --bin /home/$USER/tools/android/emulator/emulator
```

### 4.2 Export Appium Tools
```bash
# Export Appium
distrobox-export --bin $(which appium)
distrobox-export --bin $(which appium-doctor)

# Exit distrobox
exit
```

### 4.3 Set Host Environment Variables
```bash
# On Bazzite host, add environment variables
echo 'export ANDROID_HOME=/home/elbaih/tools/android' >> ~/.bashrc
echo 'export ANDROID_SDK_ROOT=/home/elbaih/tools/android' >> ~/.bashrc
echo 'export PATH=$PATH:~/.local/bin' >> ~/.bashrc
source ~/.bashrc
```

## Part 5: Configure IntelliJ IDEA

### 5.1 Locate IntelliJ Installation
Your IntelliJ is installed at:
```
/home/elbaih/.local/share/JetBrains/Toolbox/apps/intellij-idea-community-edition/
```

### 5.2 Configure Android SDK in IntelliJ
1. **Open IntelliJ IDEA** (from JetBrains Toolbox)
2. **Go to File → Project Structure** (Ctrl+Alt+Shift+S)
3. **Select "SDKs" in left panel**
4. **Click "+" → "Android SDK"**
5. **Set Android SDK Location to**: `/home/elbaih/tools/android`
6. **Click "Apply" and "OK"**

## Part 6: Create and Run AVD

### 6.1 Create Android Virtual Device
```bash
# List available system images
avdmanager list

# Create an AVD (example)
avdmanager create avd -n "TestDevice" -k "system-images;android-34;google_apis;x86_64"

# List created AVDs
avdmanager list avd
```

### 6.2 Start Emulator
```bash
# Start the emulator
emulator -avd TestDevice

# Or start with specific options
emulator -avd TestDevice -no-snapshot-load
```

## Part 7: Test the Complete Setup

### 7.1 Verify Tools Work from Host
```bash
# These should all work from your Bazzite host
adb version
avdmanager list avd
appium --version
emulator -list-avds
```

### 7.2 Start Appium Server
```bash
# Start Appium server (accessible at http://localhost:4723)
appium
```

### 7.3 Test ADB Connection
```bash
# With emulator running
adb devices
```

## Part 8: Project Configuration in IntelliJ

### 8.1 Create Appium Test Project
1. **Create new Java project** in IntelliJ
2. **Add Maven dependencies** for Appium:
   ```xml
   <dependency>
       <groupId>io.appium</groupId>
       <artifactId>java-client</artifactId>
       <version>9.2.2</version>
   </dependency>
   ```

### 8.2 Sample Appium Test Configuration
```java
DesiredCapabilities caps = new DesiredCapabilities();
caps.setCapability("platformName", "Android");
caps.setCapability("deviceName", "TestDevice");
caps.setCapability("automationName", "UiAutomator2");
caps.setCapability("avd", "TestDevice");
```

## Troubleshooting

### Common Issues:
- **Tools not found**: Ensure exported tools are in `~/.local/bin` and PATH is updated
- **Emulator won't start**: Check if virtualization is enabled in BIOS
- **ADB not connecting**: Restart ADB server with `adb kill-server && adb start-server`
- **Appium connection issues**: Ensure Appium server is running on correct port (4723)

### Useful Commands:
```bash
# Enter development container
distrobox enter android-dev

# List containers
distrobox list

# Restart ADB
adb kill-server && adb start-server

# Check Appium setup
appium-doctor --android
```

## Directory Structure
Your final setup will look like:
```
Host (Bazzite):
├── ~/.local/bin/ (exported tools)
└── ~/.local/share/JetBrains/Toolbox/apps/intellij-idea-community-edition/

Container (android-dev):
├── ~/tools/android/
│   ├── cmdline-tools/latest/
│   ├── platform-tools/
│   ├── emulator/
│   └── platforms/
└── ~/.npm-global/ (Appium installation)
```

This setup gives you a complete Android development environment with Appium testing capabilities while keeping your Bazzite system clean and organized!


Perfect! Let me walk you through setting up distrobox and creating an Ubuntu container on Bazzite.





