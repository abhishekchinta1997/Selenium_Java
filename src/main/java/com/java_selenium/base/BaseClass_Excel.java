package com.java_selenium.base;

import com.aventstack.extentreports.ExtentTest;
import com.java_selenium.utils.extent_reports_manager_excel.ExtentManagerExcel;
import com.java_selenium.utils.extent_reports_manager_excel.ExtentTestManagerExcel;
import org.testng.ITestResult;
import org.testng.Reporter;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;

import java.lang.reflect.Method;
import java.util.Arrays;


public class BaseClass_Excel
{

    @BeforeClass
    public void Setup()
    {

    }

    @BeforeMethod
    public void StartTest(Method method)
    {
        // Create a new test for each individual test run
        String className = Reporter.getCurrentTestResult().getTestClass().getName().substring(
                Reporter.getCurrentTestResult().getTestClass().getName().lastIndexOf('.') + 1
        );

        String methodName = method.getName();  // Get method name dynamically and Use 'method.getName()' instead of Reporter
        ExtentTestManagerExcel.CreateTest(className, methodName);
    }


    @AfterMethod
    public void AfterTest(ITestResult result)
    {
        // Determine test result status and stack trace
        String stackTrace = result.getThrowable() != null ?
                "<pre>Message: <br>" + result.getThrowable().getMessage() +
                        "<br><br>Stack Trace: <br>" + Arrays.toString(result.getThrowable().getStackTrace()) + "</pre>" : "";

        // Handle FAILURE
        if (result.getStatus() == ITestResult.FAILURE)
        {
            // Apply red color formatting for the message and stack trace
            String failureMessage = "<span style='color:red;'>Test failed</span>";
            String failureStackTrace = stackTrace.isEmpty() ? "" : "<span style='color:red;'>" + stackTrace + "</span>";

            ExtentTestManagerExcel.GetTest().fail(failureMessage);
            if (!stackTrace.isEmpty())
            {
                ExtentTestManagerExcel.GetTest().fail(failureStackTrace);
                ExtentTestManagerExcel.GetTest().fail("Test Ended with Fail");
            }
        }
        else if (result.getStatus() == ITestResult.SKIP)
        {
            ExtentTestManagerExcel.GetTest().warning("Test skipped");
            if (!stackTrace.isEmpty())
            {
                ExtentTestManagerExcel.GetTest().warning(stackTrace);
                ExtentTestManagerExcel.GetTest().warning("Test Ended with Warning");
            }
        }
        // Handle PASS
        else if (result.getStatus() == ITestResult.SUCCESS)
        {
            ExtentTestManagerExcel.GetTest().pass("Test Ended with Pass");
        }
        ExtentTestManagerExcel.clearTest(); // Clear the ThreadLocal reference to the current test
    }

    @AfterClass
    public void TearDown()
    {
        // Ensure that all reports are flushed after the tests are complete
        ExtentManagerExcel.getInstance().flush();
    }


    // Logs an informational message in the test report
    public static void LogInfo(String msg)
    {
        ExtentTest test = ExtentTestManagerExcel.GetTest();
        if (test != null)
        {
            test.info(msg);
        }
        else
        {
            System.err.println("ExtentTest is null while trying to log info: " + msg);
        }
    }


    // Logs a pass message in the test report when a test passes
    public static ExtentTest LogPass(String msg)
    {
        ExtentTest test = ExtentTestManagerExcel.GetTest();
        if (test != null)
        {
            test.pass(msg);
        } else
        {
            System.err.println("ExtentTest is null while trying to log pass: " + msg);
        }
        return test;
    }

    // Logs a fail message in the test report when a test fails
    public static ExtentTest LogFail(String msg)
    {
        ExtentTest test = ExtentTestManagerExcel.GetTest();
        if (test != null)
        {
            test.fail(msg);
        } else
        {
            System.err.println("ExtentTest is null while trying to log fail: " + msg);
        }
        return test;
    }










}

