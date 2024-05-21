package com.antelope.af.utilities;

import org.apache.commons.lang3.StringUtils;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.util.concurrent.TimeUnit;
import java.text.SimpleDateFormat;
import java.io.BufferedReader;
import io.github.sukgu.Shadow;
import java.util.Collections;
import java.io.IOException;
import java.util.ArrayList;
import java.io.FileReader;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.io.File;

import org.openqa.selenium.html5.LocalStorage;
import org.openqa.selenium.support.pagefactory.AjaxElementLocatorFactory;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.remote.BrowserType;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Keys;
import org.openqa.selenium.By;

import com.antelope.af.testconfig.popertiesinit.PropertiesGetter;
import com.antelope.af.logging.SelTestLog;
import com.google.common.base.Function;

/**
 * static class containing webdriver utilities
 *
 * @author Fadi zaboura
 */
public class WebdriverUtils {

    public static class EmptyElemetCondition implements ExpectedCondition<Boolean> {

        private final WebElement element;

        public EmptyElemetCondition(WebElement element) {
            this.element = element;
        }

        public Boolean apply(WebDriver input) {
            return StringUtils.isEmpty(element.getAttribute("value"));
        }
    }

    /**
     * delete attribue for web element
     *
     * @param driver
     * @param element
     * @param attributeToRemove
     */
    public static void removeAttributeForElement(WebDriver driver, WebElement el, String attributToRemove) {
        ((JavascriptExecutor) driver).executeScript("arguments[0].removeAttribute('" + attributToRemove + "')", el);
    }

    /**
     * return the text of web element in javaScript
     *
     * @param driver
     * @param web    element
     * @return String
     */
    public static String getTextInJS(WebDriver driver, WebElement el) {
        return ((JavascriptExecutor) driver).executeScript("return arguments[0].innerText", el).toString();
    }

    /**
     * opens a link in a new tab, and returns reference to old tab
     *
     * @param driver
     * @param link
     * @return String handle of the old tab
     */
    public static String openLinkInNewTab(WebDriver driver, String link) {
        int numberOfTabs = driver.getWindowHandles().size();
        String oldTab = driver.getWindowHandle();

        ((JavascriptExecutor) driver).executeScript("window.open('about:blank', 'myPopup');");

        System.out.println("Getting redirected to link: " + link);
        ArrayList<String> newTab = new ArrayList<String>(driver.getWindowHandles());
        if (newTab.size() == numberOfTabs) {
            throw new WebDriverException("Could not open a new tab!");
        }
        driver.switchTo().window(newTab.get(numberOfTabs));
        driver.get(link);
        WebdriverUtils.sleep(1000);
        return oldTab;
    }

    public static void focusOnRequestedTab(WebDriver driver, int tabIndex) {
        ArrayList<String> allCurrentTabs = new ArrayList<String>(driver.getWindowHandles());
        if (allCurrentTabs.size() == 1) {
            SelTestLog.error("could not focus on a new tab");
            return;
        }
        driver.switchTo().window(allCurrentTabs.get(tabIndex));
        sleep(1500);
        return;
    }

    public static void closeRequestedTab(WebDriver driver, int tabIndex) {
        ArrayList<String> allCurrentTabs = new ArrayList<String>(driver.getWindowHandles());
        if (allCurrentTabs.size() == 0) {
            SelTestLog.error("there are no opened tabs");
            return;
        }
        String oldTab;
        oldTab = driver.getWindowHandle();
        focusOnRequestedTab(driver, tabIndex);
        driver.close();
        driver.switchTo().window(oldTab);
        sleep(1500);
        return;
    }

    public static String timestamp() {
        Calendar cal = Calendar.getInstance();
        return String.valueOf(cal.getTimeInMillis());
    }

    public static String generateEmail() {
        return generateEmail("automation+1", "@antelopesystem.com");
    }

    public static String generateEmail(String prefix, String suffix) {
        Calendar cal = Calendar.getInstance();
        String email = prefix + String.valueOf(cal.getTimeInMillis() + suffix);
        return email;
    }

    public static String getTimeStamp(String prefix) {
        Calendar cal = Calendar.getInstance();
        String timeStamp = prefix + String.valueOf(cal.getTimeInMillis() + WebElementUtils.getRandomNumberByRange(0, 1000));
        return timeStamp;
    }

