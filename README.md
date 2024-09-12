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
1. Appium/Appium Desktop Server should be installed in the Host PC
2. Appium Inspector should be installed to get the element ID's
3. Java compatible IDE (IntelliJ IDE or Eclipse).
4. Android Device with developer mode. Connect it with host using USB cable.
5. Dependencies for the project to be mentioned in pom.xml file.
6. NPM installed in the HOST PC. https://nodejs.org/en
7. cmd : `npm install appium`

**Cmd prompt**
//adb logcat *:E | findstr "com.atomberg.app"

// robot -d Output Test/Login.robot

//adb shell monkey -p com.atomberg.app -v 5000 --throttle 100

**Communication**

Turn on the developer options in the Android mobile. Now connect the mobile with the Host PC using USB cable.
After that run cmd prompt and run `adb devices`