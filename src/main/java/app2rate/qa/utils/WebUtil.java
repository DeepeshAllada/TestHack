package app2rate.qa.utils;

import org.openqa.selenium.WebElement;

public class WebUtil {
    public static long PAGE_LOAD_TIMEOUT = 60;
    public static long IMPLICIT_WAIT = 20;
    
	public static String webElementToString(WebElement elm){
		System.out.println("element is:"+elm);
		String webElm = elm.toString();
		String splitElm[] = webElm.split("By.");
		String splitWebElm[] = splitElm[splitElm.length-1].split(": ");
		String locator = splitWebElm[splitWebElm.length-1].replace("'","");
		String finalLocator = "\"" + locator + "\"";
		System.out.println(finalLocator);
		return  finalLocator;


	}
	
	public static String getFileName(String browser, String file ){
		
		return browser + "_" + file;
		
	}
}
