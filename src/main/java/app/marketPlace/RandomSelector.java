package app.marketPlace;

import java.util.List;
import java.util.Random;

public class RandomSelector {

    private static final Random random = new Random();

    public static SelectedPath selectRandomPath() throws Exception {
        CategoryReader reader = new CategoryReader();
        List<CategoryReader.Category> categories = reader.getCategories();
        CategoryReader.Category category = categories.get(random.nextInt(categories.size()));

        CategoryReader.Product product = category.products.get(random.nextInt(category.products.size()));

        String color = product.colors.isEmpty() ? null :
                product.colors.get(random.nextInt(product.colors.size()));

        String sweep = product.sweeps.isEmpty() ? null :
                product.sweeps.get(random.nextInt(product.sweeps.size()));

        // Optionally print
        System.out.println("Selected Category: " + category.name);
        System.out.println("  Product: " + product.name);
        System.out.println("  Rating: " + product.rating);
        System.out.println("    Color: " + color);
        System.out.println("    Sweep Size: " + sweep);

        return new SelectedPath(category, product, color, sweep);
    }
}

