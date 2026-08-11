package app;

import app.util.ActionsUtil;
import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import java.time.Duration;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * SwitchFamily - Iterates through all available families and runs a callback in each.
 * Uses a callback interface to allow custom actions (e.g., device check, analytics).
 */
public class SwitchFamily {
    private final AndroidDriver atomberg;

    // === Locators ===
    private static final By VIEW_ELEMENT = By.className("android.view.View");

    public SwitchFamily(AndroidDriver driver) {
        this.atomberg = driver;
        this.atomberg.manage().timeouts().implicitlyWait(Duration.ofSeconds(6));
    }

    /**
     * Switches between all families and executes the intermediate function in each.
     */
    public void switchFamily() {
        String currentFamilyName = getCurrentFamilyName();
        if (currentFamilyName == null) {
            System.err.println("Failed to get current family name.");
            return;
        }


        List<String> familyNames = getAvailableFamilyNames();
        if (familyNames.isEmpty()) {
            System.out.println("No additional families found.");
            return;
        }

        for (int i = 0; i < familyNames.size(); i++) {
            String targetFamily = familyNames.get(i);
            System.out.println("Switching to family: " + targetFamily);

            if (Objects.equals(targetFamily, currentFamilyName)) {
                System.out.println("Already on family: " + targetFamily);
                atomberg.navigate().back();
            } else {
                if (selectFamily(targetFamily)) {
                    System.out.println(targetFamily + " selected");
                } else {
                    System.err.println("Failed to select family: " + targetFamily);
                    continue;
                }
            }

            // Prepare for next iteration
            if (i < familyNames.size() - 1) {
                currentFamilyName = waitForFamilyHeader(5); // Refresh current family name
                if (currentFamilyName != null) {
                    clickFamilyHeader();
                }
                ActionsUtil.sleep(2000);
            }
        }
    }

    // === Internal Helpers ===

    /**
     * Gets the currently displayed family name from top header.
     */
    private String getCurrentFamilyName() {
        List<WebElement> elements = findVisibleViews();
        if (!elements.isEmpty()) {
            try {
                String desc = elements.get(0).getDomAttribute("content-desc");
                System.out.println("Current Family: " + desc);
                return desc;
            } catch (Exception e) {
                System.err.println("Error reading current family name: " + e.getMessage());
            }
        }
        return null;
    }

    /**
     * Clicks the family header to open family list.
     */
    private void clickFamilyHeader() {
        List<WebElement> elements = findVisibleViews();
        if (!elements.isEmpty()) {
            try {
                elements.get(0).click();
            } catch (Exception e) {
                System.err.println("Failed to click family header: " + e.getMessage());
            }
        }
    }

    /**
     * Gets list of available family names from dropdown.
     */
    private List<String> getAvailableFamilyNames() {
        ActionsUtil.sleep(2500); // Allow UI load

        List<WebElement> rawViews = findVisibleViews();
        List<String> familyNames = rawViews.stream()
                .map(this::safeGetContentDesc)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        // Remove last two items (typically "Cancel" and separator)
        while (familyNames.size() > 2) {
            familyNames.remove(familyNames.size() - 1);
        }

        System.out.println("Number of families present = " + familyNames.size());
        return familyNames;
    }

    /**
     * Safely gets content-desc attribute.
     */
    private String safeGetContentDesc(WebElement element) {
        try {
            return element.getDomAttribute("content-desc");
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Finds all visible android.view.View elements with non-null content-desc.
     */
    private List<WebElement> findVisibleViews() {
        atomberg.findElement(VIEW_ELEMENT).click();
        ActionsUtil.SSleep(1);
        return atomberg.findElements(VIEW_ELEMENT).stream()
                .filter(el -> {
                    try {
                        return el.isDisplayed() && el.getDomAttribute("content-desc") != null;
                    } catch (Exception e) {
                        return false;
                    }
                })
                .collect(Collectors.toList());
    }

    /**
     * Selects a family by name.
     */
    private boolean selectFamily(String familyName) {
        List<WebElement> families = findVisibleViews();
        return families.stream()
                .anyMatch(el -> Objects.equals(safeGetContentDesc(el), familyName) && clickIfClickable(el));
    }

    /**
     * Clicks element only if clickable.
     */
    private boolean clickIfClickable(WebElement el) {
        try {
            if (Boolean.parseBoolean(el.getDomAttribute("clickable"))) {
                el.click();
                return true;
            }
        } catch (Exception e) {
            System.err.println("Click failed: " + e.getMessage());
        }
        return false;
    }

    /**
     * Waits briefly for family header to reload and returns its name.
     */
    private String waitForFamilyHeader(int maxRetries) {
        for (int i = 0; i < maxRetries; i++) {
            ActionsUtil.sleep(500);
            String name = getCurrentFamilyName();
            if (name != null) return name;
        }
        return null;
    }
}