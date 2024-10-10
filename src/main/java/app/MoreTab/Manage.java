package app.MoreTab;

import app.util.ActionsUtil;
import app.util.AppUtil;
import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class Manage {
    public AppiumDriver driver;
    public Manage(AppiumDriver driver){
        this.driver = driver;
    }
    public void ChangePassword() {
        WebElement ChangePassword = driver
                .findElement(By.xpath("//android.view.View[@content-desc=\"Change password\"]"));
        ChangePassword.click();
        AppUtil.captureScreenshot(driver);
        System.out.println("Tap on Change Password");

        driver.navigate().back();
    }

    public void DeleteAccount() {
        WebElement DeleteAccount = driver
                .findElement(By.xpath("//android.view.View[@content-desc=\"Delete account\"]"));
        DeleteAccount.click();
        AppUtil.captureScreenshot(driver);
        System.out.println("Tap on Delete Account");

        driver.navigate().back();
    }

    public void DeveloperOptions() {
        WebElement DevOps = driver.findElement(By.xpath("//android.view.View[@content-desc=\"Developer options\"]"));
        DevOps.click();
        AppUtil.captureScreenshot(driver);
        System.out.println("Tap on Developer Options");
        driver.navigate().back();
    }

    public void Logout() {
        checkLogout();
        WebElement Logout = driver.findElement(By.xpath("//android.view.View[@content-desc=\"Logout\"]"));
        Logout.click();
        AppUtil.captureScreenshot(driver);
        System.out.println("Tap on Logout");
        WebElement yes = driver.findElement(By.xpath("//android.widget.Button[@content-desc=\"Yes\"]"));
        yes.click();
        /*driver.navigate().back();*/
    }

    public void Family() {
        goToMore();
        WebElement ManageFamily = null;
        while (ManageFamily == null) {
            ActionsUtil.Scroll.Up(driver);
            try {
                ManageFamily = driver.findElement(By.xpath("//android.view.View[@content-desc=\"Manage family\"]"));
            } catch (Exception ignored) {
            }
        }
        ManageFamily.click();
        AppUtil.captureScreenshot(driver);
        System.out.println("Tap on Manage Family");

        List<WebElement> elements = driver.findElements(By.className("android.widget.ImageView"));
        List<WebElement> families = elements.stream().filter(element -> element.getAttribute("content-desc") != null).collect(Collectors.toList());
        for(WebElement family : families)
        {
            String familyName = family.getAttribute("content-desc");
            System.out.println(family.getAttribute("content-desc"));
            family.click();
            if(!Objects.equals(familyName, "Add")) {
                Home();
                countMember();
                driver.navigate().back();
            } else{
                AddHome();
            }
        }
    }

    public void Member() {

        WebElement RemoveMember = driver.findElement(By.xpath("//android.view.View[@content-desc=\"Remove Member\"]"));
        RemoveMember.click();
        System.out.println("Remove Member");
        AppUtil.captureScreenshot(driver);

        WebElement Cancel2 = driver.findElement(By.xpath("//android.widget.Button[@content-desc=\"Cancel\"]"));
        Cancel2.click();
        WebElement MakeAdmin = driver.findElement(By.xpath("//android.view.View[@content-desc=\"Make Admin\"]"));
        MakeAdmin.click();

        System.out.println("Make Admin");
        AppUtil.captureScreenshot(driver);

        WebElement Cancel3 = driver.findElement(By.xpath("//android.widget.Button[@content-desc=\"Cancel\"]"));
        Cancel3.click();
        driver.navigate().back();

        WebElement AddMember = driver.findElement(By.xpath("//android.widget.FrameLayout[@resource-id=\"android:id/content\"]/android.widget.FrameLayout/android.widget.FrameLayout/android.view.View/android.view.View/android.view.View/android.view.View/android.view.View[3]"));
        AddMember.click();

        AppUtil.captureScreenshot(driver);
        ActionsUtil.sleep(3000);

        WebElement Share = driver.findElement(By.xpath("//android.widget.Button[@content-desc=\"Share\"]"));
        Share.click();
        AppUtil.captureScreenshot(driver);

        driver.navigate().back();
        driver.navigate().back();
        driver.navigate().back();
    }

    private void AddHome() {

        WebElement AddHome = null;

        try { //Checking if the '+' Add button is in the screen or not
            AddHome = driver.findElement(By.xpath("//android.widget.ImageView[@content-desc=\"Add\"]"));
        } catch (Exception ignored) {
        }
        if (AddHome == null) {

            ActionsUtil.Scroll.Up(driver);
            AddHome = driver.findElement(By.xpath("//android.widget.ImageView[@content-desc=\"Add\"]"));
        }

        AddHome.click();

        AppUtil.captureScreenshot(driver);
        ActionsUtil.Tap.withPercentage(driver, 0.50, 0.75); // Narzo 0.50, 0.625 Tab 0.50, 0.75

        AppUtil.captureScreenshot(driver);
        WebElement HomeName = driver.findElement(By.xpath("//android.widget.EditText"));
        HomeName.click();
        HomeName.sendKeys("Script");
        AppUtil.captureScreenshot(driver);


        WebElement Create = driver.findElement(By.xpath("//android.widget.Button[@content-desc=\"Create\"]"));
        Create.click();

        AppUtil.captureScreenshot(driver);
        ActionsUtil.sleep(5000);

    }

    private void Home() {

        WebElement FamilyEdit = driver.findElement(By.xpath("//android.widget.FrameLayout[@resource-id=\"android:id/content\"]/android.widget.FrameLayout/android.widget.FrameLayout/android.view.View/android.view.View/android.view.View/android.view.View/android.view.View[1]/android.view.View/android.view.View[2]"));
        FamilyEdit.click();
        System.out.println("Family Edit");
        AppUtil.captureScreenshot(driver);

        WebElement LeaveHome = driver.findElement(By.xpath("//android.view.View[@content-desc=\"Leave home\"]"));
        LeaveHome.click();
        System.out.println("Leave home");
        AppUtil.captureScreenshot(driver);

        WebElement Cancel = driver.findElement(By.xpath("//android.widget.Button[@content-desc=\"Cancel\"]"));
        Cancel.click();
        System.out.println("Cancel");
        AppUtil.captureScreenshot(driver);

        WebElement delete =driver.findElement(By.xpath("//android.view.View[@content-desc=\"Delete home\"]"));
        delete.click();

        Cancel = driver.findElement(By.xpath("//android.widget.Button[@content-desc=\"Cancel\"]"));
        Cancel.click(); // android.widget.Button[@content-desc="Cancel"]
        driver.navigate().back();
    }

    private void countMember(){
        List<WebElement> Elements = driver.findElements(By.className("android.view.View"));
        List<WebElement> elements = Elements.stream().filter(element -> Objects.equals(element.getAttribute("clickable"), "true")).collect(Collectors.toList());
        System.out.println(elements.size());
        for (WebElement e: elements){
            System.out.println(e.getTagName());
        }
    }

    private void goToMore() {
        List<WebElement> MT = driver.findElements(By.className("android.widget.ImageView"));
        System.out.println(MT.size());
        System.out.println("More");
        List<WebElement> mt = MT.stream().filter(webElement -> webElement.getAttribute("content-desc") != null).collect(Collectors.toList());
        System.out.println(mt.size());
        System.out.println("More1");
        List<WebElement> moreTab = mt.stream().filter(webElement -> Objects.equals(webElement.getAttribute("selected"), "true")).collect(Collectors.toList());
        System.out.println(moreTab.size());
        for (WebElement e : moreTab) {
            System.out.println(e.getAttribute("content-desc"));
        }
        if (moreTab.isEmpty()) {
            driver.findElement(By.xpath("//android.widget.ImageView[@content-desc=\"More\n" +
                    "Tab 3 of 3\"]")).click();

        } else if (!Objects.requireNonNull(moreTab.get(0).getAttribute("content-desc")).endsWith("Tab 3 of 3")) {
            System.out.println("tap on moreTab");
            driver.findElement(By.xpath("//android.widget.ImageView[@content-desc=\"More\n" +
                    "Tab 3 of 3\"]")).click();
        }
    }

    private void checkLogout() {
        goToMore();
        List<WebElement> moreTab;
        do {
            List<WebElement> MT = driver.findElements(By.className("android.view.View"));
            System.out.println(MT.size());
            List<WebElement> mt = MT.stream().filter(webElement -> webElement.getAttribute("content-desc") != null).collect(Collectors.toList());
            System.out.println(mt.size());
            moreTab = mt.stream().filter(webElement -> Objects.equals(webElement.getAttribute("content-desc"), "Logout")).collect(Collectors.toList());
            System.out.println(moreTab.size());

            if (moreTab.isEmpty()) {
                ActionsUtil.Scroll.Up(driver);
            } else if (!moreTab.get(0).isDisplayed()) // Rare case where the Logout button is in the DOM but not on the Screen
            {
                System.out.println(!moreTab.get(0).isDisplayed());
                ActionsUtil.Scroll.Up(driver);
            }
        } while (moreTab.isEmpty());
    }
}

