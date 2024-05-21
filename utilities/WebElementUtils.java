package com.antelope.af.utilities;

import java.awt.Color;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.openqa.selenium.By;
import org.openqa.selenium.ElementNotVisibleException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Point;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.antelope.af.logging.SelTestLog;
import com.antelope.af.utilities.testpparameters.DriverParamsThreadLocal;
import com.google.common.base.CharMatcher;
import com.google.common.base.Splitter;
import com.google.common.collect.Iterables;

/**
 * static class containing webelement utilities
 * 
 * @author Fadi zaboura
 */
public class WebElementUtils {

	/**
	 * try clicking a stale element, if that throws an exception, relocate and
	 * click again. note: this only works if the element was previously found by
	 * it's XPath.
	 * 
	 * @param staleElement
	 *            , the problematic element that might throw a
	 *            StaleElementReference Exception.
	 * @param index
	 *            : the index of the element in case it was in a list. if index
	 *            = 0; then it locates the first one in a list, or the only one
	 *            if its a unique locator.
	 */
	public static void tryClickingStaleElement(WebElement staleElement, int index, WebDriver driver) {
		try {
			staleElement.click();
			WebdriverUtils.sleep(1000);
			return;
		} catch (StaleElementReferenceException ex) {
			staleElement = relocateStaleElement(staleElement, index, driver);
			staleElement.click();
		}
		WebdriverUtils.sleep(1000);
	}

	/**
	 * relocates a stale element and uses as anchor, from anchor, locates
	 * another element and clicks it. note: this only works if the element was
	 * previously found by it's XPath.
	 * 
	 * @param staleElement
	 *            , the problematic element that might throw a
	 *            StaleElementReference Exception.
	 * @param index
	 *            : the index of the element in case it was in a list. if index
	 *            = 0; then it locates the first one in a list, or the only one
	 *            if its a unique locator.
	 */
	public static void tryClickingStaleElement(WebElement staleElement, int index, WebDriver driver, By locator) {
		try {
			staleElement.findElement(locator).click();
			WebdriverUtils.sleep(1000);
			return;
		} catch (StaleElementReferenceException ex) {
			staleElement = relocateStaleElement(staleElement, index, driver);
			staleElement.findElement(locator).click();
		}
		WebdriverUtils.sleep(1000);
	}

	public static void hoverOverFieldAndClickInnerElement(WebDriver driver, WebElement mainElement,
			By innerElementLocator) {
		Actions action = new Actions(driver);
		action.moveToElement(mainElement).build().perform();
		WebdriverUtils.sleep(800);
		mainElement.findElement(innerElementLocator).click();
		// WebdriverUtils.sleep(1000);
	}

	public static void hoverOverFieldAndClickInnerElement(WebDriver driver, WebElement mainElement,
			By innerElementLocator, int index) {
		Actions action = new Actions(driver);
		action.moveToElement(mainElement).build().perform();
		WebdriverUtils.sleep(800);
		try{
		mainElement.findElements(innerElementLocator).get(index).click();
		}catch(ElementNotVisibleException e){
			mainElement.click();
			mainElement.findElements(innerElementLocator).get(index).click();
		}
		// WebdriverUtils.sleep(1000);
	}

	public static void hoverOverElement(WebDriver driver,  WebElement element){
		WebElement webElementToBeHovered = element;
		Actions builder = new Actions(driver);
		builder.moveToElement(webElementToBeHovered).build().perform();
	}

	public static WebElement relocateStaleElement(WebElement staleElement, int index, WebDriver driver) {
		String relocatePath = "";
		relocatePath = "html/" + getElementXPath(driver, staleElement);
		if (index == 0)
			staleElement = driver.findElement(By.xpath(relocatePath));
		else
			staleElement = driver.findElements(By.xpath(relocatePath)).get(index);
		return staleElement;
	}

