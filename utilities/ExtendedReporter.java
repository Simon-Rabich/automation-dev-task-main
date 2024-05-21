package com.antelope.af.utilities;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import org.apache.commons.io.FileUtils;
import org.testng.ITestResult;
import org.testng.Reporter;

import com.antelope.af.logging.SelTestLog;

/**
 * @author Fadi Zaboura * Wrapper for the TestNG HTML report
 */
public class ExtendedReporter extends Reporter {

	static {
		// Allowing adding HTML to the report
		System.setProperty("org.uncommons.reportng.escape-output", "false");
		setEscapeHtml(false);
	}

	public enum Style {
		REGULAR(""), BOLD("b"), ITALIC("i"), PLAINTEXT("pre"), EMPHASIZED("em"), STRIKETHROUGH("strike");

		private final String value;

		private Style(String value) {
			this.value = value;
		}

	}

	public enum ColorReport {
		RED, BLUE, YELLOW, GREEN, BLACK, DARKMAGENTA, ORANGE;
	}

	public void logEscapeHtml(String s) {
		setEscapeHtml(true);
		log(s);
		setEscapeHtml(false);
	}

	public static void log(String s, ExtentTest node) {
		log(s, false, null, null, node);
	}

	public static void log(String s, boolean logToStandardOut, ExtentTest node) {
		log(s, logToStandardOut, null, null, node);
	}

	public static void log(final String s, Style style, ExtentTest node) {
		log(s, false, style, null, node);
	}

	public static void log(final String s, ColorReport color, ExtentTest node) {
		log(s, false, null, color, node);
	}

	public static void log(final String s, ColorReport color) {
		log(s, false, null, color);
	}

	public static void log(final String s, Style style, ColorReport color, ExtentTest node) {
		log(s, false, style, color, node);
	}

	public static void log(final String s, Style style, ColorReport color) {
		log(s, false, style, color);
	}

	public static void step(String step, ExtentTest node) {
		step(step, Style.REGULAR, ColorReport.BLUE, node);
	}

	public static void step(String step, ColorReport color) {
		step(step, Style.REGULAR, color);
	}

	public static void step(String step) {
		step(step, Style.REGULAR, ColorReport.BLUE);
	}

	/**
	 * 
	 * @param receive
	 *            runnable, and print SUCCESS or FAILED next to step.
	 * @throws Exception
	 */
	public static void step(String description, Runnable runnable, ExtentTest node) {
		try {
			runnable.run();
			ExtendedReporter.step("[SUCCESS] " + description, ColorReport.GREEN, node);
			node.log(Status.PASS, "[SUCCESS] " + description);
		} catch (Exception ex) {
			ExtendedReporter.step("[FAILED] " + description, ColorReport.RED, node);
			node.log(Status.WARNING, "[WARNING] " + description);
			throw ex;
		}
	}

	public static void step(String description, Runnable runnable) {
		try {
			runnable.run();
			ExtendedReporter.step("[SUCCESS] " + description, ColorReport.GREEN);
		} catch (Exception ex) {
			ExtendedReporter.step("[FAILED] " + description, ColorReport.RED);
			throw ex;
		}
	}

	public static void step(String step, ITestResult result, ExtentTest node) {
		setCurrentTestResult(result);
		log("Click to see steps", step, ColorReport.RED, node);
	}

	public static void step(String step, ColorReport color, ExtentTest node) {
		log(step, Style.REGULAR, color, node);
	}

	public static void step(String step, Style style, ColorReport color, ExtentTest node) {
		log(step, style, color, node);
	}

	public static void step(String step, Style style, ColorReport color) {
		log(step, style, color);
	}

	public static void log(final Throwable t, ExtentTest node) {
		log(t.getMessage(), t, node);
	}

	public static void log(final String title, final Throwable t, ExtentTest node) {
		log(title, getStackTrace(t), false, node);
	}

