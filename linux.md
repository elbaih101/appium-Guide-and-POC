
Perfect! Let me walk you through setting up distrobox and creating an Ubuntu container on Bazzite.

## Step 1: Install Distrobox
Distrobox should already be available on Bazzite, but let's make sure:

```bash
# Check if distrobox is already installed
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

## Step 2: Create Ubuntu Distrobox
```bash
# Create an Ubuntu container (this will download Ubuntu image)
distrobox create --name android-dev --image ubuntu:22.04

# Alternative: Use Ubuntu 24.04 LTS (newer)
distrobox create --name android-dev --image ubuntu:24.04
```

## Step 3: Enter the container for first setup
```bash
# Enter the container (this will set it up on first run)
distrobox enter android-dev
```

The first time you enter, it will:
- Download the Ubuntu image
- Set up the container
- Install distrobox integration
- This may take a few minutes

## Step 4: Basic Ubuntu setup (inside container)
```bash
# Update package lists
sudo apt update && sudo apt upgrade -y

# Install essential tools
sudo apt install -y wget curl unzip git build-essential
```

## Step 5: Exit and verify
```bash
# Exit the container
exit

# List your containers
distrobox list

# You should see your android-dev container
```

## Useful Distrobox Commands:
```bash
# Enter container
distrobox enter android-dev

# Stop container
distrobox stop android-dev

# Remove container
distrobox rm android-dev

# List containers
distrobox list

# Export apps from container to host
distrobox-export --app firefox  # (example)
```

## Step 6: Now install Android tools (as we discussed earlier)
```bash
# Enter your container
distrobox enter android-dev

# Follow the Android SDK installation steps from before
sudo apt install -y openjdk-17-jdk
mkdir -p ~/tools/android/cmdline-tools
# ... etc
```

## Integration Benefits:
- Your home directory is automatically mounted
- GUI apps work seamlessly
- You can access files from both host and container
- Network access is shared

Your container will persist across reboots and you can enter/exit it anytime. The beauty of distrobox is that it feels like you're just switching between environments, but with full isolation for development tools.


## Step 1: Enter your Ubuntu distrobox
```bash
distrobox enter <your-container-name>
```

## Step 2: Update and install dependencies
```bash
sudo apt update
sudo apt install -y wget unzip openjdk-17-jdk
```

## Step 3: Create the specific directory structure and download tools
```bash
# Create the directory structure
mkdir -p ~/tools/android/cmdline-tools
cd ~/tools/android/cmdline-tools

# Download the Linux command line tools
wget https://dl.google.com/android/repository/commandlinetools-linux-11076708_latest.zip

# Extract and organize
unzip commandlinetools-linux-*_latest.zip
mv cmdline-tools latest
rm commandlinetools-linux-*_latest.zip
```

## Step 4: Set up environment variables
```bash
# Add to your ~/.bashrc
echo 'export ANDROID_HOME=~/tools/android' >> ~/.bashrc
echo 'export ANDROID_SDK_ROOT=~/tools/android' >> ~/.bashrc
echo 'export PATH=$PATH:$ANDROID_HOME/cmdline-tools/latest/bin:$ANDROID_HOME/platform-tools' >> ~/.bashrc

# Apply the changes
source ~/.bashrc
```

## Step 5: Test the installation
```bash
# Check if sdkmanager works
sdkmanager --version

# List available packages
sdkmanager --list
```

## Step 6: Install essential Android SDK components
```bash
# Accept licenses
sdkmanager --licenses

# Install platform tools (adb, fastboot)
sdkmanager "platform-tools"

# Install a recent Android API (optional)
sdkmanager "platforms;android-34"
sdkmanager "build-tools;34.0.0"
```

Your directory structure will now be:
```
~/tools/android/
├── cmdline-tools/
│   └── latest/
│       └── bin/
│           ├── sdkmanager
│           └── avdmanager
└── platform-tools/
    ├── adb
    └── fastboot
```

This keeps everything organized in your preferred `tools/android` directory structure!
