package app2rate.demo;

import java.util.concurrent.TimeUnit;

import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import app2rate.qa.base.Base;
import app2rate.qa.page.Fn_AndroidLogin;

public class SignIn extends Base {

	WebDriverWait wait;
	Fn_AndroidLogin androidLogin;

	@BeforeMethod
	public void Initialization() {
		setup("android");
		StartTesting(TestCaseName + "_" + Device);
		androidLogin = new Fn_AndroidLogin(logger, androidDriver);
		wait = new WebDriverWait(androidDriver, 100);
		androidDriver.manage().timeouts().implicitlyWait(100, TimeUnit.SECONDS);

	}

	@Test(priority = 1)
	public void logIn() throws Exception {
		androidDriver.manage().timeouts().implicitlyWait(90, TimeUnit.SECONDS);

		// Starting Login
		log("Logging in after Guest Account Creation - Complete");
		androidLogin.validatesChromePage();
	}
}
