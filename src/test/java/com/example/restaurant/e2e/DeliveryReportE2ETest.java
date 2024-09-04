package com.example.restaurant.e2e;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.Duration;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.chrome.ChromeOptions;

public class DeliveryReportE2ETest {
    private static WebDriver driver;
    private static WebDriverWait wait;

    @BeforeAll
    public static void setUp() {
        String chromeDriverPath = "C:\\Tools\\chromedriver.exe";
        System.setProperty("webdriver.chrome.driver", chromeDriverPath);

        ChromeOptions options = new ChromeOptions();
        driver = new ChromeDriver(options);
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    @Test
    public void testDeliveryReportGeneration() {
        driver.get("http://localhost:3000/admin");
        WebElement usernameField = driver.findElement(By.cssSelector("input[placeholder='Enter username']"));
        usernameField.sendKeys("adminuser1");

        WebElement passwordField = driver.findElement(By.cssSelector("input[placeholder='Enter password']"));
        passwordField.sendKeys("adminuser1");

        WebElement submitButton = driver.findElement(By.cssSelector("button"));
        submitButton.click();
        wait.until(ExpectedConditions.urlToBe("http://localhost:3000/admin/dashboard"));

        driver.get("http://localhost:3000/manage/report");

        // Select "Delivery" from the report type dropdown
        WebElement reportTypeDropdown = driver.findElement(By.cssSelector("select[class='form-input']"));
        reportTypeDropdown.sendKeys("Delivery");

        WebElement startDateField = driver.findElement(By.cssSelector("input[type='date']"));
        startDateField.sendKeys("2024-01-01");

        WebElement endDateField = driver.findElement(By.cssSelector("input[type='date']"));
        endDateField.sendKeys("2024-12-31");

        WebElement generateButton = driver.findElement(By.cssSelector("button[class='generate-btn']"));
        generateButton.click();

        wait.until(ExpectedConditions.urlToBe("http://localhost:3000/report-display/delivery"));

        WebElement reportTable = driver.findElement(By.cssSelector("table.main-table"));
        assertTrue(reportTable.isDisplayed(), "Report table should be displayed.");

        WebElement totalDeliveriesElement = driver.findElement(By.cssSelector("div.summary-info p"));
        String totalDeliveriesText = totalDeliveriesElement.getText();
        assertTrue(totalDeliveriesText.contains("Total Deliveries:"), "Total Deliveries should be displayed.");

        System.out.println("Delivery report generated successfully and displayed.");
    }

    @AfterAll
    public static void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}
