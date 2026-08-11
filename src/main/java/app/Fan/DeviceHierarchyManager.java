package app.Fan;

import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import java.util.*;

/**
 * DeviceHierarchyManager - Handles parent device and child element relationships.
 * Manages device hierarchy where:
 * - Parent Device: Aris Fan (1 child element, Offline status)
 * - Parent Device: Aris Gladius Fan (4 child elements, Online status)
 * Provides methods to verify device status and interact with child elements.
 */
public class DeviceHierarchyManager {
    private final AndroidDriver driver;

    // Device status enum
    public enum DeviceStatus {
        ONLINE("Online"),
        OFFLINE("Offline");

        private final String displayName;

        DeviceStatus(String displayName) {
            this.displayName = displayName;
        }

        public String getDisplayName() {
            return displayName;
        }
    }

    public enum DeviceType {
        FAN("Fan"),
        LOCK("Lock"),
        PURIFIER("Purifier");

        private final String displayType;

        DeviceType(String displayType) {
            this.displayType = displayType;
        }

        public String getDisplayType() {
            return displayType;
        }
    }


    /**
     * Represents a device with its hierarchy and status
     */
    public static class DeviceInfo {
        private final String deviceName;
        private final String modelName;
        private final DeviceStatus status;
        private final List<ChildElement> childElements;
        private final WebElement deviceElement;

        public DeviceInfo(String deviceName, String modelName, DeviceStatus status,
                         WebElement deviceElement) {
            this.deviceName = deviceName;
            this.modelName = modelName;
            this.status = status;
            this.deviceElement = deviceElement;
            this.childElements = new ArrayList<>();
        }

        // Getters and utility methods
        public String getDeviceName() { return deviceName; }
        public String getModelName() { return modelName; }
        public DeviceStatus getStatus() { return status; }
        public List<ChildElement> getChildElements() { return childElements; }
        public WebElement getDeviceElement() { return deviceElement; }
        public int getChildCount() { return childElements.size(); }

        public void addChild(ChildElement child) {
            this.childElements.add(child);
        }

        public boolean isOnline() {
            return this.status == DeviceStatus.ONLINE;
        }

        public boolean isOffline() {
            return this.status == DeviceStatus.OFFLINE;
        }

        @Override
        public String toString() {
            return String.format(
                "Device{name='%s', model='%s', status=%s, children=%d}",
                deviceName, modelName, status.getDisplayName(), childElements.size()
            );
        }
    }

    /**
     * Represents a child element within a device
     */
    public static class ChildElement {
        private final String elementId;
        private final String elementName;
        private final WebElement webElement;
        private final int index;

        public ChildElement(String elementId, String elementName, WebElement webElement, int index) {
            this.elementId = elementId;
            this.elementName = elementName;
            this.webElement = webElement;
            this.index = index;
        }

        public String getElementId() { return elementId; }
        public String getElementName() { return elementName; }
        public WebElement getWebElement() { return webElement; }
        public int getIndex() { return index; }

        @Override
        public String toString() {
            return String.format("Child{id='%s', name='%s', index=%d}", elementId, elementName, index);
        }
    }

    public DeviceHierarchyManager(AndroidDriver driver) {
        this.driver = driver;
    }

    // ═══════════════════════════════════════════════════════════════════════
    // PRIMARY METHODS: Device Status Verification
    // ═══════════════════════════════════════════════════════════════════════

    /**
     * Verifies if a device is ONLINE or OFFLINE
     * @param deviceElement The device WebElement to check
     * @return DeviceStatus (ONLINE or OFFLINE)
     */
    public DeviceStatus verifyDeviceStatus(WebElement deviceElement) {
        try {
            int childCount = getChildElementCount(deviceElement);

            if (childCount > 1) {
                System.out.println("✅ Device Status: ONLINE (child elements: " + childCount + ")");
                return DeviceStatus.ONLINE;
            } else {
                System.out.println("❌ Device Status: OFFLINE (child elements: " + childCount + ")");
                return DeviceStatus.OFFLINE;
            }
        } catch (Exception e) {
            System.err.println("Error verifying device status: " + e.getMessage());
            return DeviceStatus.OFFLINE;
        }
    }

