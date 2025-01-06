package app.util;

import app.AppInitializer;
import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.By;

public class ScreenRecording {
    public static void start(){
        AppInitializer appInitializer = new AppInitializer();
        appInitializer.initializeDriver();
        AndroidDriver driver = appInitializer.getDriver();
        driver.activateApp("com.example.screenrecorder");
        ActionsUtil.SSleep(5);
        ActionsUtil.Tap.withCoordinates(driver, 530, 1570);
        driver.findElement(By.xpath("//android.widget.Button[@content-desc=\"Start Recording\"]")).click();
        driver.findElement(By.xpath("//android.widget.TextView[@text=\"A single app\"]")).click();
        driver.findElement(By.xpath("//android.widget.TextView[@text=\"Entire screen\"]")).click();
        driver.findElement(By.xpath("//android.widget.Button[@text=\"Start\"]")).click();
        System.out.println("Recording Screen");
    }
    public static void stop(){
        AppInitializer appInitializer = new AppInitializer();
        appInitializer.initializeDriver();
        AndroidDriver driver = appInitializer.getDriver();
        driver.activateApp("com.example.screenrecorder");
        ActionsUtil.Tap.withCoordinates(driver, 530, 1570);
    }
}
