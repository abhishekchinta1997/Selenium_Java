package com.java_selenium.tests.web_ui_tests;

import com.java_selenium.base.BaseClass;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.util.List;



import java.time.Duration;

public class Example_Wait_Tests_01 extends BaseClass
{
    @Test (priority = 1)
    public void Test_Implicit_Wait()
    {
        WebDriver driver = GetDriver();  // Get the WebDriver instance from BaseClass
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));   // Add implicit wait for 10 seconds
        driver.get("https://the-internet.herokuapp.com/drag_and_drop");  // Navigate to the Drag and Drop page
        LogInfo("Navigated to 'https://the-internet.herokuapp.com/drag_and_drop'");

        // Locate the two draggable elements (Box A and Box B)
        WebElement boxA = driver.findElement(By.id("column-a"));
        WebElement boxB = driver.findElement(By.id("column-b"));

        // Assert that both boxes are displayed before performing any actions
        Assert.assertTrue(boxA.isDisplayed(), "Box A is not displayed.");
        LogInfo("Box A is Displayed");
        Assert.assertTrue(boxB.isDisplayed(), "Box B is not displayed.");
        LogInfo("Box A is Displayed");

        // Create an Actions object to perform drag and drop
        Actions actions = new Actions(driver);

        // Perform the drag and drop action: Drag Box A to Box B's position
        actions.dragAndDrop(boxA, boxB).build().perform();

        // Assert that the boxes have been swapped (Box A should now be in Box B's position)
        Assert.assertEquals(boxA.getText(), "B", "Box A did not move to Box B's position.");
        LogInfo("Box A is now moved to Box B's position.");
        Assert.assertEquals(boxB.getText(), "A", "Box B did not move to Box A's position.");
        LogInfo("Box B is now moved to Box A's position.");
    }


    @Test (priority = 2)
    public void Test_Explicit_Wait()
    {
        WebDriver driver = GetDriver();  // Get the WebDriver instance from BaseClass
        driver.get("http://the-internet.herokuapp.com/dynamic_controls");
        LogInfo("Navigated to 'http://the-internet.herokuapp.com/dynamic_controls'");

        // Create a WebDriverWait instance
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        // Click the "Remove" button to dynamically remove the checkbox
        WebElement removeButton = driver.findElement(By.xpath("//button[normalize-space()='Remove']"));
        removeButton.click();
        LogInfo("'Remove' button clicked");

        // Use WebDriverWait to wait for the checkbox to become invisible
        boolean isCheckboxInvisible = wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath("//input[@type='checkbox']")));

        // Assert that the checkbox is invisible (i.e., it has been removed)
        Assert.assertTrue(isCheckboxInvisible, "Checkbox is still visible, expected it to be removed.");
        LogInfo("Checkbox is invisible, as expected.");

        WebElement msg = driver.findElement(By.xpath("//p[@id='message']"));
        Assert.assertTrue(msg.isDisplayed(), "Message is not Displayed");
        CaptureScreenshot(driver, "Msg");
        LogInfo("Message is Displayed, as expected");
    }


    @Test (priority = 3)
    public void Test_Fluent_Wait()
    {
        WebDriver driver = GetDriver();  // Get the WebDriver instance from BaseClass
        driver.get("http://the-internet.herokuapp.com/dynamic_controls");
        LogInfo("Navigated to 'http://the-internet.herokuapp.com/dynamic_controls'");

        Wait<WebDriver> wait = new FluentWait<>(driver)
                .withTimeout(Duration.ofSeconds(20))
                .pollingEvery(Duration.ofSeconds(2))
                .ignoring(NoSuchElementException.class);

        WebElement removeButton = driver.findElement(By.xpath("//button[normalize-space()='Remove']"));
        removeButton.click();
        LogInfo("'Remove' button clicked");

        // Fluent wait: Check if the checkbox is not displayed
        Boolean isCheckboxRemoved = wait.until(driver1 -> {
            List<WebElement> checkboxes = driver1.findElements(By.xpath("//input[@type='checkbox']"));
            return checkboxes.isEmpty();  // Return true when checkbox is removed (list is empty)
        });

        // Assertion to check that checkbox is removed
        Assert.assertTrue(isCheckboxRemoved, "Checkbox was not successfully removed");
        LogInfo("Checkbox is invisible, as expected.");

        WebElement msg = driver.findElement(By.xpath("//p[@id='message']"));
        Assert.assertTrue(msg.isDisplayed(), "Message is not Displayed");
        CaptureScreenshot(driver, "Msg");
        LogInfo("Message is Displayed, as expected");
    }




}
