package app.STF;

import app.util.ActionsUtil;
import org.openqa.selenium.*;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Point;
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

public class Connect{
    WebDriver driver1 = new ChromeDriver();
    public String copiedText;

    public void ipAddress() throws IOException, UnsupportedFlavorException {
        driver1.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        String URL = "http://192.168.10.32:7100/";
        driver1.get(URL);
        System.out.println("Website Opened");
        driver1.manage().window().maximize();
        stfLogin();
        List<WebElement> elementList = driver1.findElements(By.tagName("li"));
        List<WebElement> devicesList = elementList.stream().filter(webElement -> webElement.getDomAttribute("id")!=null).collect(Collectors.toList());
        int i = (int) (Math.random() * devicesList.size());
        System.out.println("Device number "+ i +" Selected");
        devicesList.get(i).click();
        List<WebElement> listOfTextBox = driver1.findElements(By.tagName("textarea"));
        WebElement ip = listOfTextBox.get(1);
        Point ipLocation = ip.getLocation();
        Dimension ipSize = ip.getSize();
        System.out.println(ipLocation);
        System.out.println(ipSize);
        Actions actions = new Actions(driver1);
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
    void stfLogin(){
        ActionsUtil.sleep(1000);
        WebElement username = driver1.findElement(By.name("username"));
        username.click();
        username.sendKeys("iot testing");
        ActionsUtil.sleep(1000);
        WebElement email = driver1.findElement(By.name("email"));
        email.click();
        email.sendKeys("iot.testing@atomberg.com");
        ActionsUtil.sleep(1000);
        driver1.findElement(By.xpath("//input[@value='Log In']")).click();
        ActionsUtil.SSleep(1);
    }
    void selectDevices(){
        List<WebElement> elementList = driver1.findElements(By.tagName("li"));
        List<WebElement> devicesList = elementList.stream().filter(webElement -> webElement.getDomAttribute("id")!=null).collect(Collectors.toList());
        for(WebElement device:devicesList){
            driver1.switchTo().frame(device);
        }
    }
}


