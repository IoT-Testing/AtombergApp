package app;

import app.util.ActionsUtil;
import app.util.ScreenRecording;
import io.appium.java_client.android.AndroidDriver;

public class AutomatedTest {

    public AndroidDriver atomberg;
    public AndroidDriver driver;

    public void run(){
        ScreenRecording.start();
        AppInitializer appInitializer = new AppInitializer();
        //TODO: do not use "openApp()" if "initializeDriver()" is used.
        appInitializer.openApp();
        atomberg = appInitializer.getDriver();
        ActionsUtil.SSleep(2);
        //TODO: Use tapOpAppLogo() if you are using initializeDriver()
        appInitializer.checkMainScreen();
        ScreenRecording.stop();
    }
}