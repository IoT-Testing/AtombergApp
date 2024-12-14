package app;

import app.util.ActionsUtil;
import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import java.time.Duration;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class SwitchFamily {
    private AppiumDriver atomberg;
    private int i;
    private int total;
    String fam1;
    SwitchFamilyIntermediateCallback callback;

    public SwitchFamily(AppiumDriver driver, SwitchFamilyIntermediateCallback callback) {
        this.atomberg = driver;
        atomberg.manage().timeouts().implicitlyWait(Duration.ofSeconds(6));
        this.callback = callback;
    }

    void switchFamily() {
        List<WebElement> elements = atomberg.findElements(By.className("android.view.View"));
        WebElement e = elements.get(0);
        String fam1 = e.getDomAttribute("content-desc");
        System.out.println(e.getDomAttribute("content-desc"));
        e.click();
        // tap on the Family name on the screen (top right corner)
        // getting the available family list
        ActionsUtil.sleep(2500);
        List<WebElement> rawFamilies = atomberg.findElements(By.className("android.view.View"));
        List<WebElement> FAMILIES = rawFamilies.stream().filter(fam -> fam.getDomAttribute("content-desc") != null).collect(Collectors.toList());
        System.out.println(FAMILIES.size());
        FAMILIES.remove(FAMILIES.size() - 1);
        FAMILIES.remove(FAMILIES.size() - 1);
        total = FAMILIES.size();
        System.out.println("number of FAMILIES present =" + total);
        for (i = 0; i < total; i++) {
            ActionsUtil.sleep(2000);
            List<WebElement> rawFamily = atomberg.findElements(By.className("android.view.View"));
            System.out.println(rawFamily.size());
            List<WebElement> families = rawFamily.stream().filter(fam -> fam.getDomAttribute("content-desc") != null).collect(Collectors.toList());
            System.out.println("Number of families present =" + families.size());
            System.out.println(families.get(families.size()-1).getDomAttribute("content-desc"));
            families.remove(families.size() - 1);
            System.out.println(families.get(families.size()-1).getDomAttribute("content-desc"));
            families.remove(families.size() - 1);
            System.out.println("number of families present =" + families.size());
            if (Objects.equals(families.get(i).getDomAttribute("content-desc"), fam1)) {
                atomberg.navigate().back();
            } else {
                System.out.println(families.get(i).getDomAttribute("content-desc") + " is clicked");
                families.get(i).click();
            }
            // put device control or analytics at this location.
            callback.intermediateFunction();
            if (i < total - 1) {
                elements = atomberg.findElements(By.className("android.view.View"));
                e = elements.get(0);
                fam1 = e.getDomAttribute("content-desc");
                e.click();
            }
        }
    }

}