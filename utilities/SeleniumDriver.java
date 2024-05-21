package com.antelope.af.utilities;

import com.antelope.af.logging.SelTestLog;
import com.antelope.af.testconfig.popertiesinit.PropertiesGetter;
import com.antelope.af.testconfig.testPatterns.commons.BaseSeleniumTest;
import com.antelope.af.utilities.testpparameters.DriverParamsThreadLocal;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.logging.LoggingPreferences;
import org.openqa.selenium.remote.*;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;
import java.util.logging.Level;

/**
 * This class is used for creating all kinds of the Selenium Drivers,
 * capabilities, user agents.. etc.
 *
 * @author Fadi Zaboura
 */
public class SeleniumDriver {

    public final static String DRIVER_PATH = System.getProperty("user.dir") + "\\drivers\\";
    private static URL remoteSeleniumHub;

    public enum BrowserType {
        CHROME, FIREFOX
    }

    public enum DisplayMode {
        PORTRAIT, LANDSCAPE
    }

    public enum UserAgent {
        IPHONE("Mozilla/5.0 (iPhone; CPU iPhone OS 6_0 like Mac OS X) AppleWebKit/536.26 (KHTML, like Gecko) Version/6.0 Mobile/10A5376e Safari/8536.25"), ANDROID_PHONE(
                "Mozilla/5.0 (Linux; U; Android 4.0.4; en-gb; GT-I9300 Build/IMM76D) AppleWebKit/534.30 (KHTML, like Gecko) Version/4.0 Mobile Safari/534.30"), IPAD(
                "Mozilla/5.0 (iPad; CPU OS 7_0 like Mac OS X) AppleWebKit/537.51.1 (KHTML, like Gecko) Version/7.0 Mobile/11A465 Safari/9537.53"), DESKTOP_CHROME(
                "Mozilla/5.0 (Windows NT 6.2; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/32.0.1667.0 Safari/537.36");

        private String userAgentString;

        private UserAgent(String ua) {
            userAgentString = ua;
        }

        public Dimension getResolution() {
            Dimension dim;
            if (this.name().equals("IPHONE"))
                dim = new Dimension(320, 600);
            else if (this.name().equals("ANDROID_PHONE"))
                dim = new Dimension(360, 640);
            else if (this.name().equals("IPAD"))
                dim = new Dimension(768, 1024);
            else if (this.name().equals("DESKTOP_CHROME"))
                dim = new Dimension(1200, 786);
            else
                dim = new Dimension(0, 0);
            return dim;
        }

        public Dimension getLandscapeResolution() {
            Dimension dim;
            if (this.name().equals("IPHONE"))
                dim = new Dimension(600, 320);
            else if (this.name().equals("ANDROID_PHONE"))
                dim = new Dimension(640, 360);
            else if (this.name().equals("IPAD"))
                dim = new Dimension(768, 320);
            else
                dim = new Dimension(0, 0);
            return dim;

        }

        /**
         * returns the user agent
         *
         * @return current used user agent
         */
        public String getUserAgentString() {
            return userAgentString;
        }

        public static UserAgent getUserAgentFor(String ua) {
            UserAgent[] agents = values();
            for (UserAgent agent : agents) {
                String val1 = agent.name().replace("_", " ");
                if (val1.equals(ua) || val1.contains(ua)) {
                    return agent;
                }
            }
            return null;
        }

    }

    /**
     * creates a driver according to the settings in BaseSeleniumTest.java
     *
     * @param browser
     * @return the newly created driver.
     * @throws MalformedURLException
     */
    public static WebDriver createDriver(BrowserType browser) {
        return createDriver(browser, null, null, null, null);
    }

    public static WebDriver createDriver(BrowserType browser, String seleniumHub) {
        return createDriver(browser, null, null, null, seleniumHub);
    }

    public static WebDriver createDriver(BrowserType browser, UserAgent userAgent, DesiredCapabilities capabilities,
                                         String seleniumHub) {
        return createDriver(browser, userAgent, capabilities, null, seleniumHub);
    }

    private static WebDriver initLocalChromeDriver(File dummyFile, ChromeOptions options, DesiredCapabilities capabilities, WebDriver driver) {
        if (StringUtils.startsWithIgnoreCase(System.getProperty("os.name"), "Mac OS X")) {
            System.setProperty("webdriver.chrome.driver", "drivers/chromedriver-for-macos");
            options.merge(capabilities);
            driver = new ChromeDriver(options);
            return driver;
        }
        else {
            File file = new File(dummyFile.getAbsolutePath() + "/drivers/chromedriver-for-macos.exe");
            System.setProperty("webdriver.chrome.driver", file.getAbsolutePath());
            if (!file.canExecute())
                System.err.println("could not find chrome driver");
            ChromeDriverService service = new ChromeDriverService.Builder()
                    .usingDriverExecutable(new File(DRIVER_PATH + "chromeDriver.exe")).usingAnyFreePort().build();
            options.merge(capabilities);
            driver = new ChromeDriver(service, options);
            return driver;
        }
    }

