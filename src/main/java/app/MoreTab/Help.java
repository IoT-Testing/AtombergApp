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
    public AppiumDriver atomberg;
    public Help(AppiumDriver driver){
        this.atomberg = driver;
    }

    public void raiseAComplaint(){
        WebElement RaiseComplaint = atomberg.findElement(By.xpath("//android.view.View[@content-desc=\"Raise a complaint\"]"));
        RaiseComplaint.click();
        WebElement newComplaint = atomberg.findElement(By.xpath("//android.view.View[@content-desc=\"New Complaint\"]"));
        await(newComplaint);
        assert newComplaint.isDisplayed();
        AppUtil.captureScreenshot(atomberg);
        System.out.println("Tap on Raise Complaint");
        videoTryCatch();
        ActionsUtil.sleep(2000);
    }

    public void trackAComplaint(){
        WebElement TrackComplaint = atomberg.findElement(By.xpath("//android.view.View[@content-desc=\"Track complaints\"]"));
        TrackComplaint.click();
        WebElement complaintStatus = atomberg.findElement(By.xpath("//android.view.View[@content-desc=\"Complaint status\"]"));
        await(complaintStatus);
        AppUtil.captureScreenshot(atomberg);
        System.out.println("Tap on Track Complaint");
        ActionsUtil.sleep(2000);
        WebElement NoComplaints = null;
        try {
            NoComplaints = atomberg.findElement(By.xpath(
                    "//android.widget.FrameLayout[@resource-id=\"android:id/content\"]/android.widget.FrameLayout/android.widget.FrameLayout/android.view.View/android.view.View/android.view.View[1]/android.view.View/android.view.View/android.view.View/android.view.View[1]"));
        } catch (Exception ignored) {        }
        if (NoComplaints != null) {
            System.out.println("No Complaints Raised.");
        }
        AppUtil.captureScreenshot(atomberg);
        videoTryCatch();
    }

    public void manual(){
        WebElement manual = atomberg.findElement(By.xpath("//android.widget.ImageView[@content-desc=\"Manual\"]"));
        manual.click();
        System.out.println("Manual Open");
        ActionsUtil.sleep(2500);
        videoTryCatch();
    }

    public void troubleshoot(){
        atomberg.findElement(By.xpath("//android.widget.ImageView[@content-desc=\"Troubleshoot\"]")).click();
        // Troubleshoot for Fans
        WebElement fan = atomberg.findElement(By.xpath("//android.widget.ImageView[@content-desc=\"Atomberg Fan\"]"));
        fan.click();
        AppUtil.captureScreenshot(atomberg);
        System.out.println("Fan Troubleshoot");
        WebElement renesa = atomberg.findElement(By.xpath("//android.widget.ImageView[@content-desc=\"Renesa\"]"));
        renesa.click();
        AppUtil.captureScreenshot(atomberg);
        System.out.println("Renesa");
        OK();
        WebElement renesaSmart = atomberg.findElement(By.xpath("//android.widget.ImageView[@content-desc=\"Renesa Smart\"]"));
        renesaSmart.click();
        AppUtil.captureScreenshot(atomberg);
        System.out.println("Renesa Smart");
        ReturnToHome();
        WebElement renesaPlus = atomberg.findElement(By.xpath("//android.widget.ImageView[@content-desc=\"Renesa+\"]"));
        renesaPlus.click();
        AppUtil.captureScreenshot(atomberg);
        System.out.println("Renesa Plus");
        OK();
        WebElement renesaSmartPlus = atomberg.findElement(By.xpath("//android.widget.ImageView[@content-desc=\"Renesa Smart+\"]"));
        renesaSmartPlus.click();
        AppUtil.captureScreenshot(atomberg);
        System.out.println("Renesa Smart +");
        EnterSerialNumber();
        WebElement studioPlus = atomberg
                .findElement(By.xpath("//android.widget.ImageView[@content-desc=\"Studio+\"]"));
        studioPlus.click();
        AppUtil.captureScreenshot(atomberg);
        OK();
        WebElement studioSmartPlus = atomberg
                .findElement(By.xpath("//android.widget.ImageView[@content-desc=\"Studio Smart+\"]"));
        studioSmartPlus.click();
        AppUtil.captureScreenshot(atomberg);
        System.out.println("Studio Plus");
        ReturnToHome();
        WebElement erica =atomberg.findElement(By.xpath("//android.widget.ImageView[@content-desc=\"Erica\"]"));
        erica.click();
        AppUtil.captureScreenshot(atomberg);
        System.out.println("Erica");
        OK();
        WebElement ericaSmart =atomberg.findElement(By.xpath("//android.widget.ImageView[@content-desc=\"Erica Smart\"]"));
        ericaSmart.click();
        AppUtil.captureScreenshot(atomberg);
        System.out.println("Erica Smart");
        ReturnToHome();
        ActionsUtil.Scroll.Up(atomberg);
        WebElement starlight = atomberg
                .findElement(By.xpath("//android.widget.ImageView[@content-desc=\"Aris Starlight\"]"));
        starlight.click();
        AppUtil.captureScreenshot(atomberg);
        System.out.println("Starlight");
        ReturnToHome();
        WebElement aris = atomberg.findElement(By.xpath("//android.widget.ImageView[@content-desc=\"Aris\"]"));
        aris.click();
        AppUtil.captureScreenshot(atomberg);
        System.out.println("Aris");
        ReturnToHome();
        WebElement arisContour = atomberg.findElement(By.xpath("//android.widget.ImageView[@content-desc=\"Aris Contour\"]"));
        arisContour.click();
        AppUtil.captureScreenshot(atomberg);
        System.out.println("Aris");
        ReturnToHome();
        WebElement renesaAlpha = atomberg
                .findElement(By.xpath("//android.widget.ImageView[@content-desc=\"Renesa Alpha\"]"));
        renesaAlpha.click();
        AppUtil.captureScreenshot(atomberg);
        System.out.println("RenesaAlpha");
        OK();
        WebElement efficio = atomberg
                .findElement(By.xpath("//android.widget.ImageView[@content-desc=\"Efficio\"]"));
        efficio.click();
        AppUtil.captureScreenshot(atomberg);
        System.out.println("Efficio");
        OK();
        WebElement ikano = atomberg.findElement(By.xpath("//android.widget.ImageView[@content-desc=\"Ikano\"]"));
        ikano.click();
        AppUtil.captureScreenshot(atomberg);
        System.out.println("Ikano");
        OK();
        ActionsUtil.Scroll.Up(atomberg);
        WebElement ozeo = atomberg.findElement(By.xpath("//android.widget.ImageView[@content-desc=\"Ozeo\"]"));
        ozeo.click();
        AppUtil.captureScreenshot(atomberg);
        System.out.println("Ozeo");
        OK();
        WebElement ameza = atomberg.findElement(By.xpath("//android.widget.ImageView[@content-desc=\"Ameza\"]"));
        ameza.click();
        AppUtil.captureScreenshot(atomberg);
        System.out.println("Ameza");
        OK();
        WebElement other = atomberg.findElement(By.xpath("//android.widget.ImageView[@content-desc=\"Other\"]"));
        other.click();
        AppUtil.captureScreenshot(atomberg);
        System.out.println("Other");
        OK();
        atomberg.navigate().back();
        // Troubleshoot for Locks
        WebElement lock = atomberg.findElement(By.xpath("//android.widget.ImageView[@content-desc=\"Atomberg Lock\"]"));
        lock.click();
        AppUtil.captureScreenshot(atomberg);
        System.out.println("Lock Troubleshoot");
        videoTryCatch();
    }

    private void ReturnToHome() {
        WebElement ReturnToHome = atomberg.findElement(By.xpath("//android.widget.Button[@content-desc=\"Return to home\"]"));
        ReturnToHome.click();
        AppUtil.captureScreenshot(atomberg);
        System.out.println("Return To Home");
    }

    private void EnterSerialNumber() {
        WebElement ManualEnter = atomberg.findElement(By.xpath("//android.widget.EditText"));
        ManualEnter.click();
        AppUtil.captureScreenshot(atomberg);
        System.out.println("Enter Barcode Manually...");

        WebElement ScanBarcode = atomberg.findElement(By.xpath("//android.widget.EditText/android.widget.ImageView"));
        ScanBarcode.click();
        AppUtil.captureScreenshot(atomberg);
        System.out.println("Scan Barcode ...");

        WebElement AllowCamera = null;
        try {
            ActionsUtil.sleep(2000);
            AllowCamera = atomberg.findElement(By.id("com.android.permissioncontroller:id/permission_message"));

        } catch (Exception ignored) {}
        if (AllowCamera != null) {

            AppUtil.captureScreenshot(atomberg);
            WebElement Camera = atomberg
                    .findElement(By.id("com.android.permissioncontroller:id/permission_allow_foreground_only_button"));
            Camera.click();
            AppUtil.captureScreenshot(atomberg);
            ActionsUtil.sleep(2000);
        }
        atomberg.navigate().back();
        cantFindSerialNumber();
        WebElement id = null;
        while(id == null)
        {
            atomberg.navigate().back();
            try {
                id=atomberg.findElement(By.xpath("//android.view.View[@content-desc=\"Identify your device\"]"));
            }catch (Exception ignored)
            {}
        }
    }

    private void OK() {
        WebElement OK = atomberg.findElement(By.xpath("//android.widget.Button[@content-desc=\"Ok\"]"));
        OK.click();
        AppUtil.captureScreenshot(atomberg);
        System.out.println("OK ...");

    }

    public void email() {
        WebElement emailUs = atomberg.findElement(By.xpath("//android.view.View[@content-desc=\"Email us\"]"));
        emailUs.click();
        WebElement helpbt = null;
        while(helpbt == null)
        {
            atomberg.navigate().back();
            try {
                helpbt=atomberg.findElement(By.xpath("//android.view.View[@content-desc=\"Help\"]"));
            }catch (Exception ignored)            {}
        }
    }

    public void call() {
        WebElement callUs =atomberg.findElement(By.xpath("//android.view.View[@content-desc=\"Call us\"]"));
        callUs.click();
        WebElement helpbt = null;
        while(helpbt == null)
        {
            atomberg.navigate().back();
            try {
                helpbt=atomberg.findElement(By.xpath("//android.view.View[@content-desc=\"Help\"]"));
            }catch (Exception ignored)            {}
        }
    }

    private void cantFindSerialNumber(){
        WebElement cantFindSerialNumber = atomberg.findElement(By.xpath("//android.view.View[@content-desc=\"Can't find serial number?\"]"));
        cantFindSerialNumber.click();
        AppUtil.captureScreenshot(atomberg);

        List<WebElement> buttonList = atomberg.findElements(By.className("android.widget.Button"));
        List<WebElement> buttons = buttonList.stream().filter(webElement -> webElement.getDomAttribute("content-desc")!=null).collect(Collectors.toList());
        System.out.println(buttons.size());
        for(int i =0 ; i< buttons.size(); i++){
            List<WebElement> dialogueButtonList = atomberg.findElements(By.className("android.widget.Button"));
            List<WebElement> dialogueButtons = dialogueButtonList.stream().filter(elements -> elements.getDomAttribute("content-desc")!=null).collect(Collectors.toList());
            System.out.println(dialogueButtons.size());
            if (Objects.equals(dialogueButtons.get(i).getDomAttribute("content-desc"), "Yes")) {
                dialogueButtons.get(i).click();
                atomberg.findElement(By.xpath("//android.widget.Button[@content-desc=\"Download\"]")).click();
            }
            else if (Objects.equals(dialogueButtons.get(i).getDomAttribute("content-desc"), "No")) {
                dialogueButtons.get(i).click();
                WebElement Email = atomberg.findElement(By.xpath("//android.view.View[@content-desc=\"Email\"]"));
                Email.click();
                AppUtil.captureScreenshot(atomberg);
                System.out.println("Email ...");
                atomberg.navigate().back();
                WebElement Call = atomberg.findElement(By.xpath("//android.view.View[@content-desc=\"Call\"]"));
                Call.click();
                AppUtil.captureScreenshot(atomberg);
                System.out.println("Call ...");
                WebElement contactSupport = null;
                while (contactSupport == null) {
                    try {
                        contactSupport = atomberg.findElement(By.xpath("//android.view.View[@content-desc=\"Contact Support\"]"));
                    } catch (Exception ignored) {}
                    if (contactSupport == null) atomberg.navigate().back();
                }
            }
            WebElement cantFindSerialNumberLink = null;
            while (cantFindSerialNumberLink == null) {
                try {
                    cantFindSerialNumberLink = atomberg.findElement(By.xpath("//android.view.View[@content-desc=\"Can't find serial number?\"]"));
                } catch (Exception ignored) {}
                if (cantFindSerialNumberLink == null) atomberg.navigate().back();
            }
            cantFindSerialNumber = atomberg.findElement(By.xpath("//android.view.View[@content-desc=\"Can't find serial number?\"]"));
            cantFindSerialNumber.click();
        }
    }

    private void videoTryCatch() {
        WebElement VideoTutorials =null;
        while(VideoTutorials == null)
        {
            try {
                VideoTutorials = atomberg.findElement(By.xpath("//android.view.View[@content-desc=\"Video tutorials\"]"));
            }catch (Exception ignored) {}
            if (VideoTutorials == null) {
                System.out.println("Back");
                atomberg.navigate().back(); // 180, 1550 860, 1960
            }
        }
    }

    private void await(WebElement element){
        Awaitility.await().until(element::isDisplayed);
    }
}