	private static String getStackTrace(Throwable t) {
		if (t != null) {
			StringWriter stringWriter = new StringWriter();
			PrintWriter printWriter = new PrintWriter(stringWriter);
			t.printStackTrace(printWriter);
			return stringWriter.toString();
		}
		return "";
	}

	/**
	 * Appending <code>s</code> to the report with time stamp
	 *
	 * @param s
	 */
	public static void log(String s, boolean logToStandardOut, Style style, ColorReport color, ExtentTest node) {
		if (!s.startsWith("</")) { // No need to add time stamp for close tag
			DateFormat df = new SimpleDateFormat("HH:mm:ss");
			String reportDate = df.format(new Date(System.currentTimeMillis()));
			s = reportDate + " - " + s + "\n";
		}
		String newS = s;
		if (null == style) {
			style = Style.REGULAR;
		}
		if (null != color) {
			newS = appendColorParagraph(newS, color);
		}
		if (style != Style.REGULAR) {
			newS = appendStyleParagraph(newS, style);
		}
		writeToLog(newS, logToStandardOut);

		// Log into local log file.
		SelTestLog.info(newS);
		node.log(Status.INFO, s);
	}

	public static void log(String s, boolean logToStandardOut, Style style, ColorReport color) {
		if (!s.startsWith("</")) { // No need to add time stamp for close tag
			DateFormat df = new SimpleDateFormat("HH:mm:ss");
			String reportDate = df.format(new Date(System.currentTimeMillis()));
			s = reportDate + " - " + s + "\n";
		}
		String newS = s;
		if (null == style) {
			style = Style.REGULAR;
		}
		if (null != color) {
			newS = appendColorParagraph(newS, color);
		}
		if (style != Style.REGULAR) {
			newS = appendStyleParagraph(newS, style);
		}
		writeToLog(newS, logToStandardOut);

		// Log into local log file.
		SelTestLog.info(newS);
	}

	/**
	 * Adds toggle element to the report
	 *
	 * @param title
	 *            Will appear as link. If none given the link will appear with
	 *            the test 'link'
	 * @param body
	 *            Will appear when clicking on the title.
	 */
	public static void log(String title, String body, ExtentTest node) {
		log(title, body, null, node);
	}

	/**
	 * Adds toggle element to the report
	 *
	 * @param title
	 *            Will appear as link. If none given the link will appear with
	 *            the test 'link'
	 * @param body
	 *            Will appear when clicking on the title.
	 * @param color
	 *            The color of the link element
	 */
	public static void log(String title, String body, ColorReport color, ExtentTest node) {
		if (null == title) {
			title = "title";
		}
		// System.out.println(title + "\n");
		if (body != null) {
			// System.out.println(body + "\n");
		}
		if (null == body || body.isEmpty()) {
			log(title, color, node);
			return;
		}
		startLogToggle(title, color, node);
		log(body);
		stopLogToggle();
	}

	public static void log(String title, String body, boolean status, ExtentTest node) {
		getCurrentTestResult().setStatus((!status || !getCurrentTestResult().isSuccess()) ? ITestResult.FAILURE
				: getCurrentTestResult().getStatus());
		log(title, body, status ? ColorReport.BLUE : ColorReport.RED, node);
	}

	/**
	 * report a
	 *
	 * @param title
	 * @param body
	 * @param status
	 */
	public static void log(String title, String body, int status, ExtentTest node) {
		getCurrentTestResult().setStatus(status);
		ColorReport statusColor;
		switch (status) {
		case ITestResult.FAILURE:
			statusColor = ColorReport.RED;
			break;
		case ITestResult.SUCCESS_PERCENTAGE_FAILURE:
			statusColor = ColorReport.YELLOW;
			break;
		case ITestResult.SUCCESS:
			statusColor = ColorReport.GREEN;
			break;
		default:
			statusColor = ColorReport.BLUE;
		}
		log(title, body, statusColor, node);
	}

