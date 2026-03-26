package app;

import app.Login.Email;
import app.util.ActionsUtil;
import app.util.AppUtil;
import app.util.PermissionUtil;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.options.UiAutomator2Options;
import io.appium.java_client.appmanagement.ApplicationState;
import io.appium.java_client.service.local.AppiumDriverLocalService;
import io.appium.java_client.service.local.AppiumServiceBuilder;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static app.resources.Credentials.*;
import static java.lang.Thread.sleep;

public class AppInitializer {
    public AndroidDriver atomberg;
    public AppiumDriverLocalService service;

    public AndroidDriver getDriver() {
        return atomberg;
    }

    public void setDriver(AndroidDriver driver) {
        this.atomberg = driver;
    }

    public void openApp(){
        UiAutomator2Options options = new UiAutomator2Options();
        options.setAppPackage("com.atomberg.app");
        options.setAppActivity("com.atomberg.app.MainActivity");
        URL url = null;
        try {
            url = new URL("http://127.0.0.1:4723/wd/hub");
        } catch (MalformedURLException e) {
            System.out.println("Malformed URL exception " + e.getMessage());
        }
        assert url != null;
        atomberg = new AndroidDriver(url, options);
        atomberg.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
    }

    public boolean checkMainScreen() throws Exception{
        ApplicationState appState = atomberg.queryAppState("com.atomberg.app");
        if (appState == ApplicationState.RUNNING_IN_FOREGROUND)
        {
            WebElement isMainScreenDisplayed = null;
            try {
                isMainScreenDisplayed = atomberg.findElement(By.xpath("//android.view.View[@content-desc=\"Experience smart living \n" + " with Atomberg\"]"));
            } catch (Exception ignored) {
            }
            if (isMainScreenDisplayed != null) {
                System.out.println("Login Screen Displayed");
                Email login = new Email(atomberg);
                login.email(DEFAULT_EMAIL, DEFAULT_PASSWORD);
            } else {
                System.out.println("Already logged in");
            }
        }
        else if(appState != ApplicationState.RUNNING_IN_FOREGROUND)
        {
            atomberg.activateApp("com.atomberg.app");
            ActionsUtil.SSleep(5);
            WebElement isMainScreenDisplayed = null;
            try {
                isMainScreenDisplayed = atomberg.findElement(By.xpath("//android.view.View[@content-desc=\"Experience smart living \n" + " with Atomberg\"]"));
            } catch (Exception ignored) {
            }
            if (isMainScreenDisplayed != null) {
                System.out.println("Login Screen Displayed");
                Email login = new Email(atomberg);
                login.email(DEFAULT_EMAIL, DEFAULT_PASSWORD);
            } else {
                System.out.println("Already logged in");
            }
        }
        return false;
    }

    public void initializeDriver(){
        //These caps only get the device, need to select Atomberg Home ap separately
        UiAutomator2Options options = new UiAutomator2Options();
        options.setCapability("platformName", "Android");
        options.setCapability("platformVersion", "15");
//        options.setCapability("appPackage", "com.atomberg.app");

        URL url = null;
        try {
            url = new URL("http://127.0.0.1:4723/wd/hub");
        } catch (MalformedURLException e) {
            System.out.println("Malformed URL exception " + e.getMessage());
        }
        assert url != null;
        atomberg = new AndroidDriver(url, options);
        atomberg.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
    }

    public void initializeDriverWithURL(){
        //These caps only get the device, need to select Atomberg Home ap separately
        UiAutomator2Options options = new UiAutomator2Options();
        options.setCapability("platformName", "Android");
        options.setCapability("platformVersion", "15");
//        options.setCapability("appPackage", "com.atomberg.app");
        URL url = service.getUrl();
        System.out.println(url);
        atomberg = new AndroidDriver(url, options);
        atomberg.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
    }

    public void login(String login, String pass) {
        WebElement emailLoginButton = atomberg.findElement(By.xpath("//android.widget.FrameLayout[@resource-id=\"android:id/content\"]/android.widget.FrameLayout/android.widget.FrameLayout/android.view.View/android.view.View/android.view.View/android.view.View/android.view.View/android.widget.ImageView[4]"));
        emailLoginButton.click();
        AppUtil.captureScreenshot(atomberg, "emailLoginButton");
        WebElement emailField = atomberg.findElement(By.xpath("//android.widget.EditText"));
        emailField.click();
        emailField.sendKeys(login); // Enter Email id
        AppUtil.captureScreenshot(atomberg, "emailField");
        System.out.println(" " + emailField.getText() + " ");
        System.out.println("Email Entered...");
        AppUtil.captureScreenshot(atomberg, "emailEntered");
        WebElement continueButton = atomberg.findElement(By.xpath("//android.widget.Button[@content-desc=\"Continue\"]"));
        continueButton.click(); // Continue button
        AppUtil.captureScreenshot(atomberg, "continueButton");
        try {
            sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        WebElement passwordField = atomberg.findElement(By.xpath("//android.widget.EditText"));
        passwordField.click();
        passwordField.sendKeys(pass);
        AppUtil.captureScreenshot(atomberg, "passwordField");
        System.out.println("Password entered..."); // Enter Password
        WebElement continueButton1 = atomberg.findElement(By.xpath("//android.widget.Button[@content-desc=\"Continue\"]"));
        continueButton1.click(); // Continue to Log in
        System.out.println("Continue...");
        ActionsUtil.sleep(5000);
        PermissionUtil.allow(atomberg);
        List<WebElement> dialogueBox = atomberg.findElements(By.className("android.view.View"));
        List<WebElement> elementList = dialogueBox.stream().filter(element -> element.getDomAttribute("content-desc")!=null).collect(Collectors.toList());
        for (WebElement element : elementList){
            if (Objects.equals(element.getDomAttribute("content-desc"), "Use Alexa to control your smart fan(s) with voice"))
            {
                atomberg.findElement(By.xpath("//android.widget.Button[@content-desc=\"Cancel\"]")).click();
                break;
            }
        }
    }

    public void openAppWithURL(){
        UiAutomator2Options options = new UiAutomator2Options();
        options.setAppPackage("com.atomberg.app");
        options.setAppActivity("com.atomberg.app.MainActivity");
        URL url = service.getUrl();
        System.out.println(url);
        System.out.println(options);
        atomberg = new  AndroidDriver(url, options);
//        atomberg.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
    }
    public void startServer() {
        AppiumServiceBuilder builder = new AppiumServiceBuilder()
                .usingAnyFreePort() // Use a random free port or specify .usingPort(4723)
                .withAppiumJS(new File("/usr/local/lib/node_modules/appium/build/lib/main.js"));// Path to Appium JS file

        service = AppiumDriverLocalService.buildService(builder);
        service.start();
        System.out.println("Appium Server Started on URL: " + service.getUrl());
    }

    public void stopServer() {
        if (service != null && service.isRunning()) {
            service.stop();
            System.out.println("Appium Server stopped.");
        }
    }
}

