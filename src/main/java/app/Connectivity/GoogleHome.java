package app.Connectivity;

import app.util.ActionsUtil;
import io.appium.java_client.AppiumDriver;
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
    public AppiumDriver driver;
    public GoogleHome(AppiumDriver driver){
        this.driver = driver;
    }

    public void Connect() {
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        WebElement SLD = null;
        try {
            SLD = driver.findElement(By.xpath("//android.view.View[@content-desc=\"Select and link device\"]"));
        } catch (Exception ignored) {
        }
        if (SLD != null) {
            googleHome();			//Google Home
        } else {
            driver.findElement(By.xpath("//android.widget.ImageView[@content-desc=\"More\n" +
                    "Tab 3 of 3\"]")).click();
            googleHome();
        }
        ALG();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
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
        } else {
            System.out.println("Google Home is already connected");
        }
    }

    private void ALG() {//Account Linking Guide
        List<WebElement> Elements = driver.findElements(By.className("android.view.View"));
        List<WebElement> elements = Elements.stream().filter(element -> element.getAttribute("content-desc")!=null).collect(Collectors.toList());
        System.out.println(elements.size());
        List<WebElement> algL = elements.stream().filter(element -> Objects.equals(element.getAttribute("content-desc"), "Account linking guide")).collect(Collectors.toList());

        if (!algL.isEmpty())
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
            List<WebElement> previousEmailCheck = previousLoginCheckList.stream().filter(Object -> Object.getAttribute("content-desc")!= null).collect(Collectors.toList());
            List<WebElement> availableEmail = previousEmailCheck.stream().filter(Object -> Objects.requireNonNull(Object.getAttribute("content-desc")).startsWith("Sign In as")).collect(Collectors.toList());
            if (!availableEmail.isEmpty()){
                availableEmail.get(0).click();
            }
        }
        System.out.println("Account entered");
        ActionsUtil.sleep(5000);
        WebElement done = driver.findElement(By.xpath("//android.widget.Button[@text=\"Done\"]"));
        done.click();
        back();
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
/*
    private void gCheck(){
        List<WebElement> CHECK =driver.findElements(By.className("android.widget.Button"));
        System.out.println("Check");
        System.out.println(CHECK.size());
        if(!CHECK.isEmpty())
        {
            WebElement signIn = driver.findElement(By.xpath("//android.widget.Button[@text=\"Sign In as Weker42331@huleos.com\"]"));
            signIn.click();
        }
        else{
            WebElement Email = driver.findElement(By.xpath("//android.widget.EditText[@resource-id=\"signInFormUsername\"]"));
            Email.click();
            Email.sendKeys("Weker42331@huleos.com");
            WebElement Password = driver.findElement(By.xpath("//android.widget.EditText[@resource-id=\"signInFormPassword\"]"));
            Password.click();
            Password.sendKeys("Atomberg@123");
            ActionsUtil.sleep(1000);
            driver.findElement(By.xpath("//android.widget.Button[@text=\"submit\"]")).click();
        }
        System.out.println("Account entered");
        ActionsUtil.sleep(5000);
        WebElement done = driver.findElement(By.xpath("//android.widget.Button[@text=\"Done\"]"));
        done.click();
        back();
    }
*/


}