    public static String getTimeSuffix() {
        Date date = new Date();
        Timestamp currentTimeStamp = new Timestamp(date.getTime());
        String timeSuffix = currentTimeStamp.toString().replace("-", "").replace(" ", "").replace(":", "").replace(".",
                "");
        return timeSuffix;
    }

    public static String getFormattedDate() {
        return getFormattedDate(false);
    }

    public static String getFormattedDate(boolean withSeconds) {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date date = new Date();
        String dateTime = formatter.format(date);
        if (withSeconds)
            return dateTime;

        int i = dateTime.lastIndexOf(":");
        return dateTime.substring(0, i);
    }

    public static String getFormattedDateWithoutTime() {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        Date date = new Date();
        String dateTime = formatter.format(date);
        return dateTime;
    }

    public static void clickOnTabButton(WebDriver driver) {
        Actions action = new Actions(driver);
        action.sendKeys(Keys.TAB).build().perform();
    }

    public static WebDriver openLinkInNewBrowser(String link) {
        WebDriver newDriver = SeleniumDriver.createDriver(SeleniumDriver.BrowserType.CHROME);
        WebdriverUtils.sleep(200);
        System.out.println("Getting redirected to link: " + link);
        newDriver.get(link);
        return newDriver;
    }

    public static float cleanAndParseFloat(String stringToParse) {
        return Float.parseFloat(stringToParse.replaceAll("[^-?0-9^.]", ""));
    }

    public static double cleanAndParseDouble(String stringToParse) {
        return Double.parseDouble(stringToParse.replaceAll("[^-?0-9^.]", ""));
    }

    public static int cleanAndParseInteger(String stringToParse) {
        return Integer.parseInt(stringToParse.replaceAll("[^-?0-9]", ""));
    }

    public static long cleanAndParseLong(String stringToParse) {
        return Long.parseLong(stringToParse.replaceAll("[^-?0-9]", ""));
    }

    public static void closeNewBrowser(WebDriver driver, WebDriver driverToClose) {
        boolean driverDown = false;
        try {
            driverToClose.getCurrentUrl();
        } catch (Exception e) {
            driverDown = true;
            System.out.println("Warning: could not close driver: " + driverToClose);
        }
        if (!driverDown) {
            driverToClose.close();
            try {
                driver.switchTo().defaultContent();
            } catch (Exception e) {
            }
        }
    }

    public static void closeTabAndFocusOnPreviousTab(WebDriver driver, String oldTab) {
        driver.close();
        // change focus back to old tab
        driver.switchTo().window(oldTab);
    }

    public static String[] cleanNulls(final String[] v) {
        List<String> list = new ArrayList<String>(Arrays.asList(v));
        list.removeAll(Collections.singleton(null));
        return list.toArray(new String[list.size()]);
    }

    public static void enableElement(WebDriver driver, WebElement element, String attribute) {

        // String js = "arguments[0].removeAttribute(\""+attribute+"\")";
        // String jss = "arguments[0].setAttribute(\""+attribute+"\",\"\");";
        String jsx = "arguments[0].disabled=false;";
        try {
            ((JavascriptExecutor) driver).executeScript(jsx, element);
        } catch (Exception e) {
            SelTestLog.warn("in the script: " + jsx, e);
        }
        sleep(600);
    }

    public static void forceElementVisibility(WebDriver driver, WebElement element) {

        String js = "arguments[0].style.height='auto'; arguments[0].style.display='block';";

        ((JavascriptExecutor) driver).executeScript(js, element);

        sleep(800);

    }

    public static void forceElementInvisibility(WebDriver driver, WebElement element) {

        String js = "arguments[0].style.height='auto'; arguments[0].style.display='none';";

        ((JavascriptExecutor) driver).executeScript(js, element);

        sleep(800);

    }

