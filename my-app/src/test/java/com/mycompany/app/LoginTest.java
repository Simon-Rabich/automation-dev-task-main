package com.mycompany.app;

import com.mycompany.app.LoginPage;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import java.util.concurrent.TimeUnit;

public class LoginTest {
    private WebDriver driver;
    private LoginPage loginPage;

    @Before
    @Test
    public void setUp() {
        System.setProperty("webdriver.chrome.driver", "drivers/chromedriver-for-macos");
        driver = new ChromeDriver();
        driver.get("https://radioparadise.com/");
        loginPage = new LoginPage(driver);
    }
//
//    @Test
//    public void testLogin() {
//        loginPage.login("admin@example.com", "password");
//        // Add assertions here to verify successful login, such as checking the presence of an element on the dashboard page
//    }

    @After
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}
