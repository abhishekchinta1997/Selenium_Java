package com.java_selenium.utils.extent_reports_manager;

import com.aventstack.extentreports.ExtentTest;
import java.util.concurrent.ConcurrentHashMap;

public class ExtentTestManager
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
        {  // Ensure thread safety when creating a parent test
            parentTest.set(ExtentManager.getInstance().createTest(testName));
            return parentTest.get();
        }
    }

    // Creates a child test under an existing parent test.
    // If the parent test doesn't exist in the map, it creates a new one.
    public static ExtentTest CreateTest(String parentName, String testName)
    {
        synchronized (synclock)
        {   // Ensure thread safety when creating child tests
            ExtentTest parentTestInstance;

            // Check if the parent test already exists in the map
            if (!parentTestMap.containsKey(parentName))
            {
                // If not, create a new parent test and add it to the map
                parentTestInstance = ExtentManager.getInstance().createTest(parentName);
                parentTestMap.put(parentName, parentTestInstance);
            }
            else
            {
                // Retrieve the existing parent test
                parentTestInstance = parentTestMap.get(parentName);
            }

            // Set the parent test and create a child test under it
            parentTest.set(parentTestInstance);
            childTest.set(parentTestInstance.createNode(testName));
            return childTest.get();
        }
    }

    // Creates a method-level test under the current parent test.
    public static ExtentTest CreateMethod(String testName)
    {
        synchronized (synclock)
        {   // Ensure thread safety when creating a method-level test
            // Check if parentTest is null before using it
            if (parentTest.get() == null)
            {
                throw new IllegalStateException("Parent test has not been created yet.");
            }

            // Create a new node (method-level test) under the current parent test
            childTest.set(parentTest.get().createNode(testName));
            return childTest.get();
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
}
