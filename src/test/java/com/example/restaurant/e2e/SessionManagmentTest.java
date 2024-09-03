package com.example.restaurant.e2e;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.Duration;
import java.util.List;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class SessionManagmentTest {
    private static WebDriver driver;
    private static WebDriverWait wait;

    @BeforeAll
    public static void setUp() {
        System.setProperty("webdriver.chrome.driver", "C:\\Windows\\System32\\chromedriver.exe");
        driver = new ChromeDriver();
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    @Test
    public void testAddToCartAndSessionUpdate() {
        // Login
        driver.get("http://localhost:3000/login");

        WebElement usernameField = driver.findElement(By.id("formBasic"));
        usernameField.sendKeys("newuser1");

        WebElement passwordField = driver.findElement(By.id("formBasicPassword"));
        passwordField.sendKeys("1234");

        WebElement submitButton = driver.findElement(By.cssSelector("button[type='submit']"));
        submitButton.click();

        wait.until(ExpectedConditions.urlToBe("http://localhost:3000/"));

        // Navigate to the menu page and add item
        driver.get("http://localhost:3000/menu");

        WebElement appetizersTab = driver.findElement(By.cssSelector("button.tab-button"));
        appetizersTab.click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("div.menu-item")));

        WebElement firstAddToCartButton = wait
                .until(ExpectedConditions.elementToBeClickable(By.cssSelector("div.add-to-cart")));
        JavascriptExecutor jss = (JavascriptExecutor) driver;
        jss.executeScript("arguments[0].scrollIntoView(true);", firstAddToCartButton); // Scroll to the element
        jss.executeScript("arguments[0].click();", firstAddToCartButton); // Click with JavaScript

        try {
            Alert alert = wait.until(ExpectedConditions.alertIsPresent());
            String alertText = alert.getText();
            alert.accept();
            assertTrue(alertText.contains("Item added to cart"), "The item should be added to the cart.");
        } catch (Exception e) {
            System.out.println("No alert appeared.");
        }
        driver.get("http://localhost:3000/cart");
        JavascriptExecutor js = (JavascriptExecutor) driver;

        // Retrieve items from session
        List<Long> cartItems = (List<Long>) js
                .executeScript("return JSON.parse(window.sessionStorage.getItem('cartItems'));");

        System.out.println("Cart Items: " + cartItems);
        assertTrue(cartItems != null && !cartItems.isEmpty(), "Cart should not be empty.");
        assertTrue(cartItems.contains(5L), "Cart should contain the added item.");

        System.out.println("Cart and session management verified successfully.");
    }

    @AfterAll
    public static void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}
