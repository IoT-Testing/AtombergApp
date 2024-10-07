package app;

import app.Fan.FanManagement;
import app.util.ActionsUtil;
import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;

public class SwitchFamily {
    private AppiumDriver driver;
    private int i;
    private int total;
    String fam1;
    public SwitchFamily(AppiumDriver driver) {
        this.driver = driver;
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(6));
    }
    void switchFamily() {
            List<WebElement> elements = driver.findElements(By.className("android.view.View"));
            WebElement e = elements.get(0);
            String fam1 = e.getAttribute("content-desc");
            System.out.println(e.getAttribute("content-desc"));
            e.click();
            // tap on the Family name on the screen (top right corner)
            // getting the available family list
            List<WebElement> rawFamilies = driver.findElements(By.className("android.view.View"));
            List<WebElement> FAMILIES = rawFamilies.stream().filter(fam -> fam.getAttribute("content-desc") != null).collect(Collectors.toList());
            FAMILIES.remove(FAMILIES.size() - 1);
            FAMILIES.remove(FAMILIES.size() - 1);
            total = FAMILIES.size();
            System.out.println("number of FAMILIES present =" + total);
            for (i = 0; i < total; i++) {
                List<WebElement> rawFamily = driver.findElements(By.className("android.view.View"));
                List<WebElement> families = rawFamily.stream().filter(fam -> fam.getAttribute("content-desc") != null).collect(Collectors.toList());
                System.out.println("number of families present =" + total);
                families.remove(families.size() - 1);
                families.remove(families.size() - 1);
                System.out.println("number of families1 present =" + total);
                if (families.get(i).getAttribute("content-desc").equals(fam1)) {
                    i++;
                }
                System.out.println(families.get(i).getAttribute("content-desc") + " is clicked");
                families.get(i).click();
                ActionsUtil.sleep(1500);
                if (i < total - 1) {
                    elements = driver.findElements(By.className("android.view.View"));
                    e = elements.get(0);
                    fam1 = e.getAttribute("content-desc");
                    e.click();
                }
            }
        }
}
