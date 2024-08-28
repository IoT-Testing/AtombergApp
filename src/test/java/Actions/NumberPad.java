package Actions;

import io.appium.java_client.ios.IOSDriver;
// number pad using co-ordinates of each element on the number pad
public class NumberPad {
	public static void one(IOSDriver driver){
		Tap.withCoordinates(driver,240,1725);
	}
	public static void two(IOSDriver driver){
		Tap.withCoordinates(driver,530,1725);
	}
	public static void three(IOSDriver driver){
		Tap.withCoordinates(driver,810,1725);
	}
	public static void four(IOSDriver driver){
		Tap.withCoordinates(driver,240,1945);
	}
	public static void five(IOSDriver driver){
		Tap.withCoordinates(driver,530,1945);
	}
	public static void six(IOSDriver driver){
		Tap.withCoordinates(driver,810,1945);
	}
	public static void seven(IOSDriver driver){
		Tap.withCoordinates(driver,240,2100);
	}
	public static void eight(IOSDriver driver){
		Tap.withCoordinates(driver,530,2100);
	}
	public static void nine(IOSDriver driver){
		Tap.withCoordinates(driver,810,2100);
	}
	public static void zero(IOSDriver driver){
		Tap.withCoordinates(driver,530,2250);
	}
	public static void clear(IOSDriver driver){
		Tap.withCoordinates(driver,240,2250);
	}
	public static void done(IOSDriver driver){
		Tap.withCoordinates(driver,810,2250);
	}
}
