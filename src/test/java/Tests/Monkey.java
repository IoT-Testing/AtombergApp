//import app.Login.Email;
//
//import static app.Resources.Credentials.*;
//
//@Test
//void testMonkey() {
//	// ✅ Fixed: Use Supplier<String> with lambda
//	extent.createTest(() -> "Monkey Test");
//
//	openAtomberg();
//
//	Email login = new Email(driver);
//	try {
//		login.email(DEFAULT_EMAIL, DEFAULT_PASSWORD);
//	} catch (Exception e) {
//		System.err.println("Login failed: " + e.getMessage());
//	}
//
//	String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
//	System.out.println("Timestamp: " + timestamp);
//	System.out.println("Monkey Started....");
//
//	// Run Android Monkey stress test
//	driver.executeScript("mobile:shell", Map.of(
//			"command", "monkey",
//			"args", "-p com.atomberg.app --throttle 50 -v 1000"
//	));
//
//	System.out.println("Monkey finished....");
//
//	driver.quit();
//	extent.flush(); // Save report
//}