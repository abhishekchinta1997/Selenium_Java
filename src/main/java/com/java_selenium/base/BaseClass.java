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
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.ITestResult;
import org.testng.Reporter;
import org.testng.annotations.*;

public class BaseClass
{
    private String browserName;
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
        browserName = System.getProperty("browserName", "chrome");
        if (browserName == null || browserName.isEmpty())
        {
            throw new IllegalArgumentException("Browser name cannot be null or empty");
        }

        InitBrowser(browserName);
        driver.get().manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
        driver.get().manage().window().maximize();
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

    // Initialize the browser based on the name
    public void InitBrowser(String browserName)
    {
        switch (browserName.toLowerCase()) {
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
    }

    // After each test method runs
    @AfterMethod
    public void AfterTest(ITestResult result)
    {
        // Determine test result
        String status = result.getStatus() == ITestResult.FAILURE ? "FAIL" : (result.getStatus() == ITestResult.SKIP ? "SKIP" : "PASS");
        String stackTrace = result.getThrowable() != null ? "Stack Trace: " + result.getThrowable().getMessage() : "";
        if (result.getStatus() == ITestResult.FAILURE)
        {
            ExtentTestManager.GetTest().fail("Test failed", CaptureScreenshot(GetDriver(), "Screenshot"));
        }
        else if (result.getStatus() == ITestResult.SKIP)
        {
            ExtentTestManager.GetTest().warning("Test skipped");
        }
        else
        {
            ExtentTestManager.GetTest().pass("Test passed");
        }

        if (!stackTrace.isEmpty())
        {
            ExtentTestManager.GetTest().info(stackTrace);
        }

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
    public static ExtentTest LogInfo(String msg)
    {
        return ExtentTestManager.GetTest().info(msg);
    }

    // Logs a pass message in the test report when a test passes
    public static ExtentTest LogPass(String msg)
    {
        return ExtentTestManager.GetTest().pass(msg);
    }

    // Logs a fail message in the test report when a test fails
    public static ExtentTest LogFail(String msg)
    {
        return ExtentTestManager.GetTest().fail(msg);
    }

}