    public static String getFieldValue(WebDriver driver, WebElement element) {

        String js = "return arguments[0].value;";
        String jsx = "return arguments[0].val()";
        String jsxx = "return arguments[0].textContent";

        String val = (String) ((JavascriptExecutor) driver).executeScript(js, element);
        sleep(800);
        val = val.equals("undefined") ? (String) ((JavascriptExecutor) driver).executeScript(jsx, element) : val;
        if (val.equals("undefined") || val.isEmpty() || val == null)
            return (String) ((JavascriptExecutor) driver).executeScript(jsxx, element);
        return val;
    }

    public static void clearFieldValue(WebDriver driver, WebElement element) {
        String js = "document.getElementById('" + element.getAttribute("id") + "').setAttribute('value','');";
        String jsx = "document.getElementById('" + element.getAttribute("id") + "').value='';";

        ((JavascriptExecutor) driver).executeScript(js, element);
        ((JavascriptExecutor) driver).executeScript(jsx, element);
    }

    public static boolean isDisplayed(WebElement element) {
        try {
            return element.isDisplayed();
        } catch (NoSuchElementException nse) {
            return false;
        } catch (Throwable e) {
            return false;
        }
    }

    public static boolean isDisplayed(WebDriver driver, By cssSelector) {
        try {
            return driver.findElement(cssSelector).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean isDisplayed(WebElement wrapper, By cssSelector) {
        try {
            return wrapper.findElement(cssSelector).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean isDisplayedWithTimeout(WebDriver driver ,WebElement element, int timeout) {
        try {
            WebdriverUtils.waitForVisibilityofElement(driver, element, timeout);
            return element.isDisplayed();
        } catch (NoSuchElementException nse) {
            return false;
        } catch (Throwable e) {
            return false;
        }
    }

    public static void waitForElementToDisappear(WebDriver driver, final By elementLocator) {
        WebDriverWait wait = new WebDriverWait(driver, 10);
        sleep(600);

        wait.until(new Function<WebDriver, Boolean>() {
            public Boolean apply(WebDriver arg0) {
                try {
                    return !arg0.findElements(elementLocator).get(0).isDisplayed();
                } catch (Exception e) {
                    return true;
                }
            }
        });
        sleep(800);
    }

    /**
     * gets an element from a list using get(index) and waits until that element
     * doesn't have a certain class.
     *
     * @param driver
     * @param classToHave
     * @param elementLocator
     * @param index
     */

    public static void waitForElementInAListToNotHaveClass(WebDriver driver, final String classToHave,
                                                           final By elementLocator, final int index) {
        waitForElementInAListToNotHaveClassWithTime(driver, classToHave, elementLocator, 20);
    }

    public static void waitForElementInAListToNotHaveClassWithTime(WebDriver driver, final String classToHave,
                                                                   final By elementLocator, int waitTime) {
        WebDriverWait wait = new WebDriverWait(driver, waitTime);
        sleep(600);
        wait.until(new Function<WebDriver, Boolean>() {
            public Boolean apply(WebDriver arg0) {
                try {
                    return !hasClass(classToHave, arg0.findElement(elementLocator));
                } catch (Exception e) {
                    return true;
                }
            }
        });
        sleep(800);
    }

    @SuppressWarnings("deprecation")
    public static Wait<WebDriver> initializePOM(WebDriver driver, Object pom) {
        PageFactory.initElements(new AjaxElementLocatorFactory(driver, 4), pom);
        return new FluentWait<WebDriver>(driver).withTimeout(8, TimeUnit.SECONDS).pollingEvery(900,
                TimeUnit.MILLISECONDS);
    }

    @SuppressWarnings("deprecation")
    public static Wait<WebDriver> initializePOM(WebDriver driver, Object pom, int pollingTime, int elementsTimeout,
                                                int waitTimeout) {
        PageFactory.initElements(new AjaxElementLocatorFactory(driver, elementsTimeout), pom);
        return new FluentWait<WebDriver>(driver).withTimeout(waitTimeout, TimeUnit.SECONDS).pollingEvery(pollingTime,
                TimeUnit.MILLISECONDS);
    }

    public static void removeAttribute(WebDriver driver, WebElement element, String attribute) {

        String js = "arguments[0].removeAttribute(\"" + attribute + "\")";

        try {
            ((JavascriptExecutor) driver).executeScript(js, element);
        } catch (Exception e) {
            SelTestLog.warn("in the script: " + js, e);
        }
        sleep(600);
    }

    public static void setAttribute(WebDriver driver, WebElement element, String attribute, String attributeValue) {

        String js = "arguments[0].setAttribute('" + attribute + "' , '" + attributeValue + "');";

        try {
            ((JavascriptExecutor) driver).executeScript(js, element);
        } catch (Exception e) {
            SelTestLog.warn("in the script: " + js, e);
        }
        sleep(600);
    }

    public static String getCurrentRunningBrowserType(WebDriver driver) {
        if (PropertiesGetter.instance().retrieveDriverProperties().isLocal()) {
            if (driver instanceof ChromeDriver)
                return BrowserType.CHROME;
            // } else if (driver instanceof ) {
            // return BrowserType.FIREFOX;
            // } else
            // return BrowserType.IE;
        } else if (driver instanceof RemoteWebDriver) {
            String browser = ((RemoteWebDriver) driver).getCapabilities().getBrowserName().toLowerCase();
            if (browser.contains("chrome")) {
                return BrowserType.CHROME;
            } else if (browser.contains("firefox")) {
                return BrowserType.FIREFOX;
            } else
                return BrowserType.IE;
        }
        return null;
    }

    public enum Align {
        LEFT("left"), RIGHT("right"), CENTER("none");

        private String floatCssVal;

        private Align(String floatCssVal) {
            this.floatCssVal = floatCssVal;
        }

        public String getFloatCssVal() {
            return floatCssVal;
        }
    }

    public static ExpectedCondition<WebElement> visibilityOfElementLocated(final By locator) {
        return new ExpectedCondition<WebElement>() {
            public WebElement apply(WebDriver driver) {
                return getDisplayedElement(locator, driver);
            }
        };
    }

    public static WebElement getDisplayedElement(final By locator, WebDriver driver) {
        List<WebElement> elemList = driver.findElements(locator);
        for (WebElement webElement : elemList) {
            if (webElement.isDisplayed()) {
                return webElement;
            }
        }
        return null;
    }

    public static ExpectedCondition<WebElement> visibilityOfWebElement(final WebElement elm) {
        return new ExpectedCondition<WebElement>() {
            public WebElement apply(WebDriver driver) {
                if (elm.isDisplayed()) {
                    return elm;
                }
                return null;
            }
        };
    }

    public static WebElement waitForVisibilityofElement(WebDriver driver, WebElement elm, int waitTime) {
        Wait<WebDriver> wait = new WebDriverWait(driver, waitTime);
        try {
            WebElement divElement = wait.until(visibilityOfWebElement(elm));
            return divElement;
        } catch (Exception e) {
            return null;
        }
    }

    public static WebElement waitForVisibility(WebDriver driver, By by, int waitTime) {
        Wait<WebDriver> wait = new WebDriverWait(driver, waitTime);
        return wait.until(visibilityOfElementLocated(by));
    }

    public static WebElement waitUntilClickable(WebDriver driver, By by) {
        WebDriverWait wait = new WebDriverWait(driver, 5);
        return wait.until(ExpectedConditions.elementToBeClickable(by));
    }

    public static WebElement waitUntilClickable(WebDriver driver, WebElement elm) {
        return waitUntilClickable(driver, elm, 5);
    }

    public static WebElement waitUntilClickable(WebDriver driver, WebElement elm, int waitTime) {
        WebDriverWait wait = new WebDriverWait(driver, waitTime);
        return wait.until(elementToBeClickableByWebElement(elm));
    }

    public static ExpectedCondition<WebElement> elementToBeClickableByWebElement(final WebElement elm) {
        return new ExpectedCondition<WebElement>() {

            public ExpectedCondition<WebElement> visibilityOfElementLocated = ExpectedConditions.visibilityOf(elm);

            public WebElement apply(WebDriver driver) {
                WebElement element = visibilityOfElementLocated.apply(driver);
                try {
                    if (element != null && element.isEnabled()) {
                        return element;
                    } else {
                        return null;
                    }
                } catch (StaleElementReferenceException e) {
                    return null;
                }
            }

            @Override
            public String toString() {
                return "element to be clickable: " + elm.toString();
            }
        };
    }

    public static Boolean isElementClickable(WebDriver driver, WebElement elm) {
        try {
            WebDriverWait wait = new WebDriverWait(driver, 2);
            wait.until(elementToBeClickableByWebElement(elm));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static ExpectedCondition<WebElement> elementToBeSelected(final WebElement elm) {
        return new ExpectedCondition<WebElement>() {

            public ExpectedCondition<WebElement> visibilityOfElementLocated = ExpectedConditions.visibilityOf(elm);

            public WebElement apply(WebDriver driver) {
                WebElement element = visibilityOfElementLocated.apply(driver);
                try {
                    if (element != null && hasClass("selected", element)) {
                        return element;
                    } else {
                        return null;
                    }
                } catch (StaleElementReferenceException e) {
                    return null;
                }
            }
        };
    }

    public static ExpectedCondition<WebElement> elementToHaveAttribute(final WebElement elm, final String attribute) {
        return new ExpectedCondition<WebElement>() {

            public ExpectedCondition<WebElement> visibilityOfElementLocated = ExpectedConditions.visibilityOf(elm);

            public WebElement apply(WebDriver driver) {
                WebElement element = visibilityOfElementLocated.apply(driver);
                try {
                    if (element != null && hasAttribute(attribute, element)) {
                        return element;
                    } else {
                        return null;
                    }
                } catch (StaleElementReferenceException e) {
                    return null;
                }
            }
        };
    }

    public static ExpectedCondition<WebElement> elementToHaveAttributeEqualTo(final WebElement elm,
                                                                              final String attribute, final String attributeString) {
        return new ExpectedCondition<WebElement>() {

            public ExpectedCondition<WebElement> visibilityOfElementLocated = ExpectedConditions.visibilityOf(elm);

            public WebElement apply(WebDriver driver) {
                WebElement element = visibilityOfElementLocated.apply(driver);
                try {
                    if (element != null && hasAttributeEqualTo(attribute, attributeString, element)) {
                        return element;
                    } else {
                        return null;
                    }
                } catch (StaleElementReferenceException e) {
                    return null;
                }
            }
        };
    }

    public static ExpectedCondition<WebElement> elementToHaveClass(final WebElement elm, final String classToHave) {
        return new ExpectedCondition<WebElement>() {
            public ExpectedCondition<WebElement> visibilityOfElementLocated = ExpectedConditions.visibilityOf(elm);

            public WebElement apply(WebDriver driver) {
                WebElement element = visibilityOfElementLocated.apply(driver);
                try {
                    if (element != null && hasClass(classToHave, element)) {
                        return element;
                    } else {
                        return null;
                    }
                } catch (StaleElementReferenceException e) {
                    return null;
                }
            }
        };
    }

    public static void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (Exception e) {
        }
    }

    public static void waitForElementToBeFound(WebDriver driver, final By elmLocator) {
        waitForElementToBeFound(driver, elmLocator, 10);
    }

    public static void waitForElementToBeFound(WebDriver driver, final By elmLocator, int waitTime) {
        WebDriverWait wait = new WebDriverWait(driver, waitTime);
        // sleep(600);
        wait.until(new Function<WebDriver, Boolean>() {
            public Boolean apply(WebDriver arg0) {
                try {
                    return arg0.findElement(elmLocator).isDisplayed();
                } catch (Exception e) {
                    return false;
                }
            }
        });
        sleep(800);
    }

    public static void waitForEmulatorLoad(WebDriver driver) {
        WebDriverWait wait = new WebDriverWait(driver, 12);

        try {
            Thread.sleep(700);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        wait.until(new Function<WebDriver, Boolean>() {
            public Boolean apply(WebDriver arg0) {
                try {
                    WebElement disabledImageZone = arg0.findElement(By.id("disabledImageZone"));
                    return (!disabledImageZone.getAttribute("style").contains("block")
                            && !disabledImageZone.getAttribute("style").contains("cursor: wait"));
                } catch (Exception e) {
                    SelTestLog.info("Exception!!!", e);
                    return false;
                }
            }
        });
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public static void click(WebElement clickable) {
        boolean flag = true;
        long endTime = System.currentTimeMillis() + 4000;
        WebDriverException e = null;
        while (flag && System.currentTimeMillis() < endTime) {
            sleep(250);
            try {
                clickable.click();
                SelTestLog.info("clicking on " + clickable.getText() + " elment");
                flag = false;
            } catch (WebDriverException wde) {
                e = wde;
            }
        }
        if (flag) {
            throw e;
        }
    }

    public static int calculateWidthPercent(WebElement elem, int parentDivWIdth) {
        System.out.println(
                Float.parseFloat(elem.getCssValue("width").replaceAll("[^0-9.]", "")) + " / " + parentDivWIdth);
        return Math
                .round((Float.parseFloat(elem.getCssValue("width").replaceAll("[^0-9.]", "")) / parentDivWIdth) * 100);
    }

    public static boolean isAligned(WebElement elm, Align align) {

        return elm.getCssValue("float").equalsIgnoreCase(align.getFloatCssVal());
    }

    public static boolean isElementPresent(WebDriver driver, By locator) {
        try {
            WebElement elm = driver.findElement(locator);
            return elm.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public static WebElement isElementPresenceInDOM(WebDriver driver, By locator, int timeInSeconds) {
        WebDriverWait w = new WebDriverWait(driver,timeInSeconds);
        // presenceOfElementLocated condition
        return w.until(ExpectedConditions.presenceOfElementLocated (locator));
    }

    /**
     * this method waits on the element to be displayed
     *
     * @param driver
     * @param element
     * @return boolean indicating if it is or not
     */
    public static boolean isElementPresent(WebElement element) {
        try {
            return element.isDisplayed();
        } catch (NoSuchElementException e) {
            return false;
        }
    }

    /**
     * this method doesn't wait on elements to be displayed
     *
     * @param element
     * @param driver
     * @param locator
     * @return boolean indicating if it is visible or not
     */
    public static boolean isElementVisible(WebDriver driver, By locator) {
        try {
            return driver.findElements(locator).size() > 0;
        } catch (NoSuchElementException e) {
            return false;
        }
    }

    public static boolean isVisible(WebElement webElement, WebDriver driver) {
        try {
            new WebDriverWait(driver, 2).until(ExpectedConditions.visibilityOf(webElement));
            return true;
        } catch (Exception e) {
            // any exception, return false
            return false;
        }
    }

    public static boolean isVisible(WebDriver driver, By locator) {
        try {
            new WebDriverWait(driver, 2).until(ExpectedConditions.visibilityOfElementLocated(locator));
            return true;
        } catch (StaleElementReferenceException e) {
            // element got stale since it's been retrieve, just return false
            return false;
        } catch (Exception e) {
            // any exception, return false
            return false;
        }
    }

    /**
     * Checks to see if a specified element contains a specific class of not.
     * The check is case-insensitive.
     *
     * @param className CSS class name to check for
     * @param element   element to check
     * @return <code>true</code>, if <tt>element</tt> has CSS class with given
     * <tt>className</tt>
     */
    public static boolean hasClass(String className, WebElement element) {
        final String classNameLowerCase = className.toLowerCase();
        String classValue;
        try {
            classValue = element.getAttribute("class");
        } catch (Exception e) {
            // SelTestLog.warn("Could not find element", e);
            return false;
        }
        if (StringUtils.isEmpty(classValue)) {
            return false;
        }
        classValue = classValue.toLowerCase();
        if (!classValue.contains(classNameLowerCase)) {
            return false;
        }
        for (String singleClass : classValue.split("\\s+")) {
            // System.out.println(singleClass);
            if (classNameLowerCase.equals(singleClass.toLowerCase())) {
                return true;
            }
        }
        return false;
    }

    public static boolean hasAttribute(String attribute, WebElement element) {
        String att = element.getAttribute(attribute);
        if (StringUtils.isEmpty(att)) {
            return false;
        }
        return true;
    }

    public static boolean hasAttributeEqualTo(String attribute, String attributeString, WebElement element) {
        try {
            return element.getAttribute(attribute).equals(attributeString);
        }catch (Exception e){
            return false;
        }
    }

    public static void scrollToTheTop(WebDriver driver) {
        try {
            Actions actions = new Actions(driver);
            actions.sendKeys(Keys.HOME).build().perform();
        } catch (Exception e) {
            SelTestLog.error("Could not scroll to the top !");
        }
    }
    public static void clickOnPageDown(WebDriver driver) {
        try {
            Actions action = new Actions(driver);
            action.sendKeys(Keys.PAGE_DOWN).build().perform();
        } catch (Exception e) {
            throw e;
        }
    }

    public static void scrollToTheBottomJS(WebDriver driver) {
        try {
            ((JavascriptExecutor) driver).executeScript("window.scrollTo(0, document.body.scrollHeight)");
            // Actions actions = new Actions(driver);
            // actions.sendKeys(Keys.END).build().perform();

        } catch (Exception e) {
            SelTestLog.error("Could not scroll to the botoom !");
        }

    }

    public static void writeJSInConsoleLog(WebDriver driver, String s) {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript(s);
    }

    public static void scrollToTheTopJS(WebDriver driver) {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("window.scrollTo(0,0);");
    }

    public static void goBack(WebDriver driver) {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("window.history.go(-1)");
        WebdriverUtils.sleep(2000);
    }

    public static void ScrollDownTo(WebDriver driver, int x, int y) {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("window.scrollTo(" + x + "," + y + ");");
        WebdriverUtils.sleep(1000);
    }

    public static void ScrollBy(WebDriver driver, int y) {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("window.scrollBy(0," + y + ")");
        WebdriverUtils.sleep(1000);
    }

    public static boolean containsClass(String className, WebElement element) {
        final String classNameLowerCase = className.toLowerCase();
        String classValue = element.getAttribute("class");
        if (StringUtils.isEmpty(classValue)) {
            return false;
        }
        classValue = classValue.toLowerCase();
        for (String singleClass : classValue.split("\\s+")) {
            // System.out.println(singleClass);
            if (singleClass.toLowerCase().contains(classNameLowerCase)) {
                return true;
            }
        }
        return false;
    }

    public static boolean waitForElementToBeLocated(WebDriver driver, By homeLocator, int i) {
        try {
            WebDriverWait wait = new WebDriverWait(driver, i);
            List<WebElement> els = driver.findElements(homeLocator);
            WebElement elm = null;
            for (WebElement el : els)
                if (WebdriverUtils.isDisplayed(el)) {
                    elm = el;
                    break;
                }
            wait.until(ExpectedConditions.visibilityOf(elm));
            // wait.until(ExpectedConditions.visibilityOfElementLocated(homeLocator));
            return true;
        } catch (Exception e) {
            SelTestLog.error("error: ", e);
            return false;
        }

    }

    public static Dimension getDimensionFromString(String res) {
        String[] parts = res.split("x");
        if (parts.length != 2) {
            throw new IllegalArgumentException("Not a valid size string: " + res);
        }
        return new Dimension(Integer.parseInt(parts[0]), Integer.parseInt(parts[1]));
    }

    public static boolean waitForInvisibilityOfElement(WebDriver driver, WebElement elm, int waitTime) {
        int tries = 0;
        int wait = waitTime * 2;
        while (tries < wait) {
            try {
                if (elm.isDisplayed()) {
                    sleep(500);
                    tries++;
                } else
                    return true;
            } catch (Exception e) {
                return true;
            }
        }
        return false;
    }

    public static String readFromFile(String fileName) throws IOException {
        File dummyFile = new File("");
        File txtFile = new File(dummyFile.getAbsolutePath() + File.separator + "src" + File.separator + "main"
                + File.separator + "java" + File.separator + "resources" + File.separator + fileName);

        String everything = "";

        BufferedReader br = new BufferedReader(new FileReader(txtFile));
        try {
            StringBuilder sb = new StringBuilder();
            String line = br.readLine();

            while (line != null) {
                sb.append(line);
                sb.append(System.lineSeparator());
                line = br.readLine();
            }
            everything = sb.toString();
        } finally {
            br.close();
        }
        return everything;
    }

    public static void moveMouseByOffset(WebDriver driver, int x, int y) {
        Actions act = new Actions(driver);
        act.moveByOffset(x, y).build().perform();
    }

    public static void clickOnCoordinates(WebDriver driver, int x, int y) {
        Actions act = new Actions(driver);
        act.moveByOffset(x, y).click().perform();
    }

    public static void waitForCRMLoadingDots(WebDriver driver, int waitTime) {
//        new WebDriverWait(driver, 40).until(ExpectedConditions.invisibilityOfAllElements(driver.findElements(By.cssSelector("spinner div.loader-dots"))));
        WebDriverWait wait = new WebDriverWait(driver, waitTime);
        wait.until(ExpectedConditions.invisibilityOfElementLocated(By.cssSelector("spinner div.loader-dots")));
    }
    //                      "console.log(\"Data: \" + data);\n" +
//                      "console.log(\"Token: \" + token);\n");
//    public static String getOperatorAuthToken(WebDriver driver) {
//        Object bodyValue;
//        bodyValue = ((JavascriptExecutor)driver).executeScript(
//                     "let ls = window.localStorage;\n" +
//                        "let key = 'ls.globals';\n" +
//                        "let data = ls[key] && JSON.parse(ls[key]).data\n" +
//                        "let token = data.currentOperator && data.currentOperator.authToken\n" +
//                    "return token");
//        return bodyValue.toString();
//    }

    public static String getOperatorAuthToken(WebDriver driver) {
        Object bodyValue;
        bodyValue = ((JavascriptExecutor)driver).executeScript(
                     "let ls = window.localStorage;\n" +
                        "let key = 'ls.ANT_AUTH_TOKEN';\n" +
                        "let token = JSON.parse(ls[key])\n" +
                        "return token");
        return bodyValue.toString();
    }

    public static String getValueFromLocalStorage(WebDriver driver, String localStorageKey) {
        ExtendedReporter.log("Getting " + localStorageKey + "...");
        return ((JavascriptExecutor) driver).executeScript("return localStorage.getItem(\"" + localStorageKey + "\")").toString();
    }

    public static void removeValueFromLocalStorage(WebDriver driver, String localStorageKey) {
        ExtendedReporter.log("Getting " + localStorageKey + "...");
        ((JavascriptExecutor) driver).executeScript("return localStorage.removeItem(\"" + localStorageKey + "\")");
    }

    public static void setValueFromLocalStorage(WebDriver driver, String localStorageKey, String value) {
        ExtendedReporter.log("Getting " + localStorageKey + "...");
        ((JavascriptExecutor) driver).executeScript("return localStorage.setItem(\"" + localStorageKey + "\", " + value + ")");
    }

    public static void clearBrowserCache(WebDriver driver) {
        String currentTab = openLinkInNewTab(driver, "chrome://settings/clearBrowserData");
        Shadow shadow = new Shadow(driver);
        WebElement confirmButton = shadow.findElement("#clearBrowsingDataConfirm");
        waitForVisibilityofElement(driver,confirmButton,5000);
        confirmButton.click();
        waitForInvisibilityOfElement(driver,confirmButton,10000);
        driver.switchTo().window(currentTab);
        closeRequestedTab(driver,driver.getWindowHandles().size()-1);
        ExtendedReporter.step("Cleared browser cache");
    }

    public static void scrollDownToElement(WebDriver driver, WebElement element) {
//        Actions actions = new Actions(driver);
//        actions.moveToElement(element);
//        actions.perform();
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
        sleep(500);
    }

    public static void scrollDownToElement(WebDriver driver, WebElement wrapper, By by) {
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", wrapper.findElement(by));
        sleep(500);
    }

    public static void scrollUpToElement(WebDriver driver, WebElement element) {
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(false);", element);
        sleep(500);
    }

    public static void scrollUpToElement(WebDriver driver, WebElement wrapper, By by) {
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(false);", wrapper.findElement(by));
        sleep(500);
    }

    public static String getContentFromClipBoard() throws IOException, UnsupportedFlavorException {
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Clipboard clipboard = toolkit.getSystemClipboard();
        String result = (String) clipboard.getData(DataFlavor.stringFlavor);
        return result;
    }

    public static String getAttributeJS(WebDriver driver, WebElement el, String attribute) {
        return ((JavascriptExecutor) driver).executeScript("return arguments[0].getAttribute(arguments[1])", el, attribute).toString();
    }

    public static String getUrlJS(WebDriver driver) {
        return ((JavascriptExecutor) driver).executeScript("return window.location.href").toString();
    }

}
