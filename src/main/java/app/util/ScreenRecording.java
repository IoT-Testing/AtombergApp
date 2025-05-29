package app.util;

import app.AppInitializer;
import app.STF.Connect;
import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.io.IOException;
import java.net.URL;


public class ScreenRecording {
    AndroidDriver driver;

    public ScreenRecording(AndroidDriver driver){
        this.driver = driver;
    }
    public ScreenRecording(URL url) throws IOException, InterruptedException {
        AppInitializer appInitializer = new AppInitializer();
        appInitializer.initializeDriverWithURL(url);
        this.driver = appInitializer.getDriver();
    }
    public void start() throws IOException, InterruptedException {
        driver.activateApp("com.hbisoft.hbrecorderexample");
        ActionsUtil.SSleep(5);
        driver.findElement(By.xpath("//android.widget.Button[@resource-id=\"com.hbisoft.hbrecorderexample:id/button_start\"]")).click();
        WebElement singleApp = null;
        try {
            singleApp = driver.findElement(By.xpath("//android.widget.TextView[@text=\"A single app\"]"));
        }catch (Exception ignored){}
        if (singleApp != null){
            singleApp.click();
            driver.findElement(By.xpath("//android.widget.TextView[@text=\"Entire screen\"]")).click();
            driver.findElement(By.xpath("//android.widget.Button[@text=\"Start\"]")).click();
        }
        else {
            driver.findElement(By.xpath("//android.widget.Button[@text=\"START NOW\"]")).click();
        }
        System.out.println("Recording Screen");
    }

    public void stop(){
        driver.activateApp("com.hbisoft.hbrecorderexample");
        driver.findElement(By.xpath("//android.widget.Button[@text=\"STOP\"]")).click();
    }
}
