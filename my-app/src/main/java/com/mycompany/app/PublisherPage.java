package com.mycompany.app;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class PublisherPage extends BasePage {

    @FindBy(css = "[data-testid='action-new']")
    private WebElement newActionButton;

    public PublisherPage(WebDriver driver) {
        super(driver);
    }

    public void clickNewActionButton() {
        newActionButton.click();
    }
}