	private static String getElementXPath(WebDriver driver, WebElement element) {
		// System.out.println(element.toString());
		// String path = substringXpath(element.toString());
		return (String) ((JavascriptExecutor) driver).executeScript(
				"gPt=function(c){if(c.id!==''){return'id(\"'+c.id+'\")';}if(c===document.body){return c.tagName.toLowerCase();}var a=0;var e=c.parentNode.childNodes;for(var b=0;b<e.length;b++){var d=e[b];if(d===c){var z;if(a==0){z='';}if(a>0){z='['+(a+1)+']';}return gPt(c.parentNode)+'/'+c.tagName.toLowerCase()+z;}if(d.nodeType===1&&d.tagName===c.tagName){a++;}}};return gPt(arguments[0]);",
				element);

	}

	public static String substringXpath(String pathToFind) {
		int firstIndex = pathToFind.indexOf("xpath");
		String subString = pathToFind.substring(firstIndex + 7, pathToFind.length() - 1);
		return subString;
	}

	/**
	 * Hovers over a field, and bypasses the hovering issues on firefox
	 * 
	 * @param field
	 * @param driver
	 * @param locator
	 *            - if u wanna hover to a locator within the field.
	 */
	public static void hoverOverField(WebElement field, WebDriver driver, By locator) {
		Actions builder = new Actions(driver);
		try {
			Actions hoverOverRegistrar = builder.moveToElement(field);
			hoverOverRegistrar.perform();
			hoverOverRegistrar.moveToElement(field).build().perform();
		} catch (Exception e) {
			SelTestLog.warn("Hover did not work - could be related to FF incompatibility\n", e);
		}

		WebdriverUtils.sleep(1000);
	}

	public static void hoverOverField(WebElement field, WebDriver driver) {
		Actions builder = new Actions(driver);
		try {
			Actions hoverOverRegistrar = builder.moveToElement(field);
			hoverOverRegistrar.perform();
			hoverOverRegistrar.moveToElement(field).build().perform();
		} catch (Exception e) {
			SelTestLog.warn("Hover did not work - could be related to FF incompatibility\n", e);
		}
	}

	public static void hoverOverFieldWithOffset(WebElement field, WebDriver driver, int xOffset, int yOffset) {
		Actions builder = new Actions(driver);
		try {
			builder.moveToElement(field, xOffset, yOffset).build().perform();
		} catch (Exception e) {
			SelTestLog.warn("Hover did not work - could be related to FF incompatibility\n", e);
		}

		WebdriverUtils.sleep(1000);
	}

	/**
	 * changes the visibiliy of an element. if By is null, then it changes the
	 * element's visibility without locating further elements within the
	 * element.
	 * 
	 * @param driver
	 * @param elm
	 * @param locator
	 */
	public static void changeVisibilityOfElement(WebDriver driver, WebElement elm, By locator) {
		WebElement elmToChangeVisibility;
		if (locator == null)
			elmToChangeVisibility = elm;
		else
			elmToChangeVisibility = elm.findElement(locator);
		String js = "arguments[0].style.height='auto'; arguments[0].style.visibility='visible';";
		((JavascriptExecutor) driver).executeScript(js, elmToChangeVisibility);
		WebdriverUtils.sleep(800);
	}

	public static void dragAndDropSliderToPercentage(WebDriver driver, WebElement slider, String targetPercent) {
		targetPercent = targetPercent.replace("%", "");
		Float lastPrecent = 0F;
		int xOffSetFix = 1;
		WebElement sliderWidthhandle = slider.findElement(By.className("ui-slider-handle"));
		while (sliderWidthhandle.getAttribute("style").replaceAll(".*left: ", "").replaceAll("%.*", "")
				.replaceAll("\\.[0-9].*", "").compareTo(targetPercent) != 0) {

			float sWidth = Float.parseFloat(slider.getCssValue("width").replace("px", ""));
			float currentPercent = Float.parseFloat(sliderWidthhandle.getAttribute("style").replaceAll(".*left: ", "")
					.replaceAll("%.*", "").replaceAll("\\.[0-9].*", ""));
			float precent = Float.parseFloat(targetPercent) - currentPercent;
			int xOffset = Math.round(sWidth * precent / 100);
			if (currentPercent == lastPrecent) {
				xOffset += xOffSetFix;
				xOffSetFix++;
			}
			new Actions(driver).dragAndDropBy(sliderWidthhandle, xOffset, 0).perform();
			// new
			// Actions(driver).clickAndHold(sliderWidthhandle).moveByOffset(xOffset,
			// 0).release().build().perform();
			lastPrecent = currentPercent;
		}
	}

