package com.java_selenium.listeners;

import com.aventstack.extentreports.model.Media;
import com.java_selenium.base.BaseClass;
import com.java_selenium.utils.extent_reports_manager.ExtentTestManager;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

public class TestListener extends BaseClass implements ITestListener
{
    @Override
    public void onStart(ITestContext context)
    {
        // Create a parent test in the Extent report
        ExtentTestManager.CreateParentTest(context.getName());
    }

    @Override
    public void onTestStart(ITestResult result)
    {
        // Create a new test in the Extent report
        ExtentTestManager.CreateMethod(result.getMethod().getMethodName());
    }

    @Override
    public void onTestSuccess(ITestResult result)
    {
        // Log the success in the Extent report
        ExtentTestManager.GetMethod().pass("Test passed");
    }

    @Override
    public void onTestFailure(ITestResult result)
    {
        // Log the failure in the Extent report
        ExtentTestManager.GetMethod().fail(result.getThrowable());

        // Take screenshot only if the test fails
        Media screenshot = CaptureScreenshot(GetDriver(), GetScreenshotName());
        ExtentTestManager.GetMethod().fail("Test failed", screenshot);
    }

    @Override
    public void onTestSkipped(ITestResult result)
    {
        // Log the skip in the Extent report
        ExtentTestManager.GetMethod().skip("Test skipped");
    }

    @Override
    public void onFinish(ITestContext context)
    {
        // Clear the test instances from ThreadLocal
        ExtentTestManager.clearTest();
    }

}
