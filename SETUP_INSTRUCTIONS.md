# 📋 Setup Instructions - Step by Step

## Complete Setup Guide for Atomberg App Test Suite

**Time Required:** 30-45 minutes  
**Difficulty:** Beginner-Friendly

---

## ✅ Pre-Setup Checklist

Before you begin, ensure you have:
- [ ] Windows 10+ / macOS 10.14+ / Ubuntu 18.04+
- [ ] Administrator access (may need for installations)
- [ ] Internet connection
- [ ] At least 5GB free disk space
- [ ] An Android device or emulator

---

## Phase 1: Install Prerequisites (15 minutes)

### Step 1.1: Install Java 17+

#### Windows
1. Download from https://www.oracle.com/java/technologies/downloads/
2. Select "Java 17" or later
3. Run installer with administrator privileges
4. Follow installation wizard
5. Verify installation:
   ```bash
   java -version
   ```
   Expected: `openjdk version "17.0.x"`

#### macOS
```bash
# Using Homebrew (recommended)
brew install openjdk@17

# Or download from
# https://www.oracle.com/java/technologies/downloads/
```

#### Linux (Ubuntu/Debian)
```bash
sudo apt update
sudo apt install openjdk-17-jdk

# Verify
java -version
```

### Step 1.2: Install Maven 3.8+

#### Windows
1. Download from https://maven.apache.org/download.cgi
2. Download Binary zip (not source)
3. Extract to `C:\Maven\` (or preferred location)
4. Add to PATH:
   - Open Environment Variables
   - Add `C:\Maven\bin` to PATH
5. Verify in new terminal:
   ```bash
   mvn --version
   ```

#### macOS
```bash
# Using Homebrew (recommended)
brew install maven

# Or download and install manually
# Add to ~/.bash_profile:
# export M2_HOME=/path/to/maven
# export PATH=$M2_HOME/bin:$PATH
```

#### Linux
```bash
sudo apt update
sudo apt install maven

# Verify
mvn --version
```

### Step 1.3: Install Android Tools

#### Windows
1. Download Android SDK from https://developer.android.com/studio
2. Install Android Studio or Android SDK tools only
3. Open Android SDK Manager
4. Install:
   - Android API 30+
   - Android SDK Platform-Tools
5. Add to PATH:
   - `C:\Users\<YourUser>\AppData\Local\Android\Sdk\platform-tools`
6. Verify:
   ```bash
   adb --version
   ```

#### macOS
```bash
# Install using Homebrew
brew install android-platform-tools

# Verify
adb --version
```

#### Linux
```bash
sudo apt update
sudo apt install android-tools-adb

# Verify
adb --version
```

### Step 1.4: Install Appium

#### All Platforms
```bash
# Install Node.js first (from https://nodejs.org/)
# Then install Appium:
npm install -g appium

# Install Appium drivers
appium driver install uiautomator2

# Verify
appium --version
```

Or use Appium Desktop:
1. Download from https://appium.io/docs/en/2.0/intro/
2. Install application
3. Launch Appium Desktop
4. Click "Start Server" button

---

## Phase 2: Setup Android Device (10 minutes)

### Step 2.1: Connect Physical Device

1. **Enable Developer Mode:**
   - Open Settings → About Phone
   - Tap "Build Number" 7 times
   - Go back, open "Developer Options"
   - Enable "USB Debugging"
   - Enable "Android Debugging over Network" (optional)

2. **Connect via USB:**
   - Connect device to computer with USB cable
   - Allow USB debugging when prompted
   - Verify connection:
     ```bash
     adb devices
     ```
   - Should show your device as "device"

3. **Or Connect via Network (optional):**
   ```bash
   # On device: Enable "Wireless debugging"
   # Get IP address: Settings → Developer Options → Wireless debugging
   
   # On computer:
   adb connect <DEVICE_IP>:5555
   adb devices
   ```

### Step 2.2: Verify Device Setup

```bash
# Check connected devices
adb devices