	/**
	 * Pick random element.
	 * 
	 * @param list
	 *            the list
	 * @return the web element
	 */
	public static WebElement pickRandomElement(List<WebElement> list) {
		return list.get(getRandomNumberByRange(0, list.size() - 1));
	}

	/**
	 * Gets the random number by range.
	 * 
	 * @param min
	 *            the min
	 * @param max
	 *            the max
	 * @return the random number by range
	 */
	public static int getRandomNumberByRange(int min, int max) {
		Random rand = new Random();
		int randomNum = 0;
		if (min > max)
			randomNum = rand.nextInt((min - max) + 1) + max;
		else
			randomNum = rand.nextInt((max - min) + 1) + min;
		 System.out.println("picked random number " + randomNum);
		return randomNum;
	}

	/**
	 * Gets the random numbers by range.
	 * 
	 * @param min
	 *            the min
	 * @param max
	 *            the max
	 * @param x
	 *            the number of the random numbers
	 * @return the random numbers by range
	 */

	public static List<String> selectRandomFromList(List<String> list, int numberOfElements) {
		Random rand = new Random();
		ArrayList<String> newList= new ArrayList<String>();

		for (int i = 0; i < numberOfElements; i++) {
			int randomIndex = rand.nextInt(list.size());
			String randomElement = list.get(randomIndex);
			newList.add(randomElement);
			list.remove(randomElement);
		}
		return newList;
	}

	public static List<Integer> getXRandomNumbersByRange(int low, int high, int limit) {
		// int intervals = ((max - min) + 1) / x, j = min;
		// List<Integer> indexes = new ArrayList<Integer>();
		// for (int i = 1; i <= x; i++) {
		// int randomNum = getRandomNumberByRange(j, j + intervals - 1);
		// indexes.add(randomNum);
		// j = j + intervals;
		// }
		// return indexes;
		List<Integer> indexes = new ArrayList<Integer>();
		int[] array = new Random().ints(limit, low, high).distinct().sorted().toArray();
		Arrays.stream(array).forEach(x -> addToList(indexes, Stream.of(x)));
		return indexes;
	}

	public static <T> void addToList(List<T> target, Stream<T> source) {
		source.collect(Collectors.toCollection(() -> target));
	}

	/**
	 * Gets the random color.
	 * 
	 * @return the random color
	 */
	public static int getRandomColor() {
		return getRandomNumberByRange(0, 255);
	}

	public static String getImageSrc(WebElement imageElement) {
		if (imageElement.getTagName() == "img") {
			return imageElement.getAttribute("src");
		} else {
			return imageElement.findElement(By.tagName("img")).getAttribute("src");
		}

	}

	public static boolean sameColor(WebElement a, WebElement b, String cssAttribute) {
		String aColor = getColorAsRgb(a.getCssValue(cssAttribute));
		String bColor = getColorAsRgb(b.getCssValue(cssAttribute));
		return aColor.equals(bColor);
	}

	public static boolean sameColor(String color1, String color2) {
		String aColor = getColorAsRgb(color1);
		String bColor = getColorAsRgb(color2);
		return aColor.equals(bColor);
	}

