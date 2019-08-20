package app2rate.qa.base;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.events.EventFiringWebDriver;
import org.testng.ITestResult;
import org.testng.Reporter;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;

import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;

import app2rate.qa.utils.WebUtil;
import app2rate.qa.utils.android;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.remote.AutomationName;
import io.appium.java_client.remote.MobileCapabilityType;

public class Base {

	public static Logger log = Logger.getLogger(Base.class);
	// browser stack parallel execution
	public String browser = "chrome";
	public String platform = "Local";
	public String Device = "";
	public String ship = "";
	public String TestCaseName = "";
	public String platformVersion ="";
	public String appPath; 	
	public String buildName = "";
	public String[] TestName;
	public static ExtentReports extent;
	public ExtentTest logger;
	public AndroidDriver androidDriver;
	public WebDriver driver;
	public static EventFiringWebDriver e_driver;
	public static Properties webProp = new Properties();
	public static Properties androidProp = new Properties();
	public static Properties androidCelebrityProp = new Properties();
	public DesiredCapabilities cap;
    String username = System.getenv("BROWSERSTACK_USERNAME");
    String accessKey = System.getenv("BROWSERSTACK_ACCESS_KEY");
	String browserstackLocal = System.getenv("BROWSERSTACK_LOCAL");
	String browserstackLocalIdentifier = System.getenv("BROWSERSTACK_LOCAL_IDENTIFIER");
	public  IOSDriver iosDriver;
	public  static Properties iosProp = new Properties();
	public  static Properties iosPropagent = new Properties();
	
	public ITestResult result;

	@BeforeClass
	@Parameters({ "Browser", "Device", "Platform", "AppPath", "BuildName", "PlatformVersion", "Ship" })
	public void platformSetup(@Optional String Browser, @Optional String Device, @Optional String Platform,
			@Optional String AppPath, @Optional String BuildName, @Optional String PlatformVersion,  @Optional String Ship) {
		if(AppPath!=null){
			this.browser = Browser;
			this.Device = Device;
			this.platform = Platform;
			this.appPath = AppPath;
			this.ship = Ship;
			this.buildName = BuildName;
			this.platformVersion = PlatformVersion;
		}

		TestName = this.getClass().getName().replace(".", "/").split("/");
		TestCaseName = TestName[TestName.length - 1];
	}

