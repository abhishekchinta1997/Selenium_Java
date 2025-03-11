package com.java_selenium.tests.web_ui_tests;

import com.java_selenium.base.BaseClass;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.time.Duration;

public class Alert_Tests extends BaseClass
{
    @Test(priority = 1)
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


}
