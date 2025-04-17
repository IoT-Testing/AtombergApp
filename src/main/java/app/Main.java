package app;

import io.appium.java_client.android.AndroidDriver;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;


public class Main {
    public AndroidDriver atomberg;
    public static void main(String[] args) throws IOException, UnsupportedFlavorException {
        AppInitializer app = new AppInitializer();
        ServerInitializer server = new ServerInitializer();
        server.startServer();
        app.openAppWithURL(server.service.getUrl());
        app.checkMainScreen();
        server.stopServer();
    }

}