	@SuppressWarnings("deprecation")
	public static String getColorAsRgb(String color1) {

		String colorprefix = "rgba";
		if (!color1.contains("rgba")) {
			colorprefix = "rgb";
		}
		Splitter extractParams = Splitter.on(colorprefix).omitEmptyStrings().trimResults();
		Splitter splitParams = Splitter.on(CharMatcher.anyOf("(),").or(CharMatcher.WHITESPACE)).omitEmptyStrings()
				.trimResults();

		List<String> rgba = new ArrayList<String>();

		for (String s : splitParams.split(Iterables.getOnlyElement(extractParams.split(color1)))) {
			rgba.add(s);
		}
		int r = Integer.parseInt(rgba.get(0));
		int g = Integer.parseInt(rgba.get(1));
		int b = Integer.parseInt(rgba.get(2));
		return new Color(r, g, b).toString();
	}

	public static String getColor(WebElement element) {
		return element.getCssValue("color");
	}

	public static void blurElement(WebDriver driver, WebElement element) {
		JavascriptExecutor executor = (JavascriptExecutor) driver;
		executor.executeScript("return arguments[0].blur();", element);

	}

	public static void clickElementEventJS(WebDriver driver, WebElement element) {
		// element.click();
		JavascriptExecutor executor = (JavascriptExecutor) driver;
		try {
			executor.executeScript("arguments[0].click();", element);
		} catch (TimeoutException toe) {
		} catch (Exception err) {
		}

	}

	public static void clickElementEventWithOffset(WebDriver driver, WebElement element, int xOffset, int yOffset) {
		Actions action = new Actions(driver);
		action.moveToElement(element, xOffset, yOffset).build().perform();
		WebdriverUtils.sleep(400);
		action.click().build().perform();
		WebdriverUtils.sleep(600);
	}

	public static void clickElementEvent(WebDriver driver, WebElement element) {
		Actions action = new Actions(driver);
		action.moveToElement(element).build().perform();
		WebdriverUtils.sleep(400);
		action.click().build().perform();
		WebdriverUtils.sleep(600);
	}

	public static void doubleClickElement(WebDriver driver, WebElement element) {
		Actions action = new Actions(driver);
		action.moveToElement(element).build().perform();
		WebdriverUtils.sleep(400);
//		action.click().build().perform();
		action.doubleClick(element).build().perform(); // double click on the
		// widget
		WebdriverUtils.sleep(600);
	}

	public static void doubleClickElementWithOfsset(WebDriver driver, WebElement element, int xOffset, int yOffset) {
		Actions action = new Actions(driver);
		action.moveToElement(element, xOffset, yOffset).build().perform();
		WebdriverUtils.sleep(400);
		action.doubleClick(element).build().perform(); // double click on the
		WebdriverUtils.sleep(600);
	}

