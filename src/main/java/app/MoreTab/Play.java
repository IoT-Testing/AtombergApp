package app.MoreTab;

import app.util.ActionsUtil;
import app.util.AppUtil;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import io.appium.java_client.android.AndroidDriver;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class Play {
    public AndroidDriver driver;

    public Play(AndroidDriver driver){
        this.driver = driver;
    }
    public void videos() {
        helpCheck();
        WebElement AppTour = driver.findElement(By.xpath("//android.widget.ImageView[@content-desc=\"App Tour\"]"));
        AppTour.click();
        AppUtil.captureScreenshot(driver);
        System.out.println("App Video Opened");
        videoTryCatch();
        videoTryCatch();
        ActionsUtil.sleep(5000);

        WebElement ConnectAlexa = driver.findElement(By.xpath("//android.widget.ImageView[@content-desc=\"Connect Alexa\"]"));
        ConnectAlexa.click();

        AppUtil.captureScreenshot(driver);
        System.out.println("Alexa Video Opened");
        videoTryCatch();
        videoTryCatch();
        videoTryCatch();
        ActionsUtil.sleep(5000);

        WebElement ConnectGoogle = driver
                .findElement(By.xpath("//android.widget.ImageView[@content-desc=\"Connect Google\"]"));
        ConnectGoogle.click();
        AppUtil.captureScreenshot(driver);
        System.out.println("Google Home Video Opened");
        videoTryCatch();
        videoTryCatch();
        ActionsUtil.sleep(5000);

        WebElement SLAppSetup = null;
        try {
            SLAppSetup = driver
                    .findElement(By.xpath("//android.widget.ImageView[@content-desc=\"Smart Locks App Setup\"]"));
        } catch (Exception ignored) {
        }
        if (SLAppSetup == null) {
            ActionsUtil.Swipe.Left(driver, 0.80, 0.50);// Tab 0.35 narzo 0.50

            WebElement SLInstall = driver
                    .findElement(By.xpath("//android.widget.ImageView[@content-desc=\"Smart Locks Installation\"]"));
            SLInstall.click();
            ActionsUtil.sleep(2000);
        } else {
            WebElement SLInstall = driver
                    .findElement(By.xpath("//android.widget.ImageView[@content-desc=\"Smart Locks Installation\"]"));
            SLInstall.click();
        }

        AppUtil.captureScreenshot(driver);
        System.out.println("SL installation Video Opened");

        videoTryCatch();
        videoTryCatch();
        ActionsUtil.sleep(5000);

        SLAppSetup = null;
        try {
            SLAppSetup = driver
                    .findElement(By.xpath("//android.widget.ImageView[@content-desc=\"Smart Locks App Setup\"]"));
        } catch (Exception ignored) {
        }
        if (SLAppSetup == null) {
            ActionsUtil.Swipe.Left(driver, 0.80, 0.50);// Tab 0.35 narzo 0.50
            ActionsUtil.sleep(2000);
            SLAppSetup = driver
                    .findElement(By.xpath("//android.widget.ImageView[@content-desc=\"Smart Locks App Setup\"]"));
        }
        WebElement SLFeatures = driver
                .findElement(By.xpath("//android.widget.ImageView[@content-desc=\"Smart Locks Features\"]"));
        SLFeatures.click();
        ActionsUtil.sleep(2000);
        AppUtil.captureScreenshot(driver);
        System.out.println("SL Feature Video Opened");
        videoTryCatch();
        videoTryCatch();
        ActionsUtil.sleep(5000);
        SLAppSetup.click();
        ActionsUtil.sleep(2000);
        AppUtil.captureScreenshot(driver);
        System.out.println("App Setup for Lock Video Opened");
        videoTryCatch();
        videoTryCatch();
    }

    private void videoTryCatch() {
        WebElement VideoTutorials =null;
        for(int i =0 ; i < 5 ; i++){
             while(VideoTutorials == null)
             {
                try {
                    VideoTutorials = driver.findElement(By.xpath("//android.view.View[@content-desc=\"Video tutorials\"]"));
                }catch (Exception ignored) {}
                if (VideoTutorials == null) {
                    System.out.println("Back");
                    driver.navigate().back(); // 180, 1550 860, 1960
                }
            }
        }
    }
    
    private void helpCheck(){
        WebElement videoTutorials = null;
        try {
            videoTutorials = driver.findElement(By.xpath("//android.view.View[@content-desc=\"Video tutorials\"]"));
        }catch (Exception ignored){}
        if(videoTutorials==null){
            List<WebElement> elementList = driver.findElements(By.className("android.view.View"));
            List<WebElement> webElementList = elementList.stream().filter(Object-> Object.getDomAttribute("content-desc")!=null).collect(Collectors.toList());
            for(WebElement webElement: webElementList){
                 if(Objects.equals(webElement.getDomAttribute("content-desc"), "Select and link device")){
                     ActionsUtil.Scroll.Up(driver);
                     WebElement help = driver.findElement(By.xpath("//android.view.View[@content-desc=\"Help\"]"));
                     help.click();
                     break;
                 } else if (Objects.equals(webElement.getDomAttribute("content-desc"), "Options")) {
                     WebElement help = driver.findElement(By.xpath("//android.view.View[@content-desc=\"Help\"]"));
                     help.click();
                     break;
                 }
                 else if (Objects.equals(webElement.getDomAttribute("content-desc"), "Help")){
                     webElement.click();
                     break;
                 }

            }
        }
    }
}
