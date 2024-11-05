package app.MoreTab;

import app.util.ActionsUtil;
import app.util.AppUtil;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import io.appium.java_client.AppiumDriver;
import org.awaitility.Awaitility;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

public class Help {
    public AppiumDriver driver;
    public Help(AppiumDriver driver){
        this.driver = driver;
    }

    public void raiseAComplaint(){
        WebElement RaiseComplaint = driver.findElement(By.xpath("//android.view.View[@content-desc=\"Raise a complaint\"]"));
        RaiseComplaint.click();
        AppUtil.captureScreenshot(driver);
        System.out.println("Tap on Raise Complaint");
        Awaitility.await().until(() -> driver.findElement(By.xpath("//android.widget.ScrollView/android.widget.EditText[1]")).isDisplayed());
        videoTryCatch();
        ActionsUtil.sleep(2000);
    }

    public void trackAComplaint(){
        WebElement TrackComplaint = driver.findElement(By.xpath("//android.view.View[@content-desc=\"Track complaints\"]"));
        TrackComplaint.click();
        AppUtil.captureScreenshot(driver);
        System.out.println("Tap on Track Complaint");
        ActionsUtil.sleep(2000);
        WebElement NoComplaints = null;
        try {
            NoComplaints = driver.findElement(By.xpath(
                    "//android.widget.FrameLayout[@resource-id=\"android:id/content\"]/android.widget.FrameLayout/android.widget.FrameLayout/android.view.View/android.view.View/android.view.View[1]/android.view.View/android.view.View/android.view.View/android.view.View[1]"));
        } catch (Exception ignored) {        }
        if (NoComplaints != null) {
            System.out.println("No Complaints Raised.");
        }
        AppUtil.captureScreenshot(driver);
        videoTryCatch();
    }

    public void manual(){
        WebElement manual = driver.findElement(By.xpath("//android.widget.ImageView[@content-desc=\"Manual\"]"));
        manual.click();
        System.out.println("Manual Open");
        ActionsUtil.sleep(2500);
        videoTryCatch();
    }

