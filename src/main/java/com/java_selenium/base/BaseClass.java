package com.java_selenium.base;

import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.model.Media;
import com.java_selenium.utils.extent_reports_manager.ExtentManager;
import com.java_selenium.utils.extent_reports_manager.ExtentTestManager;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import java.lang.reflect.Method;
import java.time.Duration;
import java.util.Arrays;
import java.util.Date;
import java.text.SimpleDateFormat;

import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.ITestResult;
import org.testng.Reporter;
import org.testng.annotations.*;

public class BaseClass
{
    private ThreadLocal<WebDriver> driver = new ThreadLocal<>();

    // Before the first test method runs
    @BeforeClass
    public void Setup()
    {
        // Create parent test at the beginning of the test run
        // String className = this.getClass().getSimpleName();
        // ExtentTestManager.CreateParentTest(className);
    }

    // After all test methods have run
    @AfterClass
    public void TearDown()
    {
        // Ensure that all reports are flushed after the tests are complete
        ExtentManager.getInstance().flush();
    }

    // Before each test method runs
    @BeforeMethod
    public void StartBrowser(Method method)
    {
        // Create a new test for each individual test run
        String className = Reporter.getCurrentTestResult().getTestClass().getName().substring(
                Reporter.getCurrentTestResult().getTestClass().getName().lastIndexOf('.') + 1
        );

        //String methodName = Reporter.getCurrentTestResult().getMethod().getMethodName();

        // Get method name dynamically
        String methodName = method.getName();  // Use 'method.getName()' instead of Reporter

        ExtentTestManager.CreateTest(className, methodName);

        // Get browser name from the system or configuration
        String browserName = System.getProperty("browserName", "chrome");
        if (browserName == null || browserName.isEmpty())
        {
            throw new IllegalArgumentException("Browser name cannot be null or empty");
        }

        switch (browserName.toLowerCase())
        {
            case "firefox":
                driver.set(new FirefoxDriver());
                break;
            case "chrome":
                driver.set(new ChromeDriver());
                break;
            case "edge":
                driver.set(new EdgeDriver());
                break;
            default:
                throw new IllegalArgumentException("Unsupported browser: " + browserName);
        }

        //GetDriver().manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
        GetDriver().manage().window().maximize();
    }


    // Get WebDriver instance
    public WebDriver GetDriver()
    {
        if (driver.get() == null)
        {
            throw new IllegalStateException("Driver is not initialized.");
        }
        return driver.get();
    }


    // After each test method runs
    @AfterMethod
    public void AfterTest(ITestResult result)
    {
        // Determine test result status and stack trace
        String stackTrace = result.getThrowable() != null ?
                "<pre>Message: <br>" + result.getThrowable().getMessage() +
                        "<br><br>Stack Trace: <br>" + Arrays.toString(result.getThrowable().getStackTrace()) + "</pre>" : "";

        Date time = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("h_mm_ss");
        String formattedTime = dateFormat.format(time);
        String screenShotName = "Screenshot_" + formattedTime + ".png";

        // Handle FAILURE
        if (result.getStatus() == ITestResult.FAILURE)
        {
            // Apply red color formatting for the message and stack trace
            String failureMessage = "<span style='color:red;'>Test failed</span>";
            String failureStackTrace = stackTrace.isEmpty() ? "" : "<span style='color:red;'>" + stackTrace + "</span>";

            ExtentTestManager.GetTest().fail(failureMessage, CaptureScreenshot(GetDriver(), screenShotName));
            if (!stackTrace.isEmpty())
            {
                ExtentTestManager.GetTest().fail(failureStackTrace);
                ExtentTestManager.GetTest().fail("Test Ended with Fail");
            }
        }
        else if (result.getStatus() == ITestResult.SKIP)
        {
            ExtentTestManager.GetTest().warning("Test skipped");
            if (!stackTrace.isEmpty())
            {
                ExtentTestManager.GetTest().warning(stackTrace);
                ExtentTestManager.GetTest().warning("Test Ended with Warning");
            }
        }
        // Handle PASS
        else if (result.getStatus() == ITestResult.SUCCESS)
        {
            ExtentTestManager.GetTest().pass("Test Ended with Pass");
        }

        ExtentTestManager.clearTest(); // Clear the ThreadLocal reference to the current test

        GetDriver().quit();  // Quit the browser driver after the test
        driver.remove();  // Clean up the thread-local instance
    }




    // Capture screenshot method
    public static Media CaptureScreenshot(WebDriver driver, String screenshotName)
    {
        String screenshotBase64 = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BASE64);
        return MediaEntityBuilder.createScreenCaptureFromBase64String(screenshotBase64, screenshotName).build();
    }

    // Logs an informational message in the test report
    public static void LogInfo(String msg)
    {
        ExtentTestManager.GetTest().info(msg);
    }


    // Logs a pass message in the test report when a test passes
    public static ExtentTest LogPass(String msg)
    {
        ExtentTestManager.GetTest().pass(msg);
        return ExtentTestManager.GetTest();
    }

    // Logs a fail message in the test report when a test fails
    public static ExtentTest LogFail(String msg)
    {
        ExtentTestManager.GetTest().fail(msg);
        return ExtentTestManager.GetTest();
    }

}
