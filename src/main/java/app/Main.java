package app;

import app.Analytics.Analytics;
import app.STF.Connect;
import app.util.ActionsUtil;
import io.appium.java_client.android.AndroidDriver;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;

import static app.util.AppUtil.device;


public class Main {
    public static AndroidDriver atomberg;
    public static void main(String[] args) throws IOException, UnsupportedFlavorException, InterruptedException {
        AppInitializer app = new AppInitializer();
        app.openApp();
        atomberg = app.getDriver();
        app.checkMainScreen();
        Analytics analytics = new Analytics(atomberg);
        analytics.Show();
    }
}
