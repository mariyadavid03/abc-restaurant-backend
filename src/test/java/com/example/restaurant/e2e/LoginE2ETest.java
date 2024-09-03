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
        String chromeDriverPath = System.getenv("CHROMEDRIVER_PATH");
        System.out.println("CHROMEDRIVER_PATH: " + chromeDriverPath);
        if (chromeDriverPath == null) {
            throw new RuntimeException("CHROMEDRIVER_PATH environment variable is not set.");
        }
        System.setProperty("webdriver.chrome.driver", chromeDriverPath);

        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--remote-debugging-port=9222");
        options.addArguments("--disable-gpu");

        driver = new ChromeDriver(options);
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    @Test
    public void testLoginSuccess() {
        driver.get("http://localhost:3000/login");

        WebElement usernameField = driver.findElement(By.id("formBasic"));
        usernameField.sendKeys("newuser1");

        WebElement passwordField = driver.findElement(By.id("formBasicPassword"));
        passwordField.sendKeys("1234");

        WebElement submitButton = driver.findElement(By.cssSelector("button[type='submit']"));
        submitButton.click();

        wait.until(ExpectedConditions.urlToBe("http://localhost:3000/"));

        String currentUrl = driver.getCurrentUrl();
        assertEquals("http://localhost:3000/", currentUrl);
        System.out.println("Login successful, redirected to: " + currentUrl);

        // Check session storage
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
        driver.get("http://localhost:3000/login");

        WebElement usernameField = driver.findElement(By.id("formBasic"));
        usernameField.sendKeys("inavliduser");

        WebElement passwordField = driver.findElement(By.id("formBasicPassword"));
        passwordField.sendKeys("1234");

        WebElement submitButton = driver.findElement(By.cssSelector("button[type='submit']"));
        submitButton.click();

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