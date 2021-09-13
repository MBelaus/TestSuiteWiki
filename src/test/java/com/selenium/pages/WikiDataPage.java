package com.selenium.pages;

import org.openqa.selenium.WebDriver;
import org.testng.Reporter;

public class WikiDataPage {
	WebDriver driver;
	
	public WikiDataPage(WebDriver ldriver) {
		driver = ldriver;
	}
	
	public boolean VerificarUrl(String valor) throws Exception {
		Reporter.log("Verificamos que se redirigió a la página de " + valor);
		String strUrl = driver.getCurrentUrl();
		return strUrl.contains(valor);
	}
}
