package app.ScreenCheck;

import app.util.ActionsUtil;
import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

/*
    THIS CHECK IS FOR ALL THE MAIN SCREENS OF THE APP
    HOME SCREEN CHECK & CLICK
    MORE TAB CHECK & CLICK
    ANALYTICS TAB CHECK & CLICK
*/

public class ScreenCheck {
    public AppiumDriver driver;
    public ScreenCheck(AppiumDriver driver){
        this.driver = driver;
    }

    public void moreTab(){
        WebElement moreTab = null;
        try {
            moreTab = driver.findElement(By.xpath("//android.widget.ImageView[@content-desc=\"More\nTab 3 of 3\"]"));
        } catch (Exception ignored) {}
        System.out.println(moreTab.isSelected());
        if(!moreTab.isSelected()) moreTab.click();
    }

    public void homeScreen(){
        WebElement devices = null;
        WebElement addYourFirstSmartDevice = null;
        try {
            addYourFirstSmartDevice = driver.findElement(By.xpath("//android.widget.ImageView[@content-desc=\"Add your first smart device\"]"));
        }catch (Exception ignored){}
        try {
            devices = driver.findElement(By.xpath("//android.view.View[@content-desc=\"Devices\"]"));
        }catch (Exception ignored){}
        if(devices == null && addYourFirstSmartDevice == null){
            ActionsUtil.Tap.withCoordinates(driver,540,2150);
        }
    }

    public void analytics(){
        WebElement analytics = driver.findElement(By.xpath("//android.view.View[@content-desc=\"Analytics\nTab 1 of 3\"]"));
        System.out.println(analytics.isSelected());
        if(!analytics.isSelected()){
            analytics.click();
        }
    }
}
