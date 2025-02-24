package com.java_selenium.base;

import com.aventstack.extentreports.ExtentTest;
import com.java_selenium.utils.extent_reports_manager.ExtentTestManager;
import com.java_selenium.utils.extent_reports_manager_db.ExtentManagerDB;
import com.java_selenium.utils.extent_reports_manager_db.ExtentTestManagerDB;
import org.testng.ITestResult;
import org.testng.Reporter;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;

import java.lang.reflect.Method;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BaseClass_Emp_DB
{
    protected static Connection connection;  // Declare connection as a class-level variable

    @BeforeClass
    public void Setup()
    {
        // Establish the DB connection once before all tests
        connection = ConnectDB();
    }

    @BeforeMethod
    public void Start_DB(Method method)
    {
        // Create a new test for each individual test run
        String className = Reporter.getCurrentTestResult().getTestClass().getName().substring(
                Reporter.getCurrentTestResult().getTestClass().getName().lastIndexOf('.') + 1
        );

        String methodName = method.getName();  // Get method name dynamically and Use 'method.getName()' instead of Reporter
        ExtentTestManagerDB.CreateTest(className, methodName);
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

            ExtentTestManagerDB.GetTest().fail(failureMessage);
            if (!stackTrace.isEmpty())
            {
                ExtentTestManagerDB.GetTest().fail(failureStackTrace);
                ExtentTestManagerDB.GetTest().fail("Test Ended with Fail");
            }
        }
        else if (result.getStatus() == ITestResult.SKIP)
        {
            ExtentTestManagerDB.GetTest().warning("Test skipped");
            if (!stackTrace.isEmpty())
            {
                ExtentTestManagerDB.GetTest().warning(stackTrace);
                ExtentTestManagerDB.GetTest().warning("Test Ended with Warning");
            }
        }
        // Handle PASS
        else if (result.getStatus() == ITestResult.SUCCESS)
        {
            ExtentTestManagerDB.GetTest().pass("Test Ended with Pass");
        }
        ExtentTestManagerDB.clearTest(); // Clear the ThreadLocal reference to the current test
    }

    @AfterClass
    public void TearDown()
    {
        // Close the DB connection after all tests are done
        try
        {
            if (connection != null && !connection.isClosed())
            {
                connection.close();
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }

        // Ensure that all reports are flushed after the tests are complete
        ExtentManagerDB.getInstance().flush();
    }


    // Logs an informational message in the test report
    public static void LogInfo(String msg)
    {
        ExtentTest test = ExtentTestManagerDB.GetTest();
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
        ExtentTest test = ExtentTestManagerDB.GetTest();
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
        ExtentTest test = ExtentTestManagerDB.GetTest();
        if (test != null)
        {
            test.fail(msg);
        } else
        {
            System.err.println("ExtentTest is null while trying to log fail: " + msg);
        }
        return test;
    }


    public static Connection ConnectDB()
    {
        try
        {
            // Load MySQL JDBC driver
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Establish connection
            String url = "jdbc:mysql://localhost:3306/employee_db"; // Change your host and database name
            String user = "root"; // MySQL username
            String password = "root"; // MySQL password

            // Get the connection
            connection = DriverManager.getConnection(url, user, password);
            LogInfo("Connected to the database!");
            return connection;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }
    }


    public static List<String> FetchEmployeeData() throws SQLException
    {
        List<String> employees = new ArrayList<>();
        assert connection != null;
        Statement stmt = connection.createStatement();
        String query = "SELECT * FROM employees";
        ResultSet rs = stmt.executeQuery(query);
        while (rs.next())
        {
            // Make sure to fetch all columns
            String employeeDetails = rs.getInt("employee_id") + "\t" +  // employee_id
                            rs.getString("first_name") + "\t" +  // first_name
                            rs.getString("last_name") + "\t" +   // last_name
                            rs.getString("name")  + "\t" +      //  name
                            rs.getString("position") + "\t" +    // position
                            rs.getDouble("salary") + "\t" +      // salary
                            rs.getString("hire_date") + "\t" +   // hire_date
                            rs.getInt("department_id");          // department_id
            employees.add(employeeDetails);
        }
        rs.close();
        stmt.close();
        return employees;
    }


}

