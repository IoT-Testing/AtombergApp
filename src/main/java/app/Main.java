package app;

import app.STF.Connect2;
import app.util.ActionsUtil;
import app.util.ScreenRecording;
import io.appium.java_client.android.AndroidDriver;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;

public class Main {
    public static AndroidDriver atomberg;
    public static String command;
    public static ServerInitializer server = new ServerInitializer();

    public static void main(String[] args) throws IOException, UnsupportedFlavorException, InterruptedException {
        Connect2 connect = new Connect2();
        connect.ipAddress();
        command = connect.copiedText;
        Runtime.getRuntime().exec(command);
        server.startServer();
        AppInitializer appInitializer = new AppInitializer();
        appInitializer.initializeDriverWithURL(server.service.getUrl(), command);
        atomberg = appInitializer.getDriver();

        ScreenRecording recording = new ScreenRecording(atomberg);
        recording.start();

        ActionsUtil.SSleep(2);
        atomberg.activateApp("com.atomberg.app");
        appInitializer.checkMainScreen();
    }
}
