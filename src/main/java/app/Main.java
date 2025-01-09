package app;

import app.util.ActionsUtil;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.options.UiAutomator2Options;
import io.appium.java_client.service.local.AppiumDriverLocalService;
import io.appium.java_client.service.local.AppiumServiceBuilder;

import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;

public class Main {
    public static AppiumDriverLocalService service;
    public AndroidDriver atomberg;
    public static void main(String[] args) throws IOException, UnsupportedFlavorException {
        AppInitializer app = new AppInitializer();
        startServer();
        app.openAppWithURL(service.getUrl());
        stopServer();
    }
    public static void startServer() {
        AppiumServiceBuilder builder = new AppiumServiceBuilder()
                .usingAnyFreePort() // Use a random free port or specify .usingPort(4723)
                .withAppiumJS(new File("/usr/local/lib/node_modules/appium/build/lib/main.js"));// Path to Appium JS file

        service = AppiumDriverLocalService.buildService(builder);
        service.start();
        System.out.println("Appium Server Started on URL: " + service.getUrl());
    }

    public static void stopServer() {
        if (service != null && service.isRunning()) {
            service.stop();
            System.out.println("Appium Server stopped.");
        }
    }
}