    private static WebDriver initRemoteChromeDriver(ChromeOptions options, DesiredCapabilities capabilities,
                                                    WebDriver driver, String seleniumHub) {
        capabilities.setCapability(ChromeOptions.CAPABILITY, options);
        SessionId session;
        try {
            initSelenuimHub(seleniumHub);
            SelTestLog.info("creating driver with url: " + remoteSeleniumHub);
            driver = new RemoteWebDriver(remoteSeleniumHub, capabilities);
            session = ((RemoteWebDriver) driver).getSessionId();
            DriverParamsThreadLocal.setSeasionId(session);
            SelTestLog.info(" created driver successfully ");
            System.err.println("created driver successfully ");

        } catch (Exception e) {
            System.err.println("!!!!!!!!!!!!!!!! Could not instantiate node through the hub!!!!!! retrying...");
            SelTestLog.error("!!!!!!!!!!!!!!!! Could not instantiate node through the hub!!!!!!", e);
            driver = new RemoteWebDriver(remoteSeleniumHub, capabilities);
            session = ((RemoteWebDriver) driver).getSessionId();
            DriverParamsThreadLocal.setSeasionId(session);
        }
        return driver;
    }

    public static WebDriver createDriver(BrowserType browser, UserAgent userAgent, DesiredCapabilities capabilities,
                                         ChromeOptions options, String seleniumHub) {
        WebDriver driver = null;
        PropertiesGetter propertiesGetter = PropertiesGetter.instance();
        LoggingPreferences logs = new LoggingPreferences();
        SelTestLog.info("enable the logTypes");
        logs.enable(LogType.BROWSER, Level.SEVERE);
        logs.enable(LogType.CLIENT, Level.ALL);
        logs.enable(LogType.DRIVER, Level.ALL);
        logs.enable(LogType.PERFORMANCE, Level.ALL);
        logs.enable(LogType.PROFILER, Level.ALL);
        logs.enable(LogType.SERVER, Level.ALL);
        File dummyFile = new File("");

        switch (browser) {
            case CHROME:
                System.setProperty("webdriver.chrome.driver", "res/chromedriver-for-macos.exe");
                if (options == null)
                    options = new ChromeOptions();

                SelTestLog.info("adding chrome capabilities");
                if (PropertiesGetter.instance().retrieveDriverProperties().getMachineType().equalsIgnoreCase("linux")
                        && !PropertiesGetter.instance().retrieveDriverProperties().isLocal()) {
                    options.addArguments(Arrays.asList("--window-position=0,0"));
                    options.addArguments("--headless", "--no-sandbox", "window-size=1920,1080");
                    options.addArguments("--disable-dev-shm-usage");
                    options.addArguments("--no-zygote");
                } else {
                    options.addArguments("--start-maximized");
                }
                options.addArguments("--disable-translate", "--disable-gpu", "--disable-popup-blocking", "disable-infobars", "--test-type"
                        /* "--incognito" */);
                if (userAgent != null) {
                    options.addArguments("--user-agent=" + userAgent.getUserAgentString());
                }

                capabilities = DesiredCapabilities.chrome();
                capabilities.setCapability(ChromeOptions.CAPABILITY, options);
                Map<String, Object> perfLogPrefs = new HashMap<String, Object>();
                perfLogPrefs.put("traceCategories", "browser,devtools.timeline,devtools");
                perfLogPrefs.put("enableNetwork", true);
                options.setExperimentalOption("perfLoggingPrefs", perfLogPrefs);
                // capabilities.setCapabilbity(ChromeOptions.CAPABILITY, options);
                capabilities.setCapability(ChromeOptions.CAPABILITY, logs);

                if (capabilities != null) {
                    SelTestLog.info("set logging prefs capability");
                    capabilities.setCapability(CapabilityType.LOGGING_PREFS, logs);
                    capabilities.setCapability("goog:loggingPrefs", logs);
                }
                System.setProperty("webdriver.chrome.logfile", "NUL");

                //////////////////////////////////////////////////////////////////////////////////////////
                HashMap<String, Object> chromeLocalStatePrefs = new HashMap<>();
                List<String> experimentalFlags = new ArrayList<>();
                experimentalFlags.add("calculate-native-win-occlusion@2");
                chromeLocalStatePrefs.put("browser.enabled_labs_experiments", experimentalFlags);
                options.setExperimentalOption("localState", chromeLocalStatePrefs);
                //////////////////////////////////////////////////////////////////////////////////////////

                if (!propertiesGetter.retrieveDriverProperties().isLocal()) {
                    driver = initRemoteChromeDriver(options, capabilities, driver, seleniumHub);
                    ((RemoteWebDriver) driver).setFileDetector(new LocalFileDetector());
                    Capabilities cap = ((RemoteWebDriver) driver).getCapabilities();
                    SelTestLog.info("Browser name : " + cap.getBrowserName());
                    SelTestLog.info("Browser Version :" + cap.getVersion());
                } else {
                    driver = initLocalChromeDriver(dummyFile, options, capabilities, driver);
                    SelTestLog.info("Browser name : " + capabilities.getBrowserName());
                }
                break;
            case FIREFOX:
                if (!PropertiesGetter.instance().retrieveDriverProperties().isLocal()) {
                    FirefoxOptions ffOptions = new FirefoxOptions();
                    ffOptions.setHeadless(true);
                    System.setProperty("webdriver.gecko.driver", "/usr/local/bin/geckodriver");
                    initSelenuimHub(seleniumHub);
                    DesiredCapabilities capability =
                            DesiredCapabilities.firefox();
                    capability.setCapability("marionette", true);
                    capability.setCapability("platform", Platform.LINUX);
                    capability.setBrowserName("firefox");
                    //capability.setCapability("version", "latest");
                    capability.setJavascriptEnabled(true);
                    capability.setCapability(FirefoxOptions.FIREFOX_OPTIONS, ffOptions);
                    //capability.acceptInsecureCerts();
                    //capability.setCapability("networkConnectionEnabled", true);
                    // capability.setCapability("browserConnectionEnabled", true);
                    capability.setCapability("binary", "/usr/bin/firefox"); //for linux
                    capability.setCapability("--window-size", "1920,1080");
                    driver = new RemoteWebDriver(remoteSeleniumHub, capability);
                    SessionId session = ((RemoteWebDriver) driver).getSessionId();
                    DriverParamsThreadLocal.setSeasionId(session);
                    SelTestLog.info(" created driver successfully ");
                    System.err.println("created driver successfully ");

                    ((RemoteWebDriver) driver).setFileDetector(new LocalFileDetector());
                    Capabilities cap = ((RemoteWebDriver) driver).getCapabilities();
                    SelTestLog.info("Browser name : " + cap.getBrowserName());
                    SelTestLog.info("Browser Version :" + cap.getVersion());
                } else {
                    System.setProperty("webdriver.gecko.driver", DRIVER_PATH + "geckodriver.exe");
                    DesiredCapabilities ffcapabilities = DesiredCapabilities.firefox();
                    ffcapabilities.setCapability("marionette", true);
                    driver = new FirefoxDriver();
                }
                driver.manage().window().setSize(new Dimension(1920, 1080));
                ((JavascriptExecutor) driver).executeScript("window.focus();");
                break;
        }
        if (!PropertiesGetter.instance().retrieveDriverProperties().getMachineType().equals("linux")) {
            String resolution = propertiesGetter.retrieveDriverProperties().getResolution();
            if (resolution.equals("maximize"))
                driver.manage().window().maximize();
            else {
                String[] dimen = resolution.split(",");
                int x = Integer.parseInt(dimen[0]);
                int y = Integer.parseInt(dimen[1]);
                driver.manage().window().setSize(new Dimension(x, y));
                System.err.println("+++++++++++++++++++++++++++++++ Dimensions are: " + x + " ," + y + '\n');
                driver.manage().window().getSize();
                int height = driver.manage().window().getSize().height;
                int width = driver.manage().window().getSize().width;
                System.err.println("+++++++++++++++++++++++++++++++ Dimensions are: " + height + " ," + width + '\n');
            }
        }
        return driver;
    }

