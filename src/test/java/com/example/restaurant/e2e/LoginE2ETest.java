package com.example.restaurant.e2e;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.Duration;

public class LoginE2ETest {

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
    public void testLoginSuccess() {
        // Act
        driver.get("http://localhost:3000/login");

        WebElement usernameField = driver.findElement(By.id("formBasic"));
        usernameField.sendKeys("newuser1");

        WebElement passwordField = driver.findElement(By.id("formBasicPassword"));
        passwordField.sendKeys("NewUser@123!");

        WebElement submitButton = driver.findElement(By.cssSelector("button[type='submit']"));
        submitButton.click();

        wait.until(ExpectedConditions.urlToBe("http://localhost:3000/"));

        // Assert
        String currentUrl = driver.getCurrentUrl();
        assertEquals("http://localhost:3000/", currentUrl);
        System.out.println("Login successful, redirected to: " + currentUrl);

        driver.get("http://localhost:3000/");
        String sessionUserId = (String) ((org.openqa.selenium.JavascriptExecutor) driver).executeScript(
                "return window.sessionStorage.getItem('userId');");

        assertTrue(sessionUserId != null && !sessionUserId.isEmpty(), "User ID should be stored in session storage.");
        System.out.println("Session storage contains user ID: " + sessionUserId);

        String sessionUser = (String) ((org.openqa.selenium.JavascriptExecutor) driver).executeScript(
                "return window.sessionStorage.getItem('user');");

        assertTrue(sessionUser != null && !sessionUser.isEmpty(), "User data should be stored in session storage.");
        System.out.println("Session storage contains user data: " + sessionUser);

    }

    @Test
    public void testLoginFail() {
        // Act
        driver.get("http://localhost:3000/login");

        WebElement usernameField = driver.findElement(By.id("formBasic"));
        usernameField.sendKeys("inavliduser");

        WebElement passwordField = driver.findElement(By.id("formBasicPassword"));
        passwordField.sendKeys("1234");

        WebElement submitButton = driver.findElement(By.cssSelector("button[type='submit']"));
        submitButton.click();

        // Assert
        String currentUrl = driver.getCurrentUrl();
        assertEquals("http://localhost:3000/login", currentUrl);
        System.out.println("Login unsucessful, cureent : " + currentUrl);

    }

    @AfterAll
    public static void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}