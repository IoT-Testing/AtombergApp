package app.WaterPurifier;

import app.ScreenCheck.ScreenCheck;
import app.util.ActionsUtil;
import app.util.AppUtil;
import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.stream.Collectors;
import static app.util.ActionsUtil.sleep;

public class ROManagement {
    public AndroidDriver atomberg;

    public ROManagement(AndroidDriver driver){
        this.atomberg = driver;
    }

    public void addRO() {
        WebElement AddButton = null;
        try {
            AddButton = atomberg.findElement(By.xpath("//android.widget.FrameLayout[@resource-id=\"android:id/content\"]/android.widget.FrameLayout/android.widget.FrameLayout/android.view.View/android.view.View/android.view.View/android.view.View/android.view.View[1]/android.view.View/android.view.View/android.view.View/android.view.View/android.view.View[3]/android.widget.ImageView"));
        } catch (Exception ignored) {}
        if (AddButton != null) {
            AddButton.click();
            sleep(1000);
        } else {
            ActionsUtil.Tap.withCoordinates(atomberg, 540, 1850);
            sleep(1000);
        }
        System.out.println("Searching for Available devices");
        sleep(15000);
        WebElement element = null;
        String xpathExpression = "//android.view.View[@content-desc=\"Atomberg Smart Water Purifier\"]";
        // Search Fan Only
        try {
            element = atomberg.findElement(By.xpath(xpathExpression));
        } catch (NoSuchElementException ignored) {}
        if (element != null){
            ActionsUtil.Tap.connectButton(atomberg, element);
        }
        WebElement pairing = atomberg.findElement(By.xpath("//android.view.View[@content-desc=\"Pairing with device\"]"));
        assert pairing.isDisplayed();
    }

    public void ROAdditionProcess(){
        ActionsUtil.SSleep(3);
        WebElement deviceSelection = atomberg.findElement(By.xpath("//android.view.View[@content-desc=\"Select your device color\"]"));
        assert deviceSelection.isDisplayed();
        //TODO : add other colors when SKU is confirmed.
        WebElement next = atomberg.findElement(By.xpath("//android.widget.Button[@content-desc=\"Next\"]"));
        next.click();
        AppUtil.additionProcess(atomberg);
        ActionsUtil.SSleep(5);
    }

    public void checkRO(){
        ScreenCheck screen = new ScreenCheck(atomberg);
        screen.homeScreen();
        WebElement emptyFamily = null;
        try {
            emptyFamily = atomberg.findElement(By.xpath("//android.widget.ImageView[@content-desc=\"Add your first smart device\"]"));
        } catch (Exception ignored) {}
        if (emptyFamily==null){
            checkROOnline();
        }
    }

    private void checkROOnline() {//Check Fan Online
        WebElement RO = null;
        try {
            RO = atomberg.findElement(By.xpath("//android.widget.ImageView[@content-desc=\"Water Purifier\"]"));
        } catch (Exception ignored) {}
        if (RO != null){
            RO.click();   // click on the fan tab
            sleep(3000);
            WebElement buyNow = null;
            try {
                buyNow = atomberg.findElement(By.xpath("//android.widget.Button[@content-desc=\"Buy Now!\"]"));
            } catch (Exception ignored) {
            }
            if (buyNow == null) {
                List<WebElement> elementList = atomberg.findElements(By.className("android.widget.ImageView"));
                System.out.println(elementList.size());
                List<WebElement> elements = elementList.stream().filter(dev -> dev.getDomAttribute("content-desc") != null).collect(Collectors.toList());
                List<WebElement> purifier = elements.stream().filter(dev -> Objects.requireNonNull(dev.getDomAttribute("content-desc")).endsWith("Living Room")).collect(Collectors.toList());
                if (!purifier.isEmpty()) {
                    System.out.println("Purifier Available " + purifier.size());
                    for (WebElement element : purifier) {
                        System.out.println(element.getDomAttribute("content-desc"));
                        element.click();// Clicks on the for and opens device control
                        ROControl();
                    }
                }
                if (purifier.isEmpty()) {
                    System.out.println("No RO Online");
                }
            }
            ActionsUtil.sleep(1000);
        }
        else {
            addRO();
            ROAdditionProcess();
            checkROOnline();
        }
    }

    private void ROControl(){
        WebElement health = atomberg.findElement(By.xpath("//android.view.View[@content-desc=\"Check Health\"]"));
        health.click();
        ActionsUtil.SSleep(1);
        health();
        ActionsUtil.SSleep(1);
        mode();
        atomberg.navigate().back();
    }

    private void health(){
        List<WebElement> elementList = atomberg.findElements(By.className("android.view.View"));
        List<WebElement> nonNullElements = elementList.stream().filter(webElement -> webElement.getDomAttribute("content-desc")!=null).collect(Collectors.toList());
        nonNullElements.remove(0);
        nonNullElements.remove(nonNullElements.size()-1);
        for(WebElement e : nonNullElements){
            System.out.println(e.getDomAttribute("content-desc"));
        }
        atomberg.navigate().back();

    }

    private void mode(){
        WebElement mode = atomberg.findElement(By.xpath("//android.widget.ImageView[@content-desc=\"Mode\"]"));
        assert mode.isDisplayed();
        mode.click();
        List<WebElement> elementList = atomberg.findElements(By.className("android.view.View"));
        List<WebElement> elements = elementList.stream().filter(webElement -> webElement.getDomAttribute("content-desc")!= null).collect(Collectors.toList());
        elements.remove(elements.size()-1);
        int count = elements.size();
        for(int i = 0; i < count; i++){
            System.out.println(elements.get(i).getDomAttribute("content-desc"));
            String content = elements.get(i).getDomAttribute("content-desc");
            elements.get(i).click();
            if(Objects.equals(content, "Custom Taste Preference")){
                customTastePreference();
            }
            if (i < count-1){
                mode = atomberg.findElement(By.xpath("//android.widget.ImageView[@content-desc=\"Mode\"]"));
                assert mode.isDisplayed();
                mode.click();
                elementList = atomberg.findElements(By.className("android.view.View"));
                elements = elementList.stream().filter(webElement -> webElement.getDomAttribute("content-desc")!= null).collect(Collectors.toList());
            }
        }
    }

    private void customTastePreference(){
        List<WebElement> elementList = atomberg.findElements(By.className("android.view.View"));
        List<WebElement> elements = elementList.stream().filter(webElement -> webElement.getDomAttribute("content-desc")!=null).collect(Collectors.toList());
        elements.remove(elements.size()-1);
        Random random = new Random();
        int randomIndex = random.nextInt(elements.size());
        WebElement element = elements.get(randomIndex);
        System.out.println(element.getDomAttribute("content-desc") + " Selected");
        element.click();
    }
}
