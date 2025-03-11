package com.java_selenium.utils.extent_reports_manager;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
import java.nio.file.Paths;

public class ExtentManager
{
    // Singleton instance of ExtentReports to ensure a single report is created throughout the test execution
    private static final ExtentReports INSTANCE = new ExtentReports();

    // Returns the single instance of ExtentReports
    public static ExtentReports getInstance()
    {
        return INSTANCE;
    }

    // Initializes the ExtentReports with a custom report path based on the test type
    public static void initialize(String testType)
    {
        // Get the current working directory of the project
        String workingDirectory = System.getProperty("user.dir");

        if (workingDirectory != null)
        {
            // Assign the project directory based on the working directory
            String projectDirectory = new String(workingDirectory);
            String reportPath;

            // Determine the report file path based on the type of test (DB, Excel, or Web)
            switch (testType.toLowerCase())
            {
                case "db":
                    // Set report path for database tests
                    reportPath = Paths.get(projectDirectory, "target", "extent-reports", "extent_reports_db.html").toString();
                    break;
                case "excel":
                    // Set report path for Excel tests
                    reportPath = Paths.get(projectDirectory, "target", "extent-reports", "extent_reports_excel.html").toString();
                    break;
                case "web":
                default:
                    // Set report path for web tests (default case)
                    reportPath = Paths.get(projectDirectory, "target", "extent-reports", "extent_reports_web.html").toString();
                    break;
            }

            // Create a reporter for generating the HTML report
            ExtentSparkReporter reporter = new ExtentSparkReporter(reportPath);
            // Set the document title for the report
            reporter.config().setDocumentTitle("Extent Reports/TestNG");
            // Set the report name for the report
            reporter.config().setReportName("Extent Reports/TestNG");
            // Set the theme of the report to DARK
            reporter.config().setTheme(Theme.DARK);

            // Attach the reporter to the ExtentReports instance
            INSTANCE.attachReporter(reporter);

            // Set system info that will be displayed in the report
            INSTANCE.setSystemInfo("Application", "Practice Project");
            INSTANCE.setSystemInfo("Environment", "QA");
            INSTANCE.setSystemInfo("Type of Test", testType);
        }
        else
        {
            // Throw an exception if the working directory cannot be determined
            throw new IllegalStateException("Working directory could not be determined. Please check the application's environment.");
        }
    }

    // Private constructor to prevent instantiation of the ExtentManager class
    private ExtentManager()
    {
    }
}
