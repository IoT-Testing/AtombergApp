package app.marketPlace;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class CategoryReader2 {

    public static class Product {
        public String name;
        public List<String> colors = new ArrayList<>();
        public List<String> sweeps = new ArrayList<>();
        public String rating;
        public String rate;
        public String stock;

        public Product(String name) {
            this.name = name;
        }
    }

    public static class Category {
        public String name;
        public List<Product> products = new ArrayList<>();

        public Category(String name) {
            this.name = name;
        }
    }

    private final List<Category> categories = new ArrayList<>();

    public CategoryReader2() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode root = mapper.readTree(new File("category_products.json"));

        JsonNode items = root.path("data").path("categories").path("items");
        JsonNode currency = root.path("data").path("currency");
        String currencyCode = currency.path("default_display_currency_symbol").asText();
        for (JsonNode defaultCategory : items) {
            for (JsonNode catNode : defaultCategory.path("children")) {
                Category category = new Category(catNode.path("name").asText());
                JsonNode productItems = catNode.path("products").path("items");

                for (JsonNode productNode : productItems) {
                    Product product = new Product(productNode.path("name").asText());
                    product.rating = productNode.path("average_rating_on_amazon").asText();
                    product.stock = productNode.path("stock_status").asText();

                    JsonNode price_range = productNode.path("price_range").path("minimum_price");
//                    String currency = price_range.path("final_price").path("currency").asText();
                    String price = price_range.path("final_price").path("value").asText();
                    product.rate = currencyCode + price;
                    JsonNode options = productNode.path("configurable_options");
                    for (JsonNode option : options) {
                        String attr = option.path("attribute_code").asText();
                        JsonNode values = option.path("values");
                        if ("color".equals(attr)) {
                            for (JsonNode val : values) {
                                product.colors.add(val.path("label").asText());
                            }
                        }
                        if ("sweepsize".equals(attr)) {
                            for (JsonNode val : values) {
                                product.sweeps.add(val.path("label").asText());
                            }
                        }
                    }
                    JsonNode attributes = productNode.path("attributes");
                    if (attributes.isArray()) {
                        for (JsonNode attr : attributes) {
                            String code = attr.path("code").asText();
                            String label = attr.path("label").asText();

//                            if ("color".equalsIgnoreCase(code) && storedColorLabel.equalsIgnoreCase(label)) {
//                                colorMatched = true;
//                                break;
//                            }
                        }
                    }
                    category.products.add(product);
                }
                categories.add(category);
            }
        }
    }

    public List<Category> getCategories() {
        return categories;
    }
    public WebElement findProductByRatingAndName(String rating, String name, AndroidDriver driver) {
        String xpath = "//android.widget.ImageView[starts-with(@content-desc, '" + rating + "\\n" + name + "')]";
        return driver.findElement(By.xpath(xpath));
    }
}
