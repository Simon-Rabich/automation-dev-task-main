package com.antelope.af.utilities.testpparameters;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.SessionId;

public class DriverParamsThreadLocal {

	public enum DriverParamsEnum {
		DRIVER, CHROMELOG_NAME, BROWSER, SEASION_ID, CHROME_LOG;
		public String getName() {
			return this.name().replaceAll("_", " ");
		}
	}

	public static SessionId getSeasionId() {
		return (SessionId) TestParamsThreadPool.get(DriverParamsEnum.SEASION_ID.getName());
	}

	public static void setSeasionId(SessionId driver) {
		TestParamsThreadPool.put(DriverParamsEnum.SEASION_ID.getName(), driver);
	}

	public static WebDriver getDriver() {
		return (WebDriver) TestParamsThreadPool.get(DriverParamsEnum.DRIVER.getName());
	}

	public static void setDriver(Object driver) {
		TestParamsThreadPool.put(DriverParamsEnum.DRIVER.getName(), driver);
	}

	public static String getChromeLogName() {
		return (String) TestParamsThreadPool.get(DriverParamsEnum.CHROMELOG_NAME.getName());
	}

	public static void setChromeLogName(String chromeLog) {
		TestParamsThreadPool.put(DriverParamsEnum.CHROMELOG_NAME.getName(), chromeLog);
	}

	public static Object getBrowserType() {
		return TestParamsThreadPool.get(DriverParamsEnum.BROWSER.getName());
	}

	public static void setBrowserType(String browserType) {
		TestParamsThreadPool.put(DriverParamsEnum.BROWSER.getName(), browserType);
	}

	public static String getAnalyzedChromeLog() {
		return (String) TestParamsThreadPool.get(DriverParamsEnum.CHROME_LOG.getName());
	}

	public static void setAnalyzedChromeLog(String chromelog) {
		TestParamsThreadPool.put(DriverParamsEnum.CHROME_LOG.getName(), chromelog);
	}
}
