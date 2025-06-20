package app.STF;

import app.util.ActionsUtil;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.*;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;

public class Connect {
    WebDriver driver = null;
    public String copiedText;
    public String ip;
    public void ipAddress() throws IOException, UnsupportedFlavorException {
        driver = new ChromeDriver();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        String URL = "http://192.168.82.202:7100/";
        driver.get(URL);
        System.out.println("Website Opened");
        driver.manage().window().maximize();
        stfLogin();
        remoteDebug();
    }

    void stfLogin(){
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

    void remoteDebug() throws IOException, UnsupportedFlavorException {
        new STFDeviceSelect(driver);
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