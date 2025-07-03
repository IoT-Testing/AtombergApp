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
import java.util.UUID;

public class Connect2 {
    public String copiedText;
    public String ip;
    ThreadLocal<ChromeDriver> driver = new ThreadLocal<>();

    public void ipAddress() throws IOException, UnsupportedFlavorException {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--disable-popup-blocking");
        options.addArguments("--remote-allow-origins=*");
        driver.set(new ChromeDriver(options));
        WebDriver localDriver = driver.get();

        localDriver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        String URL = "http://192.168.11.88:7100/";
        localDriver.get(URL);
        System.out.println("Website Opened");
        localDriver.manage().window().maximize();
        stfLogin(localDriver);
        DeviceManager2.init(localDriver);
        remoteDebug(localDriver);
    }
    private String getUniqueUserDataDir() {
        return System.getProperty("java.io.tmpdir") + "/profile-" + UUID.randomUUID();
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
        STFDeviceSelect deviceSelect = new STFDeviceSelect(driver);
        String deviceId = deviceSelect.selectedDeviceId;
// After test finishes
        STFDeviceSelect.releaseDevice(deviceId);
        List<WebElement> listOfTextBox = driver.findElements(By.tagName("textarea"));
        WebElement ip = listOfTextBox.get(1);
        Actions actions = new Actions(driver);
        actions.moveToElement(ip);
        actions.click(ip);
        actions.perform();
        actions.setActivePointer(PointerInput.Kind.MOUSE, "mouse");
        actions.keyDown(Keys.CONTROL).sendKeys("c").keyUp(Keys.CONTROL).perform();
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        // Retrieve the copied text
        copiedText = (String) clipboard.getData(DataFlavor.stringFlavor);
        System.out.println(copiedText);
    }
}