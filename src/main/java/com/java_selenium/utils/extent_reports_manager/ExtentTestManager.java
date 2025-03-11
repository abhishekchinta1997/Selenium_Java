package com.java_selenium.utils.extent_reports_manager;

import com.aventstack.extentreports.ExtentTest;
import java.util.concurrent.ConcurrentHashMap;

public class ExtentTestManager
{
    // A ConcurrentHashMap to store parent tests by their name for later retrieval
    private static final ConcurrentHashMap<String, ExtentTest> parentTestMap = new ConcurrentHashMap<>();

    // ThreadLocal to store the current parent test for each thread
    private static final ThreadLocal<ExtentTest> parentTest = ThreadLocal.withInitial(() -> null);

    // ThreadLocal to store the current child test for each thread
    private static final ThreadLocal<ExtentTest> childTest = ThreadLocal.withInitial(() -> null);

    // A lock object used for synchronizing access to the test creation methods
    private static final Object synclock = new Object();

    /**
     * Creates a parent test and stores it in the map and in the ThreadLocal for the current thread.
     *
     * @param testName The name of the parent test.
     * @return The created ExtentTest object for the parent test.
     */
    public static ExtentTest CreateParentTest(String testName)
    {
        // Synchronizing to ensure that the test creation is thread-safe
        synchronized (synclock)
        {
            // Create a new parent test using the ExtentManager
            ExtentTest test = ExtentManager.getInstance().createTest(testName);

            // Store the created parent test in the ThreadLocal and map
            parentTestMap.put(testName, test);
            parentTest.set(test);

            // Return the created test
            return test;
        }
    }

    /**
     * Creates a child test under the given parent test name.
     *
     * @param parentName The name of the parent test.
     * @param testName   The name of the child test to be created.
     * @return The created ExtentTest object for the child test.
     */
    public static ExtentTest CreateTest(String parentName, String testName)
    {
        // Synchronizing access to ensure thread-safety for shared resources
        synchronized (synclock)
        {

            // Retrieve or create a parent test instance from the map.
            // If no entry exists for the parentName, create a new parent test using ExtentManager.
            ExtentTest parentTestInstance = parentTestMap.computeIfAbsent(parentName,
                    name -> ExtentManager.getInstance().createTest(name)
            );

            // Set the current thread's parent test instance.
            // Assuming 'parentTest' is a ThreadLocal or similar mechanism to maintain thread-specific data
            parentTest.set(parentTestInstance);

            // Create a child node (test) under the parent test instance.
            ExtentTest childTestInstance = parentTestInstance.createNode(testName);

            // Set the current thread's child test instance.
            childTest.set(childTestInstance);

            // Return the child test instance, which is now associated with the parent.
            return childTestInstance;
        }
    }


    /**
     * Creates a method-level test (child test) under the current parent test.
     *
     * @param testName The name of the method-level test.
     * @return The created ExtentTest object for the method-level test.
     * @throws IllegalStateException if the parent test has not been created yet.
     */
    public static ExtentTest CreateMethod(String testName)
    {
        // Synchronizing to ensure thread-safety during method-level test creation
        synchronized (synclock)
        {
            // Check if the parent test is not created yet
            if (parentTest.get() == null)
            {
                throw new IllegalStateException("Parent test has not been created yet.");
            }

            // Create a method-level test (child test) under the current parent test
            ExtentTest test = parentTest.get().createNode(testName);

            // Set the child test for the current thread
            childTest.set(test);

            // Return the created method-level test
            return test;
        }
    }

    /**
     * Retrieves the current method-level test (child test) for the current thread.
     *
     * @return The current ExtentTest object for the method-level test.
     */
    public static ExtentTest GetMethod()
    {
        // Synchronizing to ensure thread-safe retrieval of the child test
        synchronized (synclock)
        {
            // Return the current method-level test
            return childTest.get();
        }
    }

    /**
     * Retrieves the current test (child test) for the current thread.
     *
     * @return The current ExtentTest object for the test.
     */
    public static ExtentTest GetTest()
    {
        // Synchronizing to ensure thread-safe retrieval of the child test
        synchronized (synclock)
        {
            // Return the current child test
            return childTest.get();
        }
    }

    /**
     * Clears the ThreadLocal references for both parent and child tests.
     */
    public static void clearTest()
    {
        // Remove the parent and child tests from the ThreadLocal references
        parentTest.remove();
        childTest.remove();
    }
}


