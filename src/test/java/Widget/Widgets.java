package Widget;

import Actions.Swipe;
import AtombergTest.Method;
import AtombergTest.Monkey;
import io.appium.java_client.AppiumDriver;
import org.awaitility.Awaitility;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class Widgets {

    public static void Home(AppiumDriver atomberg) {
        WebElement widget = null;
        try {
            widget = atomberg.findElement(By.className("android.widget.RelativeLayout"));
        } catch (Exception e) {
        }
        assert widget != null;
        if (widget.equals(null)) {
            Swipe.Right(atomberg, 0.50, 0.80);
        }

        WebElement spdBtUp= atomberg.findElement(By.id("com.atomberg.app:id/bt_up"));
        WebElement spdBtDown = atomberg.findElement(By.id("com.atomberg.app:id/bt_down"));
        WebElement prevFan = atomberg.findElement(By.id("com.atomberg.app:id/bt_prev"));
        WebElement nextFan = atomberg.findElement(By.id("com.atomberg.app:id/bt_next"));
        WebElement powerBt = atomberg.findElement(By.id("com.atomberg.app:id/bt_power"));

        prevFan.click();
        sleep(1000);
        spdBtUp.click();
        sleep(1000);
        powerBt.click();
        sleep(1000);
        spdBtDown.click();
        sleep(1000);
        nextFan.click();

/*        List<WebElement> widgetButtons = atomberg.findElements(By.className("android.widget.ImageButton"));
        System.out.println(widgetButtons.size());
        for(int i=0; i<widgetButtons.size(); i++)
        {
            Random random = new Random();
            int index = random.nextInt(widgetButtons.size());

            widgetButtons.get(index).click();
        }*/
    }

    public static void sleep(long millis) {
        Awaitility.await().atMost(millis, TimeUnit.MILLISECONDS);
    }
}