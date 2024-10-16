package app.Connectivity;

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

        WebElement SLD = null;
        try {
            SLD = driver.findElement(By.xpath("//android.view.View[@content-desc=\"Select and link device\"]"));
        } catch (Exception ignored) {
        }
        if (SLD != null) {
            alexa();
        } else {
            driver.findElement(By.xpath("//android.widget.ImageView[@content-desc=\"More\n" +
                    "Tab 3 of 3\"]")).click();
            alexa();
        }
    }

    private void alexa() {
        WebElement alexaConnect = null;
        try {
            alexaConnect = driver
                    .findElement(By.xpath("//android.widget.ImageView[@content-desc=\"Amazon Alexa\nConnect\"]"));
        } catch (Exception ignored) {
        }
        if (alexaConnect != null) {
            alexaConnect.click();
        } else {
            System.out.println("Alexa is already connected");
        }
        //check alexa linking guide
        ALG();
        linkCheck();
    }

    private void ALG() {

        driver.findElement(By.xpath("//android.widget.Button[@content-desc=\"Link\"]")).click();

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
        WebElement CHECK = null;
        try {
            CHECK = driver.findElement(By.xpath("//android.widget.TextView[@text=\"Sign in with your email and password\"]"));
        }catch(Exception ignored){}
        if(CHECK != null)
        {
            WebElement Email = driver.findElement(By.xpath("//android.widget.EditText[@resource-id=\"signInFormUsername\"]"));
            Email.click();
            Email.sendKeys("Weker42331@huleos.com");
            WebElement Password = driver.findElement(By.xpath("//android.widget.EditText[@resource-id=\"signInFormPassword\"]"));
            Password.click();
            Password.sendKeys("Atomberg@123");
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
        for(WebElement e:successM)
            if(Objects.equals(e.getAttribute("content-desc"), "Alexa Linked Successfully")){
                System.out.println("Alexa Linked Successfully");
                ActionsUtil.Tap.withPercentage(driver, 0.20,0.20);
            }
    }

}
