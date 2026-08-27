//package app.marketPlace;
//
//import app.ScreenCheck.ScreenCheck;
//import app.util.ActionsUtil;
//import app.util.AppUtil;
//import io.appium.java_client.android.AndroidDriver;
//import org.openqa.selenium.By;
//import org.openqa.selenium.WebElement;
//import java.util.List;
//import java.util.Objects;
//import java.util.stream.Collectors;
//
//public class Marketplace {
//    public AndroidDriver atomberg;
/////  TO DO : Try all the products from MarketPlace.
//    public Marketplace(AndroidDriver driver) {
//        this.atomberg = driver;
//    }
//
//    public void openMarket() {
//        ScreenCheck screen = new ScreenCheck(atomberg);
//        screen.moreTab();
//        try {
//            WebElement marketPlace = null;
//            while (marketPlace == null) {
//                try {
//                    marketPlace = atomberg.findElement(By.xpath("//android.view.View[@content-desc=\"Marketplace\"]"));
//                } catch (Exception ignored) {
//                }
//                if (marketPlace == null) {
//                    ActionsUtil.Scroll.Up(atomberg);
//                    WebElement logout = null;
//                    try {
//                        logout = atomberg.findElement(By.xpath("//android.view.View[@content-desc=\"Logout\"]"));
//                    } catch (Exception ignored) {
//                    }
//                    if (logout != null) ActionsUtil.Scroll.Down(atomberg);
//                }
//            }
//            marketPlace.click();
//        } catch (Exception ignored) {
//        }
//    }
//
//    private void back() {
//        WebElement marketPlace = null;
//        while (marketPlace == null) {
//            try {
//                marketPlace = atomberg.findElement(By.xpath("//android.view.View[@content-desc=\"Marketplace\"]"));
//            } catch (Exception ignored) {
//            }
//            if (marketPlace == null) atomberg.navigate().back();
//        }
//    }
//
//    public void scrollTillProductAvailable(SelectedPath path, WebElement xPath) {
//        String color;
//        WebElement product = null;
//        while (product == null) {
//            try {
//                product = xPath;
//            } catch (Exception ignored) {
//            }
//            if (product == null) ActionsUtil.Scroll.slowUp(atomberg);
//        }
//        WebElement addTOCart = null;
//        try {
//            addTOCart = product.findElement(By.xpath(".//android.widget.Button[@content-desc=\"Add to Cart\"]"));
//        } catch (Exception ignored) {
//        }
//        System.out.println(addTOCart == null);
//        if (addTOCart == null) {
//            ActionsUtil.Scroll.slowUp(atomberg);
//            try {
//                addTOCart = product.findElement(By.xpath(".//android.widget.Button[@content-desc=\"Add to Cart\"]"));
//            } catch (Exception ignored) {
//            }
//            if(addTOCart == null)
//            {
//                product.click();
//                ActionsUtil.SSleep(3);
//                try {
//                    addTOCart = product.findElement(By.xpath(".//android.widget.Button[@content-desc=\"Add to Cart\"]"));
//                } catch (Exception ignored) {
//                }
//                assert addTOCart != null;
//                if (!addTOCart.isDisplayed())
//                {
//                    ActionsUtil.Scroll.slowUp(atomberg);
//                    addTOCart = atomberg.findElement(By.xpath("//android.widget.Button[@content-desc=\"Add to Cart\"]"));
//                }
//            }
//        }
//        addTOCart.click();
//
//        color = path.color;
//        if (color != null && !color.equals("null")) {
//            List<WebElement> Elements = atomberg.findElements(AppiumBy.className("android.view.View"));
//            List<WebElement> availableColors = Elements.stream().filter(element -> element.getDomAttribute("content-desc") != null).collect(Collectors.toList());
//            for (WebElement availableColor : availableColors) {
//                if (Objects.requireNonNull(availableColor.getDomAttribute("content-desc")).equals(color)) {
//                    availableColor.click();
//                }
//            }
//        }
//        try {
//            addTOCart = atomberg.findElement(By.xpath("//android.widget.Button[@content-desc=\"Add to Cart\"]"));
//        } catch (Exception ignored) {
//        }
//        if (addTOCart != null) addTOCart.click();
//        else {
//            WebElement outOfStock = null;
//            try {
//                outOfStock = atomberg.findElement(By.xpath("//android.widget.Button[@content-desc=\"Out of stock\"]"));
//            } catch (Exception ignored) {
//            }
//            if (outOfStock == null) back();
//        }
//    }
//
//
//    public void swipeTillCategoryAvailable(String category) {
//        String xPath = "//android.widget.ImageView[@content-desc=\"" + category + "\"]";
//        if (!Objects.equals(category, "Ceiling Fans")) {
//            WebElement product = null;
//            if (Objects.equals(category, "Exhaust Fans") || Objects.equals(category, "Wall Fans")) {
//                product = atomberg.findElement(By.xpath(xPath));
//            } else {
//                while (product == null) {
//                    ActionsUtil.Swipe.Right(atomberg, 0.80, 0.25);
//                    try {
//                        product = atomberg.findElement(By.xpath(xPath));
//                    } catch (Exception ignored) {
//                    }
//                }
//            }
//            product.click();
//        }
//    }
////a
//    public WebElement findProductElement(CategoryReader.Product product) {
//        String xpath;
//        if (product.rating != null && !product.rating.isEmpty() && !product.rating.equals("null")) {
//            // With rating
//            xpath = "//android.widget.ImageView[@content-desc=\"" + product.rating + "\n" + product.name + "\n" + product.rate + "\"]";
//            System.out.println(xpath);
//        } else {
//            // No rating — product name is the first line
//            xpath = "//android.widget.ImageView[@content-desc=\"" + product.name + "\n" + product.rate + "\"]";
//            System.out.println(xpath);
//        }
//        return atomberg.findElement(By.xpath(xpath));
//    }
//
//    public void selectRandomProduct() throws Exception {
//        CategoryReader reader = new CategoryReader();
//        List<CategoryReader.Category> categories = reader.getCategories();
////     Run full random path
//        RandomSelector.selectRandomPath();
//    }
//
//}