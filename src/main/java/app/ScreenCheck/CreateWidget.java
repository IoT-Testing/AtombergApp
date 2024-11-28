package app.ScreenCheck;

import app.util.ActionsUtil;
import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.List;
import java.util.stream.Collectors;

public class CreateWidget {
    public AppiumDriver driver;

    public CreateWidget(AppiumDriver driver){
        this.driver = driver;
    }
    // Create Atomberg Widget
    public void createWidget(){
        ActionsUtil.Tap.withCoordinates(driver, 540, 2350);
        ActionsUtil.sleep(1000);
        ActionsUtil.longPress(driver, 500, 1100);
        driver.findElement(By.xpath("(//android.widget.ImageView[@resource-id=\"com.android.launcher:id/item_icon\"])[3]")).click();
        WebElement atomberg = null;
        while (atomberg==null){
            ActionsUtil.Scroll.Up(driver);
            try {
                atomberg = driver.findElement(By.xpath("//android.widget.TextView[@content-desc=\"Atomberg Home widget\"]"));
            } catch (Exception ignored) {}
        }
        atomberg.click();
        ActionsUtil.sleep(1500);
        driver.findElement(By.xpath("//android.widget.Button[@resource-id=\"com.android.launcher:id/exit\"]")).click();
    }

    public void createWidget2(){
        ActionsUtil.Tap.withCoordinates(driver, 540, 2350);
        ActionsUtil.sleep(1000);
        ActionsUtil.longPress(driver, 500, 1100);
        driver.findElement(By.xpath("(//android.widget.ImageView[@resource-id=\"com.android.launcher:id/item_icon\"])[3]")).click();
        WebElement atomberg = null;
        while (atomberg==null){
            List<WebElement> elementList = driver.findElements(By.className("android.widget.TextView"));
            System.out.println(elementList.size());
            List<WebElement> widgetsList = elementList.stream().filter(webElement -> webElement.getAttribute("content-desc")!=null).collect(Collectors.toList());
            System.out.println(widgetsList.size());
            for (WebElement widget : widgetsList) {
//                System.out.println(element.getAttribute("text"));
                if (!widget.getAttribute("content-desc").equals("Atomberg Home widget")) {
                    ActionsUtil.Scroll.Up(driver);
                } else if (widget.getAttribute("content-desc").equals("Atomberg Home widget")) {
                    widget.click();
                }
                break;
            }
            try {
                atomberg = driver.findElement(By.xpath("//android.widget.Button[@resource-id=\"com.android.launcher:id/exit\"]"));
            }catch (Exception ignored){}
        }
        atomberg.click();
    }
}
