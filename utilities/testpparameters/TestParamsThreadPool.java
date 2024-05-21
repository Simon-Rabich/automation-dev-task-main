package com.antelope.af.utilities.testpparameters;

import com.antelope.af.utilities.SeleniumDriver;
import com.antelope.af.utilities.SeleniumDriver.BrowserType;
import org.openqa.selenium.WebDriver;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class TestParamsThreadPool {

	/**
	 * @author Fadi Zaboura A thread friendly HashMap Mapping types of objects
	 *         as a string to their relevant Objects.
	 */
	private static ThreadLocal<Map<String, Object>> testParams = new ThreadLocal<Map<String, Object>>();

	public static Object get(String key) {
		if (!key.equals(DriverParamsThreadLocal.DriverParamsEnum.DRIVER.getName()))
			return getTestParameters().get(key);
		WebDriver dr = (WebDriver) getTestParameters().get(key);
		BrowserType browser = (BrowserType) DriverParamsThreadLocal.getBrowserType();
		if (dr == null && browser != null)
			SeleniumDriver.createDriver(browser);

		return getTestParameters().get(key);
	}

	public static void put(String key, Object value) {
		getTestParameters().put(key, value);
	}

	private static Map<String, Object> getTestParameters() {
		if (testParams.get() == null) {
			testParams.set(new ConcurrentHashMap<>());
		}
		return testParams.get();
	}

	public static void remove(String key) {
		getTestParameters().remove(key);
	}

	public static void clear() {
		testParams.get().clear();
	}
}
