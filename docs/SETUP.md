# Setup & Installation Guide

This guide will help you set up the Daily Challenge development environment on your machine.

---

## Table of Contents

1. [Prerequisites](#prerequisites)
2. [Installation Steps](#installation-steps)
3. [Building the Project](#building-the-project)
4. [Running on Different Platforms](#running-on-different-platforms)
5. [Development Tools](#development-tools)
6. [Troubleshooting](#troubleshooting)
7. [Next Steps](#next-steps)

---

## Prerequisites

### Required Software

#### 1. Android Studio

**Version**: Ladybug 2024.2.2 or newer
**Download**: [developer.android.com/studio](https://developer.android.com/studio)

**Why Android Studio?**
- Official IDE for Android development
- Built-in Kotlin support
- Compose preview
- Gradle integration
- Android emulator

**Installation:**
- Download the installer for your OS
- Run the installer
- Follow the setup wizard
- Install Android SDK (API 24-36)

#### 2. Java Development Kit (JDK)

**Version**: JDK 17 or higher
**Download**: [Oracle JDK](https://www.oracle.com/java/technologies/javase-downloads.html) or [OpenJDK](https://adoptium.net/)

**Check Installation:**
```bash
java -version
# Should show version 17 or higher
```

**Set JAVA_HOME:**
```bash
# macOS/Linux
export JAVA_HOME=$(/usr/libexec/java_home -v 17)

# Windows
set JAVA_HOME=C:\Program Files\Java\jdk-17
```

#### 3. Git

**Version**: Latest stable
**Download**: [git-scm.com](https://git-scm.com/downloads)

**Verify Installation:**
```bash
git --version
```

#### 4. Xcode (macOS only, for iOS)

**Version**: 14.0 or higher
**Download**: Mac App Store or [developer.apple.com/xcode](https://developer.apple.com/xcode/)

**Required for:**
- iOS app development
- iOS Simulator
- Darwin HTTP engine

**Install Command Line Tools:**
```bash
xcode-select --install
```

---

## Installation Steps

### 1. Clone the Repository

```bash
# Clone via HTTPS
git clone https://github.com/nyinyiz/DailyChallenge.git

# Or clone via SSH (if you have SSH keys set up)
git clone git@github.com:nyinyiz/DailyChallenge.git

# Navigate to project directory
cd DailyChallenge
```

### 2. Open in Android Studio

1. Launch Android Studio
2. Click **"Open"** or **"Open an Existing Project"**
3. Navigate to the cloned `DailyChallenge` folder
4. Click **"OK"**

Android Studio will:
- Index the project
- Download Gradle dependencies (this may take a few minutes)
- Sync Gradle files

### 3. Gradle Sync

If Gradle doesn't sync automatically:

1. Click **File** → **Sync Project with Gradle Files**
2. Or click the **"Sync Now"** prompt that appears

**First sync will:**
- Download dependencies (~500MB)
- Configure Kotlin Multiplatform
- Set up build variants

### 4. SDK Setup

Ensure required Android SDK components are installed:

1. Open **Tools** → **SDK Manager**
2. Install the following:
   - **Android SDK Platform 36** (Compile SDK)
   - **Android SDK Platform 35** (Target SDK)
   - **Android SDK Platform 24** (Min SDK)
   - **Android SDK Build-Tools** (latest)
   - **Android Emulator**
   - **Android SDK Platform-Tools**

---

## Building the Project

### Using Android Studio

#### Build All Variants
1. **Build** → **Make Project** (Ctrl+F9 / Cmd+F9)

#### Clean Build
1. **Build** → **Clean Project**
2. **Build** → **Rebuild Project**

### Using Command Line

#### Build Debug APK (Android)
```bash
./gradlew :composeApp:assembleDebug
```

#### Build Release APK (Android)
```bash
./gradlew :composeApp:assembleRelease
```

#### Build iOS App (macOS only)
```bash
./gradlew :composeApp:linkDebugFrameworkIosArm64
```

#### Build Desktop App
```bash
./gradlew :composeApp:packageDistributionForCurrentOS
```

#### Run All Tests
```bash
./gradlew test
```

#### Check for Dependency Updates
```bash
./gradlew dependencyUpdates
```

---

## Running on Different Platforms

### Android

#### Using Android Studio

1. **Select Device:**
   - Physical device via USB (enable Developer Options & USB Debugging)
   - Or Android Emulator (create via AVD Manager)

2. **Run Configuration:**
   - Select **"composeApp"** from the run configurations dropdown
   - Click the **Run** button (green play icon) or press **Shift+F10**

#### Using Command Line

```bash
# Install and run on connected device
./gradlew :composeApp:installDebug
adb shell am start -n com.nyinyi.dailychallenge/.MainActivity
```

#### Create Android Emulator

1. **Tools** → **Device Manager**
2. Click **"Create Device"**
3. Choose a device (e.g., Pixel 7)
4. Select system image (API 35 recommended)
5. Click **"Finish"**

#### Troubleshooting Android

**Issue**: `INSTALL_FAILED_INSUFFICIENT_STORAGE`
```bash
# Clear cache
./gradlew clean
```

**Issue**: Device not detected
```bash
# Check ADB connection
adb devices

# Restart ADB server
adb kill-server
adb start-server
```

---

### iOS (macOS only)

#### Using Android Studio

1. Select **"iosApp"** run configuration
2. Choose iOS Simulator from device list
3. Click **Run**

#### Using Xcode

1. Open `iosApp/iosApp.xcodeproj` in Xcode
2. Select target device/simulator
3. Click **Run** (Cmd+R)

#### Using Command Line

```bash
# Build framework
./gradlew :composeApp:linkDebugFrameworkIosArm64

# Open in Xcode
open iosApp/iosApp.xcodeproj

# Or use xcodebuild
xcodebuild -project iosApp/iosApp.xcodeproj -scheme iosApp -configuration Debug -sdk iphonesimulator
```

#### iOS Simulator Setup

```bash
# List available simulators
xcrun simctl list devices

# Boot a simulator
xcrun simctl boot "iPhone 15 Pro"

# Install app
xcrun simctl install booted path/to/DailyChallenge.app
```

#### Troubleshooting iOS

**Issue**: Framework not found
```bash
# Clean and rebuild Kotlin framework
./gradlew :composeApp:clean
./gradlew :composeApp:linkDebugFrameworkIosArm64
```

**Issue**: Xcode build errors
```bash
# Clean Xcode build
rm -rf iosApp/build
rm -rf ~/Library/Developer/Xcode/DerivedData
```

---

### Desktop

#### Using Android Studio

1. Select **"desktopRun"** configuration (if available)
2. Click **Run**

#### Using Command Line

```bash
# Run desktop app
./gradlew :composeApp:run

# Create distributable
./gradlew :composeApp:packageDistributionForCurrentOS
```

The distributable will be in:
```
composeApp/build/compose/binaries/main/
```

#### Platform-Specific Packaging

**macOS:**
```bash
./gradlew :composeApp:packageDmg
```

**Windows:**
```bash
./gradlew :composeApp:packageMsi
```

**Linux:**
```bash
./gradlew :composeApp:packageDeb
```

---

## Development Tools

### Recommended Android Studio Plugins

1. **Kotlin** (built-in)
   - Syntax highlighting
   - Code completion
   - Refactoring tools

2. **Compose Multiplatform** (built-in)
   - Compose preview
   - Component inspector

3. **Compose Hot Reload** (included in project)
   - Instant UI updates
   - No rebuild needed

4. **GitToolBox**
   - Enhanced Git integration
   - Inline blame
   - Auto fetch

### Gradle Configuration

The project uses Gradle Version Catalog for dependency management:

**Location**: `gradle/libs.versions.toml`

**Update Dependencies:**
```bash
# Check for updates
./gradlew dependencyUpdates

# Manually edit libs.versions.toml
```

### Environment Variables

Optional environment variables:

```bash
# Gradle JVM options
export GRADLE_OPTS="-Xmx4096m -XX:MaxMetaspaceSize=1024m"

# Android SDK location (if not auto-detected)
export ANDROID_HOME=/path/to/android/sdk

# Enable parallel builds
export GRADLE_OPTS="$GRADLE_OPTS -Dorg.gradle.parallel=true"
```

---

## Development Workflow

### 1. Code Changes with Hot Reload

Compose Hot Reload is enabled by default:

1. Make changes to Composable functions
2. Save the file (Ctrl+S / Cmd+S)
3. Changes appear instantly in running app

**Note**: Hot reload doesn't work for:
- ViewModel changes
- Data model changes
- Non-Composable code

### 2. Debugging

#### Android
- Set breakpoints in code
- Click **Debug** button (Shift+F9)
- Use **Logcat** to view logs

#### Logging
```kotlin
// Use println for multiplatform
println("Debug message: $value")

// Or create expect/actual logger
expect object Logger {
    fun log(message: String)
}
```

### 3. Code Inspection

```bash
# Run lint checks
./gradlew lint

# View lint report
open composeApp/build/reports/lint-results.html
```

### 4. Format Code

In Android Studio:
- **Code** → **Reformat Code** (Ctrl+Alt+L / Cmd+Opt+L)
- **Code** → **Optimize Imports** (Ctrl+Alt+O / Cmd+Opt+O)

---

## Configuration Files

### gradle.properties

```properties
# Kotlin code style
kotlin.code.style=official

# Multiplatform
kotlin.mpp.stability.nowarn=true

# Memory settings
org.gradle.jvmargs=-Xmx4096m -XX:MaxMetaspaceSize=1024m

# Parallel builds
org.gradle.parallel=true
org.gradle.caching=true
```

### local.properties

Create if needed:
```properties
sdk.dir=/path/to/android/sdk
```

---

## Testing Setup

### Run Unit Tests

```bash
# All tests
./gradlew test

# Specific module
./gradlew :composeApp:test

# With reports
./gradlew test --info
```

### Test Reports

Located at:
```
composeApp/build/reports/tests/test/index.html
```

### Continuous Testing

```bash
# Run tests on every change
./gradlew test --continuous
```

---

## Troubleshooting

### Common Issues

#### 1. Gradle Sync Failed

**Error**: "Could not resolve dependencies"

**Solution:**
```bash
# Clear Gradle cache
./gradlew clean
rm -rf ~/.gradle/caches

# Restart Android Studio
# Sync project again
```

#### 2. Out of Memory

**Error**: "Out of memory: Java heap space"

**Solution:**
Add to `gradle.properties`:
```properties
org.gradle.jvmargs=-Xmx4096m -XX:MaxMetaspaceSize=1024m
```

#### 3. SDK Not Found

**Error**: "SDK location not found"

**Solution:**
Create `local.properties`:
```properties
sdk.dir=/Users/YourUsername/Library/Android/sdk
```

#### 4. Kotlin Version Mismatch

**Error**: "Incompatible Kotlin version"

**Solution:**
Check `gradle/libs.versions.toml` and update:
```toml
[versions]
kotlin = "2.2.20"
```

#### 5. Compose Preview Not Working

**Solution:**
- Ensure @Preview annotation is used
- Rebuild project
- Invalidate caches: **File** → **Invalidate Caches / Restart**

#### 6. iOS Build Fails

**Error**: "Framework not found"

**Solution:**
```bash
# Rebuild framework
./gradlew :composeApp:clean
./gradlew :composeApp:linkDebugFrameworkIosArm64

# Open Xcode project
cd iosApp
pod install  # If using CocoaPods
```

### Getting Help

If you encounter issues:

1. Check [GitHub Issues](https://github.com/nyinyiz/DailyChallenge/issues)
2. Review [Troubleshooting Guide](#troubleshooting)
3. Read [Kotlin Multiplatform Docs](https://kotlinlang.org/docs/multiplatform.html)
4. Ask in discussions
5. Contact: nyinyizaw.dev@gmail.com

---

## Verification

Verify your setup is working:

```bash
# 1. Build project
./gradlew build

# 2. Run tests
./gradlew test

# 3. Assemble debug APK
./gradlew :composeApp:assembleDebug

# If all pass, you're good to go!
```

---

## IDE Configuration

### Android Studio Settings

Recommended settings:

1. **Editor** → **Code Style** → **Kotlin**
   - Set to "Kotlin style guide"

2. **Build, Execution, Deployment** → **Compiler**
   - Enable "Build project automatically"
   - Enable "Compile independent modules in parallel"

3. **Appearance & Behavior** → **System Settings**
   - Uncheck "Reopen projects on startup" (optional)

### Keyboard Shortcuts

| Acti| Tool | Minimum Version | Purpose |
| :--- | :---: | :--- |
| **🤖 Android Studio** | Ladybug 2024.2.2 | IDE |
| **☕ Kotlin** | 2.2.20 | Language |
| **☕ Java JDK** | 17 | Runtime |
| **🛠️ Gradle** | 8.11.2 | Build tool |
| **🍎 Xcode** | 14+ | iOS development (macOS only) | Cmd+Opt+L |
| Find | Ctrl+F | Cmd+F |
| Find in Files | Ctrl+Shift+F | Cmd+Shift+F |

---

## Performance Optimization

### Build Performance

Add to `gradle.properties`:
```properties
# Enable configuration cache
org.gradle.configuration-cache=true

# Enable build cache
org.gradle.caching=true

# Parallel builds
org.gradle.parallel=true

# Daemon
org.gradle.daemon=true
```

### Android Studio Performance

1. **Increase Memory:**
   - **Help** → **Edit Custom VM Options**
   - Set: `-Xmx4096m`

2. **Exclude Directories:**
   - Right-click `build` folders
   - **Mark Directory as** → **Excluded**

---

## Next Steps

Now that your environment is set up:

1. **Explore the Code**
   - Read [ARCHITECTURE.md](ARCHITECTURE.md) to understand the structure
   - Check [FEATURES.md](FEATURES.md) for feature details

2. **Make Changes**
   - Follow [CONTRIBUTING.md](CONTRIBUTING.md) guidelines
   - Create a feature branch
   - Start coding!

3. **Run the App**
   - Test on Android, iOS, or Desktop
   - Try different game modes
   - Explore the codebase

4. **Contribute**
   - Fix bugs
   - Add features
   - Improve documentation
   - Submit pull requests

---

## Related Documentation

- [Architecture Guide](ARCHITECTURE.md) - Technical architecture
- [Contributing Guidelines](CONTRIBUTING.md) - How to contribute
- [Tech Stack Guide](TECH_STACK.md) - Technologies used
- [Features Guide](FEATURES.md) - App features

---

**Last Updated**: October 2025

**Need Help?** Open an issue or contact nyinyizaw.dev@gmail.com
