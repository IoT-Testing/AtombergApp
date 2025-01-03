package app;

import app.Lock.LockManagement;
import app.WaterPurifier.ROManagement;
import app.util.ActionsUtil;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.appmanagement.ApplicationState;
import org.openqa.selenium.By;

import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;

import static io.appium.java_client.android.NetworkSpeed.LTE;

public class AutomatedTest {

    public AndroidDriver atomberg;
    public AndroidDriver driver;

    public void run() throws IOException, UnsupportedFlavorException {
        startScreenRecorder();
        AppInitializer app = new AppInitializer();
        app.openApp();
        atomberg = app.getDriver();
        app.checkMainScreen();
        LockManagement lock = new LockManagement(atomberg);
        lock.checkLock();
        ActionsUtil.SSleep(5);
        stopScreenRecorder();
    }
    private void startScreenRecorder(){
        AppInitializer appInitializer = new AppInitializer();
        appInitializer.initializeDriver();
        driver = appInitializer.getDriver();
        driver.activateApp("com.example.screenrecorder");
        ActionsUtil.SSleep(2);
        ActionsUtil.Tap.withCoordinates(driver, 530, 1570);
        driver.findElement(By.xpath("//android.widget.Button[@content-desc=\"Start Recording\"]")).click();
        driver.findElement(By.xpath("//android.widget.TextView[@text=\"A single app\"]")).click();
        driver.findElement(By.xpath("//android.widget.TextView[@text=\"Entire screen\"]")).click();
        driver.findElement(By.xpath("//android.widget.Button[@text=\"Start\"]")).click();
        System.out.println("Recording Screen");
    }
    private void stopScreenRecorder(){
        AppInitializer appInitializer = new AppInitializer();
        appInitializer.initializeDriver();
        driver = appInitializer.getDriver();
        driver.activateApp("com.example.screenrecorder");
        ActionsUtil.Tap.withCoordinates(driver, 530, 1570);
    }
}