package app2rate.qa.page;

import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import com.relevantcodes.extentreports.ExtentTest;

import app2rate.qa.base.Base;
import io.appium.java_client.android.AndroidDriver;

public class Fn_AndroidLogin extends Base {

	// Page Factory - OR:

	@FindBy(id = "url_bar")
	public WebElement url;

	@FindBy(id = "next_button")
	public WebElement nextBtn;

	@FindBy(id = "positive_button")
	public WebElement positiveBtn;

	// Initializing the Page Objects:
	public Fn_AndroidLogin(ExtentTest logger, AndroidDriver androidDriver) {
		this.androidDriver = androidDriver;
		this.logger = logger;
		PageFactory.initElements(androidDriver, this);
	}

	// Actions:

	public void validatesChromePage() {
	
		url.sendKeys("google.com\n");
//		Actions action = new Actions(androidDriver);
//
//		action.sendKeys(Keys.ENTER).build().perform();
//		url.sendKeys(Keys.ENTER);
		log("cliked on cotinue button");
		/*
		nextBtn.click();
		log("");
		
		positiveBtn.click();
		log("");
		*/
		
		

	}

}
