package app;

import app.Analytics.Analytics;
import app.MoreTab.Manage;
import app.STF.Connect;
import app.STF.Connect2;
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
    public static String command;
    public static ServerInitializer server = new ServerInitializer();

    public static void main(String[] args) throws IOException, UnsupportedFlavorException, InterruptedException {
        Connect2 connect = new Connect2();
        connect.ipAddress();
    }
}
