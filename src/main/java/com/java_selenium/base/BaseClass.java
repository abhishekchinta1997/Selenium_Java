package com.java_selenium.base;

import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.model.Media;
import com.java_selenium.utils.extent_reports_manager.ExtentManager;
import com.java_selenium.utils.extent_reports_manager.ExtentTestManager;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.testng.ITestNGMethod;
import org.testng.ITestResult;
import org.testng.Reporter;
import org.testng.annotations.*;

import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

public class BaseClass
{
    // ThreadLocal to store WebDriver instance per thread
    private ThreadLocal<WebDriver> driver = new ThreadLocal<>();

    // Static connection object for DB connection
    protected static Connection connection;

    // This method is called before any tests are executed (once per class)
    @BeforeClass
    public void Setup()
    {
        // Determine the test type dynamically based on the package name
        String testType = determineTestType(this.getClass().getPackage().getName());

        // Initialize the extent reporting system with the test type
        ExtentManager.initialize(testType);
    }

    private String determineTestType(String packageName)
    {
        if (packageName.contains("web_tests"))
        {
            return "web";
        }
        else if (packageName.contains("db_tests"))
        {
            return "db";
        }
        else if (packageName.contains("excel_tests"))
        {
            return "excel";
        }
        else
        {
            return "web"; // Default to web if not specified
        }
    }

    // This method is called before each test method execution
    @BeforeMethod
    public void StartTest(Method method)
    {
        // Extract class and method names for the test report
        String className = Reporter.getCurrentTestResult().getTestClass().getName().substring(
                Reporter.getCurrentTestResult().getTestClass().getName().lastIndexOf('.') + 1
        );
        String methodName = method.getName();

        // Check test type and initialize corresponding resources (WebDriver, DB connection, etc.)
        if (isWebTest(method))
        {
            ExtentTestManager.CreateTest(className, methodName);
            initializeWebDriver();
        }
        else if (isDBTest(method))
        {
            ExtentTestManager.CreateTest(className, methodName);
            initializeDBConnection();
        }
        else if (isExcelTest(method))
        {
            ExtentTestManager.CreateTest(className, methodName);
        }
    }

    // This method is called after each test method execution
    @AfterMethod
    public void AfterTest(ITestResult result)
    {
        // Handle the test result and manage resources (close WebDriver, DB connection, etc.)
        if (isWebTest(result.getMethod()))
        {
            handleTestResult(result, ExtentTestManager.GetTest());
            GetDriver().quit(); // Close the WebDriver instance
            driver.remove(); // Remove the WebDriver from the thread-local storage
        }
        else if (isDBTest(result.getMethod()))
        {
            handleTestResult(result, ExtentTestManager.GetTest());
        }
        else if (isExcelTest(result.getMethod()))
        {
            handleTestResult(result, ExtentTestManager.GetTest());
        }
    }

    // This method is called after all tests in the class are finished
    @AfterClass
    public void TearDown()
    {
        // Close the database connection if it is still open
        if (connection != null)
        {
            try
            {
                connection.close();
            }
            catch (SQLException e)
            {
                e.printStackTrace(); // Log the exception if the connection cannot be closed
            }
        }

        // Flush the ExtentReports instance to save all the test logs
        ExtentManager.getInstance().flush();
    }

    // Initialize WebDriver based on the browser specified in the system properties
    private void initializeWebDriver()
    {
        String browserName = System.getProperty("browserName", "chrome"); // Default to chrome if not specified

        // Switch based on browser name to initialize the appropriate WebDriver instance
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
        // Maximize the browser window
        GetDriver().manage().window().maximize();
    }

    // Initialize the database connection
    private void initializeDBConnection()
    {
        try
        {
            // Load the MySQL JDBC driver
            Class.forName("com.mysql.cj.jdbc.Driver");
            String url = "jdbc:mysql://localhost:3306/employee_db"; // Database URL
            String user = "root"; // Database username
            String password = "root"; // Database password

            // Establish the database connection
            connection = DriverManager.getConnection(url, user, password);
            LogInfo("Connected to the database!"); // Log a success message
        }
        catch (Exception e)
        {
            e.printStackTrace(); // Log any exception that occurs while connecting to the database
        }
    }