    public void troubleshoot(){
        driver.findElement(By.xpath("//android.widget.ImageView[@content-desc=\"Troubleshoot\"]")).click();
        // Troubleshoot for Fans
        WebElement fan = driver.findElement(By.xpath("//android.widget.ImageView[@content-desc=\"Atomberg Fan\"]"));
        fan.click();
        AppUtil.captureScreenshot(driver);
        System.out.println("Fan Troubleshoot");
        WebElement renesa = driver.findElement(By.xpath("//android.widget.ImageView[@content-desc=\"Renesa\"]"));
        renesa.click();
        AppUtil.captureScreenshot(driver);
        System.out.println("Renesa");
        OK();
        WebElement renesaSmart = driver.findElement(By.xpath("//android.widget.ImageView[@content-desc=\"Renesa Smart\"]"));
        renesaSmart.click();
        AppUtil.captureScreenshot(driver);
        System.out.println("Renesa Smart");
        ReturnToHome();
        WebElement renesaPlus = driver.findElement(By.xpath("//android.widget.ImageView[@content-desc=\"Renesa+\"]"));
        renesaPlus.click();
        AppUtil.captureScreenshot(driver);
        System.out.println("Renesa Plus");
        OK();
        WebElement renesaSmartPlus = driver.findElement(By.xpath("//android.widget.ImageView[@content-desc=\"Renesa Smart+\"]"));
        renesaSmartPlus.click();
        AppUtil.captureScreenshot(driver);
        System.out.println("Renesa Smart +");
        EnterSerialNumber();
        WebElement studioPlus = driver
                .findElement(By.xpath("//android.widget.ImageView[@content-desc=\"Studio+\"]"));
        studioPlus.click();
        AppUtil.captureScreenshot(driver);
        OK();
        WebElement studioSmartPlus = driver
                .findElement(By.xpath("//android.widget.ImageView[@content-desc=\"Studio Smart+\"]"));
        studioSmartPlus.click();
        AppUtil.captureScreenshot(driver);
        System.out.println("Studio Plus");
        ReturnToHome();
        WebElement erica =driver.findElement(By.xpath("//android.widget.ImageView[@content-desc=\"Erica\"]"));
        erica.click();
        AppUtil.captureScreenshot(driver);
        System.out.println("Erica");
        OK();
        WebElement ericaSmart =driver.findElement(By.xpath("//android.widget.ImageView[@content-desc=\"Erica Smart\"]"));
        ericaSmart.click();
        AppUtil.captureScreenshot(driver);
        System.out.println("Erica Smart");
        ReturnToHome();
        ActionsUtil.Scroll.Up(driver);
        WebElement starlight = driver
                .findElement(By.xpath("//android.widget.ImageView[@content-desc=\"Aris Starlight\"]"));
        starlight.click();
        AppUtil.captureScreenshot(driver);
        System.out.println("Starlight");
        ReturnToHome();
        WebElement aris = driver.findElement(By.xpath("//android.widget.ImageView[@content-desc=\"Aris\"]"));
        aris.click();
        AppUtil.captureScreenshot(driver);
        System.out.println("Aris");
        ReturnToHome();
        WebElement arisContour = driver.findElement(By.xpath("//android.widget.ImageView[@content-desc=\"Aris Contour\"]"));
        arisContour.click();
        AppUtil.captureScreenshot(driver);
        System.out.println("Aris");
        ReturnToHome();
        WebElement renesaAlpha = driver
                .findElement(By.xpath("//android.widget.ImageView[@content-desc=\"Renesa Alpha\"]"));
        renesaAlpha.click();
        AppUtil.captureScreenshot(driver);
        System.out.println("RenesaAlpha");
        OK();
        WebElement efficio = driver
                .findElement(By.xpath("//android.widget.ImageView[@content-desc=\"Efficio\"]"));
        efficio.click();
        AppUtil.captureScreenshot(driver);
        System.out.println("Efficio");
        OK();
        WebElement ikano = driver.findElement(By.xpath("//android.widget.ImageView[@content-desc=\"Ikano\"]"));
        ikano.click();
        AppUtil.captureScreenshot(driver);
        System.out.println("Ikano");
        OK();
        ActionsUtil.Scroll.Up(driver);
        WebElement ozeo = driver.findElement(By.xpath("//android.widget.ImageView[@content-desc=\"Ozeo\"]"));
        ozeo.click();
        AppUtil.captureScreenshot(driver);
        System.out.println("Ozeo");
        OK();
        WebElement ameza = driver.findElement(By.xpath("//android.widget.ImageView[@content-desc=\"Ameza\"]"));
        ameza.click();
        AppUtil.captureScreenshot(driver);
        System.out.println("Ameza");
        OK();
        WebElement other = driver.findElement(By.xpath("//android.widget.ImageView[@content-desc=\"Other\"]"));
        other.click();
        AppUtil.captureScreenshot(driver);
        System.out.println("Other");
        OK();
        driver.navigate().back();
        // Troubleshoot for Locks
        WebElement lock = driver.findElement(By.xpath("//android.widget.ImageView[@content-desc=\"Atomberg Lock\"]"));
        lock.click();
        AppUtil.captureScreenshot(driver);
        System.out.println("Lock Troubleshoot");
        videoTryCatch();
    }

    private void ReturnToHome() {
        WebElement ReturnToHome = driver.findElement(By.xpath("//android.widget.Button[@content-desc=\"Return to home\"]"));
        ReturnToHome.click();
        AppUtil.captureScreenshot(driver);
        System.out.println("Return To Home");
    }

    private void EnterSerialNumber() {
        WebElement ManualEnter = driver.findElement(By.xpath("//android.widget.EditText"));
        ManualEnter.click();
        AppUtil.captureScreenshot(driver);
        System.out.println("Enter Barcode Manually...");

        WebElement ScanBarcode = driver.findElement(By.xpath("//android.widget.EditText/android.widget.ImageView"));
        ScanBarcode.click();
        AppUtil.captureScreenshot(driver);
        System.out.println("Scan Barcode ...");

        WebElement AllowCamera = null;
        try {
            ActionsUtil.sleep(2000);
            AllowCamera = driver.findElement(By.id("com.android.permissioncontroller:id/permission_message"));

        } catch (Exception ignored) {}
        if (AllowCamera != null) {

            AppUtil.captureScreenshot(driver);
            WebElement Camera = driver
                    .findElement(By.id("com.android.permissioncontroller:id/permission_allow_foreground_only_button"));
            Camera.click();
            AppUtil.captureScreenshot(driver);
            ActionsUtil.sleep(2000);
        }
        driver.navigate().back();
        cantFindSerialNumber();
        WebElement id = null;
        while(id == null)
        {
            driver.navigate().back();
            try {
                id=driver.findElement(By.xpath("//android.view.View[@content-desc=\"Identify your device\"]"));
            }catch (Exception ignored)
            {}
        }
    }

