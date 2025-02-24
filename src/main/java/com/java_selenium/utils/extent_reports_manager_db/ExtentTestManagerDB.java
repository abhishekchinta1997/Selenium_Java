package com.java_selenium.utils.extent_reports_manager_db;

import com.aventstack.extentreports.ExtentTest;

import java.util.concurrent.ConcurrentHashMap;

public class ExtentTestManagerDB
{
    // Map to store parent test instances by their names.
    private static final ConcurrentHashMap<String, ExtentTest> parentTestMap = new ConcurrentHashMap<>();

    // Thread-local variables to store the parent and child test instances.
    private static final ThreadLocal<ExtentTest> parentTest = ThreadLocal.withInitial(() -> null);

    private static final ThreadLocal<ExtentTest> childTest = ThreadLocal.withInitial(() -> null);

    // Lock object to ensure thread safety when creating tests.
    private static final Object synclock = new Object();

    // Creates a new parent test with the specified name.
    public static ExtentTest CreateParentTest(String testName)
    {
        synchronized (synclock)
        {
            ExtentTest test = ExtentManagerDB.getInstance().createTest(testName);
            parentTest.set(test);
            parentTestMap.put(testName, test);
            return test;
        }
    }

    // Creates a child test under an existing parent test.
    // If the parent test doesn't exist in the map, it creates a new one.
    public static ExtentTest CreateTest(String parentName, String testName)
    {
        synchronized (synclock)
        {
            ExtentTest parentTestInstance;
            if (!parentTestMap.containsKey(parentName))
            {
                parentTestInstance = ExtentManagerDB.getInstance().createTest(parentName);
                parentTestMap.put(parentName, parentTestInstance);
            }
            else
            {
                parentTestInstance = parentTestMap.get(parentName);
            }
            parentTest.set(parentTestInstance);
            ExtentTest test = parentTestInstance.createNode(testName);
            childTest.set(test);
            return test;
        }
    }

    public static ExtentTest CreateMethod(String testName)
    {
        synchronized (synclock)
        {
            if (parentTest.get() == null)
            {
                throw new IllegalStateException("Parent test has not been created yet.");
            }
            ExtentTest test = parentTest.get().createNode(testName);
            childTest.set(test);
            return test;
        }
    }

    // Retrieves the current method-level test (child test).
    public static ExtentTest GetMethod()
    {
        synchronized (synclock)
        { // Ensure thread safety when accessing the child test
            return childTest.get();
        }
    }

    // Retrieves the current test (could be either a parent or child test).
    public static ExtentTest GetTest()
    {
        synchronized (synclock)
        {  // Ensure thread safety when accessing the current test
            return childTest.get();
        }
    }

    // Clear the ThreadLocal instances for parent and child tests
    public static void clearTest()
    {
        parentTest.remove();
        childTest.remove();
    }


}
