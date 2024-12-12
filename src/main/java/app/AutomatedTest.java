package app;

import app.STF.Connect;
import app.util.ActionsUtil;
import io.appium.java_client.AppiumDriver;
import org.checkerframework.checker.units.qual.A;

import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
public class AutomatedTest {

    public AppiumDriver driver;

    public void run() throws IOException, UnsupportedFlavorException {
        Connect connect = new Connect();
        connect.ipAddress();
        String command = connect.copiedText;
        Runtime.getRuntime().exec(command);
        ActionsUtil.sleep(1000);
        AppInitializer appInitializer = new AppInitializer();
        appInitializer.openApp();
        appInitializer.email();
   }
}