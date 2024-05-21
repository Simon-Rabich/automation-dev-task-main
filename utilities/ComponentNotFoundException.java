package com.antelope.af.utilities;

import org.openqa.selenium.WebDriverException;

/**
 * Component Not Found Exception class represents an exception in case component
 * is not found for any reason
 * 
 * @author Fadi Zaboura
 *
 */
public class ComponentNotFoundException extends WebDriverException {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ComponentNotFoundException(String message) {
		super(message);
	}

	public ComponentNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}

	public ComponentNotFoundException(Throwable cause) {
		super(cause);
	}

}
