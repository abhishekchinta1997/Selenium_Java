package com.java_selenium.utils.extent_reports_manager;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
import java.nio.file.Paths;

public class ExtentManager
{
    // Lazy initialization of ExtentReports to ensure the instance is created only once when accessed
    private static final ExtentReports INSTANCE = new ExtentReports();

    // Public static method to access the single instance of ExtentReports
    public static ExtentReports getInstance() {
        return INSTANCE;
    }

    // Static block to initialize the ExtentReports instance
    static {
        // Get the current working directory (where the executable is located)
        String workingDirectory = System.getProperty("user.dir");

        // Check if the working directory is not null
        if (workingDirectory != null)
        {
            // Get the project's directory by moving two levels up from the working directory
            String projectDirectory = new String(workingDirectory);

            // Define the path for the generated HTML report (index.html in the project root)
            // Change reportPath to use 'target' folder
            String reportPath = Paths.get(projectDirectory, "target", "extent-reports", "extent_reports_web.html").toString();

            // Create a new ExtentSparkReporter to generate an HTML report at the specified path
            ExtentSparkReporter htmlReporter = new ExtentSparkReporter(reportPath);

            // Configure the properties of the report
            htmlReporter.config().setDocumentTitle("Extent Reports/TestNG");  // Set the document title
            htmlReporter.config().setReportName("Extent Reports/TestNG");    // Set the report name
            htmlReporter.config().setTheme(Theme.DARK);  // Set the theme of the report to dark

            // Attach the configured reporter to the ExtentReports instance
            INSTANCE.attachReporter(htmlReporter);

            // Add system information to the report (useful for reporting context)
            INSTANCE.setSystemInfo("Application", "Practice Project");
            INSTANCE.setSystemInfo("Environment", "QA");

        }
        else
        {
            // Throw an exception if the working directory is null
            throw new IllegalStateException("Working directory could not be determined. Please check the application's environment.");
        }
    }

    // Private constructor to prevent instantiation of the ExtentManager class
    // Ensures that the class can only be accessed through the static getInstance() method
    private ExtentManager()
    {
    }
}
