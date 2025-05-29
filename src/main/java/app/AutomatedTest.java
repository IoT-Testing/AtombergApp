package app;

import app.util.ActionsUtil;
import app.util.ScreenRecording;
import io.appium.java_client.android.AndroidDriver;

import java.io.IOException;

public class AutomatedTest {

    public AndroidDriver atomberg;
    public AndroidDriver driver;

    public void run() throws IOException, InterruptedException {
        ServerInitializer server = new ServerInitializer();
        ScreenRecording recording = new ScreenRecording(server.service.getUrl());
        recording.start();
        AppInitializer appInitializer = new AppInitializer();
        //TODO: do not use "openApp()" if "initializeDriver()" is used.
        appInitializer.openApp();
        atomberg = appInitializer.getDriver();
        ActionsUtil.SSleep(2);
        //TODO: Use tapOpAppLogo() if you are using initializeDriver()
        appInitializer.checkMainScreen();
        recording.stop();
    }
}