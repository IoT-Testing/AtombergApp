# Atomberg App — Test Automation

Black-box UI automation for the **Atomberg Home** Android app (`com.atomberg.app`), a
Flutter application. Because the app is Flutter, the native tree exposes no
`resource-id`s — elements are located via **accessibility labels (`content-desc`)**,
text, and XPath through **Appium + UiAutomator2**.

For internet-connected devices (fan, water purifier), UI actions are cross-verified
against real device state via the Atomberg developer API. The smart lock is
Bluetooth-only and has no cloud state, so it is verified through the UI alone.

---

## Stack

| Piece | Value |
|---|---|
| Language / build | Java 17 (builds & runs on JDK 21) · Maven |
| Automation | Appium `java-client` 10.1.1 · Selenium 4.45.0 · UiAutomator2 |
| Runner | TestNG 7.12.0 |
| Device allocation | [appium-device-farm](https://github.com/AppiumTestDistribution/appium-device-farm) plugin |
| Reporting | ExtentReports 5.1.2 (Spark) + JSON→HTML dashboard |

---

## Prerequisites

- **JDK 21** (project targets 17; either works) and **Maven**
- **Node + Appium 3** with the device-farm plugin:
  ```bash
  npm install -g appium
  appium plugin install --source=npm appium-device-farm
  appium driver install uiautomator2
  ```
- **Android SDK / adb** on `PATH`, with one or more devices connected (`adb devices`)
- The Atomberg app installed on each device

---

## Setup

1. Copy the environment template and fill in real values:
   ```bash
   cp test.env.example test.env
   ```
   Then load it before running:
   ```bash
   # Git Bash
   export $(grep -v '^#' test.env | xargs)
   ```
   Credentials are **never** committed — see [test.env.example](test.env.example) for the
   full list (login accounts, Wi-Fi provisioning, and the developer-API keys used by the
   state oracle).

2. (Optional) Provide the data-driven login pool `accounts.csv` locally
   (`email,password` per line — see [accounts.csv.example](accounts.csv.example)). It is
   git-ignored.

3. Build:
   ```bash
   mvn clean test-compile
   ```

---

## Running

### Appium server (device-farm)
Start the server with the checked-in config, which enables the device-farm plugin and
Appium-3 base path `/`:
```bash
appium --config server-config.json
```
The device-farm dashboard is at <http://127.0.0.1:4723/device-farm>.

### Option A — TestNG suite
Runs the full suite defined in [testng.xml](testng.xml):
```bash
mvn test
```
Device allocation is automatic — each `<test>` slot opens a session and the plugin
assigns a free connected device. For multi-device parallel runs, uncomment additional
slots in `testng.xml` and raise `thread-count` (one slot per phone).

### Option B — Standalone runner
[`Main.java`](src/main/java/app/Main.java) is a scriptable single-device flow (launch →
login → fan control) independent of TestNG. Requires `TEST_EMAIL` / `TEST_PASSWORD` in
the environment.

---

## Test classes

Active in the default `testng.xml` slot:

| Class | Covers |
|---|---|
| `OpenAppTest` | Launch + auto-login smoke |
| `LoginTest` | Valid/invalid login, logout, re-login |
| `AppTest` | Login → Analytics → More tab → logout |
| `FanTest` | Power, speeds 1–6, sleep, timer |
| `ManageProfileTest` | Profile edit |
| `ManageFamilyTest` | Family management entry |
| `QuickSmokeTest` | Device-farm session smoke |
| `DeviceProvTest` | Provisioning (hardware loop gated by `-DDEVICE_PROV_HARDWARE_ENABLED`) |

`SecondAppTest` is **quarantined** (pending a rewrite) and excluded from the suite.

---

## Device-state oracle (API cross-verification)

`app.api.DeviceStateVerifier` reads real device state from the Atomberg developer API to
confirm the effect of a UI action:

```java
DeviceStateVerifier verifier = new DeviceStateVerifier();
fan.setSpeed(3);
assert verifier.awaitState(deviceId, "speed", "3");   // polls through cloud lag
```

Fields: `power`, `speed`, `timer`, `led`, `sleep`. Requires `ATOMBERG_API_KEY`,
`ATOMBERG_REFRESH_TOKEN` (same account as the app login) and a device id. **Scope:** fan
and water purifier only — the BLE smart lock has no cloud state.

---

## Reporting

- **ExtentReports** → `reports/Atomberg<yyyy-MM-dd>/<timestamp>.html`, grouped per device slot.
- **Dashboard** — [`index.html`](src/main/java/app/index.html) renders the JSON that
  `DashboardReporter` writes to `test-results/dashboard-data.json`. Serve the folder (e.g.
  `python -m http.server`) or copy the JSON next to `index.html` — browsers block
  `file://` fetches, in which case the dashboard shows clearly-labelled sample data.

---

## CI

[`.github/workflows/tests.yml`](.github/workflows/tests.yml) runs **build + static
analysis only** — GitHub-hosted runners have no Android device, so the Appium suite cannot
execute there. To run the real suite in CI, register a **self-hosted runner** on the host
machine with the phones attached (a commented `device-tests` job shows the shape).

---

## Layout

```
src/main/java/app/
  api/               # DeviceStateVerifier + AtombergApiClient (state oracle)
  Fan/ Lock/ Login/  # screen action classes ("page objects")
  MoreTab/ sharing/ BLEOnlyFans/ WaterPurifier/ ...
  resources/Locators/Android/   # By locators, grouped by screen
  util/              # AppUtil, ActionsUtil, Navigation, ScreenRecording, ...
  Main.java          # standalone runner
src/test/java/com/appTest/
  tests/             # TestNG test classes (BaseTest = driver lifecycle)
  listeners/ models/ util/
server-config.json   # Appium device-farm config
testng.xml           # suite definition (device slots)
test.env.example     # environment template (copy to test.env)
```
