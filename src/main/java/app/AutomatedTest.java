package app;

import app.Lock.LockManagement;
import app.WaterPurifier.ROManagement;
import app.util.ActionsUtil;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.appmanagement.ApplicationState;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;

import static io.appium.java_client.android.NetworkSpeed.LTE;

public class AutomatedTest {

    public AndroidDriver atomberg;

    public void run() throws IOException, UnsupportedFlavorException {
        AppInitializer appInitializer = new AppInitializer();
        appInitializer.openApp();
        atomberg = appInitializer.getDriver();
        appInitializer.checkMainScreen();
        LockManagement lock = new LockManagement(atomberg);
        lock.checkLock();
        ActionsUtil.SSleep(5);
    }
}