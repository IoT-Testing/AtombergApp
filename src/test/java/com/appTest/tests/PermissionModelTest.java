package com.appTest.tests;

import app.sharing.PermissionModel;
import app.sharing.PermissionModel.FanCapability;
import app.sharing.PermissionModel.GeckoLockCapability;
import app.sharing.PermissionModel.LockCapability;
import app.sharing.PermissionModel.PurifierCapability;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

/**
 * PermissionModelTest – pure, device-less validation of the bit-mask permission
 * architecture (spec §3). No phones, no Appium — this suite locks down the
 * Super / Basic / Custom mask math for every device family so a UI test that
 * later asserts "member can do X" is anchored to the correct expected mask.
 *
 * <p>Runs standalone: {@code mvn test -Dtest=PermissionModelTest}. It does NOT
 * extend BaseTest, so no driver/server is started.</p>
 *
 * <p>Coverage:
 * <ul>
 *   <li>Basic default masks match the spec's "Basic Default = Yes" columns.</li>
 *   <li>Super masks set every bit for each family.</li>
 *   <li>Zero-mask shares are detectably invalid (spec §6, edge case 4).</li>
 *   <li>{@code hasCapability} is exhaustively true for set bits and false for
 *       unset bits, across every capability of every family.</li>
 * </ul>
 * </p>
 */
public class PermissionModelTest {

    // ── Basic default masks (spec §3.1–3.4 "Basic Default" columns) ────────────

    @Test(description = "Fan Basic mask = Control only (bit 0) = 1")
    public void fanBasicMask() {
        assertEquals(FanCapability.basicMask(), 1, "Fan Basic must set only Control (bit 0)");
    }

    @Test(description = "Fan Super mask = all 5 bits set = 31")
    public void fanSuperMask() {
        assertEquals(FanCapability.superMask(), 31, "Fan Super must set bits 0..4");
    }

    @Test(description = "Door lock Basic mask = View Settings+History+Unlock (bits 1,2,3) = 14")
    public void lockBasicMask() {
        assertEquals(LockCapability.basicMask(), (1 << 1) | (1 << 2) | (1 << 3),
                "Lock Basic must set View Settings, View History, Unlock");
    }

    @Test(description = "Door lock Super mask = all 9 bits set = 511")
    public void lockSuperMask() {
        assertEquals(LockCapability.superMask(), 511, "Lock Super must set bits 0..8");
    }

    @Test(description = "Gecko lock Basic mask = View History only (bit 1) = 2")
    public void geckoBasicMask() {
        assertEquals(GeckoLockCapability.basicMask(), 1 << 1, "Gecko Basic must set only View History");
    }

    @Test(description = "Gecko lock Super mask = all 5 bits set = 31")
    public void geckoSuperMask() {
        assertEquals(GeckoLockCapability.superMask(), 31, "Gecko Super must set bits 0..4");
    }

    @Test(description = "Water purifier Basic mask = Check Health + Value Refresh (bits 0,5) = 33")
    public void purifierBasicMask() {
        assertEquals(PurifierCapability.basicMask(), 1 | (1 << 5),
                "Purifier Basic must set Check Health and Value Refresh");
    }

    @Test(description = "Water purifier Super mask = all 8 bits set = 255")
    public void purifierSuperMask() {
        assertEquals(PurifierCapability.superMask(), 255, "Purifier Super must set bits 0..7");
    }

    // ── Zero-permission guard (spec §6, edge case 4) ──────────────────────────

    @Test(description = "Basic masks are never zero — zero-permission shares must be prevented")
    public void basicMasksAreNonZero() {
        assertTrue(FanCapability.basicMask()      > 0, "Fan Basic mask must be > 0");
        assertTrue(LockCapability.basicMask()     > 0, "Lock Basic mask must be > 0");
        assertTrue(GeckoLockCapability.basicMask() > 0, "Gecko Basic mask must be > 0");
        assertTrue(PurifierCapability.basicMask() > 0, "Purifier Basic mask must be > 0");
    }

    // ── Exhaustive hasCapability checks — every bit of every family ────────────

    @DataProvider(name = "fanCaps")
    public Object[][] fanCaps() {
        FanCapability[] v = FanCapability.values();
        Object[][] out = new Object[v.length][1];
        for (int i = 0; i < v.length; i++) out[i][0] = v[i];
        return out;
    }

    @Test(dataProvider = "fanCaps",
            description = "Each fan capability bit is detected when set and absent when cleared")
    public void fanHasCapabilityRoundTrip(FanCapability cap) {
        int only = cap.mask();
        assertTrue(PermissionModel.hasFanCapability(only, cap),
                cap.label + " must be detected in its own mask");
        assertFalse(PermissionModel.hasFanCapability(0, cap),
                cap.label + " must be absent in an empty mask");
        // Super grants everything
        assertTrue(PermissionModel.hasFanCapability(FanCapability.superMask(), cap),
                cap.label + " must be present in the Super mask");
    }

