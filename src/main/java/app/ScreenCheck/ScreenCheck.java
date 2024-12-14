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
    public AppiumDriver atomberg;
    CommonElements ce = new CommonElements();
    HomeELements he = new HomeELements();

    public ScreenCheck(AppiumDriver atomberg){
        this.atomberg = atomberg;
    }

    public void moreTab(){

        //Check for More Tab Screen
        WebElement moreTab = null;
        try {
            moreTab = atomberg.findElement(By.xpath(ce.moreTabId));
        } catch (Exception ignored) {}
        assert moreTab != null;
        System.out.println(moreTab.isSelected());
        if(!moreTab.isSelected()) moreTab.click();
        rateUs();
    }

    public void homeScreen(){
        //Check for Home Screen
        rateUs();
        List<WebElement> elementList = atomberg.findElements(By.className("android.widget.ImageView"));
        List<WebElement> tabs = elementList.stream().filter(webElement -> webElement.getDomAttribute("content-desc")!=null).collect(Collectors.toList());
        List<WebElement> home = tabs.stream().filter(webElement -> Objects.requireNonNull(webElement.getDomAttribute("content-desc")).startsWith("Hi")).collect(Collectors.toList());
        System.out.println(home.size());
        assert home.size() == 1;
        for (WebElement e:home){
            e.click();
        }
        rateUs();
    }

    public void analytics(){
        //Check for Analytics Screen
        rateUs();
        WebElement analytics = atomberg.findElement(By.xpath(ce.analyticsId));
        System.out.println(analytics.isSelected());
        if(!analytics.isSelected()){
            analytics.click();
        }
        rateUs();
    }

    private void rateUs() {
        List<WebElement> dialogueBox = atomberg.findElements(By.className("android.widget.Button"));
        List<WebElement> cancel = dialogueBox.stream().filter(webElement -> Objects.equals(webElement.getDomAttribute("content-desc"), "Cancel")).collect(Collectors.toList());
        for (WebElement ele : cancel) {
            if (Objects.equals(ele.getDomAttribute("content-desc"), "Cancel")) { //checks for the cancel button and the clicks on it if there
                ele.click();
                System.out.println("Canceled Rate us");
            }
        }
    }
}
