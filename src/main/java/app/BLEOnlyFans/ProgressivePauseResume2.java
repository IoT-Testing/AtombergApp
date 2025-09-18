package app.BLEOnlyFans;

import app.util.ActionsUtil;
import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.*;

public class ProgressivePauseResume2 {
    //Pre-requisite: App should be open and the Screen should be on Device control Screen.
    private AndroidDriver driver;
    public ProgressivePauseResume2(AndroidDriver driver) {
        this.driver = driver;
    }

    public void performPauseResumeCycle() {
        driver.findElement(By.xpath("//android.widget.Button[@content-desc=\"Start\"]")).click();
        sleep(1400);

        for (int i = 0; i < 15; i++){
            ActionsUtil.Tap.withCoordinates(driver, 525, 2020);
            sleep(1400);
            ActionsUtil.Tap.withCoordinates(driver, 525, 2020);
            sleep(1400);
            System.out.println(i);
        }
    }


    private void sleep(long millis) {
        try {
            Thread.sleep(millis);
            System.out.println("sleep");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.err.println("Sleep interrupted: " + e.getMessage());
        }
    }
}