package com.java_selenium.tests.web_ui_tests;

import com.java_selenium.base.BaseClass;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.time.Duration;


public class Alert_Tests extends BaseClass
{
    @Test(priority = 1)
    public void Test_Simple_Alert()
    {
        WebDriver driver = GetDriver();  // Get the WebDriver instance from BaseClass

        driver.get("https://the-internet.herokuapp.com/javascript_alerts");  // Navigate to the JavaScript alerts page
        LogInfo("Navigated to 'https://the-internet.herokuapp.com/javascript_alerts'");

        // Click the button 'Click for JS Alert' to trigger the prompt alert
        driver.findElement(By.xpath("(//button[normalize-space()='Click for JS Alert'])[1]")).click();

        // Wait for the alert to appear
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.alertIsPresent());
        LogInfo("Clicked the button 'Click for JS Alert' to trigger the prompt alert");

        // Switch to the alert and print its text
        Alert alert = driver.switchTo().alert();
        String alertText = alert.getText();
        LogInfo("Alert text: " + alertText);

        // Assert that the alert text is not empty
        Assert.assertFalse(alertText.isEmpty(), "Alert text is empty!"); // TestNG assertion, failure will throw an exception

        alert.accept();  // Accept the alert
        LogInfo("Accepted the alert");
    }


    @Test(priority = 2)
    public void Test_JS_Confirm()
    {
        WebDriver driver = GetDriver();  // Get the WebDriver instance from BaseClass

        driver.get("https://the-internet.herokuapp.com/javascript_alerts");  // Navigate to the JavaScript alerts page
        LogInfo("Navigated to 'https://the-internet.herokuapp.com/javascript_alerts'");

        // Click the button 'Click for JS Confirm' to trigger the confirmation alert
        driver.findElement(By.xpath("(//button[normalize-space()='Click for JS Confirm'])[1]")).click();

        // Wait for the alert to appear
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.alertIsPresent());
        LogInfo("Clicked the button 'Click for JS Confirm' to trigger the confirm alert");

        // Switch to the alert and print its text
        Alert alert = driver.switchTo().alert();
        String alertText = alert.getText();
        LogInfo("Alert text: " + alertText);

        // Assert that the alert text is not empty
        Assert.assertFalse(alertText.isEmpty(), "Alert text is empty!"); // TestNG assertion, failure will throw an exception

        // Accept the alert (clicking "OK")
        alert.accept();
        LogInfo("Accepted the confirm alert");
    }


    @Test(priority = 3)
    public void Test_JS_Prompt()
    {
        WebDriver driver = GetDriver();  // Get the WebDriver instance from BaseClass

        driver.get("https://the-internet.herokuapp.com/javascript_alerts");  // Navigate to the JavaScript alerts page
        LogInfo("Navigated to 'https://the-internet.herokuapp.com/javascript_alerts'");

        // Click the button 'Click for JS Prompt' to trigger the prompt alert
        driver.findElement(By.xpath("(//button[normalize-space()='Click for JS Prompt'])[1]")).click();

        // Wait for the alert to appear
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.alertIsPresent());
        LogInfo("Clicked the button 'Click for JS Prompt' to trigger the prompt alert");

        // Switch to the alert and print its text
        Alert alert = driver.switchTo().alert();
        String alertText = alert.getText();
        LogInfo("Alert text: " + alertText);

        // Assert that the alert text is not empty
        Assert.assertFalse(alertText.isEmpty(), "Alert text is empty!"); // TestNG assertion, failure will throw an exception

        // Send some input text to the prompt (e.g., "Test input")
        alert.sendKeys("Test input");
        LogInfo("Entered text 'Test input' into the prompt");

        // Accept the prompt (clicking "OK")
        alert.accept();
        LogInfo("Accepted the prompt alert");
    }




}