    private void OK() {
        WebElement OK = driver.findElement(By.xpath("//android.widget.Button[@content-desc=\"Ok\"]"));
        OK.click();
        AppUtil.captureScreenshot(driver);
        System.out.println("OK ...");

    }

    public void email() {
        WebElement emailUs = driver.findElement(By.xpath("//android.view.View[@content-desc=\"Email us\"]"));
        emailUs.click();
        driver.navigate().back();
    }

    public void call() {
        WebElement callUs =driver.findElement(By.xpath("//android.view.View[@content-desc=\"Call us\"]"));
        callUs.click();
        WebElement id = null;
        while(id == null)
        {
            driver.navigate().back();
            try {
                id=driver.findElement(By.xpath("//android.view.View[@content-desc=\"Help\"]"));
            }catch (Exception ignored)            {}
        }
    }

    private void cantFindSerialNumber(){
        WebElement cantFindSerialNumber = driver.findElement(By.xpath("//android.view.View[@content-desc=\"Can't find serial number?\"]"));
        cantFindSerialNumber.click();
        AppUtil.captureScreenshot(driver);

        List<WebElement> buttonList = driver.findElements(By.className("android.widget.Button"));
        List<WebElement> buttons = buttonList.stream().filter(webElement -> webElement.getAttribute("content-desc")!=null).collect(Collectors.toList());
        System.out.println(buttons.size());
        for(int i =0 ; i< buttons.size(); i++){
            List<WebElement> dialogueButtonList = driver.findElements(By.className("android.widget.Button"));
            List<WebElement> dialogueButtons = dialogueButtonList.stream().filter(elements -> elements.getAttribute("content-desc")!=null).collect(Collectors.toList());
            System.out.println(dialogueButtons.size());
            if (Objects.equals(dialogueButtons.get(i).getAttribute("content-desc"), "Yes")) {
                dialogueButtons.get(i).click();
                driver.findElement(By.xpath("//android.widget.Button[@content-desc=\"Download\"]")).click();
            }
            else if (Objects.equals(dialogueButtons.get(i).getAttribute("content-desc"), "No")) {
                dialogueButtons.get(i).click();
                WebElement Email = driver.findElement(By.xpath("//android.view.View[@content-desc=\"Email\"]"));
                Email.click();
                AppUtil.captureScreenshot(driver);
                System.out.println("Email ...");
                driver.navigate().back();
                WebElement Call = driver.findElement(By.xpath("//android.view.View[@content-desc=\"Call\"]"));
                Call.click();
                AppUtil.captureScreenshot(driver);
                System.out.println("Call ...");
                WebElement contactSupport = null;
                while (contactSupport == null) {
                    try {
                        contactSupport = driver.findElement(By.xpath("//android.view.View[@content-desc=\"Contact Support\"]"));
                    } catch (Exception ignored) {}
                    if (contactSupport == null) driver.navigate().back();
                }
            }
            WebElement cantFindSerialNumberLink = null;
            while (cantFindSerialNumberLink == null) {
                try {
                    cantFindSerialNumberLink = driver.findElement(By.xpath("//android.view.View[@content-desc=\"Can't find serial number?\"]"));
                } catch (Exception ignored) {}
                if (cantFindSerialNumberLink == null) driver.navigate().back();
            }
            cantFindSerialNumber = driver.findElement(By.xpath("//android.view.View[@content-desc=\"Can't find serial number?\"]"));
            cantFindSerialNumber.click();
        }
    }

    private void videoTryCatch() {
        WebElement VideoTutorials =null;
        while(VideoTutorials == null)
        {
            try {
                VideoTutorials = driver.findElement(By.xpath("//android.view.View[@content-desc=\"Video tutorials\"]"));
            }catch (Exception ignored) {}
            if (VideoTutorials == null) {
                System.out.println("Back");
                driver.navigate().back(); // 180, 1550 860, 1960
            }
        }
    }

    private void await(WebElement element){
        Awaitility.await().until(element::isDisplayed);
    }
}

