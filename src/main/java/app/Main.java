package app;

import app.server.AppiumServerManager;
import app.util.ActionsUtil;
import com.aventstack.extentreports.util.Assert;
import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

public class Main {
    public static AndroidDriver atomberg;
    public String command;
    public ServerInitializer server = new ServerInitializer();

    public static void main(String[] args) throws Exception {
        String platform = "ios";
        System.out.println("Starting appium Server");
        AppiumServerManager.startServer(platform);
        ActionsUtil.SSleep(30);

//        AppInitializer app = new AppInitializer();
//        app.initializeDriver();
//        atomberg = app.getDriver();
//        ActionsUtil.SSleep(2);
//        atomberg.activateApp("com.atomberg.app");
//        ActionsUtil.SSleep(2);
//        ActionsUtil.Scroll.Down(atomberg);
//        atomberg.findElement(By.xpath("//android.widget.Button[@content-desc=\"Intellon Water Purifier\n" +
//                "Kitchen\"]/android.widget.ImageView[2]")).click();
//        WebElement popup = atomberg.findElement(By.xpath("//android.view.View[@content-desc=\"RO + UV Mode is selected\"]"));
//        assert popup.isDisplayed();
//        atomberg.navigate().back();
//        Assert.notNull(popup, "Should Not be null");
//        ActionsUtil.Swipe.screenRight(atomberg);
//        analytics analytics = new analytics(atomberg);
//        analytics.Show();
//        List<WebElement> elementList = atomberg.findElements(By.className("android.widget.Button"));xetDomAttribute("clickable"), "true")).collect(Collectors.toList());
//        System.out.println(clickableElements.size());
        
    }
}








































































































































//            CategoryReader reader = new CategoryReader();
//            List<CategoryReader.Category> categories = reader.getCategories();
//     Run full random path
//            RandomSelector.selectRandomPath(categories);
//            CategoryReader reader = new CategoryReader();
//            List<CategoryReader.Category> categories = reader.getCategories();
//
//            for (CategoryReader.Category cat : categories) {
//                System.out.println("Category: " + cat.name);
//                for (CategoryReader.Product prod : cat.products) {
//                    System.out.println("  Product: " + prod.name);
//                    System.out.println("    Rating: " + prod.rating);
//                    System.out.println("    Colors: " + String.join(", ", prod.colors));
//                    System.out.println("    Sweep Sizes: " + String.join(", ", prod.sweeps));
//                }
//            }