	public void loadConfig(Properties prop, String fileName) {
		try {
			FileInputStream ip = new FileInputStream(System.getProperty("user.dir") + "/src/main/java/app2rate"
					+ "/qa/config/" + fileName + "Config.properties");
			prop.load(ip);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void webDriverInitialization() {
	if(platform.equalsIgnoreCase("Local")){
		if (browser.equals("firefox")) {
			//System.setProperty("webdriver.gecko.driver","src/main/resources/geckodriver.exe");
			driver = new FirefoxDriver();
		} else if (browser.equals("IE")) {
			//System.setProperty("webdriver.ie.driver", "src/main/resources/IEDriverServer.exe");
			DesiredCapabilities capabilities = DesiredCapabilities.internetExplorer();
			capabilities.setCapability("ignoreZoomSetting", true);
			capabilities.setCapability("ignoreProtectedModeSettings", true);
			driver = new InternetExplorerDriver(capabilities);
		} else {
			System.setProperty("webdriver.chrome.driver", "src/main/resources/chromedriver.exe");
			driver = new ChromeDriver();
		}
		e_driver = new EventFiringWebDriver(driver);
		driver = e_driver;
		driver.manage().window().maximize();
		driver.manage().deleteAllCookies();
		driver.manage().timeouts().pageLoadTimeout(WebUtil.PAGE_LOAD_TIMEOUT, TimeUnit.SECONDS);
		driver.manage().timeouts().implicitlyWait(WebUtil.IMPLICIT_WAIT, TimeUnit.SECONDS);
		}
		else{
			DesiredCapabilities caps = new DesiredCapabilities();
			caps.setCapability("os", "Windows");
			caps.setCapability("os_version", "10");
			caps.setCapability("browser", "Chrome");
			caps.setCapability("browser_version", "62.0");
			caps.setCapability("build",buildName);
          	caps.setCapability("name", TestCaseName);
          	caps.setCapability("browserstack.debug", "true");
			caps.setCapability("browserstack.selenium_version", "3.4.0");
			//caps.setCapability("browserstack.local", "true");
          // caps.setCapability("browserstack.user", webProp.getProperty("BrowserStack.user"));
			//caps.setCapability("browserstack.key", webProp.getProperty("BrowserStack.key"));
          	caps.setCapability("browserstack.local",  browserstackLocal); 
           // caps.setCapability("browserstack.localIdentifier", browserstackLocalIdentifier); 
			String browserStackUrl = "https://" + username + ":"
					+ accessKey + "@" + webProp.getProperty("BrowserStack.server")
					+ "/wd/hub";
			try {
				driver = new RemoteWebDriver(new URL(browserStackUrl), caps);
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
          
          
	public void androidDriverInitialization(Properties prop) {
		cap = new DesiredCapabilities();
		if (platform.equalsIgnoreCase("BS")) {
			cap.setCapability("fullReset", prop.getProperty("BrowserStack.noReset"));
			cap.setCapability("device", Device);
			cap.setCapability("browserstack.debug", prop.getProperty("BrowserStack.debug"));
			cap.setCapability("app", appPath);
//			cap.setCapability("browserstack.local", prop.getProperty("BrowserStack.local"));
//		    cap.setCapability("browserstack.local", browserstackLocal);
			cap.setCapability("browserstack.local", false);
//     		cap.setCapability("browserstack.localIdentifier", browserstackLocalIdentifier);
			cap.setCapability("build", buildName);
			cap.setCapability("name", TestCaseName);
			cap.setCapability("browserstack.networkLogs", true);
			cap.setCapability("browserstack.user", prop.getProperty("BrowserStack.user"));
			cap.setCapability("browserstack.key", prop.getProperty("BrowserStack.key"));

			String browserStackUrl = "https://" + prop.getProperty("BrowserStack.user") + ":"
					+ prop.getProperty("BrowserStack.key") + "@" + prop.getProperty("BrowserStack.server")
					+ "/wd/hub";
			try {
				androidDriver = new AndroidDriver(new URL(browserStackUrl), cap);
			} catch (MalformedURLException ex) {
				throw new RuntimeException("appium driver could not be initialised for device ");
			}
			System.out.println("Driver in initdriver is : " + androidDriver);
		} /*else if(platform.equalsIgnoreCase("DeviceConnect")){
			
			cap.setCapability("deviceConnectUserName", prop.getProperty("DeviceConnect.deviceConnectUserName"));
			cap.setCapability("deviceConnectApiKey", prop.getProperty("DeviceConnect.deviceConnectApiKey"));
			cap.setCapability("udid", prop.getProperty("DeviceConnect.udid"));
			cap.setCapability("platformName", prop.getProperty("DeviceConnect.platformName"));
			cap.setCapability("automationName", prop.getProperty("DeviceConnect.automationName"));
			cap.setCapability("bundleId", prop.getProperty("DeviceConnect.bundleId"));
			cap.setCapability("deviceConnectPassword", prop.getProperty("DeviceConnect.password"));
			//cap.setCapability("bundleId", prop.getProperty("DeviceConnect.bundleId"));
		}*/
		else{
			cap.setCapability(MobileCapabilityType.PLATFORM, prop.getProperty("platformName"));
				if(appPath!=null){
					cap.setCapability("deviceName", Device);
					cap.setCapability("app", appPath);
					cap.setCapability("platformVersion",platformVersion);
					
				}
				else{    
					
					cap.setCapability(MobileCapabilityType.DEVICE_NAME, prop.getProperty("deviceName"));
					cap.setCapability(MobileCapabilityType.APP, prop.getProperty("app"));
					cap.setCapability(MobileCapabilityType.PLATFORM_VERSION, prop.getProperty("platformVersion"));
					
				}
			
			cap.setCapability(MobileCapabilityType.NEW_COMMAND_TIMEOUT, android.COMMAND_TIMEOUT);
			cap.setCapability("noReset", prop.getProperty("noReset"));
			cap.setCapability("appPackage", prop.getProperty("appPackage"));
			cap.setCapability("appActivity", prop.getProperty("appActivity"));
//			cap.setCapability("autoAcceptAlerts", prop.getProperty("autoAcceptAlerts"));
//			cap.setCapability("autoGrantPermissions",prop.getProperty("autoGrantPermissions"));
			String serverUrl = "http://" + android.serverIp + ":" + android.appiumPort + "/wd/hub";
			
			//String serverUrl = "http://10.10.11.229:4723";
			try {
				
				androidDriver = new AndroidDriver(new URL(serverUrl), cap);
			} catch (MalformedURLException ex) {
				throw new RuntimeException("appium driver could not be initialised for device ");
			}
			System.out.println("Driver in initdriver is : " + androidDriver);
		}
    }
	

	public void iosDriverInitialization(Properties prop){

		cap = new DesiredCapabilities();
		System.out.println("Platform" + platform);
		if (platform.equalsIgnoreCase("Local") ){
			if(appPath!=null){
				cap.setCapability("deviceName", Device);
				cap.setCapability("app", appPath);
			}
			else{
				cap.setCapability(MobileCapabilityType.DEVICE_NAME, iosProp.getProperty("deviceName"));
				cap.setCapability(MobileCapabilityType.APP, iosProp.getProperty("app"));
				cap.setCapability(MobileCapabilityType.AUTOMATION_NAME, AutomationName.IOS_XCUI_TEST);
				cap.setCapability("xcodeOrgId", prop.getProperty("xcodeOrgId"));
				cap.setCapability("xcodeSigningId", prop.getProperty("xcodeSigningId"));
				cap.setCapability("autoAcceptAlerts",prop.getProperty("autoAcceptAlerts"));
			}
			cap.setCapability(MobileCapabilityType.PLATFORM, iosProp.getProperty("platformName"));
			cap.setCapability(MobileCapabilityType.AUTOMATION_NAME, iosProp.getProperty("automationName"));
			cap.setCapability(MobileCapabilityType.PLATFORM_VERSION, iosProp.getProperty("platformVersion"));
			cap.setCapability(MobileCapabilityType.UDID, iosProp.getProperty("udid"));
			cap.setCapability("noReset", iosProp.getProperty("noReset"));			
			cap.setCapability("realDeviceLogger", "/usr/local/bin/");
			String serverUrl = "http://127.0.0.1:4723/wd/hub";
			//String serverUrl = "http://" + android.serverIp + ":" + android.appiumPort + "/wd/hub";
			try {
				iosDriver = new IOSDriver(new URL(serverUrl), cap);
				iosDriver.manage().timeouts().implicitlyWait(WebUtil.IMPLICIT_WAIT, TimeUnit.SECONDS);

				//e_driver = new AppiumWebDriverEventListener();
			} catch (MalformedURLException ex) {
				throw new RuntimeException("appium driver could not be initialised for device ");
			}
			System.out.println("Driver in initdriver is : " + iosDriver);

		}
		else if (platform.equalsIgnoreCase("BS")) {
			cap.setCapability("fullReset", iosProp.getProperty("BrowserStack.noReset"));
			cap.setCapability("device", Device);
			// cap.setCapability(MobileCapabilityType.NEW_COMMAND_TIMEOUT,
			// android.COMMAND_TIMEOUT);
			// cap.setCapability("name",
			// androidProp.getProperty("BrowserStack.name"));
			cap.setCapability("browserstack.debug", iosProp.getProperty("BrowserStack.debug"));
			// cap.setCapability("app",
			// androidProp.getProperty("BrowserStack.app"));
			cap.setCapability("app", appPath);
			cap.setCapability("os_version", iosProp.getProperty("BrowserStack.os"));
			cap.setCapability("browserstack.local", iosProp.getProperty("BrowserStack.local"));
			// cap.setCapability("build",
			// androidProp.getProperty("BrowserStack.build"));
			cap.setCapability("build", buildName);
			cap.setCapability("name", TestCaseName);
			cap.setCapability("browserstack.user", iosProp.getProperty("BrowserStack.user"));
			cap.setCapability("browserstack.key", iosProp.getProperty("BrowserStack.key"));

			String browserStackUrl = "https://" + iosProp.getProperty("BrowserStack.user") + ":"
					+ iosProp.getProperty("BrowserStack.key") + "@" + iosProp.getProperty("BrowserStack.server")
					+ "/wd/hub";

			// String browserStackUrl =
			// "http://hub-cloud.browserstack.com/wd/hub";
			try {
				iosDriver = new IOSDriver(new URL(browserStackUrl), cap);
				// e_driver = new AppiumWebDriverEventListener();
			} catch (MalformedURLException ex) {
				throw new RuntimeException("appium driver could not be initialised for device ");
			}
			System.out.println("Driver in initdriver is : " + iosDriver);


		}
	}
	
	public void iosAgentappDriverInitialization(Properties prop){

		cap = new DesiredCapabilities();
		if (platform.equalsIgnoreCase("Local") ){
			System.out.println("platform : " + platform );
				if(appPath!=null){
					cap.setCapability("deviceName", Device);
					cap.setCapability("app", appPath);
				}
				else{
					cap.setCapability(MobileCapabilityType.DEVICE_NAME, iosPropagent.getProperty("deviceName"));
					cap.setCapability(MobileCapabilityType.APP, iosPropagent.getProperty("app"));
				}
			cap.setCapability(MobileCapabilityType.PLATFORM, iosPropagent.getProperty("platformName"));
			cap.setCapability(MobileCapabilityType.AUTOMATION_NAME, iosPropagent.getProperty("automationName"));
			cap.setCapability(MobileCapabilityType.PLATFORM_VERSION, iosPropagent.getProperty("platformVersion"));
			cap.setCapability("UDID", iosPropagent.getProperty("udid"));
			cap.setCapability("noReset", iosPropagent.getProperty("noReset"));			
			cap.setCapability("realDeviceLogger", "/usr/local/bin/");
			String serverUrl = "http://" + android.serverIp + ":" + android.appiumPort + "/wd/hub";
			//String serverUrl = "http://" + "127.0.0.1" + ":" + android.appiumPort + "/wd/hub";
			try {
				iosDriver = new IOSDriver(new URL(serverUrl), cap);
				iosDriver.manage().timeouts().implicitlyWait(WebUtil.IMPLICIT_WAIT, TimeUnit.SECONDS);

				//e_driver = new AppiumWebDriverEventListener();
			} catch (MalformedURLException ex) {
				throw new RuntimeException("appium driver could not be initialised for device ");
			}
			System.out.println("Driver in initdriver is : " + iosDriver);

		}
		else if (platform.equalsIgnoreCase("BS")) {
			cap.setCapability("fullReset", iosPropagent.getProperty("BrowserStack.noReset"));
			cap.setCapability("device", Device);
			// cap.setCapability(MobileCapabilityType.NEW_COMMAND_TIMEOUT,
			// android.COMMAND_TIMEOUT);
			// cap.setCapability("name",
			// androidProp.getProperty("BrowserStack.name"));
			cap.setCapability("browserstack.debug", iosPropagent.getProperty("BrowserStack.debug"));
			// cap.setCapability("app",
			// androidProp.getProperty("BrowserStack.app"));
			cap.setCapability("app", appPath);
			cap.setCapability("os_version", iosPropagent.getProperty("BrowserStack.os"));
			cap.setCapability("browserstack.local", iosPropagent.getProperty("BrowserStack.local"));
			cap.setCapability("real_mobile", iosPropagent.getProperty("BrowserStack.real_mobile"));
			
			cap.setCapability("browserstack.localIdentifier", iosPropagent.getProperty("browserstack.localIdentifier"));
			// cap.setCapability("build",
			// androidProp.getProperty("BrowserStack.build"));
			cap.setCapability("build", buildName);
            cap.setCapability("browserstack.local", browserstackLocal);
//			cap.setCapability("browserstack.localIdentifier", browserstackLocalIdentifier);
			cap.setCapability("name", TestCaseName);
			cap.setCapability("browserstack.user", iosPropagent.getProperty("BrowserStack.user"));
			cap.setCapability("browserstack.key", iosPropagent.getProperty("BrowserStack.key"));

			String browserStackUrl = "https://" + iosPropagent.getProperty("BrowserStack.user") + ":"
					+ iosPropagent.getProperty("BrowserStack.key") + "@" + iosPropagent.getProperty("BrowserStack.server")
					+ "/wd/hub";

			// String browserStackUrl =
			// "http://hub-cloud.browserstack.com/wd/hub";
			try {
				iosDriver = new IOSDriver(new URL(browserStackUrl), cap);
				// e_driver = new AppiumWebDriverEventListener();
			} catch (MalformedURLException ex) {
				throw new RuntimeException("appium driver could not be initialised for device ");
			}
			System.out.println("Driver in initdriver is : " + iosDriver);


		}
	}


	// Setup Method
	public void setup(String platform) {
		switch (platform) {

		case "web":
			loadConfig(webProp, "web");
			webDriverInitialization();
			break;

		case "android":
			loadConfig(androidProp, "android");
			androidDriverInitialization(androidProp);
			break;


		case "IOS":
			loadConfig(iosProp,"ios");
			iosDriverInitialization(iosProp);
			break;
			
		case "IOSAgent":
			loadConfig(iosPropagent,"iosAgentApp");
			iosAgentappDriverInitialization(iosPropagent);
			break;
		
		}
	}


	// Reporting Stuff
	@BeforeTest
	public ExtentReports getInstance() {
		if (extent == null) {
			extent = new ExtentReports(System.getProperty("user.dir") + "/test-output/STMExtentReport.html", true);

			extent.loadConfig(new File(System.getProperty("user.dir") + "//Extent-config.xml"));
		}
		return extent;
	}


	@BeforeSuite
	public void BeforeSuite() {
		System.out.println("Suite Started");
	}

	public String getScreenShot(String fileName) throws IOException {
		String pathScreenShotsFolder = System.getProperty("user.dir");

		//System.out.println("Exception Occured Please Check the Scrrenshot at--> " + pathScreenShotsFolder);
		try {
			File scrFile = ((TakesScreenshot) androidDriver).getScreenshotAs(OutputType.FILE);
			FileUtils.copyFile(scrFile, new File(pathScreenShotsFolder + "/" + fileName + ".jpg"));
			fileName = pathScreenShotsFolder + "/" + fileName + ".jpg";
			return fileName;
		} catch (Exception e) {
		}
		try {
			File scrFile = ((TakesScreenshot) iosDriver).getScreenshotAs(OutputType.FILE);
			FileUtils.copyFile(scrFile, new File(pathScreenShotsFolder + "/" + fileName + ".jpg"));
			fileName = pathScreenShotsFolder + "/" + fileName + ".jpg";
			return fileName;
		} catch (Exception e) {
		}

		try {
			File scrFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
			FileUtils.copyFile(scrFile, new File(pathScreenShotsFolder + "/" + fileName + ".jpg"));
			fileName = pathScreenShotsFolder + "/" + fileName + ".jpg";
			return fileName;
		} catch (Exception e) {
		}
		return fileName;

	}

	public void log(String data) {
		log.info(data);
		Reporter.log(data);
		logger.log(LogStatus.INFO, data);
	}

	public ExtentTest StartTestingforEndtoEnd(String TestName) {
		ExtentReports rep = getInstance();
		logger = rep.startTest(TestName);
		return logger;
	}

	public void StartTesting(String TestName) {
		ExtentReports rep = getInstance();
		logger = rep.startTest(TestName);
	}

	@AfterMethod
	public void getResult(ITestResult result) throws IOException {
		
		if (logger==null){
			logger=extent.startTest("Starting Test");
		}
			 
			
		if (result.getStatus() == ITestResult.FAILURE) {
			 
			
			logger.log(LogStatus.FAIL, "Test Case " + result.getName() + " is Failed");
			
			String screenshot_path = getScreenShot(result.getName());
			
			String image = logger.addScreenCapture(screenshot_path);
			
			logger.log(LogStatus.FAIL, "Title verification", image);
			
			logger.log(LogStatus.FAIL, "Test Case Failed is " + result.getThrowable());
		} else if (result.getStatus() == ITestResult.SKIP) {
			logger.log(LogStatus.SKIP, "Test Case " + result.getName() + " is Skipped");
			String screenshot_path = getScreenShot(result.getName());
			String image = logger.addScreenCapture(screenshot_path);
			logger.log(LogStatus.SKIP, "Title verification", image);
		} else if (result.getStatus() == ITestResult.SUCCESS) {
			logger.log(LogStatus.PASS, "Test Case " + result.getName() + " is passed");
			
			String screenshot_path = getScreenShot(result.getName());
			
			String image = logger.addScreenCapture(screenshot_path);
			logger.log(LogStatus.PASS, "Title verification", image);
		}

		// ending test
		// endTest(logger) : It ends the current test and prepares to create
		// HTML report
		extent.endTest(logger);
		// tearDown();
	}

	@AfterSuite
	public void endReport() {
		// writing everything to document
		// flush() - to write or update test information to your report.
		extent.flush();
		extent.close();
		try {
			androidDriver.quit();
		} catch (Exception e) {
		}

		try {
			iosDriver.quit();
		} catch (Exception e) {
		}
		try {
			
			driver.quit();
			
		} catch (Exception e) {
		}
	}	
}
