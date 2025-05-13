package app.util;

import app.AppInitializer;
import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.By;
import java.net.URL;


public class ScreenRecording {
    AndroidDriver driver;

    public ScreenRecording(AndroidDriver driver){
//        AppInitializer appInitializer = new AppInitializer();
//        appInitializer.initializeDriverWithURL(url);
        this.driver = driver;
    }
    public ScreenRecording(URL url){
        AppInitializer appInitializer = new AppInitializer();
        appInitializer.initializeDriverWithURL(url);
        this.driver = appInitializer.getDriver();
    }
    public void start(){
        driver.activateApp("com.example.screenrecorder");
        ActionsUtil.SSleep(5);
        ActionsUtil.Tap.withCoordinates(driver, 530, 1570);
        driver.findElement(By.xpath("//android.widget.Button[@content-desc=\"Start Recording\"]")).click();
        driver.findElement(By.xpath("//android.widget.TextView[@text=\"A single app\"]")).click();
        driver.findElement(By.xpath("//android.widget.TextView[@text=\"Entire screen\"]")).click();
        driver.findElement(By.xpath("//android.widget.Button[@text=\"Start\"]")).click();
        System.out.println("Recording Screen");
    }

    public void stop(){
        driver.activateApp("com.example.screenrecorder");
        ActionsUtil.Tap.withCoordinates(driver, 530, 1570);
    }
}
