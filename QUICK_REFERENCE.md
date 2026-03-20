# Quick Reference Card

## Common Issues & Solutions

### Issue: TestNG Can't Instantiate Test Class
```
org.testng.TestNGException: An error occurred while instantiating class
```
**Solution:** Make sure your test class:
- Extends `BaseTest`
- Has NO custom constructor
- Has `@BeforeClass` method that calls `getDriver()`

```java
// ❌ WRONG
public MyTest(String param) { }

// ✅ CORRECT
public class MyTest extends BaseTest {
    @BeforeClass
    public void setup() {
        driver = getDriver();
    }
}
```

---

### Issue: Driver is Null
```
NullPointerException: driver is null
```
**Solution:** Initialize driver in `@BeforeClass`, not at field level

```java
// ❌ WRONG
public AndroidDriver driver = getDriver();

// ✅ CORRECT
public AndroidDriver driver;

@BeforeClass
public void setup() {
    driver = getDriver();
}
```

---

### Issue: Build Fails with pom.xml Error
```
[ERROR] 'dependencies.dependency' is referencing itself
```
**Solution:** Remove self-referencing dependency from pom.xml

```xml
<!-- ❌ Remove this -->
<dependency>
    <groupId>com.atomberg.app</groupId>
    <artifactId>atombergapp-test</artifactId>
</dependency>
```

---

### Issue: Duplicate Plugin Error
```
[WARNING] 'build.plugins.plugin' must be unique but found duplicate
```
**Solution:** Remove duplicate plugin declarations in pom.xml

```xml
<!-- Keep only ONE instance of each plugin -->
<!-- ❌ Delete duplicate <plugin> blocks -->
```

---

### Issue: Test Method Not Found
```
TestNGException: Method not found
```
**Solution:** Check testng.xml method names match actual methods

```xml
<!-- ✅ CORRECT -->
<methods>
    <include name="testOpenApp"/>  <!-- Must match actual method -->
</methods>
```

---

## Command Cheat Sheet

```bash
# Build & Compile
mvn clean
mvn clean compile
mvn clean compile -DskipTests

# Run Tests
mvn test
mvn test -Dtest=LoginTest
mvn test -Dtest=LoginTest#correctCredentials_LoginSuccess
mvn test -DdeviceSlot=device1

# Maven Advanced
mvn install                          # Build & install
mvn package                          # Create JAR
mvn clean verify                     # Full build & tests
mvn dependency:tree                  # Show dependencies
mvn surefire-report:report          # Generate report

# Debugging
mvn test -e                         # Show errors
mvn test -X                         # Debug logging
mvn -Dmaven.surefire.debug test    # Debug mode
```

---

## Test Template (Copy & Paste)

```java
package com.appTest.tests;

import io.appium.java_client.android.AndroidDriver;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import com.aventstack.extentreports.Status;

/**
 * YourTestClass - Description of test.
 */
public class YourTestClass extends BaseTest {
    public AndroidDriver driver;

    @BeforeClass
    public void setUp() {
        driver = getDriver();
        System.out.println("Setup completed");
    }

    @Test(priority = 1, description = "Clear test description")
    void testMethod() {
        try {
            reporter.startTest("Test Name", deviceSlot);
            
            // Your test code here
            driver.activateApp("com.atomberg.app");
            
            // Assert & verify
            Assert.assertNotNull(driver, "Driver should exist");
            
            reporter.log(Status.PASS, "Test passed");
        } catch (Exception e) {
            reporter.log(Status.FAIL, "Test failed: " + e.getMessage());
            afterTestFailure(driver);
            throw e;
        } finally {
            reporter.endTest();
        }
    }
}
```

---

## TestNG XML Template

```xml
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE suite SYSTEM "https://testng.org/testng-1.0.dtd">
<suite name="MySuite" parallel="tests" thread-count="2">
    
    <listeners>
        <listener class-name="com.appTest.listeners.TestListeners"/>
    </listeners>

    <test name="MyTest">
        <parameter name="deviceSlot" value="device1"/>
        <classes>
            <class name="com.appTest.tests.MyTestClass"/>
        </classes>
    </test>

</suite>
```

---

## TestHelper Usage Examples

```java
// Wait for element
WebElement button = TestHelper.waitForElement(driver, BUTTON_LOCATOR, 5);

// Check if element exists
if (TestHelper.isElementDisplayed(driver, OPTIONAL_ELEMENT)) {
    // Element exists
}

// Click if exists
TestHelper.clickIfExists(driver, BUTTON);

// Get element text
String text = TestHelper.getElementText(driver, LABEL);

// Set input text
TestHelper.setText(driver, INPUT_FIELD, "text to enter");

// Retry operation with backoff
TestHelper.retryOperation(() -> {
    FanManagement fan = new FanManagement(driver);
    fan.checkFan();
}, 3, 1000);  // 3 attempts, 1 second initial delay

// Navigate back with limit
TestHelper.navigateBackWithLimit(driver, 10);

// Scroll
TestHelper.scroll(driver, TestHelper.ScrollDirection.DOWN);
```

