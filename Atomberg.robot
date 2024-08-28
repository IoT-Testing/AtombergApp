
*** Settings ***
Library    AppiumLibrary

*** Test Cases ***
Login Test
    
    Open Application    platformName=Android    deviceName=Pixel 5     appPackage=com.atomberg.app    appActivity=com.atomberg.app.MainActivity
    Tap    Object  AtombergApp/XCUIElementTypeImage    500ms
    Tap    Object  AtombergApp/android.widget.EditText    2000ms
    Input Text    Object  AtombergApp/android.widget.EditText    wametis763@bookspre.com
    Tap    Object  AtombergApp/XCUIElementTypeButton    500ms
    Tap    Object  AtombergApp/android.widget.EditText    500ms
    Input Text    Object  AtombergApp/android.widget.EditText    Atomberg@123
    Tap    Object  AtombergApp/XCUIElementTypeButton    500ms
        Start Application Crawler
    Close Application

*** Keywords ***
Open Application
    Open Application    ${desired_caps}

Close Application
    Close Application