    // Determine if the test is a web test based on the method's package name
    private boolean isWebTest(Method method)
    {
        return method.getDeclaringClass().getPackage().getName().contains("web_tests");
    }

    // Determine if the test is a DB test based on the method's package name
    private boolean isDBTest(Method method)
    {
        return method.getDeclaringClass().getPackage().getName().contains("db_tests");
    }

    // Determine if the test is an Excel test based on the method's package name
    private boolean isExcelTest(Method method)
    {
        return method.getDeclaringClass().getPackage().getName().contains("excel_tests");
    }

    // Determine if the test method belongs to a web test based on its package name
    private boolean isWebTest(ITestNGMethod method)
    {
        // Check if the package name of the class containing the test method contains "web_tests"
        return method.getRealClass().getPackage().getName().contains("web_tests");
    }

    // Determine if the test method belongs to a DB test based on its package name
    private boolean isDBTest(ITestNGMethod method)
    {
        // Check if the package name of the class containing the test method contains "db_tests"
        return method.getRealClass().getPackage().getName().contains("db_tests");
    }

    // Determine if the test method belongs to an Excel test based on its package name
    private boolean isExcelTest(ITestNGMethod method)
    {
        // Check if the package name of the class containing the test method contains "excel_tests"
        return method.getRealClass().getPackage().getName().contains("excel_tests");
    }


    // Handle the test result and log it to the ExtentTest
    private void handleTestResult(ITestResult result, ExtentTest test)
    {
        String stackTrace = result.getThrowable() != null ?
                "<pre>Message: <br>" + result.getThrowable().getMessage() +
                        "<br><br>Stack Trace: <br>" + Arrays.toString(result.getThrowable().getStackTrace()) + "</pre>" : "";

        // Handle the failure case and log details
        if (result.getStatus() == ITestResult.FAILURE)
        {
            String failureMessage = "<span style='color:red;'>Test failed</span>";
            String failureStackTrace = stackTrace.isEmpty() ? "" : "<span style='color:red;'>" + stackTrace + "</span>";
            test.fail(failureMessage, CaptureScreenshot(GetDriver(), GetScreenshotName()));
            if (!stackTrace.isEmpty())
            {
                test.fail(failureStackTrace);
                test.fail("Test Ended with Fail");
            }
        }
        // Handle the skipped case and log details
        else if (result.getStatus() == ITestResult.SKIP)
        {
            test.warning("Test skipped");
            if (!stackTrace.isEmpty())
            {
                test.warning(stackTrace);
                test.warning("Test Ended with Warning");
            }
        }
        // Handle the success case and log a pass message
        else if (result.getStatus() == ITestResult.SUCCESS) {
            test.pass("Test Ended with Pass");
        }
    }

    // Retrieve the WebDriver instance from thread-local storage
    public WebDriver GetDriver()
    {
        if (driver.get() == null)
        {
            throw new IllegalStateException("Driver is not initialized.");
        }
        return driver.get();
    }

    // Generate a unique screenshot name based on the current time
    public static String GetScreenshotName()
    {
        Date time = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("h_mm_ss");
        String formattedTime = dateFormat.format(time);
        return "Screenshot_" + formattedTime + ".png";
    }

    // Capture a screenshot and return it as a Media object for reporting
    public static Media CaptureScreenshot(WebDriver driver, String screenshotName)
    {
        String screenshotBase64 = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BASE64); // Take screenshot as base64 string
        return MediaEntityBuilder.createScreenCaptureFromBase64String(screenshotBase64, screenshotName).build();
    }

    // Log an informational message in the ExtentTest report
    public static void LogInfo(String msg)
    {
        ExtentTestManager.GetTest().info(msg);
    }

    // Log a pass message in the ExtentTest report
    public static ExtentTest LogPass(String msg)
    {
        ExtentTestManager.GetTest().pass(msg);
        return ExtentTestManager.GetTest();
    }

    // Log a fail message in the ExtentTest report
    public static ExtentTest LogFail(String msg)
    {
        ExtentTestManager.GetTest().fail(msg);
        return ExtentTestManager.GetTest();
    }


}
