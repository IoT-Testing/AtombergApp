package app;

import app.STF.Connect;
import app.util.ActionsUtil;
import io.appium.java_client.android.AndroidDriver;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;


public class Main {
    public AndroidDriver atomberg;
    public static void main(String[] args) throws IOException, UnsupportedFlavorException {
        Connect connect = new Connect();
        connect.ipAddress();
        String command = connect.copiedText;
//        Runtime.getRuntime().exec(command);
//        ActionsUtil.sleep(1000);
//        ServerInitializer server = new ServerInitializer();
//        server.startServer();
    }

}