    private static void initSelenuimHub(String seleniumHub) {
        if (seleniumHub == null) {
            try {
                SelTestLog.info("trying to initialize remoteSeleniumHub");
                SelTestLog.info("active hub: " + BaseSeleniumTest.seleniumHub);
                remoteSeleniumHub = new URL(BaseSeleniumTest.seleniumHub + "/wd/hub");
                SelTestLog.info("succeeded to initialize remoteSeleniumHub");
            } catch (MalformedURLException e) {
                SelTestLog.warn(e.getMessage());
            }
        } else {
            try {
                remoteSeleniumHub = new URL(BaseSeleniumTest.seleniumHub + "/wd/hub");
                SelTestLog.info("succeeded to initialize remoteSeleniumHub");
            } catch (MalformedURLException e) {
                SelTestLog.error("Double check the HUB URL: " + seleniumHub);
            }
        }
    }

    public static void setChromeDownloadLocation(WebDriver driver) {
        driver.get("chrome://settings/advanced");
        JavascriptExecutor js = (JavascriptExecutor) driver;
        String prefId = "download.default_directory";
        File f = new File("");
        File tempDir = new File(f.getAbsolutePath() + File.separator + "seleniumDownloads" + File.separator);
        if (driver.findElements(By.xpath(String.format(".//input[@pref='%s']", prefId))).size() == 0) {
            driver.get("chrome://settings-frame");
            WebElementUtils.hoverOverField(driver.findElement(By.id("advanced-settings-expander")), driver, null);
            driver.findElement(By.id("advanced-settings-expander")).click();
        }
        String tmpDirEscapedPath = null;
        try {
            tmpDirEscapedPath = tempDir.getCanonicalPath().replace("\\", "\\\\");
        } catch (IOException e) {
            e.printStackTrace();
        }
        WebElementUtils.hoverOverField(driver.findElement(By.id("advanced-settings-expander")), driver, null);
        BaseSeleniumTest.takeScreenshot(driver, WebdriverUtils.getTimeStamp("downloadsPath"));
        js.executeScript(String.format("Preferences.setStringPref('%s', '%s', true)", prefId, tmpDirEscapedPath));
    }
}