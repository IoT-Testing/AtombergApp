package app.ScreenCheck;

import app.Resources.CommonElements;
import app.Resources.HomeELements;
import app.ScreenCheckCallbackAction;
import app.util.ActionsUtil;
import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/*
    THIS CHECK IS FOR ALL THE MAIN SCREENS OF THE APP
    HOME SCREEN CHECK & CLICK
    MORE TAB CHECK & CLICK
    ANALYTICS TAB CHECK & CLICK
*/

public class ScreenCheck {
    public AppiumDriver driver;

    CommonElements ce = new CommonElements();
    HomeELements he = new HomeELements();

    public ScreenCheck(AppiumDriver driver){
        this.driver = driver;
    }

    public void moreTab(){
        rateUs();
        //Check for More Tab Screen
        WebElement moreTab = null;
        try {
            moreTab = driver.findElement(By.xpath(ce.moreTabId));
        } catch (Exception ignored) {}
        assert moreTab != null;
        System.out.println(moreTab.isSelected());
        if(!moreTab.isSelected()) moreTab.click();
        rateUs();
    }

    public void homeScreen(){
        //Check for Home Screen
        rateUs();
        WebElement devices = null;
        WebElement addYourFirstSmartDevice = null;
        try {
            addYourFirstSmartDevice = driver.findElement(By.xpath(he.addYourFirstSmartDeviceId));
        }catch (Exception ignored){}
        try {
            devices = driver.findElement(By.xpath(he.devicesId));
        }catch (Exception ignored){}
        if(devices == null && addYourFirstSmartDevice == null){
            ActionsUtil.Tap.withCoordinates(driver,540,2150);
        }
        rateUs();
    }

    public void analytics(){
        //Check for Analytics Screen
        rateUs();
        WebElement analytics = driver.findElement(By.xpath(ce.analyticsId));
        System.out.println(analytics.isSelected());
        if(!analytics.isSelected()){
            analytics.click();
        }
        rateUs();
    }

    private void rateUs() {
        List<WebElement> dialogueBox = driver.findElements(By.className("android.widget.Button"));
        List<WebElement> cancel = dialogueBox.stream().filter(webElement -> Objects.equals(webElement.getDomAttribute("content-desc"), "Cancel")).collect(Collectors.toList());
        for (WebElement ele : cancel) {
            if (Objects.equals(ele.getDomAttribute("content-desc"), "Cancel")) { //checks for the cancel button and the clicks on it if there
                ele.click();
                System.out.println("Canceled Rate us");
            }
        }
    }
}
