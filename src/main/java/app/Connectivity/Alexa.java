package app.Connectivity;

import app.ScreenCheck.ScreenCheck;
import app.util.ActionsUtil;
import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/*
 *  THIS IS TO CONNECT ATOMBERG HOME APP TO AMAZON ALEXA
 *  IT HAS SKILLS TO CONNECT WITH THE SMART DEVICES
 *  "SKILLS & GAMES" OPTION WITHIN ALEXA APP
 *  SEARCH FOR ATOMBERG HOME
 *  LOGIN IN WITH ATOMBERG ACCOUNT
 */

public class Alexa {
    public AppiumDriver driver;
    public Alexa(AppiumDriver driver){
        this.driver = driver;
    }
    public void Connect() {
        ScreenCheck screen = new ScreenCheck(driver);
        screen.moreTab();
        alexa();
    }

    public void Disconnect(){
        WebElement selectAndLinkDevice = null;
        try {
            selectAndLinkDevice = driver.findElement(By.xpath("//android.view.View[@content-desc=\"Select and link device\"]"));
        } catch (Exception ignored) {
        }
        if (selectAndLinkDevice!=null){
            WebElement alexaConnected = null;
            try {
                alexaConnected = driver.findElement(By.xpath("//android.widget.ImageView[@content-desc=\"Amazon Alexa\nConnected\"]"));
            } catch (Exception ignored) {}
            if (alexaConnected!=null)
            {
                alexaConnected.click();
                driver.findElement(By.xpath("//android.widget.Button[@content-desc=\"Yes\"]")).click();
            }
            unlinkCheck();
        }
    }

    private void alexa() {
        WebElement alexaConnect = null;
        try {
            alexaConnect = driver.findElement(By.xpath("//android.widget.ImageView[@content-desc=\"Amazon Alexa\nConnect\"]"));
        } catch (Exception ignored) {
        }
        if (alexaConnect != null) {
            alexaConnect.click();
            System.out.println("Alexa Connect ");
            pairAlexaCheck();
            accountLinkingGuide(); //check alexa linking guide
            linkCheck();
        } else {
            System.out.println("Alexa is already connected");
        }
    }

    private void pairAlexaCheck(){
        WebElement pairAlexa = null;
        try {
            pairAlexa = driver.findElement(By.xpath("//android.view.View[@content-desc=\"Pair Alexa\"]"));
        }catch (Exception ignored){}
        if (pairAlexa!= null) driver.findElement(By.xpath("//android.widget.Button[@content-desc=\"Link\"]")).click();
    }

    private void accountLinkingGuide() {

        List<WebElement> Elements = driver.findElements(By.className("android.view.View"));
        List<WebElement> elements = Elements.stream().filter(element -> element.getAttribute("content-desc")!=null).collect(Collectors.toList());
        List<WebElement> algL = elements.stream().filter(element -> Objects.equals(element.getAttribute("content-desc"), "Account linking guide")).collect(Collectors.toList());
        System.out.println(algL.size());
        if (!algL.isEmpty())
        {
            driver.findElement(By.xpath("//android.widget.Button[@content-desc=\"OK\"]")).click();
            ActionsUtil.sleep(5000);
            driver.findElement(By.xpath("//android.widget.TextView[@text=\"LINK\"]")).click();
            ActionsUtil.sleep(5000);
            alexaCheck();
        }
    }

    private void alexaCheck(){
        WebElement loginScreenCheck = null;
        try {
            loginScreenCheck = driver.findElement(By.xpath("//android.webkit.WebView[@text=\"Signin\"]"));
        }catch(Exception ignored){}
        if(loginScreenCheck != null)
        {
            WebElement signInFormUsername = driver.findElement(By.xpath("//android.widget.EditText[@resource-id=\"signInFormUsername\"]"));
            signInFormUsername.click();
            signInFormUsername.sendKeys("teboham827@agaseo.com");
            WebElement signInFormPassword = driver.findElement(By.xpath("//android.widget.EditText[@resource-id=\"signInFormPassword\"]"));
            signInFormPassword.click();
            signInFormPassword.sendKeys("Atomberg@123");
            ActionsUtil.sleep(1000);
            driver.findElement(By.xpath("//android.widget.Button[@text=\"submit\"]")).click();
        }
    }

    private void back(){
        WebElement SLD =null;
        while(SLD == null)
        {
            driver.navigate().back();
            try {
                SLD=driver.findElement(By.xpath("//android.view.View[@content-desc=\"Select and link device\"]"));
            }catch (Exception ignored)
            {}
        }
    }

    private void linkCheck(){
        List<WebElement> Success = driver.findElements(By.className("android.view.View"));
        List<WebElement> successM = Success.stream().filter(element -> element.getAttribute("content-desc")!=null).collect(Collectors.toList());
        for(WebElement e:successM) {
            if (Objects.equals(e.getAttribute("content-desc"), "Alexa Linked Successfully")) {
                System.out.println("Alexa Linked Successfully");
                ActionsUtil.Tap.withPercentage(driver, 0.20, 0.20);
            }
        }
    }

    private void unlinkCheck(){
            WebElement unlink = null;
            try {
                unlink = driver.findElement(By.xpath("//android.view.View[@content-desc=\"Unlink Successful\"]"));
            }catch (Exception ignored){}
            if (unlink!= null) System.out.println("Alexa Unlink Successful");
    }
}
