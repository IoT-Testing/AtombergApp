package app.MoreTab;

import app.util.ActionsUtil;
import app.util.AppUtil;
import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.List;
import java.util.stream.Collectors;

public class Profile {
    public AppiumDriver driver;

    public void editProfile(AppiumDriver driver){
        List<WebElement> ELEMENTS = driver.findElements(By.className("android.widget.ImageView"));
        List<WebElement> elements = ELEMENTS.stream().filter(element -> element.getAttribute("content-desc") != null).collect(Collectors.toList());
        System.out.println(elements.size());
        List<WebElement> ele = elements.stream().filter(element -> element.getAttribute("content-desc").startsWith("Hi,")).collect(Collectors.toList());
        System.out.println(elements.size());
        for (WebElement e : elements) {
            if (e.getAttribute("content-desc").startsWith("Hi,")) {
                e.click();
                System.out.println("Edit Profile");
                break;
            }
        }
        ActionsUtil.sleep(3000);

        WebElement ChangeAvatar = driver.findElement(By.xpath("//android.widget.ImageView[@content-desc=\"Change avatar\"]"));
        ChangeAvatar.click();
        AppUtil.captureScreenshot(driver);
        System.out.println("Tap On Change Avatar");
        for (int i = 1; i < 25; i++) {
            WebElement Avatar1 = driver.findElement(By.xpath("//android.widget.ScrollView/android.view.View[2]/android.view.View/android.view.View/android.widget.ImageView[" + i + "]"));
            Avatar1.click();
            AppUtil.captureScreenshot(driver);
        }
        driver.navigate().back();
        WebElement editName = driver.findElement(By.xpath("//android.widget.EditText[@index=\"1\"]"));
        editName.click();
        editName.clear();
        editName.sendKeys("Hi Hi Hi");

        WebElement EditNumber = driver.findElement(By.xpath("//android.view.View[@content-desc=\"+91\"]"));
        EditNumber.click();
        AppUtil.captureScreenshot(driver);
        driver.findElement(By.xpath("//android.widget.Button[@content-desc=\"Update\"]")).click();
        ActionsUtil.sleep(250);
        WebElement SLD = null;
        back(driver);
    }

    private void back(AppiumDriver driver){
        WebElement SLD =null;
        while(SLD == null)
        {
            driver.navigate().back();
            try {
                SLD=driver.findElement(By.xpath("//android.view.View[@content-desc=\"Select and link device\"]"));
            }catch (Exception ignored) {}
        }
    }

}