# Should output something like:
# List of attached devices
# emulator-5554          device
# <or your device name>  device
```

---

## Phase 3: Clone & Setup Project (5 minutes)

### Step 3.1: Get Project Files

```bash
# Navigate to your desired location
cd D:\AtombergAppBoF

# OR clone if from Git
git clone https://github.com/your-repo/AtombergAppBoF.git
cd AtombergAppBoF
```

### Step 3.2: Verify Project Structure

Check that you have:
```
AtombergAppBoF/
├── pom.xml                    ✅ Present?
├── testng.xml                 ✅ Present?
├── src/
│   ├── main/java/app/        ✅ Present?
│   └── test/java/com/appTest/ ✅ Present?
├── README.md                  ✅ Present?
├── QUICK_REFERENCE.md         ✅ Present?
└── verify_tests.bat           ✅ Present?
```

### Step 3.3: Install Dependencies

```bash
# Navigate to project directory
cd D:\AtombergAppBoF

# Download all dependencies
mvn clean install

# This will take 5-10 minutes on first run
# Wait for: BUILD SUCCESS
```

---

## Phase 4: Verify Setup (5 minutes)

### Step 4.1: Run Verification Script

#### Windows
```bash
verify_tests.bat
```

#### macOS/Linux
```bash
bash verify_tests.sh
```

### Step 4.2: Expected Output

```
╔═══════════════════════════════════════════════════════════════╗
║          ATOMBERG APP TEST SUITE VERIFICATION SCRIPT         ║
╚═══════════════════════════════════════════════════════════════╝

✅ Maven is installed
✅ Java is installed
✅ Project cleaned
✅ Code compiled
✅ Tests compiled
✅ Dependencies resolved
✅ LoginTest.java exists
✅ testng.xml exists
✅ pom.xml exists
✅ TEST_FIXES_SUMMARY.md exists
✅ TEST_EXECUTION_GUIDE.md exists
✅ QUICK_REFERENCE.md exists
✅ REFACTORING_COMPLETE.md exists
✅ TestHelper.java exists

═══════════════════════════════════════════════════════════════

PASSED: 15
FAILED: 0

[SUCCESS] All verifications passed! Ready to run tests.
```

### Step 4.3: If Verification Fails

**Issue: Maven not found**
```bash
# Verify Maven PATH
mvn --version

# Add to PATH (Windows)
# Control Panel → System → Environment Variables
# Add: C:\Maven\bin
```

**Issue: Java not found**
```bash
# Verify Java PATH
java -version

# Add to PATH (Windows)
# Control Panel → System → Environment Variables
# Add: C:\Program Files\Java\jdk-17\bin
```

**Issue: Build fails**
```bash
# Clear Maven cache
mvn clean

# Reinstall
mvn clean install

# Check for errors
mvn clean compile -DskipTests
```

---

## Phase 5: Run Your First Tests (5 minutes)

### Step 5.1: Start Appium Server

#### Windows
```bash
appium --address 127.0.0.1 --port 4723
```

#### macOS/Linux
```bash
appium --address 127.0.0.1 --port 4723
```

#### Or Use Appium Desktop
1. Open Appium Desktop application
2. Click "Start Server"
3. Should show "Server is running"

### Step 5.2: Run Smoke Tests

Open a new terminal and run:
```bash
cd D:\AtombergAppBoF

# Run quick smoke tests (2-3 minutes)
mvn test -Dtest=SmokeTests
```

**Expected Output:**
```
[INFO] Running com.appTest.tests.OpenAppTest
[INFO] Running com.appTest.tests.LoginTest
...
[INFO] BUILD SUCCESS
[INFO] Tests run: 5, Failures: 0, Errors: 0, Skipped: 0
```

### Step 5.3: View Test Reports

After tests complete:
```bash
# Windows
start test-output/ExtentReports/index.html

# macOS
open test-output/ExtentReports/index.html

