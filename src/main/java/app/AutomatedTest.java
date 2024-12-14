package app;

import app.ScreenCheck.ScreenCheck;
import app.util.ActionsUtil;
import app.util.AppUtil;
import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class AutomatedTest {

    public AppiumDriver atomberg;

    public void run() throws IOException, UnsupportedFlavorException {
        AppInitializer appInitializer = new AppInitializer();
        appInitializer.openApp();
        appInitializer.checkMainScreen();
        atomberg = appInitializer.getDriver();
        ActionsUtil.SSleep(3);
        List<WebElement> elementList = atomberg.findElements(By.className("android.widget.ImageView"));
        System.out.println(elementList.size());
        List<WebElement> tabs = elementList.stream().filter(webElement -> webElement.getDomAttribute("content-desc")!=null).collect(Collectors.toList());
        System.out.println(tabs.size());
        List<WebElement> home = tabs.stream().filter(webElement -> Objects.requireNonNull(webElement.getDomAttribute("content-desc")).startsWith("Hi")).collect(Collectors.toList());
        System.out.println(home.size());
    }
}