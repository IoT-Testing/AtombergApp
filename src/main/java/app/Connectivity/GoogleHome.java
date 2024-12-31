package app.Connectivity;

import app.ScreenCheck.ScreenCheck;
import app.ScreenCheckCallbackAction;
import app.util.ActionsUtil;
import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.time.Duration;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/*
*  THIS IS TO CONNECT ATOMBERG HOME APP TO GOOGLE HOME
*  IT HAS SKILLS TO CONNECT WITH THE SMART DEVICES
*  "WORK WITH GOOGLE" OPTION WITHIN GOOGLE HOME
*  SEARCH FOR ATOMBERG HOME
*  LOGIN IN WITH ATOMBERG ACCOUNT
*/

public class GoogleHome {
    public AndroidDriver driver;
    public GoogleHome(AndroidDriver driver){
        this.driver = driver;
    }

    public void Connect() {
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        ScreenCheck screen = new ScreenCheck(driver);
        screen.moreTab();
        googleHome();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
    }

    public void Disconnect(){
        ScreenCheck screen = new ScreenCheck(driver);
        screen.moreTab();
        WebElement googleConnected = null;
        try {
            googleConnected = driver.findElement(By.xpath("//android.widget.ImageView[@content-desc=\"Google\nConnected\"]"));
        } catch (Exception ignored) {}
        if (googleConnected!=null)
        {
            googleConnected.click();
            ActionsUtil.sleep(2500);
            googleHomeControl();
        }
        screen.homeScreen();
        ActionsUtil.sleep(2500);
        ActionsUtil.refresh(driver);
        screen.moreTab();
        WebElement googleIsDisconnected = null;
        try {
            googleIsDisconnected = driver.findElement(By.xpath("//android.widget.ImageView[@content-desc=\"Google\nConnect\"]"));
        }catch (Exception ignored){}
        if(googleIsDisconnected!= null)System.out.println("Google Home Unlinked Successfully");
        else {
            screen.homeScreen();
            ActionsUtil.sleep(2500);
            ActionsUtil.refresh(driver);
            screen.moreTab();
            googleIsDisconnected = driver.findElement(By.xpath("//android.widget.ImageView[@content-desc=\"Google\nConnect\"]"));
            if(googleIsDisconnected == null){
                System.out.println("Google unlink Error");
            }
        }
    }

    private void googleHomeControl(){
        ActionsUtil.sleep(2500);
        List<WebElement> Services = driver.findElements(By.className("android.widget.TextView"));
        List<WebElement> services = Services.stream().filter(Object-> Object.getDomAttribute("text")!=null).collect(Collectors.toList());
        List<WebElement> atomberg = services.stream().filter(Object-> Objects.equals(Object.getDomAttribute("text"), "Atomberg Home")).collect(Collectors.toList());
        System.out.println(atomberg.size());
        if(!atomberg.isEmpty()){
            atomberg.get(0).click();
            driver.findElement(By.xpath("//android.widget.TextView[@text=\"Unlink account\"]")).click();
            driver.findElement(By.xpath("//android.widget.Button[@text=\"UNLINK\"]")).click();
            ActionsUtil.sleep(10000);
            driver.navigate().back();
            driver.navigate().back();
        }
    }

    private void unlinkCheck(){
        //TODO write the code to check the Unlink Successful popup is displayed or not
    }

    private void googleHome() {
        WebElement googleConnect = null;
        try{
            googleConnect = driver.findElement(By.xpath("//android.widget.ImageView[@content-desc=\"Google\nConnect\"]"));
        }catch (Exception ignored) {
        }
        if (googleConnect != null) {
            System.out.println("Connecting Google Home");
            googleConnect.click();
            accountLinkingGuide();
        } else {
            System.out.println("Google Home is already connected");
        }
    }

    private void accountLinkingGuide() {//Account Linking Guide
        List<WebElement> Elements = driver.findElements(By.className("android.view.View"));
        List<WebElement> elements = Elements.stream().filter(element -> element.getDomAttribute("content-desc")!=null).collect(Collectors.toList());
        System.out.println(elements.size());
        List<WebElement> accountLinkingGuideList = elements.stream().filter(element -> Objects.equals(element.getDomAttribute("content-desc"), "Account linking guide")).collect(Collectors.toList());
        if (!accountLinkingGuideList.isEmpty())
        {
            WebElement OK = driver.findElement(By.xpath("//android.widget.Button[@content-desc=\"OK\"]"));
            OK.click();
            ActionsUtil.sleep(1000);
            System.out.println("OK");
            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
            checkContinue();
            loginScreenCheck();// check if account is present
        }
    }

    private void loginScreenCheck(){
        WebElement signInFormUsername = null;
        try {
            signInFormUsername = driver.findElement(By.xpath("//android.widget.EditText[@resource-id=\"signInFormUsername\"]"));
        }catch (Exception ignored){}
        if (signInFormUsername!=null){
            signInFormUsername.click();
            signInFormUsername.sendKeys("Teboham827@agaseo.com");
            WebElement signInFormPassword = driver.findElement(By.xpath("//android.widget.EditText[@resource-id=\"signInFormPassword\"]"));
            signInFormPassword.click();
            signInFormPassword.sendKeys("Atomberg@123");
            ActionsUtil.sleep(1000);
            driver.findElement(By.xpath("//android.widget.Button[@text=\"submit\"]")).click();
        }
        else {
            List<WebElement> previousLoginCheckList =driver.findElements(By.className("android.widget.Button"));
            System.out.println(previousLoginCheckList.size());
            for(WebElement email : previousLoginCheckList){
                if(Objects.requireNonNull(email.getDomAttribute("text")).startsWith("Sign In as")){
                    email.click();
                }
            }
        }
        System.out.println("Account entered");
        ActionsUtil.sleep(8000);
        WebElement navigate = driver.findElement(By.xpath("//android.widget.ImageButton[@content-desc=\"Navigate up\"]"));
        navigate.click();
        WebElement createHome = null;
        try {
            createHome = driver.findElement(By.xpath("//android.widget.Button[@content-desc=\"Create home\"]"));
        }catch (Exception ignored){}
            if (createHome!=null) {back();
            ScreenCheck screen = new ScreenCheck(driver);
            screen.homeScreen();
            ActionsUtil.sleep(2500);
            ActionsUtil.refresh(driver);
        }
    }

    private void back(){
        WebElement SLD =null;
        while(SLD == null)
        {
            driver.navigate().back();
            try {
                SLD = driver.findElement(By.xpath("//android.view.View[@content-desc=\"Select and link device\"]"));
            } catch (Exception ignored) {}
        }
    }

    private void checkContinue(){
        WebElement continueBt = null;
        try {
            continueBt = driver.findElement(By.xpath("//android.widget.Button[@text=\"Continue\"]"));
        }catch (Exception ignored){}
        if(continueBt != null)
        {
            continueBt.click();
        }
        else {
            ActionsUtil.Tap.withCoordinates(driver, 890, 1290);
        }
    }
}