	/**
	 * new Tooltip checker mechanism
	 * 
	 * @param driver
	 *            - WebDriver
	 * @param element
	 *            - The element you already hovered over
	 * @return if tooltip is displayed then true otherwise false
	 */
	public static boolean isTooltipDisplayed(WebDriver driver, WebElement element) {
		try {
			String str = ((JavascriptExecutor) driver)
					.executeScript("return window.getComputedStyle(arguments[0],'::after').getPropertyValue('opacity')",
							element)
					.toString();

			if (str.equals("0")) {
				return false;
			} else
				return true;
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * Stimulate javascript command to get the desired href
	 * 
	 * @param driver
	 * @param element
	 *            - The element of the export (excel or png)
	 * @return javascript exported link
	 */
	public static String getExportLink(WebDriver driver, WebElement element) {
		try {
			String javaScript = "return $(arguments[0]).scope().chartConfig.export.csvUrl";
			// document.getElementsByClassName('swWidgetCog-popupItem')[1]
			return ((JavascriptExecutor) driver).executeScript(javaScript, element).toString();
		} catch (Exception e) {
			return "";
		}
	}

	public static String getText(WebElement element) {
		try {
			return element.getText().trim();
		} catch (Exception e) {
			return "";
		}
	}

	public static Point getLocation(WebElement element) {
		try {
			return element.getLocation();
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * Deeb's idea
	 * 
	 * @param el
	 * @return
	 */
	public static WebElement getElementsParent(WebElement el) {
		return el.findElement(By.xpath(".."));
	}

	/**
	 * Fadi's idea
	 * 
	 * @param el
	 * @return
	 */
	public static WebElement getElementsGrandFather(WebElement el) {
		return el.findElement(By.xpath("../.."));
	}

	public static WebElement getElementFromListByText(List<WebElement> webElements, String text) {
		for (WebElement elm : webElements) {
			if (elm.getText().equals(text))
				return elm;
		}
		return null;
		// return webElements.stream().filter(x ->
		// x.getText().equals(text)).findFirst().get();
	}

	public static int getChildrenOfElementSize(WebElement el) {
		try {
			return el.findElements(By.cssSelector("*")).size();
		} catch (Exception e) {
			return -1;
		}
	}

	public static WebElement getInnerElement(WebElement el, By locator) {
		try {
			return el.findElement(locator);
		} catch (Exception e) {
			return null;
		}
	}

	// The script creates an <input> web element to receive the file sent by
	// SendKeys
	public static void DropFile(File filePath, WebElement target, int offsetX, int offsetY) {
		if (!filePath.exists())
			throw new WebDriverException("File not found: " + filePath.toString());

		WebDriver driver = DriverParamsThreadLocal.getDriver();
		JavascriptExecutor jse = (JavascriptExecutor) driver;
		WebdriverUtils.sleep(15000);
		WebDriverWait wait = new WebDriverWait(driver, 100); //30


		String JS_DROP_FILE = "var target = arguments[0]," + "    offsetX = arguments[1],"
				+ "    offsetY = arguments[2]," + "    document = target.ownerDocument || document,"
				+ "    window = document.defaultView || window;" + "" + "var input = document.createElement('INPUT');"
				+ "input.type = 'file';" + "input.style.display = 'none';" + "input.onchange = function () {"
				+ "  var rect = target.getBoundingClientRect(),"
				+ "      x = rect.left + (offsetX || (rect.width >> 1)),"
				+ "      y = rect.top + (offsetY || (rect.height >> 1)),"
				+ "      dataTransfer = { files: this.files };" + ""
				+ "  ['dragenter', 'dragover', 'drop'].forEach(function (name) {"
				+ "    var evt = document.createEvent('MouseEvent');"
				+ "    evt.initMouseEvent(name, !0, !0, window, 0, 0, 0, x, y, !1, !1, !1, !1, 0, null);"
				+ "    evt.dataTransfer = dataTransfer;" + "    target.dispatchEvent(evt);" + "  });" + ""
				+ "  setTimeout(function () { document.body.removeChild(input); }, 25);" + "};"
				+ "document.body.appendChild(input);" + "return input;";

		WebElement input = (WebElement) jse.executeScript(JS_DROP_FILE, target, offsetX, offsetY);
		input.sendKeys(filePath.getAbsoluteFile().toString());
		wait.until(ExpectedConditions.stalenessOf(input));
	}

	public static ExpectedCondition<By> findFirstElement(By... selectors)
	{
		return driver ->
		{
			for (By selector : selectors)
			{
				try
				{
					assert driver != null;
					WebElement webElement = driver.findElement(selector);
					if (webElement.isDisplayed())
					{
						return selector;
					}
				} catch (Exception ignored)
				{

				}
			}
			return null;
		};
	}

	public static By waitForFirstElement(WebDriver driver, long timeout, By... selectors)
	{
		Wait wait = new WebDriverWait(driver, timeout);
		try {
			return (By) wait.until(findFirstElement(selectors));
		} catch (TimeoutException e) {
			return null;
		}
	}

	public static void markElement(WebDriver driver, WebElement element) {
		JavascriptExecutor js = (JavascriptExecutor)driver;
		js.executeScript("arguments[0].setAttribute('style','background: yellow; border: 2px solid red;');", element);
	}

}