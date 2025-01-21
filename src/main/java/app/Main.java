package app;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.service.local.AppiumDriverLocalService;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;

public class Main {
    public static AppiumDriverLocalService service;
    public AndroidDriver atomberg;
    public static void main(String[] args) throws IOException, UnsupportedFlavorException {
        AutomatedTest test = new AutomatedTest();
        test.run();
    }
}