	public static void startLogToggle(String title, ExtentTest node) {
		startLogToggle(title, null, node);
	}

	public static void startLogToggle(String title, ColorReport color, ExtentTest node) {
		if (null == title) {
			title = "link";
		}
		StringBuilder toggleElement = new StringBuilder();
		final String id = System.currentTimeMillis() + "_" + new Random().nextInt(10000);

		// Creating link
		toggleElement.append(" <a href=\"javascript:toggleElement('");
		toggleElement.append(id);
		toggleElement.append("', 'block')\" title=\"Click to expand/collapse\"><b>");
		toggleElement.append(title).append("</b></a><br>");

		// Creating body
		toggleElement.append("<div id='");
		toggleElement.append(id);
		toggleElement.append("' style='display: none;'>");
		log(toggleElement.toString(), false, null, color, node);
	}

	public static void stopLogToggle() {
		log("</div>", false);
	}

	public static void logImage(String title, final File file) {
		File newFile = copyFileToReporterFolder(file);
		if (null == newFile) {
			return;
		}
		if (null == title) {
			title = file.getName();
		}
		log("<img src='" + newFile.getName() + "' alt='" + title + "' >");

	}

	/**
	 * Copy file to the report and add link. If another file is alrady exists in
	 * the reports folder with the same name the old file will be deleted.
	 *
	 * @param title
	 *            The title to appear as link to the file
	 * @param file
	 *            The file to copy to the report
	 */
	public static void logFile(String title, final File file) {
		File newFile = copyFileToReporterFolder(file);
		if (null == newFile) {
			return;
		}
		// Creating link

		if (null == title || title.isEmpty()) {
			title = file.getName();
		}
		// System.out.println(title);
		log("<a href='" + newFile.getName() + "'>" + title + "</a>", false);
	}

	private static void writeToLog(String s, boolean logToStandardOut) {
		org.testng.Reporter.log(toHtml(s), false);
		if (logToStandardOut) {
			// System.out.println(s);
		}
	}

	private static String appendStyleParagraph(String s, Style style) {
		final StringBuilder sb = new StringBuilder();
		sb.append("<p>");
		sb.append("<").append(style.value).append(">");
		sb.append(s);
		sb.append("</").append(style.value).append(">");
		sb.append("</p>");
		return sb.toString();
	}

	private static String appendColorParagraph(String s, ColorReport color) {
		if (color == null) {
			return s;
		}
		final StringBuilder sb = new StringBuilder();
		sb.append("<p style='color:").append(color.name()).append("'>");
		sb.append(s);
		sb.append("</p>");
		return sb.toString();
	}

	private static File copyFileToReporterFolder(File file) {
		if (null == file || !file.exists() || !file.isFile()) {
			// File is not exist
			return null;
		}

		// Creating parent folder
		final File parentFolder = new File(
				new File(getCurrentTestResult().getTestContext().getOutputDirectory()).getParent() + File.separator
						+ "html");
		if (!parentFolder.exists()) {
			if (!parentFolder.mkdirs()) {
				log("Failed to create folder for logging file");
			}
		}

		// Copying the file to the parent folder
		final File newFile = new File(parentFolder, file.getName());
		if (newFile.exists()) {
			newFile.delete();
		}
		try {
			FileUtils.copyFile(file, newFile);
		} catch (IOException e1) {
			log("Failed copying file " + file.getAbsolutePath());
		}
		return newFile;
	}

	public static boolean step(String description, boolean condition, ExtentTest node) {
		String prefixMessage = condition ? "[SUCCESS]" : "[FAILED]";
		ColorReport color = condition ? ColorReport.GREEN : ColorReport.RED;
		ExtendedReporter.step(prefixMessage + description, color, node);
		return condition;
	}

	private static String toHtml(String str) {
		return str.contains("<pre>") ? str : str.replace("\n", "<br/>");
	}

}