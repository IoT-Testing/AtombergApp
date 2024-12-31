package app.util;

import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

public class ScreenRecording {
    public static void start(AndroidDriver driver){
        ActionsUtil.Swipe.Notifications(driver, 0.60, 0.05);
        ActionsUtil.sleep(500);
        WebElement recordbutton = null;
        try{
            recordbutton = driver.findElement(By.xpath("(//android.widget.ImageView[@resource-id=\"com.android.systemui:id/btn_letter_space\"])[4]"));
        }
        catch (Exception ignored){}
        if (recordbutton == null) {
            ActionsUtil.Swipe.Notifications(driver, 0.50,0.15);
            ActionsUtil.sleep(500);
            ActionsUtil.Tap.withCoordinates(driver, 410, 1010);
        }
        else {
            recordbutton.click();
            ActionsUtil.Tap.withCoordinates(driver,640,1820);
        }System.out.println("Recording Started");
    }

    public static void stop(AndroidDriver driver){
        ActionsUtil.Tap.withCoordinates(driver,300, 50);
        ActionsUtil.sleep(500);
        WebElement done = driver.findElement(By.xpath("//android.widget.TextView[@content-desc=\"Finish recording\"]"));
        done.click();
        System.out.println("Recording Stopped");
    }
}
