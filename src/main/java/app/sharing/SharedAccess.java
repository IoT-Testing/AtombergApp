package app.sharing;

import app.sharing.PermissionModel.FanCapability;
import app.sharing.PermissionModel.LockCapability;
import app.sharing.PermissionModel.PermissionLevel;
import app.sharing.PermissionModel.PurifierCapability;

/**
 * SharedAccess – the "store the set access to verify later" record for Flow 2.
 *
 * <p>When the admin long-presses a device tile and shares it at a given access
 * level, the exact access is captured here so the member phone can later assert
 * that the received permissions match. The expected permission bitmask is derived
 * from {@link PermissionModel} — this class holds no mask math of its own.</p>
 *
 * <pre>
 *   SharedAccess.superAccess(DeviceType.FAN)
 *   SharedAccess.basicAccess(DeviceType.LOCK)
 *   SharedAccess.customAccess(DeviceType.FAN, mask, "Control+Analytics")
 * </pre>
 */
public final class SharedAccess {

    /** The device family being shared. Maps to a {@link PermissionModel} capability set. */
    public enum DeviceType {
        FAN("Fan"),
        LOCK("Door Lock"),
        WATER_PURIFIER("Water Purifier");

        public final String label;
        DeviceType(String label) { this.label = label; }

        /** Basic-default mask for this family (spec §3 "Basic Default" columns). */
        public int basicMask() {
            return switch (this) {
                case FAN            -> FanCapability.basicMask();
                case LOCK           -> LockCapability.basicMask();
                case WATER_PURIFIER -> PurifierCapability.basicMask();
            };
        }

        /** Super mask for this family (all capability bits set). */
        public int superMask() {
            return switch (this) {
                case FAN            -> FanCapability.superMask();
                case LOCK           -> LockCapability.superMask();
                case WATER_PURIFIER -> PurifierCapability.superMask();
            };
        }
    }

    private final DeviceType deviceType;
    private final PermissionLevel level;
    private final int customMask;   // only meaningful when level == CUSTOM
    private final String label;     // human-readable, for reporting

    private SharedAccess(DeviceType deviceType, PermissionLevel level, int customMask, String label) {
        this.deviceType = deviceType;
        this.level      = level;
        this.customMask = customMask;
        this.label      = label;
    }

    // ── Factories ───────────────────────────────────────────────────────────────

    public static SharedAccess superAccess(DeviceType type) {
        return new SharedAccess(type, PermissionLevel.SUPER, type.superMask(), "Super");
    }

    public static SharedAccess basicAccess(DeviceType type) {
        return new SharedAccess(type, PermissionLevel.BASIC, type.basicMask(), "Basic");
    }

    /**
     * A Custom share with an explicit capability mask.
     *
     * @param type  device family
     * @param mask  OR of the granted capability bits (e.g.
     *              {@code FanCapability.CONTROL.mask() | FanCapability.VIEW_ANALYTICS.mask()})
     * @param label short description for the test report (e.g. "Control+Analytics")
     */
    public static SharedAccess customAccess(DeviceType type, int mask, String label) {
        return new SharedAccess(type, PermissionLevel.CUSTOM, mask, "Custom " + label);
    }

    // ── Accessors ─────────────────────────────────────────────────────────────

    public DeviceType deviceType()  { return deviceType; }
    public PermissionLevel level()  { return level; }
    public String label()           { return label; }

    /** The permission bitmask the member is expected to hold after this share. */
    public int expectedMask() {
        return switch (level) {
            case SUPER  -> deviceType.superMask();
            case BASIC  -> deviceType.basicMask();
            case CUSTOM -> customMask;
        };
    }

    /** True when the given capability bit is expected to be granted. */
    public boolean expectsBit(int bit) {
        return (expectedMask() & (1 << bit)) != 0;
    }

    /** True when the given fan capability is expected to be granted. */
    public boolean expectsFanCapability(FanCapability cap) {
        return expectsBit(cap.bit);
    }

    @Override
    public String toString() {
        return deviceType.label + " @ " + label + " (mask=" + expectedMask() + ")";
    }
}
