# AtombergApp
Testing Atomberg Home app

This project is made to test the atomberg home app, with automated steps.
As the app is flutter based and also the mobile needs to be near the fan or lock to control them
this will work only with the physical devices connected to the host.

There are few stress tests in the project.

1. Prerequisites
2. Communication
3. Initialization
4. Error Handling
5. Test Report format


**Prerequisites**
1. Appium Server should be installed in the Host PC
2. Java compatible IDE (IntelliJ IDE or Eclipse).
3. Android Device with developer mode. Connect it with host using USB cable.
4. Dependencies for the project to be mentioned in pom.xml file.
5. NPM installed in the HOST PC. https://nodejs.org/en
6. cmd : `npm install appium`
7. Linux : `./appium.AppImage --no-sandbox`


**Communication**

Turn on the developer options in the Android mobile. Now connect the mobile with the Host PC using USB cable.
After that run cmd prompt and run `adb devices`
If you have list of devices visible after running the command, then you can use those devices for the process.
Run Appium Server GUI, or `appium` commands in the terminal to start Appium Server.

Run Appium Inspector, in there provide the desired capabilities.
1. platformName(Android or iOS).
2. platformVersion(The OS version of the specific device).
3. uuid(is visible in ADB devices list, for iOS in XCODE).
4. deviceName(Name of the device).

After this you will be able to see the devices screen on the inspector UI.
After you click on any element of the App or Device, the inspector will provide you the locator of that specific element.

The above same Desired Capabilities are to be used in the Code as well.
```
MutableCapabilities capabilities = new UiAutomator2Options();
 capabilities.setCapability("platformName", "Android");
 capabilities.setCapability("appPackage", "com.appPackage.app");
 capabilities.setCapability("appActivity", "com.appPackage.app.MainActivity");

 driver = new AndroidDriver(new URL("http://127.0.0.1:4723/wd/hub"),capabilities);
 //this will initialize the driver.
 
 driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
 /*
 this will set an implicit wait, where the driver will wait for
 maximum of 10 seconds for checking the visibility of the element.
 */
```
When you run the above snippet, you will see that the App has been opened in you connected device.

**Error Handling**

\\\\\

**Test Report Format**

For the Test report, we have Extent Reports, from this we can use the required data and add them in the .html file.
We just have to specify which data we want to mention in the report.
We can add test status.
Status includes: PASS / FAIL / SKIPPED / ABORTED 