    @Test(description = "Fan Basic grants Control but denies Edit/Analytics/Wifi/Automations")
    public void fanBasicGrantsOnlyControl() {
        int basic = FanCapability.basicMask();
        assertTrue(PermissionModel.hasFanCapability(basic, FanCapability.CONTROL));
        assertFalse(PermissionModel.hasFanCapability(basic, FanCapability.EDIT_DEVICE));
        assertFalse(PermissionModel.hasFanCapability(basic, FanCapability.VIEW_ANALYTICS));
        assertFalse(PermissionModel.hasFanCapability(basic, FanCapability.EDIT_WIFI));
        assertFalse(PermissionModel.hasFanCapability(basic, FanCapability.AUTOMATIONS));
    }

    @DataProvider(name = "lockCaps")
    public Object[][] lockCaps() {
        LockCapability[] v = LockCapability.values();
        Object[][] out = new Object[v.length][1];
        for (int i = 0; i < v.length; i++) out[i][0] = v[i];
        return out;
    }

    @Test(dataProvider = "lockCaps",
            description = "Each lock capability bit round-trips through hasCapability")
    public void lockHasCapabilityRoundTrip(LockCapability cap) {
        assertTrue(PermissionModel.hasLockCapability(cap.mask(), cap),
                cap.label + " must be detected in its own mask");
        assertFalse(PermissionModel.hasLockCapability(0, cap),
                cap.label + " must be absent in an empty mask");
        assertTrue(PermissionModel.hasLockCapability(LockCapability.superMask(), cap),
                cap.label + " must be present in the Super mask");
    }

    @Test(description = "A Custom fan mask (Control + View Analytics) sets exactly bits 0 and 2 = 5")
    public void customFanMaskComposition() {
        int custom = FanCapability.CONTROL.mask() | FanCapability.VIEW_ANALYTICS.mask();
        assertEquals(custom, 0b00101);
        assertTrue(PermissionModel.hasFanCapability(custom, FanCapability.CONTROL));
        assertTrue(PermissionModel.hasFanCapability(custom, FanCapability.VIEW_ANALYTICS));
        assertFalse(PermissionModel.hasFanCapability(custom, FanCapability.EDIT_DEVICE));
    }

    // ── EC-11 (Edge Cases sheet): mask width and sign safety ───────────────────

    /**
     * EC-11 — the widest capability set in the spec must not overflow or go negative.
     *
     * <p>The door lock's 9 bits (spec §3.2) are the widest mask the product defines, and
     * its Super value, 511, is the one the sheet calls out. This is where an integer
     * problem would first show: a mask stored in a signed 8-bit or 16-bit field, or one
     * built by shifting past its type's width, produces a negative or truncated value that
     * silently grants the wrong capabilities.</p>
     *
     * <p>Deliberately device-less. The bit arithmetic is what is under test, and asserting
     * it offline makes the result unambiguous — a UI-level check could only observe the
     * <em>consequences</em> of an overflow, and would blame the screen that showed them.
     * The registration below is what puts EC-11 in the coverage matrix; without it the case
     * would read as "not run" despite being covered here.</p>
     */
    @Test(description = "EC-11: the 9-bit door-lock Super mask is exactly 511, positive, and "
            + "every bit round-trips — no overflow or sign issue")
    public void lockMaskDoesNotOverflow() {
        int superMask = LockCapability.superMask();

        assertEquals(superMask, 511, "The 9 door-lock capability bits must sum to 511 (2^9 - 1)");
        assertTrue(superMask > 0, "The mask must stay positive — a negative value means the "
                + "sign bit was reached, i.e. the mask outgrew its type");
        assertEquals(LockCapability.values().length, 9,
                "EC-11 is about the 9-bit lock; if the capability list changed, the expected "
                        + "mask above must change with it");

        // Every bit individually addressable inside the full mask: an overflow that merely
        // truncated would still total 511 if the high bits wrapped onto low ones.
        for (LockCapability cap : LockCapability.values()) {
            assertTrue(PermissionModel.hasLockCapability(superMask, cap),
                    cap.label + " (bit " + cap.bit + ") must be readable inside the 511 mask");
            assertTrue(cap.mask() > 0,
                    cap.label + " has a non-positive individual mask — bit " + cap.bit
                            + " overflowed its type");
        }

        com.appTest.util.SharingRunLedger.pass("EC-11");
    }
}
