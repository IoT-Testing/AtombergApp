package app.sharing;

/**
 * PermissionModel – Encapsulates the permission bit-mask architecture from
 * the Device Sharing & Permission Management feature specification (v1.0).
 * ── How bitmasks work ────────────────────────────────────────────────────────
 * Each capability maps to a fixed bit position per device model family.
 * A permission integer is the OR of all granted capability bits.
 * Example:
 *   Fan with Control (bit 0) + View Analytics (bit 2) = 0b00101 = 5
 *   FanPermission.basic().mask() == 1  (only Control bit set by default)
 * ── Spec section reference ────────────────────────────────────────────────────
 * Section 2.3 – Permission Levels (Super / Basic / Custom)
 * Section 3.1 – Fan Capabilities
 * Section 3.2 – Door Lock Capabilities
 * Section 3.3 – Gecko Door Lock Capabilities
 * Section 3.4 – Water Purifier Capabilities
 */
public final class PermissionModel {

    private PermissionModel() {}

    // ══════════════════════════════════════════════════════════════════════════
    // Fan Capabilities — studio_nexus, aris, renesa+, aris_wo_underlight,
    //                    aris_contour  (spec §3.1)
    // ══════════════════════════════════════════════════════════════════════════
    public enum FanCapability {
        CONTROL        (0, "Control",          true),   // bit 0 — basic default YES
        EDIT_DEVICE    (1, "Edit Device",      false),  // bit 1
        VIEW_ANALYTICS (2, "View Analytics",   false),  // bit 2
        EDIT_WIFI      (3, "Edit Wifi Details",false),  // bit 3
        AUTOMATIONS    (4, "Automations",      false);  // bit 4

        public final int  bit;
        public final String label;
        public final boolean basicDefault;

        FanCapability(int bit, String label, boolean basicDefault) {
            this.bit          = bit;
            this.label        = label;
            this.basicDefault = basicDefault;
        }

        public int mask() { return 1 << bit; }

        /** Returns the Basic permission mask for fans (only Control bit set). */
        public static int basicMask() {
            int m = 0;
            for (FanCapability c : values()) if (c.basicDefault) m |= c.mask();
            return m;   // = 1
        }

        /** Returns the Super permission mask for fans (all bits set). */
        public static int superMask() {
            int m = 0;
            for (FanCapability c : values()) m |= c.mask();
            return m;   // = 31
        }
    }

    // ══════════════════════════════════════════════════════════════════════════
    // Door Lock Capabilities — sitana, smart_lock_black  (spec §3.2)
    // ══════════════════════════════════════════════════════════════════════════
    public enum LockCapability {
        VIEW_KEYS_PINS          (0, "View Keys & Pins",          false),
        VIEW_SETTINGS           (1, "View Settings",             true),
        VIEW_HISTORY            (2, "View History",              true),
        UNLOCK                  (3, "Unlock",                    true),
        ADD_UNLOCK_METHODS      (4, "Add Unlock Methods",        false),
        MODIFY_PREFERENCES      (5, "Modify Preferences",        false),
        MODIFY_OTP_REFILL       (6, "Modify OTP Refill Settings",false),
        MODIFY_HISTORY_SETTINGS (7, "Modify History Settings",   false),
        EDIT_DEVICE             (8, "Edit Device",               false);

        public final int    bit;
        public final String label;
        public final boolean basicDefault;

        LockCapability(int bit, String label, boolean basicDefault) {
            this.bit          = bit;
            this.label        = label;
            this.basicDefault = basicDefault;
        }

        public int mask() { return 1 << bit; }

        public static int basicMask() {
            int m = 0;
            for (LockCapability c : values()) if (c.basicDefault) m |= c.mask();
            return m;   // bits 1,2,3 → 14
        }

        public static int superMask() {
            int m = 0;
            for (LockCapability c : values()) m |= c.mask();
            return m;   // = 511
        }
    }

    // ══════════════════════════════════════════════════════════════════════════
    // Gecko Door Lock Capabilities — cypheo_elite  (spec §3.3)
    // ══════════════════════════════════════════════════════════════════════════
    public enum GeckoLockCapability {
        VIEW_KEYS          (0, "View Keys",          false),
        VIEW_HISTORY       (1, "View History",       true),
        EDIT_WIFI          (2, "Edit Wifi Details",  false),
        MODIFY_PREFERENCES (3, "Modify Preferences", false),
        EDIT_DEVICE        (4, "Edit Device",        false);

        public final int    bit;
        public final String label;
        public final boolean basicDefault;

        GeckoLockCapability(int bit, String label, boolean basicDefault) {
            this.bit          = bit;
            this.label        = label;
            this.basicDefault = basicDefault;
        }

        public int mask() { return 1 << bit; }

        public static int basicMask() {
            int m = 0;
            for (GeckoLockCapability c : values()) if (c.basicDefault) m |= c.mask();
            return m;   // bit 1 → 2
        }

        public static int superMask() {
            int m = 0;
            for (GeckoLockCapability c : values()) m |= c.mask();
            return m;   // = 31
        }
    }

    // ══════════════════════════════════════════════════════════════════════════
    // Water Purifier Capabilities — intellon  (spec §3.4)
    // ══════════════════════════════════════════════════════════════════════════
    public enum PurifierCapability {
        CHECK_HEALTH   (0, "Check Health",    true),
        RUN_DIAGNOSTIC (1, "Run Diagnostics", false),
        EDIT_DEVICE    (2, "Edit Device",     false),
        EDIT_WIFI      (3, "Edit Wifi Details",false),
        MODE_CHANGE    (4, "Mode Change",     false),
        VALUE_REFRESH  (5, "Value Refresh",   true),
        SETTINGS       (6, "Settings",        false),
        VIEW_ANALYTICS (7, "View Analytics",  false);

        public final int    bit;
        public final String label;
        public final boolean basicDefault;

        PurifierCapability(int bit, String label, boolean basicDefault) {
            this.bit          = bit;
            this.label        = label;
            this.basicDefault = basicDefault;
        }

        public int mask() { return 1 << bit; }

        public static int basicMask() {
            int m = 0;
            for (PurifierCapability c : values()) if (c.basicDefault) m |= c.mask();
            return m;   // bits 0,5 → 33
        }

        public static int superMask() {
            int m = 0;
            for (PurifierCapability c : values()) m |= c.mask();
            return m;   // = 255
        }
    }

    // ══════════════════════════════════════════════════════════════════════════
    // Helper – PermissionLevel enum (spec §2.3)
    // ══════════════════════════════════════════════════════════════════════════
    public enum PermissionLevel {
        SUPER  ("Super"),
        BASIC  ("Basic"),
        CUSTOM ("Custom");

        public final String label;
        PermissionLevel(String label) { this.label = label; }
    }

    // ══════════════════════════════════════════════════════════════════════════
    // Utility: check whether a specific capability bit is set in a mask
    // ══════════════════════════════════════════════════════════════════════════
    public static boolean hasCapability(int permissionMask, int capabilityBit) {
        return (permissionMask & (1 << capabilityBit)) != 0;
    }

    /** Convenience: check FanCapability */
    public static boolean hasFanCapability(int mask, FanCapability cap) {
        return hasCapability(mask, cap.bit);
    }

    /** Convenience: check LockCapability */
    public static boolean hasLockCapability(int mask, LockCapability cap) {
        return hasCapability(mask, cap.bit);
    }
}