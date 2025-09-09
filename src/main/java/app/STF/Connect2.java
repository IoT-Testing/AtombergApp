package app.STF;

import app.util.ActionsUtil;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.interactions.PointerInput;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.time.Duration;
import java.util.List;

public class Connect2 {
    public String ip;
    static ThreadLocal<String> threadLocalClipboard = new ThreadLocal<>();
    ThreadLocal<ChromeDriver> driver = new ThreadLocal<>();

    public void ipAddress() throws IOException, UnsupportedFlavorException {
        driverSetup();
        WebDriver localDriver = driver.get();
        localDriver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        String URL = "http://192.168.56.1:7100/";
        localDriver.get(URL);
        System.out.println("Website Opened");
        localDriver.manage().window().maximize();
        stfLogin(localDriver);
        remoteDebug(localDriver);
    }

    void driverSetup(){
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--disable-popup-blocking");
        options.addArguments("--remote-allow-origins=*");
        driver.set(new ChromeDriver(options));

    }

    void stfLogin(WebDriver driver){
        ActionsUtil.sleep(1000);
        WebElement username = driver.findElement(By.name("username"));
        username.click();
        username.sendKeys("iot testing");
        ActionsUtil.sleep(1000);
        WebElement email = driver.findElement(By.name("email"));
        email.click();
        email.sendKeys("iot.testing@atomberg.com");
        ActionsUtil.sleep(1000);
        driver.findElement(By.xpath("//input[@value='Log In']")).click();
        ActionsUtil.SSleep(1);
    }

    void remoteDebug(WebDriver driver) throws IOException, UnsupportedFlavorException {
        new STFDeviceSelect(driver);
        List<WebElement> listOfTextBox = driver.findElements(By.tagName("textarea"));
        WebElement ip = listOfTextBox.get(1);

        Actions actions = new Actions(driver);
        actions.moveToElement(ip).click(ip).perform();
        actions.setActivePointer(PointerInput.Kind.MOUSE, "mouse");
        actions.keyDown(Keys.CONTROL).sendKeys("c").keyUp(Keys.CONTROL).perform();

        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        String copiedText = (String) clipboard.getData(DataFlavor.stringFlavor);

        threadLocalClipboard.set(copiedText);

        System.out.println("Copied Text for Thread " + Thread.currentThread().getId() + ": " + copiedText);
    }

    // Getter to use the copied text later in the same thread
    public String getCopiedText() {
        return threadLocalClipboard.get();
    }

    void releaseDevice(){
        driverSetup();

    }

}