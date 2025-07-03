package app;

import app.marketPlace.Marketplace;
import app.marketPlace.RandomSelector;
import app.marketPlace.SelectedPath;
import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.WebElement;

public class Main {
    public static AndroidDriver atomberg;
    public String command;
    public ServerInitializer server = new ServerInitializer();

    public static void main(String[] args) throws Exception {
        try {
            AppInitializer app = new AppInitializer();
            app.openApp();
            atomberg = app.getDriver();
            app.checkMainScreen();
            Marketplace marketplace = new Marketplace(atomberg);
            marketplace.openMarket();
//            marketplace.selectRandomProduct();
            SelectedPath selected = RandomSelector.selectRandomPath();

// Now you can use each part:
            String category = selected.category.name;
            String rating = selected.rating;
            String product = selected.product.name;
            String color = selected.color;
            String sweep = selected.sweep;
            String rate = selected.product.rate;
            String stock = selected.stock;


/// TO DO : Check for all possible data in the .json file for further easy
            marketplace.swipeTillCategoryAvailable(category);
            WebElement productID = marketplace.findProductElement(selected.product);
//            System.out.println(productID.getDomAttribute("content-desc"));
            marketplace.scrollTillProductAvailable(selected, productID);
        } catch (Exception ignored) {
        }
//        atomberg.quit();
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