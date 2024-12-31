package app.util;

import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.By;

public class ChromeCacheClear {
    public AndroidDriver driver;
    public ChromeCacheClear(AndroidDriver driver){
        this.driver = driver;
    }
    public void cacheClear(){
        ActionsUtil.minimize(driver);
        ActionsUtil.longPress(driver,415,2155);
        ActionsUtil.sleep(1000);
        driver.findElement(By.xpath("//android.widget.ImageView[@content-desc=\"App info\"]")).click();
        driver.findElement(By.xpath("//android.widget.TextView[@text=\"Storage usage\"]")).click();
        driver.findElement(By.xpath("//android.widget.Button[@text=\"Clear cache\"]")).click();
        ActionsUtil.sleep(1500);
        ActionsUtil.Tap.withPercentage(driver, 0.28, 1.05);
        driver.findElement(By.xpath("//android.widget.ScrollView[@resource-id=\"com.android.launcher:id/dockview_panel\"]/android.view.View[2]")).click();
    }
}
