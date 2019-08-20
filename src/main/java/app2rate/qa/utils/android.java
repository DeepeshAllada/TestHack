package app2rate.qa.utils;

import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.touch.TouchActions;

import com.relevantcodes.extentreports.ExtentTest;

import app2rate.qa.base.Base;
import io.appium.java_client.TouchAction;
import io.appium.java_client.android.AndroidDriver;

public class android extends Base{
	
	
	public static long COMMAND_TIMEOUT = 4000;
	public static long IMPLICIT_WAIT = 20;
	public static String appiumPort = "4723";
	public static String serverIp = "127.0.0.1";
	
	public android(ExtentTest logger, AndroidDriver androidDriver) {
		this.androidDriver=androidDriver;
		this.logger=logger;

	}

	public void swipeUp() throws InterruptedException{
		Dimension size = androidDriver.manage().window().getSize();
		System.out.println(size);
		int starty = (int) (size.height * 0.80);
		int endy = (int) (size.height * 0.20);
		int startx = (int)(size.width * 0.05);
		androidDriver.swipe(startx, starty, startx, endy, 500);

	}

	public void swipeDown() throws InterruptedException{
		Dimension size = androidDriver.manage().window().getSize();
		System.out.println(size);
		int starty = (int) (size.height * 0.80);
		int endy = (int) (size.height * 0.20);
		int startx = size.width / 2;
		androidDriver.swipe(startx, endy, startx,starty , 3000);
		Thread.sleep(5000);
	}

	public void scrollTo(WebElement elm){
		TouchActions action = new TouchActions(androidDriver);
		action.scroll(elm, 10, 100);
		action.perform();
	}
	public void swipetoptoBottom() throws Exception
	{
		Thread.sleep(5000);
		org.openqa.selenium.Dimension size = androidDriver.manage().window().getSize();

		System.out.println(size);
		int starty = (int) (size.height * 0.80);
		int endy = (int) (size.height * 0.20);
		int startx = size.width / 2;
		androidDriver.swipe(startx, starty, startx, endy, 10000); 			
		Thread.sleep(10000);

	}


	public void swiperighttoLeft() throws Exception
	{
		Thread.sleep(5000);
		Dimension size = androidDriver.manage().window().getSize();

		System.out.println(size);
		int startx = (int) (size.width * 0.90);
		int endx = (int) (size.width * 0.09);
		int starty = size.height / 2;
		androidDriver.swipe(startx, starty, endx, starty, 1000); 			
		Thread.sleep(10000);

	}


	public void swipelefttoRight() throws Exception
	{
		Thread.sleep(5000);
		Dimension size = androidDriver.manage().window().getSize();

		System.out.println(size);
		int startx = (int) (size.width * 0.90);
		int endx = (int) (size.width * 0.09);
		int starty = size.height / 2;
		androidDriver.swipe(endx, starty, startx, starty, 10000); 			
		Thread.sleep(10000);

	}
	public void scrolltillvisibleText(String visibleText) {
		androidDriver.findElementByAndroidUIAutomator("new UiScrollable(new UiSelector().scrollable(true).instance(0)).scrollIntoView(new UiSelector().textContains(\""+visibleText+"\").instance(0))").click();
	}
	public   void selectEntertainOption(String EntertainOption) throws Exception{

		System.out.println(EntertainOption);

		if(EntertainOption.equals("Music & Dance"))
		{
			for(int i=1; i<=1;i++)
				swiperighttoLeft();
		}

		if(EntertainOption == "Music & Dance")
		{
			for(int i=1; i<=1;i++)
				swiperighttoLeft();
		}

		else if(EntertainOption == "Events")
		{
			for(int i=1; i<=2;i++)
				swiperighttoLeft();
		}

		else if(EntertainOption == "Shows & Movies	")
		{
			for(int i=1; i<=3;i++)
				swiperighttoLeft();
		}

		else if(EntertainOption == "Casino")
		{
			for(int i=1; i<=4;i++)
				swiperighttoLeft();
		}


		else if(EntertainOption == "Character Meet & Greet")
		{
			for(int i=1; i<=5;i++)
				swiperighttoLeft();
		}


		else if(EntertainOption.equals("Parades"))
		{
			for(int i=1; i<=6;i++)
				swiperighttoLeft();
		}	

	}
	public   void selectDiningOption(String ResturantOption) throws Exception{

		if(ResturantOption.equals("Restaurants"))
		{
			for(int i=1; i<=1;)
				break;
		}

		if(ResturantOption.equals("Bar & Lounges"))
		{
			for(int i=1; i<=2;i++)
				swiperighttoLeft();
		}

		if(ResturantOption.equals("Coffee & Snacks"))
		{
			for(int i=1; i<=3;i++)
				swiperighttoLeft();
		}

	}
	
	public   void swipeBottomToTopOnElement(WebElement element,int x,int y)
	{
		
		TouchAction touch=new TouchAction(androidDriver);
		touch.longPress(element).moveTo(x,y).release().perform();
		log.info("Swiped on Activity Planner");
		
	}

}
