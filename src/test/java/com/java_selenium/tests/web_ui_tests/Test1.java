package com.java_selenium.tests.web_ui_tests;

import com.java_selenium.base.BaseClass;
import com.java_selenium.utils.extent_reports_manager.ExtentTestManager;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;

public class Test1 extends BaseClass
{
    @Test
    public void TestGoogleSearch()
    {
        WebDriver driver = GetDriver();  // Get the WebDriver instance from BaseClass
        driver.get("https://www.google.com");
        ExtentTestManager.GetTest().info(driver.getTitle());
    }

    @Test
    public void TestGoogleSearch1()
    {
        WebDriver driver = GetDriver();  // Get the WebDriver instance from BaseClass
        driver.get("https://www.google.com");
        ExtentTestManager.GetTest().info(driver.getTitle());
    }
}