    /**
     * Checks for visual online indicator (green dot, checkmark, etc.)
     * @param deviceElement The device element to inspect
     * @return true if online indicator is visible
     */
    private boolean hasOnlineIndicator(WebElement deviceElement) {
        try {
            // Common XPath patterns for online indicators
            List<String> indicatorPatterns = Arrays.asList(
                ".//android.widget.ImageView[@content-desc='Online' or contains(@content-desc, 'online')]",
                ".//android.view.View[@content-desc='Online' or contains(@content-desc, 'online')]",
                ".//android.widget.ImageView[contains(@resource-id, 'status') or contains(@resource-id, 'online')]"
            );

            for (String pattern : indicatorPatterns) {
                List<WebElement> indicators = deviceElement.findElements(By.xpath(pattern));
                if (!indicators.isEmpty()) {
                    System.out.println("Found online indicator: " + pattern);
                    return true;
                }
            }

            return false;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Verifies device status by comparing with parent device info
     * @param deviceName Name of the device
     * @param expectedStatus Expected status
     * @return true if device status matches expected status
     */
    public boolean verifyDeviceStatusById(String deviceName, DeviceStatus expectedStatus) {
        try {
            By deviceLocator = By.xpath(
                String.format("//android.widget.Button[contains(@content-desc, '%s')]", deviceName)
            );

            WebElement device = driver.findElement(deviceLocator);
            DeviceStatus actualStatus = verifyDeviceStatus(device);

            boolean matches = actualStatus == expectedStatus;
            System.out.println(String.format(
                "Device '%s' - Expected: %s, Actual: %s - %s",
                deviceName, expectedStatus.getDisplayName(), actualStatus.getDisplayName(),
                matches ? "✅ PASS" : "❌ FAIL"
            ));

            return matches;
        } catch (Exception e) {
            System.err.println("Error verifying device status by ID: " + e.getMessage());
            return false;
        }
    }

    // ═══════════════════════════════════════════════════════════════════════
    // SECONDARY METHODS: Child Element Management
    // ═══════════════════════════════════════════════════════════════════════

    /**
     * Retrieves all child elements of a device
     * @param deviceElement The parent device element
     * @return List of ChildElement objects
     */
    public List<ChildElement> getChildElements(WebElement deviceElement) {
        List<ChildElement> children = new ArrayList<>();

        try {
            // Find all clickable child elements within the device
            List<WebElement> childElements = deviceElement.findElements(
                By.xpath(".//*[@clickable='true']")
            );

            System.out.println("Found " + childElements.size() + " child elements");

            for (int i = 0; i < childElements.size(); i++) {
                WebElement child = childElements.get(i);
                String childId = child.getDomAttribute("resource-id");
                String childName = child.getDomAttribute("content-desc");

                if (childName == null || childName.isEmpty()) {
                    childName = child.getText();
                }

                ChildElement childElement = new ChildElement(childId, childName, child, i);
                children.add(childElement);
                System.out.println("Child " + i + ": " + childElement);
            }

        } catch (Exception e) {
            System.err.println("Error retrieving child elements: " + e.getMessage());
        }

        return children;
    }

    /**
     * Gets count of child elements for a device
     * @param deviceElement The parent device element
     * @return Number of child elements
     */
    public int getChildElementCount(WebElement deviceElement) {
        return getChildElements(deviceElement).size();
    }

    /**
     * Clicks a specific child element by index
     * @param deviceElement The parent device element
     * @param childIndex Index of the child element to click
     * @return true if click was successful
     */
    public boolean clickChildElement(WebElement deviceElement, int childIndex) {
        try {
            List<ChildElement> children = getChildElements(deviceElement);

            if (childIndex >= 0 && childIndex < children.size()) {
                ChildElement child = children.get(childIndex);
                child.getWebElement().click();
                System.out.println("✅ Clicked child element: " + child.getElementName());
                return true;
            } else {
                System.err.println("❌ Child index " + childIndex + " out of bounds. Max: " + (children.size() - 1));
                return false;
            }
        } catch (Exception e) {
            System.err.println("Error clicking child element: " + e.getMessage());
            return false;
        }
    }

    /**
     * Clicks a specific child element by name
     * @param deviceElement The parent device element
     * @param childName Name of the child element to click
     * @return true if click was successful
     */
    public boolean clickChildElementByName(WebElement deviceElement, String childName) {
        try {
            List<ChildElement> children = getChildElements(deviceElement);

            for (ChildElement child : children) {
                if (child.getElementName().equalsIgnoreCase(childName)) {
                    child.getWebElement().click();
                    System.out.println("✅ Clicked child element: " + childName);
                    return true;
                }
            }

            System.err.println("❌ Child element '" + childName + "' not found");
            return false;
        } catch (Exception e) {
            System.err.println("Error clicking child element by name: " + e.getMessage());
            return false;
        }
    }

    // ═══════════════════════════════════════════════════════════════════════
    // COMPREHENSIVE DEVICE INFO RETRIEVAL
    // ═══════════════════════════════════════════════════════════════════════

    /**
     * Creates complete DeviceInfo object with all hierarchy and status info
     * @param deviceElement The device WebElement
     * @return DeviceInfo object with full details
     */
    public DeviceInfo buildDeviceInfo(WebElement deviceElement) {
        try {
            String deviceName = deviceElement.getDomAttribute("content-desc");
            String modelName = extractModelName(deviceElement);
            DeviceStatus status = verifyDeviceStatus(deviceElement);

            DeviceInfo deviceInfo = new DeviceInfo(deviceName, modelName, status, deviceElement);

            // Load all child elements
            List<ChildElement> children = getChildElements(deviceElement);
            for (ChildElement child : children) {
                deviceInfo.addChild(child);
            }

            System.out.println("Built device info: " + deviceInfo);
            return deviceInfo;
        } catch (Exception e) {
            System.err.println("Error building device info: " + e.getMessage());
            return null;
        }
    }

    /**
     * Extracts model name from device element
     * @param deviceElement The device element
     * @return Model name (e.g., "Aris Fan", "Aris Gladius Fan")
     */
    private String extractModelName(WebElement deviceElement) {
        try {
            // Try to find model info in content-desc or text
            String contentDesc = deviceElement.getDomAttribute("content-desc");

            if (contentDesc != null && contentDesc.contains("Model:")) {
                return contentDesc.split("Model:")[1].trim().split(",")[0];
            }

            // Alternative: look for model element nearby
            List<WebElement> modelElements = deviceElement.findElements(
                By.xpath(".//*[contains(text(), 'Model') or contains(@content-desc, 'Model')]")
            );

            if (!modelElements.isEmpty()) {
                return modelElements.get(0).getText();
            }

            return "Unknown Model";
        } catch (Exception e) {
            return "Unknown Model";
        }
    }

    // ═══════════════════════════════════════════════════════════════════════
    // BULK OPERATIONS
    // ═══════════════════════════════════════════════════════════════════════

    /**
     * Retrieves all devices with their complete information
     * @return List of all DeviceInfo objects
     */
    public List<DeviceInfo> getAllDevicesInfo() {
        List<DeviceInfo> devices = new ArrayList<>();

        try {
            // Find all device buttons on the home screen
            List<WebElement> deviceElements = driver.findElements(
                By.xpath("//android.widget.Button[contains(@content-desc, 'Fan') and not(contains(@content-desc, 'Add'))]")
            );

            System.out.println("Total devices found: " + deviceElements.size());

            for (WebElement device : deviceElements) {
                DeviceInfo info = buildDeviceInfo(device);
                if (info != null) {
                    devices.add(info);
                }
            }
        } catch (Exception e) {
            System.err.println("Error retrieving all devices: " + e.getMessage());
        }

        return devices;
    }

    /**
     * Filters devices by status
     * @param status The desired status (ONLINE or OFFLINE)
     * @return List of devices matching the status
     */
    public List<DeviceInfo> getDevicesByStatus(DeviceStatus status) {
        List<DeviceInfo> allDevices = getAllDevicesInfo();
        List<DeviceInfo> filtered = new ArrayList<>();

        for (DeviceInfo device : allDevices) {
            if (device.getStatus() == status) {
                filtered.add(device);
            }
        }

        System.out.println(String.format("Found %d %s devices", filtered.size(), status.getDisplayName()));
        return filtered;
    }

    /**
     * Generates detailed report of device hierarchy
     * @return String report of all devices and their children
     */
    public String generateHierarchyReport() {
        StringBuilder report = new StringBuilder();
        report.append("\n═══════════════════════════════════════════════════════════════\n");
        report.append("DEVICE HIERARCHY REPORT\n");
        report.append("═══════════════════════════════════════════════════════════════\n");

        List<DeviceInfo> devices = getAllDevicesInfo();

        for (DeviceInfo device : devices) {
            report.append(String.format("\n📱 %s\n", device.getDeviceName()));
            report.append(String.format("   Model: %s\n", device.getModelName()));
            report.append(String.format("   Status: %s\n", device.getStatus().getDisplayName()));
            report.append(String.format("   Child Elements: %d\n", device.getChildCount()));

            for (ChildElement child : device.getChildElements()) {
                report.append(String.format("      └─ %d. %s\n", child.getIndex(), child.getElementName()));
            }
        }

        report.append("\n═══════════════════════════════════════════════════════════════\n");
        System.out.println(report.toString());
        return report.toString();
    }
}