---

## BaseTest Methods Available

```java
// From BaseTest (available to all test classes)
AndroidDriver getDriver()              // Get driver instance
void afterTestFailure(AndroidDriver)   // Recovery on failure
boolean isOnHomeScreen(AndroidDriver)  // Check home screen

// Static properties accessible
public static String deviceSlot        // Current device
public static ExtentReportAT reporter  // Report instance
public static ServerInitializer server // Appium server
```

---

## Report Location

After running tests, reports are available at:
```
test-output/ExtentReports/index.html
```

Open in browser:
```bash
# Windows
start test-output/ExtentReports/index.html

# macOS
open test-output/ExtentReports/index.html

# Linux
xdg-open test-output/ExtentReports/index.html
```

---

## Common Locator Patterns

```java
// Import locators
import static app.resources.Locators.Android.HomeLocators.*;
import static app.resources.Locators.Android.AppLocators.Login.*;

// Use in tests
WebElement element = driver.findElement(HOME_BUTTON);
WebElement input = driver.findElement(EMAIL_INPUT);

// Or use By directly
WebElement element = driver.findElement(
    By.xpath("//android.widget.Button[@text='Login']")
);
```

---

## Test Execution Flow

```
1. mvn test
   ↓
2. TestNG loads testng.xml
   ↓
3. For each <test>:
   a. Create test class instance
   b. Call @BeforeClass setup()
   c. Run each @Test method
   d. Call @AfterClass tearDown()
   ↓
4. Generate ExtentReport
   ↓
5. Display results
```

---

## Status Codes in Reports

```
✅ PASS    - Test passed successfully
❌ FAIL    - Test failed
⚠️  SKIP   - Test skipped
ℹ️  INFO   - Information log
🔴 ERROR  - Critical error
```

---

## File Structure

```
AtombergAppBoF/
├── pom.xml                          # Maven configuration
├── testng.xml                       # TestNG configuration
├── src/
│   ├── main/java/app/              # Application code
│   │   ├── Login/
│   │   ├── Fan/
│   │   ├── MoreTab/
│   │   └── util/
│   └── test/java/com/appTest/
│       ├── tests/                  # Test classes (FIXED)
│       │   ├── BaseTest.java
│       │   ├── LoginTest.java
│       │   ├── AppTest.java
│       │   ├── OpenAppTest.java
│       │   ├── ManageProfileTest.java
│       │   ├── ManageFamilyTest.java
│       │   ├── DeviceProvTest.java
│       │   └── SecondAppTest.java
│       ├── util/
│       │   └── TestHelper.java     # NEW - Utility framework
│       └── listeners/
├── target/                          # Compiled output
├── test-output/
│   └── ExtentReports/              # Test reports
│
├── TEST_FIXES_SUMMARY.md           # Issue documentation
├── TEST_EXECUTION_GUIDE.md         # User manual
├── REFACTORING_COMPLETE.md         # Complete summary
└── QUICK_REFERENCE.md              # This file
```

---

## Troubleshooting Flowchart

```
Tests Not Running?
├─ Check device connected: adb devices
├─ Check Appium running: port 4723
└─ Check pom.xml builds: mvn clean compile

Tests Fail to Instantiate?
├─ Check: extends BaseTest
├─ Check: NO custom constructor
└─ Check: @BeforeClass with getDriver()

Tests Fail with NullPointerException?
├─ Check: driver = getDriver() in @BeforeClass
└─ Check: NOT at field initialization

Build Fails?
├─ Check: No self-referencing dependencies
├─ Check: No duplicate plugins
└─ Check: mvn clean compile

No Report Generated?
├─ Check: reporter.startTest() called
├─ Check: reporter.endTest() in finally
└─ Check: ExtentReports jar in pom.xml
```

---

## Tips & Tricks

### Speed Up Tests
```bash
mvn test -DskipReports=true     # Skip report generation
mvn test -q                      # Quiet mode
```

### Debug Single Test
```bash
mvn test -Dtest=LoginTest#correctCredentials_LoginSuccess -X
```

### Run Smoke Tests Only
```bash
mvn test -DsuiteXmlFile=testng.xml -Dgroups=smoke
```

### Check Maven Version
```bash
mvn --version
```

### Update Dependencies
```bash
mvn dependency:resolve
mvn dependency:tree
```

---

## Emergency Commands

```bash
# Clear all compiled code
mvn clean

# Rebuild everything
mvn clean install

# Force re-download dependencies
mvn clean install -U

# Kill hanging processes
pkill -f java

# Check port 4723 (Appium)
netstat -an | grep 4723
```

---

**Last Updated:** March 12, 2026
**Status:** ✅ Production Ready


