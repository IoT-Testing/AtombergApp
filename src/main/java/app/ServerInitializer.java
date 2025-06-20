package app;

import io.appium.java_client.service.local.AppiumDriverLocalService;
import io.appium.java_client.service.local.AppiumServiceBuilder;
import java.io.File;

public class ServerInitializer {
    public AppiumDriverLocalService service;
    public void startServer() {
        AppiumServiceBuilder builder = new AppiumServiceBuilder()
                .usingAnyFreePort() // Use a random free port or specify .usingPort(4723)
                .withAppiumJS(new File("C:\\Users\\Rohit Bhagat\\AppData\\Roaming\\npm\\node_modules\\appium\\build\\lib\\main.js"));// Path to Appium JS file
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
