package com.antelope.af.utilities;

import com.antelope.af.antelope.components.client.ClientTable;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;

import java.io.File;

import static com.antelope.af.testconfig.popertiesinit.PropertiesGetter.instance;
import static com.antelope.af.testconfig.testPatterns.commons.BaseSeleniumTest.OUTPUT_PATH;
import static com.antelope.af.testconfig.testPatterns.commons.BaseSeleniumTest.USER_DIR;

/**
 * @author Micheal Shamshoum * Wrapper for the Extent Reports
 */

public class ExtentReporterNG {
	static ExtentReports extent;

	public static ExtentReports getReportObject(String brandName, String environment)
	{
		String path = USER_DIR + OUTPUT_PATH + "html" + File.separator + "extent_reports_index.html";
		ExtentSparkReporter reporter = new ExtentSparkReporter(path);

		if(environment.equals("antelope") || environment.equals("baff")) {
			reporter.config().setReportName(brandName + " Sanity Tests Report");
			reporter.config().setDocumentTitle(brandName + " Sanity Tests Report");
		}
		else if(environment.equals("antelope_baff")) {
			reporter.config().setReportName(brandName + " Regression Tests Report");
			reporter.config().setDocumentTitle(brandName + " Regression Tests Report");
		}
		else {
			reporter.config().setReportName("Automation Results");
			reporter.config().setDocumentTitle("Test Results");
		}

		extent =new ExtentReports();
		extent.attachReporter(reporter);
		extent.setSystemInfo("Brand", brandName);
		extent.setSystemInfo("Flavor", environment.toUpperCase());
		if(environment.equals("antelope") || environment.equals("baff"))
			extent.setSystemInfo("Testing Type", "Sanity");
		if(environment.equals("antelope_baff"))
			extent.setSystemInfo("Testing Type", "Regression");

		return extent;
	}

}