package app.MoreTab;

import app.ScreenCheck.ScreenCheck;
import app.util.ActionsUtil;
import app.util.AppUtil;
import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class Profile {
    public AndroidDriver atomberg;

    public Profile(AndroidDriver driver) {
        this.atomberg = driver;
    }

    public void edit(){
        ScreenCheck screenCheck = new ScreenCheck(atomberg);
        screenCheck.moreTab();
        List<WebElement> ELEMENTS = atomberg.findElements(By.className("android.widget.ImageView"));
        List<WebElement> elements = ELEMENTS.stream().filter(element -> element.getDomAttribute("content-desc") != null).collect(Collectors.toList());
        System.out.println(elements.size());
        List<WebElement> ele = elements.stream().filter(element -> Objects.requireNonNull(element.getDomAttribute("content-desc")).startsWith("Hi,")).collect(Collectors.toList());
        System.out.println(ele.size());
        for (WebElement e : ele) {
            if (Objects.requireNonNull(e.getDomAttribute("content-desc")).startsWith("Hi,")) {
                assert e.isDisplayed();
                e.click();
                System.out.println("Edit Profile");
                break;
            }
        }
        ActionsUtil.sleep(3000);
        WebElement ChangeAvatar = atomberg.findElement(By.xpath("//android.widget.ImageView[@content-desc=\"Change avatar\"]"));
        ChangeAvatar.click();
        AppUtil.captureScreenshot(atomberg);
        System.out.println("Tap On Change Avatar");
        List<WebElement> elementList = atomberg.findElements(By.className("android.widget.ImageView"));
        for (WebElement avatar: elementList){
            assert avatar.isDisplayed();
            avatar.click();
            AppUtil.captureScreenshot(atomberg);
        }
        atomberg.navigate().back();
        WebElement editName = atomberg.findElement(By.xpath("//android.widget.EditText[@index=\"1\"]"));
        editName.click();
        editName.clear();
        editName.sendKeys("Hi Hi Hi");

        WebElement EditNumber = atomberg.findElement(By.xpath("//android.view.View[@index=\"2\"]"));
        EditNumber.click();
        AppUtil.captureScreenshot(atomberg);
        atomberg.findElement(By.xpath("//android.widget.Button[@content-desc=\"Update\"]")).click();
        ActionsUtil.SSleep(2);
        back(atomberg);
    }

    private void back(AndroidDriver driver){
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
