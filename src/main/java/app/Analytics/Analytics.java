package app.Analytics;

import app.util.ActionsUtil;
import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class Analytics {
    private WebElement analytics;
    private WebElement moreTab;
    private AppiumDriver driver;

    public Analytics(AppiumDriver driver){
        this.driver = driver;
    }
    public void Show() {
        analytics = driver.findElement(By.xpath("//android.view.View[@content-desc=\"Analytics\n" +
                "Tab 1 of 3\"]"));
        moreTab = driver.findElement(By.xpath("//android.widget.ImageView[@content-desc=\"More\n" +
                "Tab 3 of 3\"]"));
        analytics.click(); //Compulsory switch to analytics screen
//        Assertions.assertTrue();
        System.out.println("switched to Analytics");
        WebElement FanCheck = null;
        try { // check if  the fan is available
            FanCheck = driver.findElement(By.xpath("//android.view.View[@content-desc=\"Please add a smart device to view analytics\"]"));
        } catch (Exception ignored) {
        }
        if (FanCheck == null) {
            System.out.println("Fan Available in Analytics");
            nextFan();
        }
        else System.out.println("Fan Not Available in Analytics");
    }

    private void fanChange() {
        List<WebElement> FANS = driver.findElements(By.className("android.view.View"));
        List<WebElement> Fans = FANS.stream().filter(ele -> ele.getDomAttribute("content-desc") != null).collect(Collectors.toList());
        List<WebElement> fans = Fans.stream().filter(ele -> Objects.requireNonNull(ele.getDomAttribute("content-desc")).endsWith("Fan")).collect(Collectors.toList());

        for (WebElement fan : fans) {
            fan.click();
        }
    }

    private void nextFan() {
        fanChange(); //to click on the fan name to get the list of available fans
        List<WebElement> availFans = driver.findElements(By.className("android.view.View"));
        List<WebElement> fans = availFans.stream().filter(ele -> ele.getDomAttribute("content-desc") != null).collect(Collectors.toList());
        fans.remove(fans.size() - 1);
        System.out.println(fans.size());
        for (int i = 0; i < fans.size(); i++) {
            ActionsUtil.sleep(2000);
            List<WebElement> availFans1 = driver.findElements(By.className("android.view.View"));
            List<WebElement> anaFans = availFans1.stream().filter(ele -> ele.getDomAttribute("content-desc") != null).collect(Collectors.toList());
            System.out.println(anaFans.size());
            anaFans.remove(anaFans.size() - 1);
            System.out.println(i);//To check which iteration is running
            System.out.println(anaFans.get(i).getDomAttribute("content-desc"));
            anaFans.get(i).click();
            info();
            System.out.println(i < (anaFans.size() - 1));
            if (i < (anaFans.size() - 1)) { // to go to the analytics screen and
                moreTab.click();
                rateUs(); //check for rate us pop-up
                ActionsUtil.sleep(1500);
                analytics.click();
                rateUs(); //check for rate us pop-up
            }
            fanChange();
        }
    }

    private void rateUs() {
        List<WebElement> dialogueBox = driver.findElements(By.className("android.widget.Button"));
        List<WebElement> cancel = dialogueBox.stream().filter(webElement -> Objects.equals(webElement.getDomAttribute("content-desc"), "Cancel")).collect(Collectors.toList());
        for (WebElement ele : cancel) {
            if (Objects.equals(ele.getDomAttribute("content-desc"), "Cancel")) { //checks for the cancel button and the clicks on it if there
                ele.click();
                System.out.println("Canceled Rate us");
                analytics = driver.findElement(By.xpath("//android.view.View[@content-desc=\"Analytics\n" +
                        "Tab 1 of 3\"]"));
                moreTab = driver.findElement(By.xpath("//android.widget.ImageView[@content-desc=\"More\n" +
                        "Tab 3 of 3\"]"));
            }
        }
    }

    private void info() {
        int i;
        for (i = 0; i < 4; i++) {// there are 4 screens in analytics
            ActionsUtil.sleep(2000);
            List<WebElement> ICONS = driver.findElements(By.xpath("//android.view.View[@clickable=\"true\"]"));
            List<WebElement> icons = ICONS.stream().filter(element -> element.getDomAttribute("content-desc") == null).collect(Collectors.toList());
//            icons.remove(icons.size() - 1);
            for (WebElement icon : icons) {
                System.out.println(icon.getDomAttribute("content-desc"));
                icon.click();
                ActionsUtil.sleep(2000);
                driver.navigate().back();
                ActionsUtil.sleep(1500);
                confetti();
            }
            if (i < 3) {// only three swipes for the screen
                ActionsUtil.Swipe.Left(driver, 0.75, 0.50);
                //TODO : try the screen change buttons in the analytics
                ActionsUtil.sleep(1500);
            }
        }
    }

    private void confetti(){
        System.out.println("checking confetti");
        List<WebElement> CONFETTI = driver.findElements(By.className("android.widget.ImageView"));
        List<WebElement> confetti1 = CONFETTI.stream().filter(element -> element.getDomAttribute("content-desc")==null).collect(Collectors.toList());
        List<WebElement> confetti2 = confetti1.stream().filter(webElement -> Objects.requireNonNull(webElement.getDomAttribute("bounds")).endsWith("482]")).collect(Collectors.toList());
        System.out.println("confetti size "+ confetti2.size());
        //[380,410][452,482] TODO : Try and get a dynamic value for confetti, it is a static value at present
        for (WebElement e: confetti2)
        {
            System.out.println(e.getDomAttribute("bounds"));
            e.click();
            ActionsUtil.sleep(2000);
            driver.navigate().back();
        }
    }
}
