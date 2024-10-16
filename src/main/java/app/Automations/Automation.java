package app.Automations;

import java.util.*;
import java.time.Duration;
import app.util.ActionsUtil;
import org.openqa.selenium.By;
import java.text.SimpleDateFormat;
import java.util.stream.Collectors;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebElement;
import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.interactions.Pause;
import org.openqa.selenium.interactions.Sequence;
import org.openqa.selenium.interactions.PointerInput;

public class Automation {
    private AppiumDriver driver;
    private WebElement automations;
    private WebElement quickAccess;
    public Automation(AppiumDriver driver){
        this.driver = driver;
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(6));
        automations = this.driver.findElement(By.xpath("//android.view.View[@content-desc=\"Automations\"]"));
    }

    public void TimeOfDay(){
        if (automations.isDisplayed()) {
            automations.click();
            System.out.println("Click on Automations");
            WebElement newAutomation = null;
            try{
                String xpath = "//android.view.View[@content-desc=\"Schedule actions\nExample: Turn ON all bedroom fans at 11 PM\"]/android.widget.ImageView[1]";
                newAutomation = driver.findElement(By.xpath(xpath));
            }catch(Exception ignored){}
            if (newAutomation == null )
                driver.findElement(By.xpath("//android.widget.FrameLayout[@resource-id=\"android:id/content\"]/android.widget.FrameLayout/android.widget.FrameLayout/android.view.View/android.view.View/android.view.View/android.view.View/android.view.View[1]/android.view.View/android.view.View/android.view.View/android.view.View/android.view.View[3]/android.widget.ImageView")).click();

            else newAutomation.click();
            newAutomation();
            WebElement dialogueBox = driver.findElement(By.xpath("//android.view.View[@content-desc=\"Scheduled Automation Added Successfully\"]"));
            assert dialogueBox.isDisplayed();
        }
    }

    public void QuickAccess() {
        ActionsUtil.sleep(2000);
        if (automations.isDisplayed()) {
            automations.click();
            System.out.println("Click on Automations");
            quickAccess = this.driver.findElement(By.xpath("//android.widget.ImageView[@content-desc=\"Quick access\"]"));
            quickAccess.click();
            WebElement newQuickAccess = null;
            try{
                newQuickAccess = driver.findElement(By.xpath("//android.view.View[@content-desc=\"Create 'Speed-dial' buttons for frequent actions\n" +
                        "Example: 'Goodbye' button to turn off all fans with one click\"]/android.view.View[5]"));
            }catch (Exception ignored){}
            if(newQuickAccess != null){
                newQuickAccess.click();
            }
            else {
                driver.findElement(By.xpath("//android.widget.FrameLayout[@resource-id=\"android:id/content\"]/android.widget.FrameLayout/android.widget.FrameLayout/android.view.View/android.view.View/android.view.View/android.view.View/android.view.View[1]/android.view.View/android.view.View/android.view.View/android.view.View/android.view.View[3]/android.widget.ImageView")).click();
            }
            WebElement quickAccessName = driver.findElement(By.xpath("//android.widget.EditText"));
            quickAccessName.click();
            String timestamp = new SimpleDateFormat("HHmmss").format(new Date());

            quickAccessName.sendKeys("QA" + timestamp);
            selectRandomFan();
            selectAction();
            driver.findElement(By.xpath("//android.widget.Button[@content-desc=\"Add\"]")).click();

            WebElement quickAccessSuccessful = driver.findElement(By.xpath("//android.view.View[@content-desc=\"Quick Access Automation Added Successfully\"]"));
            assert quickAccessSuccessful.isDisplayed();
        }
    }

    private void newAutomation() {
        String timestamp = new SimpleDateFormat("HHmmss").format(new Date());

        WebElement AutoName = driver.findElement(By.xpath("//android.widget.EditText"));
        AutoName.click();
        AutoName.sendKeys("Automation" + timestamp);
        selectRandomFan();
        driver.findElement(By.xpath("//android.view.View[@content-desc=\"Select time\"]")).click();
        seekBar();
        ActionsUtil.sleep(250);
        seekBar();
        ActionsUtil.sleep(1000);
        driver.findElement(By.xpath("//android.widget.Button[@content-desc=\"Ok\"]")).click();
        selectAction();
        ActionsUtil.Scroll.Up(driver);
        driver.findElement(By.xpath("//android.widget.Switch")).click();
        driver.findElement(By.xpath("//android.widget.Button[@content-desc=\"Add\"]")).click();
        System.out.println("New automation created");
    }

    private void selectRandomFan() {

        WebElement selectFan = driver.findElement(By.xpath("//android.view.View[@content-desc=\"Select fans\"]"));
        selectFan.click();
        List<WebElement> fans = driver.findElements(By.className("android.widget.CheckBox"));
        if (fans.size() == 1) {
            WebElement fan = fans.get(0);
            fan.click();
        }
        if (fans.size() > 1) {
            int x = new Random().nextInt(fans.size());
            WebElement fan = fans.get(x);
            fan.click();
        }
        WebElement Ok = driver.findElement(By.xpath("//android.widget.Button[@content-desc=\"OK\"]"));
        Ok.click();
    }

    private void seekBar() {
        Dimension size = driver.manage().window().getSize(); // Assuming getWindowSize() returns the window size

        int startY = (int) (size.getHeight() * 0.635);
        int startX = (int) (size.getWidth() * 0.21); // Adjusted to swipe right
        double x = 0.22 + (new Random().nextDouble()) * 0.8;
        int endX = (int) (size.getWidth() * x); // Adjusted to swipe right
        PointerInput finger = new PointerInput(PointerInput.Kind.TOUCH, "finger");
        Sequence sequence = new Sequence(finger, 1)
                .addAction(finger.createPointerMove(Duration.ZERO, PointerInput.Origin.viewport(), startX, startY))
                .addAction(finger.createPointerDown(PointerInput.MouseButton.LEFT.asArg()))
                .addAction(new Pause(finger, Duration.ofMillis(150)))
                .addAction(
                        finger.createPointerMove(Duration.ofMillis(250), PointerInput.Origin.viewport(), endX, startY))
                .addAction(finger.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));
        driver.perform(Collections.singletonList(sequence));
        System.out.println("SeekBar slide");
    }

    private void selectAction() {
        driver.findElement(By.xpath("//android.widget.Button[@content-desc=\"Power ON\"]")).click();

        List<WebElement> ACTIONS = driver.findElements(By.className("android.view.View"));
        int x = new Random().nextInt(ACTIONS.size());
        WebElement action = ACTIONS.get(x);
        action.click();

    }

    public void allScreenQA() {
        WebElement devices = driver.findElement(By.xpath("//android.view.View[@content-desc=\"Devices\"]"));
        devices.click();
        WebElement allScreenQuickAccess = driver.findElement(By.xpath("//android.widget.FrameLayout[@resource-id=\"android:id/content\"]/android.widget.FrameLayout/android.widget.FrameLayout/android.view.View/android.view.View/android.view.View/android.view.View/android.widget.ImageView"));
        assert allScreenQuickAccess.isDisplayed();
        allScreenQuickAccess.click();
        List<WebElement> ACTIONS = driver.findElements(By.className("android.widget.Button"));
        ACTIONS.remove(0);
        ACTIONS.remove(0);
        ACTIONS.remove(0);
        int i;
        int total = ACTIONS.size();
        for (i = 0; i < total; i++) {
            List<WebElement> Actions = driver.findElements(By.className("android.widget.Button"));
            Actions.remove(0);
            Actions.remove(0);
            Actions.remove(0);
            WebElement action = Actions.get(i);
            System.out.println(action.getAttribute("content-desc"));
//            action.click();
            ActionsUtil.sleep(1000);
            if (i < total - 1) {
                quickAccess.click();
            }
        }
    }

    public void deleteTimeOfDay(){
        driver.findElement(By.xpath("//android.view.View[@content-desc=\"Automations\"]")).click();
        WebElement timeOfDay = null;
        try{
            timeOfDay = driver.findElement(By.xpath("//android.widget.ImageView[@content-desc=\"Time of day\"]"));
        }catch(Exception ignored){}
        if(timeOfDay==null)driver.findElement(By.xpath("//android.widget.ImageView[@content-desc=\"Time of day\"]")).click();
        else timeOfDay.click();

        List<WebElement> Elements = driver.findElements(By.className("android.widget.ImageView"));
        List<WebElement> Elements2 = Elements.stream().filter(element -> element.getAttribute("content-desc")!=null).collect(Collectors.toList());
        List<WebElement> elements = Elements2.stream().filter(element -> Objects.requireNonNull(element.getAttribute("content-desc")).startsWith("Automation")).collect(Collectors.toList());
        int size = elements.size();
        for (int i = 0; i < size; i++) {
            List<WebElement> ELEMENTS= driver.findElements(By.className("android.widget.ImageView"));
            List<WebElement> ELEMENTS2 = ELEMENTS.stream().filter(element -> element.getAttribute("content-desc")!=null).collect(Collectors.toList());
            List<WebElement> Automations = ELEMENTS2.stream().filter(element -> Objects.requireNonNull(element.getAttribute("content-desc")).startsWith("Automation")).collect(Collectors.toList());

            Automations.get(0).click();
            driver.findElement(By.xpath("//android.widget.FrameLayout[@resource-id=\"android:id/content\"]/android.widget.FrameLayout/android.widget.FrameLayout/android.view.View/android.view.View/android.view.View/android.view.View/android.view.View/android.view.View/android.widget.Button")).click();
            driver.findElement(By.xpath("//android.widget.Button[@content-desc=\"Yes\"]")).click();
            WebElement deleteAutomation = driver.findElement(By.xpath("//android.view.View[@content-desc=\"Automation Removed Successfully\"]"));
            assert deleteAutomation.isDisplayed();
            System.out.println("Automation Deleted");
        }
        Elements = driver.findElements(By.className("android.widget.ImageView"));
        Elements2 = Elements.stream().filter(element -> element.getAttribute("content-desc")!=null).collect(Collectors.toList());
        elements = Elements2.stream().filter(element -> Objects.requireNonNull(element.getAttribute("content-desc")).startsWith("Automation")).collect(Collectors.toList());
        size = elements.size();
        if (size != 0) deleteTimeOfDay();
    }

    public void deleteQuickAccess() {
        driver.findElement(By.xpath("//android.view.View[@content-desc=\"Automations\"]")).click();
        driver.findElement(By.xpath("//android.widget.ImageView[@content-desc=\"Quick access\"]")).click();
        List<WebElement> Elements = driver.findElements(By.className("android.widget.ImageView"));
        List<WebElement> Elements2 = Elements.stream().filter(element -> element.getAttribute("content-desc") != null).collect(Collectors.toList());
        List<WebElement> elements = Elements2.stream().filter(element -> Objects.requireNonNull(element.getAttribute("content-desc")).startsWith("QA")).collect(Collectors.toList());
        int size = elements.size();
        for (int i = 0; i < size; i++) {
            List<WebElement> ELEMENTS = driver.findElements(By.className("android.widget.ImageView"));
            List<WebElement> ELEMENTS2 = ELEMENTS.stream().filter(element -> element.getAttribute("content-desc") != null).collect(Collectors.toList());
            List<WebElement> Automations = ELEMENTS2.stream().filter(element -> Objects.requireNonNull(element.getAttribute("content-desc")).startsWith("QA")).collect(Collectors.toList());

            Automations.get(0).click();
            driver.findElement(By.xpath("//android.widget.FrameLayout[@resource-id=\"android:id/content\"]/android.widget.FrameLayout/android.widget.FrameLayout/android.view.View/android.view.View/android.view.View/android.view.View/android.view.View[1]/android.view.View/android.widget.Button")).click();
            driver.findElement(By.xpath("//android.widget.Button[@content-desc=\"Yes\"]")).click();
            WebElement deleteAutomation = driver.findElement(By.xpath("//android.view.View[@content-desc=\"Automation Removed Successfully\"]"));
            assert deleteAutomation.isDisplayed();
            System.out.println("Quick Access Deleted");
        }
        Elements = driver.findElements(By.className("android.widget.ImageView"));
        Elements2 = Elements.stream().filter(element -> element.getAttribute("content-desc") != null).collect(Collectors.toList());
        elements = Elements2.stream().filter(element -> Objects.requireNonNull(element.getAttribute("content-desc")).startsWith("QA")).collect(Collectors.toList());
        size = elements.size();
        if (size!=0) deleteQuickAccess();
    }


}