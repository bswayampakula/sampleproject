package tests;

import org.junit.Assert;
import org.testng.Reporter;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import services.TestBase;

public class ITestListenerDemoEx extends TestBase {

	@BeforeTest
	public void launchApplication() {
		driver.get("http://www.facebook.com");
	}

	@Test
	public void testOne() {
		Reporter.log("testOne-Log Message one");
		Reporter.log("testOne-Log Message one moreeeeeeee;");
	}

	@Test
	public void testTwo() {
		Reporter.log("testTwo-Log Message Two;");

	}

	@Test
	public void testThree() {
		System.out.println("Test threeeeee");
		Reporter.log("testThree-Log Message Three;");
		Assert.assertTrue(false);
	}

	@Test
	public void testFour() {
		Reporter.log("testTwo-Log Message four;");

	}

	@Test
	public void testFive() {
		Reporter.log("testTwo-Log Message five;");
		Assert.assertTrue(false);
	}

	@AfterTest
	public void tearDown() {
		System.out.println(Reporter.getOutput());
		Reporter.getOutput(Reporter.getCurrentTestResult());
	}

}