# Linux
xdg-open test-output/ExtentReports/index.html
```

You should see:
- ✅ Test execution timeline
- ✅ Pass/fail statistics
- ✅ Test method details
- ✅ Screenshots (if tests failed)

---

## Phase 6: Run Full Test Suite (10 minutes)

### Step 6.1: Run All Tests

```bash
# Run complete test suite
mvn test

# Expected: 10-15 minutes
# All 8 test classes will execute
```

### Step 6.2: Monitor Test Execution

```bash
# In Appium terminal, you'll see:
[Appium] New WebDriver session created...
[Appium] Test commands being executed...
[Appium] Session ended

# In Maven terminal, you'll see:
[INFO] Running com.appTest.tests.OpenAppTest
[INFO] Running com.appTest.tests.LoginTest
[INFO] Running com.appTest.tests.AppTest
...
[INFO] BUILD SUCCESS
```

### Step 6.3: Review Results

```bash
# Open reports
open test-output/ExtentReports/index.html

# Or check Maven output
# Should show: Tests run: 50+, Failures: 0, Errors: 0
```

---

## 🎯 Troubleshooting Setup

### Port 4723 Already in Use

```bash
# Find process using port 4723
# Windows:
netstat -ano | findstr :4723

# Kill process:
taskkill /PID <PID> /F

# Or use different port:
appium --address 127.0.0.1 --port 4724
```

### Device Not Detected

```bash
# Restart ADB
adb kill-server
adb start-server

# Check devices
adb devices

# If still not showing:
# 1. Unplug device
# 2. Revoke USB debugging
# 3. Replug device
# 4. Accept USB debugging prompt
```

### Build Fails

```bash
# Clear everything and rebuild
mvn clean

# Remove Maven cache (if needed)
# Windows: Delete %USERPROFILE%\.m2\repository
# Unix: rm -rf ~/.m2/repository

# Reinstall
mvn clean install
```

### Test Fails to Run

```bash
# Verify test is discoverable
mvn test -Dtest=help

# Check testng.xml syntax
# mvn test -X  (debug mode)

# Check if device is ready
adb devices
```

---

## ✅ Final Verification

When everything is set up correctly, you should be able to:

```bash
# 1. Verify build
mvn clean compile -DskipTests
# Output: BUILD SUCCESS ✅

# 2. Verify tests exist
mvn test -Dtest=help
# Output: Finds 8 test classes ✅

# 3. Run smoke tests
mvn test -Dtest=SmokeTests
# Output: All tests PASS ✅

# 4. View reports
open test-output/ExtentReports/index.html
# Shows: Test dashboard with statistics ✅
```

---

## 🎓 Next Steps

1. **Read Documentation**
   - `QUICK_REFERENCE.md` - Quick tips (5 min read)
   - `TEST_EXECUTION_GUIDE.md` - Detailed guide (15 min read)

2. **Run Tests**
   ```bash
   mvn test -Dtest=SmokeTests      # Quick (3 min)
   mvn test                         # Full (15 min)
   ```

3. **Customize Tests**
   - Add your own test classes
   - Extend BaseTest
   - Use TestHelper utilities

4. **Set Up CI/CD**
   - Configure GitHub Actions
   - Set up Jenkins pipeline
   - Configure other CI tools

---

## 📞 Getting Help

If you get stuck:

1. **Check console output** - Usually explains the error
2. **Read QUICK_REFERENCE.md** - Common issues & solutions
3. **Review TEST_EXECUTION_GUIDE.md** - Detailed instructions
4. **Check device status** - `adb devices`
5. **Verify Appium is running** - `netstat -ano | grep 4723`

---

## ✨ You're All Set!

Once you see:
```
BUILD SUCCESS
Tests run: XX, Failures: 0, Errors: 0
```

**Congratulations! Your test framework is ready to use.**

### Start testing:
```bash
mvn test
```

### View results:
```bash
open test-output/ExtentReports/index.html
```

---

**Setup Duration:** 30-45 minutes  
**Difficulty:** Beginner-Friendly  
**Status:** ✅ Ready to Test!


