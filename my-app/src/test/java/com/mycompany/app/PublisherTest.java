package com.mycompany.app;;

import com.mycompany.app.PublisherPage;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

public class PublisherTest {
    private WebDriver driver;
    private PublisherPage publisherPage;

    @Before
    public void setUp() {
        // Set path to your WebDriver executable
        System.setProperty("webdriver.chrome.driver", "path/to/chromedriver");
        driver = new ChromeDriver();
        driver.get("http://localhost:3000/admin/resources/Publisher");
        publisherPage = new PublisherPage(driver);
    }

    @Test
    public void testClickNewActionButton() {
        publisherPage.clickNewActionButton();
        // Add assertions here to verify the outcome of clicking the button
    }

    @After
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}
