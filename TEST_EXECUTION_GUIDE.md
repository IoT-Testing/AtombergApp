# Test Execution Guide

## Prerequisites

1. **Java 17+** installed
2. **Maven** installed and configured
3. **Android Device/Emulator** connected
4. **Appium Server** running
5. **All dependencies** installed via `mvn clean install`

---

## Quick Start

### 1. Clean and Build
```bash
mvn clean install
```

### 2. Run All Tests
```bash
mvn test
```

### 3. Run Smoke Tests (Fast validation)
```bash
mvn test -Dgroups=smoke
```

---

## Running Specific Test Suites

### Run by TestNG Suite
```bash
mvn test -DsuiteXmlFile=testng.xml
```

### Run Specific Test Class
```bash
mvn test -Dtest=OpenAppTest
```

### Run Specific Test Method
```bash
mvn test -Dtest=LoginTest#correctCredentials_LoginSuccess
```

### Run Multiple Specific Tests
```bash
mvn test -Dtest=LoginTest,AppTest,DeviceProvTest
```

---

## Running with Parameters

### Specify Device Slot
```bash
mvn test -DdeviceSlot=device1
```

### Specify Platform
```bash
mvn test -Dplatform=Android
```

### Custom Configuration
```bash
mvn test -DdeviceSlot=device1 -Dplatform=Android -Dtest=CoreAppTests
```

---

## Test Execution Order

### Optimal Sequential Order (for reuse)
1. **OpenAppTest** - Initialize device
2. **LoginTest** - Validate login
3. **AppTest** - Test main features
4. **ManageFamilyTest** - Family features
5. **ManageProfileTest** - Profile features
6. **DeviceProvTest** - Device provisioning
7. **SecondAppTest** - Comprehensive flow

### Parallel Execution
Tests can run in parallel per testng.xml settings:
```xml
<suite name="AtombergAppFullTestSuite" parallel="tests" thread-count="4">
```

---

## Viewing Test Results

### HTML Report
Located at: `test-output/ExtentReports/`
```bash
# On Windows
start test-output/ExtentReports/index.html

# On macOS
open test-output/ExtentReports/index.html

# On Linux
xdg-open test-output/ExtentReports/index.html
```

### Console Output
```bash
# Verbose output
mvn test -X

# Quiet output
mvn test -q
```

### Surefire Reports
```bash
mvn surefire-report:report
```

---

## Troubleshooting

### Issue: Tests Won't Start
**Solution:** Check device connection
```bash
adb devices
adb connect <IP_ADDRESS>
```

### Issue: Appium Server Not Found
**Solution:** Start Appium server
```bash
appium --address 127.0.0.1 --port 4723
```

### Issue: Driver Not Initialized
**Solution:** Verify BaseTest setup method is called
```java
@BeforeClass
public void setup() {
    this.driver = getDriver();  // Must be called
}
```

### Issue: Test Dependency Fails
**Solution:** Check testng.xml for correct method names
```xml
<!-- Check these match actual method names -->
<class name="com.appTest.tests.TestClass">
    <methods>
        <include name="methodName"/>
    </methods>
</class>
```

### Issue: Report Not Generated
**Solution:** Check ExtentReportAT initialization
```bash
# Verify report directory exists
mkdir -p test-output/reports
```

---

## Test Debugging

### Enable Debug Logging
```bash
mvn test -Dorg.slf4j.simpleLogger.defaultLogLevel=debug
```

### Run Single Test with Debug
```bash
mvn -Dmaven.surefire.debug test -Dtest=LoginTest
```

### View Stack Traces
```bash
# In test output or IDE console
mvn test 2>&1 | tee test-output.log
```

---

## Performance Tips

### Run Tests in Parallel
```bash
mvn test -DthreadCount=4
```

### Skip Report Generation (Faster)
```bash
mvn test -DskipReports=true
```

### Run Only Tests (Skip Compilation)
```bash
mvn test -DskipTests=false
```

---

## Continuous Integration Setup

### GitHub Actions Example
```yaml
name: Run Tests

on: [push, pull_request]

jobs:
  test:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v2
      - uses: actions/setup-java@v2
        with:
          java-version: '17'
      - run: mvn clean test
      - uses: actions/upload-artifact@v2
        if: always()
        with:
          name: test-reports
          path: test-output/
```

---

## Test Data Management

### Credentials (Environment Variables)
```bash
export TEST_EMAIL=your-email@example.com
export TEST_PASSWORD=your-password
mvn test
```

### Or use System Properties
```bash
mvn test -Dtest.email=user@example.com -Dtest.password=password
```

---

## Best Practices Checklist

- [ ] Always extend BaseTest
- [ ] Use @BeforeClass for setup
- [ ] Use @AfterClass for cleanup
- [ ] Add @Test(description = "...") to all tests
- [ ] Use reporter.log() for status updates
- [ ] Throw exceptions on failure (fail fast)
- [ ] Add proper try-catch-finally blocks
- [ ] Use TestHelper utility methods
- [ ] Keep test methods independent
- [ ] Document complex test logic

---

## Common Test Scenarios

### Login and Navigate
```java
@Test
public void testLoginAndNavigate() {
    // 1. Launch app
    driver.activateApp("com.atomberg.app");
    
    // 2. Wait for login screen
    TestHelper.waitForElement(driver, LOGIN_BUTTON, 5);
    
    // 3. Perform login
    Email login = new Email(driver);
    login.email("user@example.com", "password");
    
    // 4. Verify home screen
    Assert.assertTrue(
        TestHelper.isElementDisplayed(driver, HOME_SCREEN),
        "Should reach home screen"
    );
}
```

### Handle Optional Elements
```java
// Safe check
if (TestHelper.isElementDisplayed(driver, OPTIONAL_BUTTON)) {
    TestHelper.clickIfExists(driver, OPTIONAL_BUTTON);
}
```

### Retry With Backoff
```java
TestHelper.retryOperation(() -> {
    FanManagement fan = new FanManagement(driver);
    fan.checkFan();
}, 3, 1000);  // 3 attempts, 1 second initial delay
```

---

## Useful Commands

```bash
# Clean build
mvn clean

# Install dependencies
mvn install

# Run tests
mvn test

# Package project
mvn package

# Generate report
mvn surefire-report:report

# Skip tests during build
mvn install -DskipTests

# Run tests with specific profile
mvn test -Pprofile-name
```

---

## Support & Documentation

- **TestNG**: https://testng.org/doc/
- **Appium**: https://appium.io/docs/
- **Selenium**: https://www.selenium.dev/documentation/
- **ExtentReports**: https://www.extentreports.com/docs.html


